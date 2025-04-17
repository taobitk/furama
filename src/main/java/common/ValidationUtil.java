
package common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final String CUSTOMER_ID_REGEX = "^KH-\\d{4}$";
    private static final String SERVICE_ID_REGEX = "^DV-\\d{4}$";
    private static final String PHONE_REGEX = "^0\\d{9,10}$";
    private static final String ID_CARD_REGEX = "^\\d{9}|\\d{12}$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String POSITIVE_INTEGER_REGEX = "^[1-9]\\d*$";
    private static final String DATE_FORMAT_REGEX = "^\\d{2}/\\d{2}/\\d{4}$";

    public static boolean isValidCustomerId(String customerId) {
        if (customerId == null) return false;
        return Pattern.matches(CUSTOMER_ID_REGEX, customerId);
    }

    public static boolean isValidServiceId(String serviceId) {
        if (serviceId == null) return false;
        return Pattern.matches(SERVICE_ID_REGEX, serviceId);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return false;
        return Pattern.matches(PHONE_REGEX, phoneNumber);
    }

    public static boolean isValidIdCard(String idCard) {
        if (idCard == null) return false;
        return Pattern.matches(ID_CARD_REGEX, idCard);
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isPositiveInteger(String numberStr) {
        if (numberStr == null || numberStr.isEmpty()) {
            return false;
        }
        if (!Pattern.matches(POSITIVE_INTEGER_REGEX, numberStr)) {
            return false;
        }
        try {
            int number = Integer.parseInt(numberStr);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isPositiveDouble(String numberStr) {
        if (numberStr == null || numberStr.isEmpty()) {
            return false;
        }
        try {
            double number = Double.parseDouble(numberStr);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || !Pattern.matches(DATE_FORMAT_REGEX, dateStr)) {
            return false;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            LocalDate.parse(dateStr, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}