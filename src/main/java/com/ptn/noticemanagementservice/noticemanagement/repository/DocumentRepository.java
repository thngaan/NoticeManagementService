package com.ptn.noticemanagementservice.noticemanagement.repository;

import com.ptn.noticemanagementservice.noticemanagement.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

}
