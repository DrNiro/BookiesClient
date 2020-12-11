package com.dts.bookies.util;

import java.util.regex.Pattern;

public class Constants {
	final public static String BASE_URL = "https://10.0.2.2:8081/"; // 10.0.2.2 is the computer's localhost, while 127.0.0.1 is the phone emulator localhost..

	final public static String DELIMITER = ";";
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	public static final String SPACE_NAME = "2021a.hadar.bonavida";

}
