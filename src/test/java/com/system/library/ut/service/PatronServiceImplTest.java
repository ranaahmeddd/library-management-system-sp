package com.system.library.ut.service;

import com.system.library.model.BorrowingRecord;
import com.system.library.model.Patron;
import com.system.library.repository.BorrowingRecordRepository;
import com.system.library.repository.PatronRepository;
import com.system.library.service.impl.PatronServiceImpl;
import com.system.library.util.ResourceNotFoundException;
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
class PatronServiceImplTest {

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private PatronServiceImpl patronService;

    @Test
    void testGetAllPatrons() {
        List<Patron> patrons = new ArrayList<>();
        patrons.add(new Patron(1L, "John", "12345", "Address 1", "john@example.com"));
        patrons.add(new Patron(2L, "Jane", "67890", "Address 2", "jane@example.com"));
        when(patronRepository.findAll()).thenReturn(patrons);

        List<Patron> result = patronService.getAllPatrons();
        assertEquals(2, result.size());
        verify(patronRepository, times(1)).findAll();
    }

    @Test
    void testGetPatronById() {
        Patron patron = new Patron(1L, "John", "12345", "Address 1", "john@example.com");
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        Optional<Patron> result = patronService.getPatronById(1L);
        assertEquals(patron, result.orElse(null));
        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    void testAddPatron() {
        Patron patron = new Patron();
        when(patronRepository.save(any())).thenReturn(patron);

        Patron result = patronService.addPatron(new Patron());
        assertNotNull(result);
        verify(patronRepository, times(1)).save(any());
    }

    @Test
    void testUpdatePatron() {
        Patron existingPatron = new Patron();
        existingPatron.setId(1L);
        when(patronRepository.existsById(1L)).thenReturn(true);
        when(patronRepository.save(any())).thenReturn(existingPatron);

        Patron updatedPatron = new Patron();
        updatedPatron.setId(1L);
        Patron result = patronService.updatePatron(1L, updatedPatron);
        assertEquals(existingPatron.getId(), result.getId());
        verify(patronRepository, times(1)).save(any());
    }

    @Test
    void testUpdatePatronNotFound() {
        when(patronRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> patronService.updatePatron(1L, new Patron()));
        verify(patronRepository, never()).save(any());
    }

    @Test
    void testDeletePatron() {
        Patron patron = new Patron();
        patron.setId(1L);
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findFirstByPatronId(1L)).thenReturn(Optional.empty());

        patronService.deletePatron(1L);
        verify(patronRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePatronBorrowing() {
        Patron patron = new Patron();
        patron.setId(1L);
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findFirstByPatronId(1L)).thenReturn(Optional.of(new BorrowingRecord()));

        assertThrows(IllegalStateException.class, () -> patronService.deletePatron(1L));
        verify(patronRepository, never()).deleteById(1L);
    }

    @Test
    void testDeletePatronNotFound() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> patronService.deletePatron(1L));
        verify(patronRepository, never()).deleteById(1L);
    }
}
