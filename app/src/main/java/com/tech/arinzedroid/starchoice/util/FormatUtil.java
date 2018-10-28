package com.tech.arinzedroid.starchoice.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FormatUtil {

    public static String formatPrice(double amt){
        NumberFormat nf = new DecimalFormat("#,##0.00");
        return "₦" + nf.format(amt);
    }
}
