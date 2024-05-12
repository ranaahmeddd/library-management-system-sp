package com.system.library.service.impl;

import com.system.library.model.Book;
import com.system.library.model.BorrowingRecord;
import com.system.library.repository.BookRepository;
import com.system.library.repository.BorrowingRecordRepository;
import com.system.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Book> getAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Book updateBook(Long id, Book updatedBook) {
        if (bookRepository.existsById(id)) {
            updatedBook.setId(id);
            return bookRepository.save(updatedBook);
        } else {
            throw new IllegalArgumentException("Book with id " + id + " does not exist.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent() && !isBorrowed(id)) {
            bookRepository.deleteById(id);
        }else if (isBorrowed(id)) {
            throw new IllegalStateException("Cannot Delete this book because it is already borrowed!");
        } else {
            throw new IllegalStateException("No existing book with id " + id +" to delete!");
        }
    }
    private boolean isBorrowed(Long bookId) {
        Optional<BorrowingRecord> activeBorrowingRecord = borrowingRecordRepository.findFirstByBookId(bookId);
        return activeBorrowingRecord.isPresent();
    }
}

