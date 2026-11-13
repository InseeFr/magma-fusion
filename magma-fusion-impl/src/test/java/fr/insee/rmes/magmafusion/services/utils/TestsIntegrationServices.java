package fr.insee.rmes.magmafusion.services.utils;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=security.disabled")
public abstract class TestsIntegrationServices {


    protected final TestsResourceLoaderService resourceLoader = new TestsResourceLoaderService();

    protected String loadExpectedJson(String resourceName) throws IOException {
        return resourceLoader.loadResource(resourceName);
    }

}

