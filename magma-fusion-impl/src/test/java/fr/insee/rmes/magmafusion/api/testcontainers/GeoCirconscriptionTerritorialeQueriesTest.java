package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.testcontainers.config.GraphDBTestContainerConfig;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestTestClient
@Tag("integration")
class GeoCirconscriptionTerritorialeQueriesTest extends BaseIntegrationTest {

    @BeforeAll
    static void setupContainer() {
        GraphDBTestContainerConfig.startContainer();
    }

    @DynamicPropertySource
    static void overrideSpringProperties(DynamicPropertyRegistry registry) {
        GraphDBTestContainerConfig.overrideSpringProperties(registry);
    }
    @Nested
    @DisplayName("geo/circonscriptionTerritoriale/{code}")
    class GetCogCir {

        @Test
        @DisplayName("When getcogcir 98601, returns CT 98601")
        void should_return_ct_98601_when_getcogcir_98601() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/circonscriptionTerritoriale/{code}?date={date}", "98601", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("circonscription-territoriale-98601-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcir 98600 (inexistant), returns 404")
        void should_return_404_when_getcogcir_98600_inexistant() {
            restTestClient.get()
                    .uri("/geo/circonscriptionTerritoriale/{code}?date={date}", "98600", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/circonscriptionTerritoriale/{code}/ascendants")
    class GetCogCirAsc {

        @Test
        @DisplayName("When getcogcirasc 98601 type null, returns 1 ascendant (COM 986)")
        void should_return_1_ascendant_when_getcogcirasc_98601_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/circonscriptionTerritoriale/{code}/ascendants?date={date}", "98601", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("circonscription-territoriale-98601-ascendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcirasc 98601 type CollectiviteDOutreMer, returns 1 ascendant (COM 986)")
        void should_return_1_ascendant_when_getcogcirasc_98601_type_collectiviteDOutreMer() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/circonscriptionTerritoriale/{code}/ascendants?date={date}&type={type}",
                            "98601", "2025-01-01", "CollectiviteDOutreMer")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("circonscription-territoriale-98601-ascendants-collectiviteDOutreMer-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}