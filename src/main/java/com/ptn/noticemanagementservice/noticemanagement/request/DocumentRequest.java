package com.ptn.noticemanagementservice.noticemanagement.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

@Data
@EqualsAndHashCode
public class DocumentRequest {

    private Long id;

    @NotBlank(message = "Filename is required")
    @Length(max = 50)
    private String filename;

    private Integer order;
}
