package is.hi.hbv501gteam23.Security;

import org.passay.*;

import java.util.List;

public class PasswordValidationUtil {

    private static final org.passay.PasswordValidator validator = new org.passay.PasswordValidator (List.of(
        new LengthRule(8, 128),
        new WhitespaceRule()
    ));

    public static boolean isValid(String password) {
        RuleResult result = validator.validate(new PasswordData(password));
        return result.isValid();
    }
}
