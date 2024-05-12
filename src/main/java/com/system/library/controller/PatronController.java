package com.system.library.controller;

import com.system.library.model.Patron;
import com.system.library.service.PatronService;
import com.system.library.util.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/patrons")
public class PatronController {

    @Autowired
    private PatronService patronService;

    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrons() {
        List<Patron> patrons = patronService.getAllPatrons();
        return ResponseEntity.ok(patrons) ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patron> getPatronById(@PathVariable Long id) {
        Optional<Patron> patron = patronService.getPatronById(id);
        if (patron.isPresent()) {
            return ResponseEntity.ok(patron.get());
        } else {
            throw new ResourceNotFoundException("Patron not found with id: " + id);
        }
    }

    @PostMapping
    public ResponseEntity<?> addPatron(@Valid @RequestBody Patron patron, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        Patron addedPatron = patronService.addPatron(patron);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedPatron);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable Long id, @RequestBody Patron updatedPatron) {
        Patron patron = patronService.updatePatron(id, updatedPatron);
        return ResponseEntity.ok(patron);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.noContent().build();
    }
}
