package com.system.library.ut.controller;

import com.system.library.controller.PatronController;
import com.system.library.model.Book;
import com.system.library.model.Patron;
import com.system.library.service.PatronService;
import com.system.library.util.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatronControllerTest {

    @Mock
    private PatronService patronService;

    @InjectMocks
    private PatronController patronController;

    private Patron testPatron;

    @BeforeEach
    void setUp() {
        testPatron = new Patron(1L, "John Doe", "123456789", "123 Main St", "john@example.com");
    }

    @Test
    void testGetAllPatrons() {
        List<Patron> patrons = new ArrayList<>();
        patrons.add(testPatron);
        patrons.add(new Patron(2L, "Jane Doe", "987654321", "456 Elm St", "jane@example.com"));

        when(patronService.getAllPatrons()).thenReturn(patrons);

        ResponseEntity<List<Patron>> responseEntity = patronController.getAllPatrons();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(patrons, responseEntity.getBody());
    }

    @Test
    void testGetPatronById() {
        when(patronService.getPatronById(1L)).thenReturn(Optional.of(testPatron));

        ResponseEntity<Patron> responseEntity = patronController.getPatronById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(testPatron, responseEntity.getBody());
    }

    @Test
    void testGetPatronByIdNotFound() {
        when(patronService.getPatronById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patronController.getPatronById(1L));
    }

    @Test
    void testAddBook() {
        Patron patron = new Patron(2L);
        when(patronService.addPatron(patron)).thenReturn(patron);

        ResponseEntity<?> responseEntity = patronController.addPatron(patron, mock(BindingResult.class));

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(patron, responseEntity.getBody());
    }


    @Test
    void testAddPatronValidationFailure() {
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "patron");
        bindingResult.addError(new ObjectError("patron", "Validation failed"));

        ResponseEntity<?> responseEntity = patronController.addPatron(testPatron, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof List);
        assertFalse(((List<?>) responseEntity.getBody()).isEmpty());
    }

    @Test
    void testAddPatronValidationErrorInService() {
        when(patronService.addPatron(any())).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class,
                () -> patronController.addPatron(testPatron, mock(BindingResult.class)));
    }

    @Test
    void testUpdatePatron() {
        Patron updatedPatron = new Patron(1L, "Updated Name", "987654321", "456 Elm St", "updated@example.com");
        when(patronService.updatePatron(eq(1L), any(Patron.class))).thenReturn(updatedPatron);

        ResponseEntity<Patron> responseEntity = patronController.updatePatron(1L, updatedPatron);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedPatron, responseEntity.getBody());
    }

    @Test
    void testDeletePatron() {
        ResponseEntity<Void> responseEntity = patronController.deletePatron(1L);

        verify(patronService).deletePatron(1L);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

}
