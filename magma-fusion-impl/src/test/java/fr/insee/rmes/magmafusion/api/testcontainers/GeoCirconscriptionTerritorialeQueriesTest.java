package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestTestClient
@Tag("integration")
class GeoCirconscriptionTerritorialeQueriesTest extends TestContainer {

    @Autowired
    private RestTestClient restTestClient;

    private String loadExpectedJson(String resourceName) throws IOException {
        return new String(
                Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("testcontainers/" + resourceName))
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
    }

    private String bodyAsString(byte[] body) {
        return new String(body, StandardCharsets.UTF_8);
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