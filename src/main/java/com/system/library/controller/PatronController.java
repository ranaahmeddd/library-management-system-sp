package com.system.library.controller;

import com.system.library.model.Patron;
import com.system.library.service.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/patrons")
public class PatronController {

    @Autowired
    private PatronService patronService;

    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrons() {
        return (ResponseEntity<List<Patron>>) patronService.getAllPatrons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patron> getPatronById(@PathVariable Long id) {
        Optional<Patron> patron = patronService.getPatronById(id);
        return patron.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Patron> addPatron(@RequestBody Patron patron) {
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
