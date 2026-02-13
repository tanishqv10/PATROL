package com.example.newsapplication;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static float convertDateToFloat(String dateStr) {
        try {
            Date date = dateFormat.parse(dateStr);
            // Convert date to milliseconds since January 1, 1970, 00:00:00 GMT
            long millis = date.getTime();
            // Convert milliseconds to days
            return millis / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}