package fr.insee.rmes.magmafusion.api.testcontainers.utils;

import fr.insee.rmes.magmafusion.api.testcontainers.config.GraphDBTestContainerConfig;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.io.IOException;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=security.disabled")
public abstract class TestsIntegration {

    @Autowired
    protected RestTestClient restTestClient;

    protected final TestsResourceLoader resourceLoader = new TestsResourceLoader();
    protected final StringUtils stringUtils = new StringUtils();

    @BeforeAll
    static void setupContainer() {
        GraphDBTestContainerConfig.startContainer();
    }

    protected String loadExpectedJson(String resourceName) throws IOException {
        return resourceLoader.loadResource(resourceName);
    }

    protected String bodyAsString(byte[] body) {
        return stringUtils.bytesToString(body);
    }
}
