package com.system.library.repository;

import com.system.library.model.BorrowingRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends CrudRepository<BorrowingRecord, Long> {
    Optional<BorrowingRecord> findByBookIdAndPatronIdAndReturnedFalse(Long bookId, Long patronId);
    Optional<BorrowingRecord> findFirstByBookIdAndReturnedFalse(Long bookId);
    Optional<BorrowingRecord> findFirstByBookId(Long bookId);
    Optional<BorrowingRecord> findFirstByPatronId(Long patronId);
}
