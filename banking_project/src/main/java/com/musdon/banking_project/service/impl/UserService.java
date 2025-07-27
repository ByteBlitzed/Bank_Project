package com.musdon.banking_project.service.impl;

import com.musdon.banking_project.dto.*;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry (EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest creditRequest,boolean saveTransaction);

    BankResponse debitAccount(CreditDebitRequest debitRequest, boolean saveTransaction);

    BankResponse transferAccount(TransferRequest request);

}
