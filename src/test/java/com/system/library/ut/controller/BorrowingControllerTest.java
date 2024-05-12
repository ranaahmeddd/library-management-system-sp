package com.system.library.ut.controller;

import com.system.library.controller.BorrowingController;
import com.system.library.service.BorrowingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingControllerTest {

    @Mock
    private BorrowingService borrowingService;

    @InjectMocks
    private BorrowingController borrowingController;

    @Test
    void testBorrowBook() {
        Long bookId = 1L;
        Long patronId = 1L;

        ResponseEntity<String> responseEntity = borrowingController.borrowBook(bookId, patronId);

        verify(borrowingService, times(1)).borrowBook(bookId, patronId);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Book borrowed successfully.", responseEntity.getBody());
    }

    @Test
    void testReturnBook() {
        Long bookId = 1L;
        Long patronId = 1L;

        ResponseEntity<String> responseEntity = borrowingController.returnBook(bookId, patronId);

        verify(borrowingService, times(1)).returnBook(bookId, patronId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Book returned successfully.", responseEntity.getBody());
    }
}

