package com.system.library.service;

public interface BorrowingService {
    void borrowBook(Long bookId, Long patronId);
    void returnBook(Long bookId, Long patronId);
}
