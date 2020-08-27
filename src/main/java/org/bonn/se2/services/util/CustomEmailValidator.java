package org.bonn.se2.services.util;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.EmailValidator;

public class CustomEmailValidator extends EmailValidator {


    public CustomEmailValidator(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        ValidationResult result = super.apply(value, context);
        if (result.isError()) {
            return ValidationResult.error("Bitte geben Sie eine gültige E-Mail an");
        } else if (!isCorrectEmail(value)) {
            return ValidationResult.error("Nehmen Sie bitte eine gültige CarSearch24 E-Mail Adresse");
        }
        return result;
    }

    private boolean isCorrectEmail(String value) {
        return value.endsWith("@carsearch24.de");
    }
}
