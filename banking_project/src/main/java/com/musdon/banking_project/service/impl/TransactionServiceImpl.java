package com.musdon.banking_project.service.impl;

import com.musdon.banking_project.dto.TransactionDto;
import com.musdon.banking_project.entity.Transaction;
import com.musdon.banking_project.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto TDto) {

        Transaction T = Transaction.builder()
                .accountNumber(TDto.getAccountNumber())
                .amount(TDto.getAmount())
                .transactionType(TDto.getTransactionType())
                .status("SUCCESS")
                .build();

        transactionRepository.save(T);
    }
}
