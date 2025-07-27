package com.musdon.banking_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class TransferRequest {

    public String creditedAccountNumber;

    public String debitedAccountNumber;

    public BigDecimal amount;
}
