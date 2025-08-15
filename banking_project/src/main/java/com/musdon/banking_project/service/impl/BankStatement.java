package com.musdon.banking_project.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.musdon.banking_project.dto.EmailDetails;
import com.musdon.banking_project.dto.StatementDto;
import com.musdon.banking_project.entity.Transaction;
import com.musdon.banking_project.entity.User;
import com.musdon.banking_project.repository.TransactionRepository;
import com.musdon.banking_project.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;



@Service
@AllArgsConstructor
@Slf4j

public class BankStatement {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private static final String FILE = "C:\\Personal\\Projects\\JavaProjects\\banking_project\\banking_project\\BankStatementFile.pdf";

    /*
    * retrieve list of transaction within a date range given an account number
    * generate a pdf file of transactions
    * send the file via email
    */


    public List<Transaction> generateStatement(StatementDto SDto) throws FileNotFoundException, DocumentException {

        LocalDate StartDate = LocalDate.parse(SDto.getStartDate());
        LocalDate endDate = LocalDate.parse(SDto.getEndDate());

        List<Transaction> transactionList = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(SDto.getAccountNumber()))
                .filter(transaction -> transaction.getCreatedAt().isAfter(StartDate))
                .filter(transaction -> transaction.getCreatedAt().isBefore(endDate))
                .toList();

        Optional<User> user = userRepository.findByAccountNumber(SDto.getAccountNumber());
        String userName = user.get().getFirstName() + " " + user.get().getLastName();

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1); // single column.
        PdfPCell bankName = new PdfPCell(new Phrase("The Java Academy Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("BH1, IIIT-A"));
        bankAddress.setBorder(0);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2); // 2 columns
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + SDto.getStartDate()));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date: "+ SDto.getEndDate()));
        stopDate.setBorder(0);

        PdfPCell customerName = new PdfPCell(new Phrase("Customer Name: " + userName));
        customerName.setBorder(0);
        PdfPCell space = new PdfPCell();
        space.setBorder(0);
        PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + user.get().getAddress()));
        address.setBorder(0);

        PdfPTable transactionsTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase(new Phrase("DATE")));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);

        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);

        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        transactionAmount.setBorder(0);

        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);

        transactionsTable.addCell(date);
        transactionsTable.addCell(transactionType);
        transactionsTable.addCell(transactionAmount);
        transactionsTable.addCell(status);


        transactionList.forEach(transaction -> {
            transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionsTable.addCell(new Phrase(transaction.getStatus()));
        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(SDto.getEndDate());
        statementInfo.addCell(customerName);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionsTable);


        document.close();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.get().getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly find your requested account statement attached!")
                .attachment(FILE)
                .build();
        emailService.sendEmailWithAttachment(emailDetails);

        return transactionList;

    }



}
