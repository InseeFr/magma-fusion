package fr.insee.rmes.magmafusion.api.testcontainers.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;


@Slf4j
public class GraphDBTestContainerConfig {

    private static final GraphDBContainer container =
            new GraphDBContainer("ontotext/graphdb:10.8.8")
                    .withReuse(false);

    public static void startContainer() {
        if (!container.isRunning()) {
            container.start();
        }
    }

    @DynamicPropertySource
    public static void overrideSpringProperties(DynamicPropertyRegistry registry) {
        String url = "http://" + container.getHost() + ":" + container.getMappedPort(7200) + "/repositories/magmafusion";
        log.info("GraphDB URL: {}", url);
        registry.add("fr.insee.rmes.magmafusion.api.sparqlEndpoint", () -> url);
    }

    public static String getSparqlEndpoint() {
        return "http://" + container.getHost() + ":" + container.getMappedPort(7200) + "/repositories/magmafusion";
    }
}

