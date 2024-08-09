package com.maids.cc.Library_Management_System.service;

import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.PatronNotFoundException;
import com.maids.cc.Library_Management_System.model.Patron;
import com.maids.cc.Library_Management_System.repository.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatronService {

    private final PatronRepository patronRepository;

    @Cacheable(value = "books", key = "#root.methodName")
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    @Cacheable(value = "books", key = "#id")
    public Patron findPatronById(Long id) {
        return patronRepository.findById(id)
                .orElseThrow(PatronNotFoundException::new);
    }

    @CacheEvict(value = "books", key = "#root.methodName",allEntries = true)
    public void savePatron(Patron patron) {
        patronRepository.save(patron);
    }

    @CacheEvict(value = "books", key = "#root.methodName",allEntries = true)
    public Patron updatePatron(Patron updatedPatron, Long id) {
        Patron oldPatron = patronRepository.findById(id)
                .orElseThrow(PatronNotFoundException::new);
        updatedPatron.setId(oldPatron.getId());
        patronRepository.save(updatedPatron);
        return updatedPatron;
    }

    @CacheEvict(value = "books", key = "#root.methodName",allEntries = true)
    public void deletePatronById(Long id) {
        patronRepository.deleteById(id);
    }



}
