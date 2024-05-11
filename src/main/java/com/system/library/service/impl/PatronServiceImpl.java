package com.system.library.service.impl;

import com.system.library.model.Patron;
import com.system.library.repository.PatronRepository;
import com.system.library.service.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatronServiceImpl implements PatronService {

    @Autowired
    private PatronRepository patronRepository;

    @Override
    public List<Patron> getAllPatrons() {
        return (List<Patron>) patronRepository.findAll();
    }

    @Override
    public Optional<Patron> getPatronById(Long id) {
        return patronRepository.findById(id);
    }

    @Override
    public Patron addPatron(Patron patron) {
        return patronRepository.save(patron);
    }

    @Override
    public Patron updatePatron(Long id, Patron updatedPatron) {
        if (patronRepository.existsById(id)) {
            updatedPatron.setId(id);
            return patronRepository.save(updatedPatron);
        } else {
            throw new IllegalArgumentException("Patron with id " + id + " does not exist.");
        }
    }

    @Override
    public void deletePatron(Long id) {
        patronRepository.deleteById(id);
    }
}
