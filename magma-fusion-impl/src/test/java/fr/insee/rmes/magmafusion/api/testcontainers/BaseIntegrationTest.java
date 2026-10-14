package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.testcontainers.config.GraphDBTestContainerConfig;
import fr.insee.rmes.magmafusion.api.testcontainers.utils.TestResourceLoader;
import fr.insee.rmes.magmafusion.api.testcontainers.utils.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.io.IOException;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=security.disabled")
public abstract class BaseIntegrationTest {

    @Autowired
    protected RestTestClient restTestClient;

    protected final TestResourceLoader resourceLoader = new TestResourceLoader();
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
