package org.opensingular.requirement.module.spring.security.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SingularCryptUtil {

    private static final SingularCryptUtil INSTANCE = new SingularCryptUtil();

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private SingularCryptUtil() {
    }

    public static SingularCryptUtil getInstance() {
        return INSTANCE;
    }

    public boolean matches(CharSequence message, String encoded) {
        if (message != null && encoded != null) {
            return encoder.matches(message, encoded);
        }
        return false;
    }

    public String encode(CharSequence message) {
        return encoder.encode(message);
    }
}
