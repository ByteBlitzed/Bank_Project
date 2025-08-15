package com.musdon.banking_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatementDto {

    private String accountNumber;
    private String startDate;
    private String endDate;
}
