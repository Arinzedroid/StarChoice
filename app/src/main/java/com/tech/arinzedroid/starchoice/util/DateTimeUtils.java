package com.tech.arinzedroid.starchoice.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    public static String parseDateTime(Date dateTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.ROOT);
        String mDateTime = "";
        try{
            mDateTime = simpleDateFormat.format(dateTime);
            return mDateTime;
        }catch (Exception ex){
            ex.printStackTrace();
            return mDateTime;
        }
    }
}
