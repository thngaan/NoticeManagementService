package com.ptn.noticemanagementservice.noticemanagement.entity;

import com.ptn.noticemanagementservice.common.entity.BaseEntity;
import com.ptn.noticemanagementservice.usermanagement.entity.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "ViewNotice")
@Data
@EqualsAndHashCode
public class ViewNotice extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "AccountId", referencedColumnName = "Id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "NoticeId", referencedColumnName = "Id")
    private Notice notice;

    @Column(name = "Score")
    private Integer score;
}
