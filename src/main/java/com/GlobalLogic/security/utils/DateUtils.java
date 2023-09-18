package com.GlobalLogic.security.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

  public static String formatDate(Date date , String pattern) {
    DateFormat dateFormat = new SimpleDateFormat(pattern);
    return dateFormat.format(date);
  }

}
