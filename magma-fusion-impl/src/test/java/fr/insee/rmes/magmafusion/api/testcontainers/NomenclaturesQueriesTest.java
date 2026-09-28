package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
class NomenclaturesQueriesTest extends TestContainer {

    @Nested
    @DisplayName("codes/{nomenclature}/{niveau}/{code}")
    class GetClassificationByCode {

        @Test
        @DisplayName("When testclassif/niveauA/T01, returns concept T01")
        void should_return_concept_T01_when_testclassif_niveauA_T01() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/codes/{nomenclature}/{niveau}/{code}", "testclassif", "niveauA", "T01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("nomenclature-testclassif-T01-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When testclassif2/niveauB/A01, returns concept A01")
        void should_return_concept_A01_when_testclassif2_niveauB_A01() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/codes/{nomenclature}/{niveau}/{code}", "testclassif2", "niveauB", "A01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("nomenclature-testclassif2-A01-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When testclassif/niveauA/T99 (code inexistant), returns 404")
        void should_return_404_when_code_inexistant() {
            restTestClient.get()
                    .uri("/codes/{nomenclature}/{niveau}/{code}", "testclassif", "niveauA", "T99")
                    .exchange()
                    .expectStatus().isNotFound();
        }

        @Test
        @DisplayName("When inexistant/niveauA/T01 (nomenclature inexistante), returns 404")
        void should_return_404_when_nomenclature_inexistante() {
            restTestClient.get()
                    .uri("/codes/{nomenclature}/{niveau}/{code}", "inexistant", "niveauA", "T01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }
}