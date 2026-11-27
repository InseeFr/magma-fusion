package fr.insee.rmes.magmafusion.services.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TestsResourceLoaderService {
    private static final String BASE_PATH = "services/";

    public static String loadResource(String resourceName) throws IOException {
        return new String(
                Objects.requireNonNull(
                        TestsResourceLoaderService.class.getClassLoader().getResourceAsStream(BASE_PATH + resourceName)
                ).readAllBytes(),
                StandardCharsets.UTF_8
        );
    }
}