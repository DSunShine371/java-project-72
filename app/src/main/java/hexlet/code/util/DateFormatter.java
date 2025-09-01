package hexlet.code.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormatter {

    public static String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return formatter.format(timestamp);
    }
}
