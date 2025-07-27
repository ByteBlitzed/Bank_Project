package com.musdon.banking_project.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;


public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "User already exist with this email";

    public static final String ACCOUNT_CREATED_CODE = "002";
    public static final String ACCOUNT_CREATED_MESSAGE = "Account has been successfully created!";

    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with the provided Account Number doest not exist";

    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "User Account Found";

    public static final String INSUFFICIENT_BALANCE_CODE = "005";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance";

    public static final String ACCOUNT_CREDITED_SUCCESS = "006";
    public static final String ACCOUNT_CREDITED_MESSAGE = "Account has been successfully credited";


    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_MESSAGE = "Account has been successfully debited";

    public static final String SENDER_ACCOUNT_NOT_EXIST_CODE = "008";
    public static final String SENDER_ACCOUNT_NOT_EXIST_MESSAGE = "The Account to be debited does not exist";

    public static final String RECEIVER_ACCOUNT_NOT_EXIST_CODE = "009";
    public static final String RECEIVER_ACCOUNT_NOT_EXIST_MESSAGE = "The Account to be credited does not exist";


    public static final String TRANSFER_SUCCESS_CODE = "010";
    public static final String TRANSFER_SUCCESS_MESSAGE = "The Transfer has been successfully completed";

    public static String generateAccountNumber(){
        Year currentYear = Year.now();

        int min = 100000, max = 1000000;

        // generate a rand numb x st min <= x <= max.

        int randNumber = (int) (Math.random()*(max - min) + min);

        // convert the current randNumber to a String, then concatenate them.

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);

        return year + randomNumber;
    }
    public static BigDecimal generateAccountBalance(){
        return BigDecimal.valueOf(0);
    }

}
