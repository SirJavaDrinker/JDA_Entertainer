package dev.javadrinker.entertainer.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
//
public class FormatDouble {
    public static double moneyDoubleFormat(Double number) {

        BigDecimal bd = BigDecimal.valueOf(number);
        bd = bd.setScale(2, RoundingMode.UP);
        double returnNumber = bd.doubleValue();

        return returnNumber;
    }
}
