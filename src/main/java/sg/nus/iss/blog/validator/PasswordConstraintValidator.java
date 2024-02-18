package sg.nus.iss.blog.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sg.nus.iss.blog.validation.ValidPassword;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(final ValidPassword constraintAnnotation) {}

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        
        String specialCharacters = ".*[!@#$%^&*()_+].*";

        // Other validation checks (length, digit, upper-case, lower-case)
        boolean meetsLengthRequirement = password.length() >= 8;
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasAlphabet = password.matches(".*[A-Z].*") || password.matches(".*[a-z].*");
        boolean hasSpecial = password.matches(specialCharacters);

        return meetsLengthRequirement && hasDigit && hasAlphabet && hasSpecial;
    }
}
