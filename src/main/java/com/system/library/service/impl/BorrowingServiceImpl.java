package com.system.library.service.impl;

import com.system.library.model.Book;
import com.system.library.model.BorrowingRecord;
import com.system.library.model.Patron;
import com.system.library.repository.BookRepository;
import com.system.library.repository.BorrowingRecordRepository;
import com.system.library.repository.PatronRepository;
import com.system.library.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;


@Service
public class BorrowingServiceImpl implements BorrowingService {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void borrowBook(Long bookId, Long patronId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Optional<Patron> patronOptional = patronRepository.findById(patronId);

        if (bookOptional.isPresent() && patronOptional.isPresent()) {
            Book book = bookOptional.get();
            Patron patron = patronOptional.get();

            // Check if the book is available for borrowing
            boolean isBookAvailable = isBookAvailableForBorrowing(bookId);
            if (isBookAvailable) {
                BorrowingRecord borrowingRecord = new BorrowingRecord();
                borrowingRecord.setBook(book);
                borrowingRecord.setPatron(patron);
                borrowingRecord.setBorrowingDate(new Date());
                borrowingRecord.setReturned(false);
                borrowingRecordRepository.save(borrowingRecord);
            } else {
                throw new IllegalStateException("The book with ID " + bookId + " is already borrowed.");
            }
        } else {
            throw new IllegalArgumentException("Book with ID " + bookId + " or patron with ID " + patronId + " not found.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void returnBook(Long bookId, Long patronId) {
        Optional<BorrowingRecord> borrowingRecordOptional = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnedFalse(bookId, patronId);

        if (borrowingRecordOptional.isPresent()) {
            BorrowingRecord borrowingRecord = borrowingRecordOptional.get();
            borrowingRecord.setReturnDate(new Date());
            borrowingRecord.setReturned(true);
            borrowingRecordRepository.save(borrowingRecord);
        } else {
            throw new IllegalStateException("No active borrowing record found for book with ID " + bookId + " and patron with ID " + patronId);
        }
    }

    // Helper method to check if the book is available for borrowing
    private boolean isBookAvailableForBorrowing(Long bookId) {
        Optional<BorrowingRecord> activeBorrowingRecord = borrowingRecordRepository.findFirstByBookIdAndReturnedFalse(bookId);
        return !activeBorrowingRecord.isPresent();
    }
}
