package com.ptn.noticemanagementservice.noticemanagement.request;

import com.ptn.noticemanagementservice.common.entity.BaseEntity;
import com.ptn.noticemanagementservice.noticemanagement.entity.Document;
import com.ptn.noticemanagementservice.usermanagement.entity.Account;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
public class NoticeRequest {

    private Date startDate;

    private Date endDate;

    private String noticeStatus;

    private String title;

    private String contentDetail;
}
