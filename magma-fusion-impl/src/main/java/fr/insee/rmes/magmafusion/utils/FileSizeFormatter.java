package fr.insee.rmes.magmafusion.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public final class FileSizeFormatter {

    private static final List<String> UNITS = List.of("o", "ko", "Mo", "Go", "To");
    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);

    private FileSizeFormatter() {
    }

    public static String humanReadable(String bytes) {
        long size;
        try {
            size = Long.parseLong(bytes == null ? "" : bytes.trim());
        } catch (NumberFormatException _) {
            return null;
        }
        if (size < 0) {
            return null;
        }
        BigDecimal value = BigDecimal.valueOf(size);
        int unit = 0;
        while (unit < UNITS.size() - 1 && rounded(value).compareTo(THOUSAND) >= 0) {
            value = value.divide(THOUSAND);
            unit++;
        }
        String number = rounded(value).stripTrailingZeros().toPlainString().replace('.', ',');
        return number + " " + UNITS.get(unit);
    }

    private static BigDecimal rounded(BigDecimal value) {
        return value.setScale(1, RoundingMode.HALF_UP);
    }
}
