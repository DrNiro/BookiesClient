package com.dts.bookies.util;

import java.util.regex.Pattern;

public class Constants {
//	final public static String BASE_URL = "http://192.168.1.22:8081/"; // Daniel's inner network ip.
	final public static String BASE_URL = "http://192.168.1.183:8081/"; // Nir's inner network ip.
//	final public static String BASE_URL = "http://77.126.13.137:8081/"; // Nir's ip address.

	final public static String BASE_BOOKS_API_URL = "https://www.googleapis.com/";
	final public static String BOOK_API_MAX_SIZE = "10";

	final public static String DELIMITER = ";";
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

//	between 6 to 50 chars, must contain at least 1 letter and at least 1 number, may contain special signs such as !@#$%^&*
	public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d^a-zA-Z0-9].{5,50}$");

	public static final String SPACE_NAME = "2021a.hadar.bonavida";

}
