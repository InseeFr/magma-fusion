package fr.insee.rmes.magmafusion.api.testcontainers.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TestResourceLoader {
    private static final String BASE_PATH = "testcontainers/";

    public static String loadResource(String resourceName) throws IOException {
        return new String(
                Objects.requireNonNull(
                        TestResourceLoader.class.getClassLoader().getResourceAsStream(BASE_PATH + resourceName)
                ).readAllBytes(),
                StandardCharsets.UTF_8
        );
    }
}
