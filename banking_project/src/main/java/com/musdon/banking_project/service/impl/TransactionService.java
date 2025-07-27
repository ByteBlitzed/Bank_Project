package com.musdon.banking_project.service.impl;

import com.musdon.banking_project.dto.TransactionDto;
import com.musdon.banking_project.entity.Transaction;

public interface TransactionService {

    void saveTransaction(TransactionDto T);
}
