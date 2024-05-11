package com.system.library.repository;

import com.system.library.model.Patron;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronRepository extends CrudRepository<Patron, Long> {
}
