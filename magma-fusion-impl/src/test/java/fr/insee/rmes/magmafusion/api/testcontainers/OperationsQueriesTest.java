package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@SpringBootTest(properties = "--spring.profiles.active=security.disabled")
@AutoConfigureMockMvc
@Tag("integration")
class OperationsQueriesTest extends TestContainer {

    @Nested
    @DisplayName("operations/rapportQualite/{id}")
    class GetOperation {

        @Test
        @DisplayName("When getRapportQualiteByCode, returns rapport with all rubrique types (TEXT, DATE, RICH_TEXT, GEOGRAPHY, ORGANIZATION, CODE_LIST)")
        void should_return_rapportQualite_with_all_rubrique_types() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/operations/rapportQualite/{id}", "9999")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("rapportQualite-9999-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getRapportQualiteByCode for rapport without rubriques, returns rapport without rubriques")
        void should_return_rapportQualite_without_rubriques() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/operations/rapportQualite/{id}", "9998")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("rapportQualite-9998-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getRapportQualiteByCode with unknown id, returns 404")
        void should_return_404_when_rapportQualite_inexistant() {
            restTestClient.get()
                    .uri("/operations/rapportQualite/{id}", "0000")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }
}