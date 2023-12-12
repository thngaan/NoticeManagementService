package com.ptn.noticemanagementservice.noticemanagement.entity;

import com.ptn.noticemanagementservice.common.entity.BaseEntity;
import com.ptn.noticemanagementservice.usermanagement.entity.Account;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "Notice")
@Data
@EqualsAndHashCode(callSuper = true)
public class Notice extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AuthorId", referencedColumnName = "Id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "notice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Document> documents = new ArrayList<>();

    @Column(name = "StartDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "EndDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "NoticeStatus")
    private String noticeStatus;

    @Column(name = "Title")
    private String title;

    @Column(name = "ContentDetail")
    private String contentDetail;

    @Column(name = "IsDeleted")
    private boolean isDeleted;

    @Column(name = "ViewCounter")
    private int viewCounter;
}
