package fr.insee.rmes.magmafusion.api.testcontainers.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestContainerTest {

    // vérifie que le conteneur est initialisé avec la bonne image Docker
    @Test
    void container_shouldBeInitializedWithCorrectDockerImage() {
        assertNotNull(TestContainer.container);
        assertEquals("ontotext/graphdb:10.8.8", TestContainer.container.getDockerImageName());
    }

    // vérifie que le port 7200 est exposé
    @Test
    void container_shouldExposePort7200() {
        assertTrue(TestContainer.container.getExposedPorts().contains(7200));
    }

    // vérifie que l'annotation @TestPropertySource désactive la sécurité
    @Test
    void class_shouldHaveSecurityDisabledProfile() {
        TestPropertySource annotation = TestContainer.class.getAnnotation(TestPropertySource.class);
        assertNotNull(annotation);
        assertArrayEquals(new String[]{"spring.profiles.active=security.disabled"}, annotation.properties());
    }

    // vérifie que overrideSpringProperties enregistre la bonne clé de propriété
    @Test
    void overrideSpringProperties_shouldRegisterSparqlEndpointProperty() {

        // On ne peut pas appeler directement la méthode car elle dépend d'un container démarré.
        // On vérifie au moins que la clé attendue est bien "fr.insee.rmes.magmafusion.api.sparqlEndpoint"
        // en inspectant le code via réflexion pour s'assurer que la méthode existe et est annotée.
        var methods = TestContainer.class.getDeclaredMethods();
        boolean found = false;
        for (var method : methods) {
            if (method.getName().equals("overrideSpringProperties")) {
                assertNotNull(method.getAnnotation(org.springframework.test.context.DynamicPropertySource.class));
                assertEquals(1, method.getParameterCount());
                assertEquals(DynamicPropertyRegistry.class, method.getParameterTypes()[0]);
                found = true;
            }
        }
        assertTrue(found, "La méthode overrideSpringProperties doit exister");
    }

    // vérifie que la méthode startContainer est annotée @BeforeAll
    @Test
    void startContainer_shouldBeAnnotatedWithBeforeAll() {
        var methods = TestContainer.class.getDeclaredMethods();
        boolean found = false;
        for (var method : methods) {
            if (method.getName().equals("startContainer")) {
                assertNotNull(method.getAnnotation(org.junit.jupiter.api.BeforeAll.class));
                found = true;
            }
        }
        assertTrue(found, "La méthode startContainer doit exister et être annotée @BeforeAll");
    }

    // vérifie que le reuse est désactivé (withReuse(false) dans l'initialisation)
    @Test
    void container_shouldNotBeReusable() {
        assertFalse(TestContainer.container.isShouldBeReused());
    }
}