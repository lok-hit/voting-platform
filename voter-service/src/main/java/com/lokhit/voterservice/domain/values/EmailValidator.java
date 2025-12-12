package com.lokhit.voterservice.domain.values;

import java.util.regex.Pattern;

final class EmailValidator {
    private static final Pattern P = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    static boolean isValid(String email) { return P.matcher(email).matches(); }
    private EmailValidator() {}
}

