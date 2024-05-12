package com.system.library.ut.controller;

import com.system.library.controller.BookController;
import com.system.library.model.Book;
import com.system.library.service.BookService;
import com.system.library.util.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L));

        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<Book>> responseEntity = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
    }

    @Test
    void testGetBookById() {
        Book book = new Book(1L);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        ResponseEntity<Book> responseEntity = bookController.getBookById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(book, responseEntity.getBody());
    }

    @Test
    void testGetBookByIdNotFound() {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookController.getBookById(1L));
    }

    @Test
    void testAddBook() {
        Book book = new Book(1L);
        when(bookService.addBook(book)).thenReturn(book);

        ResponseEntity<?> responseEntity = bookController.addBook(book, mock(BindingResult.class));

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(book, responseEntity.getBody());
    }
    @Test
    void testUpdateBook() {
        Long bookId = 1L;
        Book updatedBook = new Book(1L);

        when(bookService.updateBook(bookId, updatedBook)).thenReturn(updatedBook);

        ResponseEntity<Book> responseEntity = bookController.updateBook(bookId, updatedBook);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedBook, responseEntity.getBody());
    }

    @Test
    void testDeleteBook() {
        Long bookId = 1L;

        ResponseEntity<Void> responseEntity = bookController.deleteBook(bookId);

        verify(bookService, times(1)).deleteBook(bookId);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

}

