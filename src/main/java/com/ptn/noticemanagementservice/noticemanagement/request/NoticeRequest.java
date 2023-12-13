package com.ptn.noticemanagementservice.noticemanagement.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
public class NoticeRequest {

    private Long id;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotBlank(message = "Title is required")
    @Length(max = 255)
    private String title;

    @NotBlank(message = "Content is required")
    @Length
    private String contentDetail;

    @Valid
    private List<DocumentRequest> documents;

}
