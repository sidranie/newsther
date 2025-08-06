package fr.sidranie.newsther.password;

public class PasswordConstraintValidator {
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{10,}$";

    public static boolean isValid(String password) {
        return password.matches(PASSWORD_REGEX);
    }
}
