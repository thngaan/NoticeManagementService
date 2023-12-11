package com.ptn.noticemanagementservice.noticemanagement.entity;

import com.ptn.noticemanagementservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "Document")
@Data
@EqualsAndHashCode
public class Document  extends BaseEntity {

    @Column(name = "ContentType")
    private String contentType;

    @Column(name = "FileSize")
    private long fileSize;

    @Column(name = "FileName")
    private String fileName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "FileContent")
    private byte[] fileContent;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "Thumbnail")
    private byte[] thumbnail;

    @Column(name = "Order")
    private int order;

    @ManyToOne
    @JoinColumn(name = "NoticeId", referencedColumnName = "Id")
    private Notice notice;
}
