package com.musdon.banking_project.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {
    private String recipient;

    private String messageBody;

    private String subject;

    private String attachment;
}
