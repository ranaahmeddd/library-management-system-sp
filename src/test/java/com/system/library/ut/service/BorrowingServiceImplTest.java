package com.system.library.ut.service;

import com.system.library.model.Book;
import com.system.library.model.BorrowingRecord;
import com.system.library.model.Patron;
import com.system.library.repository.BookRepository;
import com.system.library.repository.BorrowingRecordRepository;
import com.system.library.repository.PatronRepository;
import com.system.library.service.impl.BorrowingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingServiceImplTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowingServiceImpl borrowingService;

    @Test
    void testBorrowBook() {
        Book book = new Book();
        Patron patron = new Patron();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findFirstByBookIdAndReturnedFalse(1L)).thenReturn(Optional.empty());

        borrowingService.borrowBook(1L, 1L);

        verify(borrowingRecordRepository, times(1)).save(any());
    }

    @Test
    void testBorrowBookWhenBookAlreadyBorrowed() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(new Patron()));
        when(borrowingRecordRepository.findFirstByBookIdAndReturnedFalse(1L)).thenReturn(Optional.of(new BorrowingRecord()));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> borrowingService.borrowBook(1L, 1L));
        assertEquals("The book with ID 1 is already borrowed.", exception.getMessage());
        verify(borrowingRecordRepository, never()).save(any());
    }

    @Test
    void testBorrowBookWhenBookOrPatronNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> borrowingService.borrowBook(1L, 1L));
        assertEquals("Book with ID 1 or patron with ID 1 not found.", exception.getMessage());
        verify(borrowingRecordRepository, never()).save(any());
    }

    @Test
    void testReturnBook() {
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnedFalse(1L, 1L)).thenReturn(Optional.of(borrowingRecord));

        borrowingService.returnBook(1L, 1L);

        assertTrue(borrowingRecord.isReturned());
        assertNotNull(borrowingRecord.getReturnDate());
        verify(borrowingRecordRepository, times(1)).save(any());
    }

    @Test
    void testReturnBookWhenNoActiveBorrowingRecordFound() {
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnedFalse(1L, 1L)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> borrowingService.returnBook(1L, 1L));
        assertEquals("No active borrowing record found for book with ID 1 and patron with ID 1", exception.getMessage());
        verify(borrowingRecordRepository, never()).save(any());
    }
}

