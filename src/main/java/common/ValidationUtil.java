package common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

public class ValidationUtil {

    /** Regex cho Mã Khách hàng (VD: KH-1234) */
    private static final String CUSTOMER_ID_REGEX = "^KH-\\d{4}$";

    /** Regex cho Mã Dịch vụ (VD: DV-1234) */
    private static final String SERVICE_ID_REGEX = "^DV-\\d{4}$";

    /** Regex cho Số điện thoại Việt Nam (Bắt đầu 0, 10 hoặc 11 số) */
    private static final String PHONE_REGEX = "^0\\d{9,10}$";

    /** Regex cho Số CMND/CCCD (9 hoặc 12 số) */
    private static final String ID_CARD_REGEX = "^\\d{9}|\\d{12}$";

    /** Regex cho Email chuẩn */
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /** Regex cho số nguyên dương (lớn hơn 0) */
    private static final String POSITIVE_INTEGER_REGEX = "^[1-9]\\d*$";

    /** Regex kiểm tra định dạng ngày DD/MM/YYYY */
    private static final String DATE_FORMAT_REGEX = "^\\d{2}/\\d{2}/\\d{4}$";

    /** Định dạng ngày sử dụng cho việc parse và kiểm tra tính hợp lệ */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT); // STRICT để không chấp nhận ngày không tồn tại (vd: 31/02)

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
            Integer.parseInt(numberStr);
            return true;
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
        try {
            LocalDate.parse(dateStr, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}