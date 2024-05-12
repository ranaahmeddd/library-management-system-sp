package com.system.library.ut.service;

import com.system.library.model.Book;
import com.system.library.model.BorrowingRecord;
import com.system.library.repository.BookRepository;
import com.system.library.repository.BorrowingRecordRepository;
import com.system.library.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertSame(books, result);
    }

    @Test
    void testGetBookById() {
        Book book = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(1L);

        assertTrue(result.isPresent());
        assertSame(book, result.get());
    }

    @Test
    void testAddBook() {
        Book book = new Book();
        when(bookRepository.save(any())).thenReturn(book);

        Book result = bookService.addBook(book);

        assertSame(book, result);
    }

    @Test
    void testUpdateBook() {
        Book updatedBook = new Book();
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);

        Book result = bookService.updateBook(1L, updatedBook);

        assertSame(updatedBook, result);
    }

    @Test
    void testDeleteBook() {
        Book book = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowingRecordRepository.findFirstByBookId(1L)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> bookService.deleteBook(1L));
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBookWhenBorrowed() {
        when(borrowingRecordRepository.findFirstByBookId(1L)).thenReturn(Optional.of(new BorrowingRecord()));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> bookService.deleteBook(1L));
        assertEquals("Cannot Delete this book because it is already borrowed!", exception.getMessage());
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteBookWhenNotExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> bookService.deleteBook(1L));
        assertEquals("No existing book with id 1 to delete!", exception.getMessage());
        verify(bookRepository, never()).deleteById(anyLong());
    }
}

