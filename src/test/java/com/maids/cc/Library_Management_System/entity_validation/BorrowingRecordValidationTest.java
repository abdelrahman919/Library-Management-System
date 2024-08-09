package com.maids.cc.Library_Management_System.entity_validation;

import com.maids.cc.Library_Management_System.model.Book;
import com.maids.cc.Library_Management_System.model.BorrowingRecord;
import com.maids.cc.Library_Management_System.model.Patron;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class BorrowingRecordValidationTest {

    Validator validator;
    BorrowingRecord recordValid;

    @BeforeEach
    void setup() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        recordValid = BorrowingRecord.builder()
                .borrowDate(LocalDate.now())
                .book(new Book())
                .patron(new Patron())
                .build();
    }

    @Test
    void validateSuccessfully() {
        Set<ConstraintViolation<BorrowingRecord>> violations = validator.validate(recordValid);
        assertThat(violations).size().isEqualTo(0);
    }

    @Test
    void validateNotNull() {
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        var notNullErrorMessages = List.of("Book can't be null",
                "Patron can't be null", "Borrow date can't be null");
        int numberOfNotNullFields = notNullErrorMessages.size();
        Set<ConstraintViolation<BorrowingRecord>> violations = validator.validate(borrowingRecord);
        Set<String> errorMessages = ValidationUtils.extractErrorMessages(violations);
        assertThat(errorMessages).containsAll(notNullErrorMessages);
        assertThat(violations.size()).isEqualTo(numberOfNotNullFields);

    }





}
