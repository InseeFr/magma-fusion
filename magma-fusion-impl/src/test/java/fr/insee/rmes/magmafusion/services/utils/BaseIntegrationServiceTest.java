package fr.insee.rmes.magmafusion.services.utils;

import fr.insee.rmes.magmafusion.api.testcontainers.config.GraphDBTestContainerConfig;
import fr.insee.rmes.magmafusion.api.testcontainers.utils.StringUtils;
import fr.insee.rmes.magmafusion.api.testcontainers.utils.TestResourceLoader;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.io.IOException;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=security.disabled")
public abstract class BaseIntegrationServiceTest {


    protected final TestResourceLoaderServiceTest resourceLoader = new TestResourceLoaderServiceTest();

    protected String loadExpectedJson(String resourceName) throws IOException {
        return resourceLoader.loadResource(resourceName);
    }

}

