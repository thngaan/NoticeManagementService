package com.ptn.noticemanagementservice.common.utils;

import com.ptn.noticemanagementservice.common.exception.NoticeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public final class ValidationUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationUtils.class);

    private ValidationUtils() {
    }

    public static void validateActiveTime(Date startDate, Date endDate) {
        Date currentDate = new Date();
        if (currentDate.before(startDate) || currentDate.after(endDate)) {
            LOGGER.error("Current time {} is not between startDate {} and endDate {}", currentDate, startDate, endDate);
            throw new NoticeException("Current time is not between startDate and endDate");
        }
    }
}
