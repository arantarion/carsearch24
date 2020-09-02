package org.bonn.se2.services.util;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.StringLengthValidator;

import java.util.regex.Pattern;

/**
 * @author Henry Weckermann
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class PasswordValidator extends StringLengthValidator {

    public final Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");

    public PasswordValidator(String errorMessage, Integer minLength, Integer maxLength) {
        super(errorMessage, minLength, maxLength);
    }

    public PasswordValidator() {
        super("", 8, 1000);
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        ValidationResult result = super.apply(value, context);
        if (result.isError()) {
            return ValidationResult.error("Ihr Passwort muss min. 8 Zeichen lang sein");
        } else if (!hasDigit(value) || !hasLetter(value) || !isPassword(value)) {
            return ValidationResult.error("Das Passwort muss mind. eine Zahl, einen großen und einen kleinen Buchstaben haben");
        }
        return result;
    }

    public boolean isPassword(String pwd) {
        return textPattern.matcher(pwd).matches();
    }

    public boolean hasDigit(String pwd) {
        return pwd.chars().anyMatch(Character::isDigit);
    }

    public boolean hasLetter(String pwd) {
        return pwd.chars().anyMatch(Character::isLetter);
    }
}
