package com.ptn.noticemanagementservice.noticemanagement.entity;

import com.ptn.noticemanagementservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "Document")
@Data
@EqualsAndHashCode(callSuper = true)
public class Document  extends BaseEntity {

    @Column(name = "ContentType")
    private String contentType;

    @Column(name = "FileSize")
    private Long fileSize;

    @Column(name = "FileName")
    private String fileName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "FileContent")
    private byte[] fileContent;

    @Column(name = "OrderNo")
    private Integer order;

    @ManyToOne
    @JoinColumn(name = "NoticeId")
    private Notice notice;
}
