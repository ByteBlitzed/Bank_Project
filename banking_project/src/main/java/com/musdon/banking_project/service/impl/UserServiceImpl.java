package com.musdon.banking_project.service.impl;

import com.musdon.banking_project.dto.*;
import com.musdon.banking_project.entity.User;
import com.musdon.banking_project.repository.UserRepository;
import com.musdon.banking_project.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /*Creating an Account is saving a new user in db*/

        // check if user already has an account

        if(userRepository.findByEmail(userRequest.getEmail()).isPresent()){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .age(userRequest.getAge())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(AccountUtils.generateAccountBalance())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(newUser);

        AccountInfo accountInfo = AccountInfo.builder()
                .accountNumber(savedUser.getAccountNumber())
                .accountBalance(savedUser.getAccountBalance())
                .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                .build();

        // send email alert.
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account has been Successfully Created.\n Your Account Details: \n" +
                        "Account Name: " +savedUser.getFirstName() + " " + savedUser.getLastName() + "\n" +
                        "Account Number: " +savedUser.getAccountNumber())
                .attachment(null)
                .build();
        emailService.sendEmailAlert(emailDetails);


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
                .accountInfo(accountInfo)
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        Optional<User> user = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        if(user.isPresent()){
            AccountInfo accountInfo = AccountInfo.builder()
                    .accountName(user.get().getFirstName() + " " + user.get().getLastName())
                    .accountBalance(user.get().getAccountBalance())
                    .accountNumber(user.get().getAccountNumber())
                    .build();

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                    .accountInfo(accountInfo)
                    .build();
        }
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                .accountInfo(null)
                .build();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        Optional<User> user = userRepository.findByAccountNumber(request.getAccountNumber());
        if(user.isPresent()){
            user.get().setAccountBalance(user.get().getAccountBalance().add(request.getAmount()));
            userRepository.save(user.get());

            AccountInfo accountInfo = AccountInfo.builder()
                    .accountName(user.get().getFirstName() + " " + user.get().getLastName())
                    .accountNumber(user.get().getAccountNumber())
                    .accountBalance(user.get().getAccountBalance())
                    .build();

            EmailDetails creditAlerts = EmailDetails.builder()
                    .recipient(user.get().getEmail())
                    .subject("AMOUNT HAS BEEN CREDITED")
                    .messageBody("The sum of " + request.getAmount() + " has been credited to your account! Your current balance is " + user.get().getAccountBalance() + "\n Thanks!!!!")
                    .attachment(null)
                    .build();

            emailService.sendEmailAlert(creditAlerts );

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_CREDITED_MESSAGE)
                    .accountInfo(accountInfo)
                    .build();
        }
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                .accountInfo(null)
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest debitRequest) {
        Optional<User> user = userRepository.findByAccountNumber(debitRequest.getAccountNumber());
        if(user.isPresent()){
            BigDecimal currBalance = user.get().getAccountBalance();

            if(debitRequest.getAmount().compareTo(currBalance) <= 0){
                user.get().setAccountBalance(currBalance.subtract(debitRequest.getAmount()));
                userRepository.save(user.get());

                AccountInfo accountInfo = AccountInfo.builder()
                        .accountName(user.get().getFirstName() + " " + user.get().getLastName())
                        .accountNumber(user.get().getAccountNumber())
                        .accountBalance(user.get().getAccountBalance())
                        .build();

                EmailDetails debitAlert = EmailDetails.builder()
                        .recipient(user.get().getEmail())
                        .subject("AMOUNT HAS BEEN DEBITED")
                        .messageBody("The sum of " + debitRequest.getAmount() + " has been deducted from your account! Your current balance is " + user.get().getAccountBalance() + "\n Thanks!!!!")
                        .attachment(null)
                        .build();

                emailService.sendEmailAlert(debitAlert);

                return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                        .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                        .accountInfo(accountInfo)
                        .build();
            }
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();

        }
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                .accountInfo(null)
                .build();
    }

    @Override
    public BankResponse transferAccount(TransferRequest request) {
        Optional<User> receiver = userRepository.findByAccountNumber(request.creditedAccountNumber);
        Optional<User> sender = userRepository.findByAccountNumber(request.debitedAccountNumber);

        if(receiver.isPresent() && sender.isPresent()){
            BigDecimal senderBalance = sender.get().getAccountBalance();
            if(request.getAmount().compareTo(senderBalance) <= 0){

                CreditDebitRequest debitRequest = CreditDebitRequest.builder()
                        .accountNumber(request.getDebitedAccountNumber())
                        .amount(request.getAmount())
                        .build();

                debitAccount(debitRequest);

                CreditDebitRequest creditRequest = CreditDebitRequest.builder()
                        .accountNumber(request.getCreditedAccountNumber())
                        .amount(request.getAmount())
                        .build();

                creditAccount(creditRequest);

                AccountInfo accountInfo = AccountInfo.builder()
                        .accountName(sender.get().getFirstName() + " " + sender.get().getLastName())
                        .accountNumber(sender.get().getAccountNumber())
                        .accountBalance(sender.get().getAccountBalance())
                        .build();

                // email alert
                EmailDetails debitAlert = EmailDetails.builder()
                        .recipient(sender.get().getEmail())
                        .subject("AMOUNT HAS BEEN DEBITED")
                        .messageBody("The sum of " + request.getAmount() + " has been deducted from your account! Your current balance is " + sender.get().getAccountBalance() + "\n Thanks!!!!")
                        .attachment(null)
                        .build();
                emailService.sendEmailAlert(debitAlert);

                EmailDetails creditAlert = EmailDetails.builder()
                        .recipient(receiver.get().getEmail())
                        .subject("AMOUNT HAS BEEN CREDITED")
                        .messageBody("The sum of " + request.getAmount() + " has been credited to your account! Your current balance is " + receiver.get().getAccountBalance() + "\n Thanks!!!!")
                        .attachment(null)
                        .build();

                emailService.sendEmailAlert(creditAlert);


                return BankResponse.builder()
                        .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                        .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
                        .accountInfo(accountInfo)
                        .build();
            }
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        else if(sender.isPresent()){
            return BankResponse.builder()
                    .responseCode(AccountUtils.RECEIVER_ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.RECEIVER_ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        return BankResponse.builder()
                .responseCode(AccountUtils.SENDER_ACCOUNT_NOT_EXIST_CODE)
                .responseMessage(AccountUtils.SENDER_ACCOUNT_NOT_EXIST_MESSAGE)
                .accountInfo(null)
                .build();
    }


}
