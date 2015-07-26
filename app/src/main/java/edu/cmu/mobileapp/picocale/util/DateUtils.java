package edu.cmu.mobileapp.picocale.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by srikrishnan_suresh on 25-07-2015.
 */
public class DateUtils {
    public static String getDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(date);
    }
}
