package com.musdon.banking_project.controller;

import com.itextpdf.text.DocumentException;
import com.musdon.banking_project.dto.StatementDto;
import com.musdon.banking_project.entity.Transaction;
import com.musdon.banking_project.service.impl.BankStatement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor

public class TransactionController {

    @Autowired
    private BankStatement bankStatement;

    @GetMapping
    public List<Transaction> generateBankStatement(@RequestBody StatementDto SDto) throws DocumentException, FileNotFoundException {
        return bankStatement.generateStatement(SDto);
    }

}
