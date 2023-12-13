package com.ptn.noticemanagementservice.noticemanagement.repository;

import com.ptn.noticemanagementservice.noticemanagement.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findByIdAndIsDeletedIsFalse(Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Notice n set n.viewCounter = n.viewCounter + 1 where n.id = :id")
    int incrementViewCounter(@Param("id") Long id);
}
