package com.lms.repository;

import com.lms.model.BorrowingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingHistoryRepository extends JpaRepository<BorrowingHistory, Long> {

    @Query("SELECT bh FROM BorrowingHistory bh JOIN bh.user u WHERE u.id = :userId")
    List<BorrowingHistory> findByUserId(@Param("userId") Long userId);

}
