package com.maids.cc.Library_Management_System.entity_validation;


import com.maids.cc.Library_Management_System.model.Patron;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/*
* Opposite to BookValidation i tested each field in isolation
*
* */

public class PatronValidationTest {

    Validator validator;
    Patron patronValid;
    @BeforeEach
    void setup() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        patronValid = Patron.builder()
                .name("01234")
                .contactInfo("01111111111")
                .build();
    }

    private void validateField(String errorMessage) {
        Set<ConstraintViolation<Patron>> violations = validator.validate(patronValid);
        Set<String> errorMessages = ValidationUtils.extractErrorMessages(violations);
        assertThat(violations.size()).isEqualTo(1);
        assertThat(errorMessages).contains(errorMessage);
    }


    @Test
    void validationSuccess() {
        Set<ConstraintViolation<Patron>> violations = validator.validate(patronValid);
        assertThat(violations).isEmpty();
    }

    @Test
    void validateNameNotBlank() {
        patronValid.setName(null);
        validateField("Name can't be blank");
    }



    @Test
    void validateNameSize() {
        patronValid.setName("1");
        validateField("Name must be between 2-50 characters");
    }

    @Test
    void validatePhone() {
        patronValid.setContactInfo("0035669987");
        validateField("Please enter a valid number");
    }

    @Test
    void validateBlankPhone() {
        patronValid.setContactInfo(null);
        validateField("ContactInfo can't be blank");
    }









}
