package com.system.library.service;

import com.system.library.model.Patron;
import java.util.List;
import java.util.Optional;

public interface PatronService {
    List<Patron> getAllPatrons();

    Optional<Patron> getPatronById(Long id);

    Patron addPatron(Patron patron);

    Patron updatePatron(Long id, Patron updatedPatron);

    void deletePatron(Long id);
}
