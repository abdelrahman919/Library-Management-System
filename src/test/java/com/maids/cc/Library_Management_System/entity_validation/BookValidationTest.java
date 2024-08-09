package com.maids.cc.Library_Management_System.entity_validation;


import com.maids.cc.Library_Management_System.model.Book;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/*
* In this suite I grouped the tests by type
* meaning each method validates all fields of the same type
* If a new validation is added just add its message to the corresponding test method
* */


public class BookValidationTest {

    Validator validator;
    Book validBook;
    @BeforeEach
    void setup() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        validBook = Book.builder()
                .title("-")
                .genre("-")
                .isbn("0123456789")
                .author("-")
                .totalCopies(1)
                .publicationYear(LocalDate.of(2024,1,12))
                .build();
    }


    @Test
    void testValidBook() {
        Set<ConstraintViolation<Book>> validate = validator.validate(validBook);
        assertThat(validate).isEmpty();
    }

    @Test
    void validateNotBlankFields() {
        Book book = Book.builder()
                .totalCopies(1)
                .publicationYear(validBook.getPublicationYear())
                .build();
        //Contains messages of all @NonBlank fields
        //To add a field to the validation test just add its method here
        var notBlankErrorMsgList = List.of("genre can't be blank", "Author can't be blank"
                , "Title can't be blank", "ISBN can't be blank");
        //The number of fields is dynamic, so we only add message
        int nonBlankFields = notBlankErrorMsgList.size();
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        Set<String> actualErrorMessages = ValidationUtils.extractErrorMessages(violations);
        assertThat(violations.size()).isEqualTo(nonBlankFields);
        assertThat(actualErrorMessages.containsAll(notBlankErrorMsgList))
                .isTrue();
    }

    @Test
    void validateSizeFields() {
        validBook.setIsbn("0");
        var sizeErrorMsgList = List.of("ISBN must be between 10 - 13 digits");
        int numberOfSizeValidatedFields = sizeErrorMsgList.size();
        Set<ConstraintViolation<Book>> violations = validator.validate(validBook);
        Set<String> violationMessages = ValidationUtils.extractErrorMessages(violations);
        assertThat(violationMessages.containsAll(sizeErrorMsgList)).isTrue();
        assertThat(numberOfSizeValidatedFields).isEqualTo(violations.size());
    }

    @Test
    void validatePositiveFields() {
        validBook.setTotalCopies(0);
        Set<ConstraintViolation<Book>> violations = validator.validate(validBook);
        var positiveErrorMsgList = List.of("Total copies can't be less than or equal to zero");
        int positiveFieldsNumber = positiveErrorMsgList.size();
        Set<String> errorMessages = ValidationUtils.extractErrorMessages(violations);
        assertThat(errorMessages).containsAll(positiveErrorMsgList);
        assertThat(violations.size()).isEqualTo(positiveFieldsNumber);
    }




}
