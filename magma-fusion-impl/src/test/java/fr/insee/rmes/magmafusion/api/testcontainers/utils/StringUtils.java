package fr.insee.rmes.magmafusion.api.testcontainers.utils;

import java.nio.charset.StandardCharsets;

public class StringUtils {
    public static String bytesToString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
