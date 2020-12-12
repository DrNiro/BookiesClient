package com.dts.bookies.util;

import java.util.regex.Matcher;

public class Functions {

    public static boolean isValidEmail(String email) {
        Matcher matcher = Constants.VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

}
