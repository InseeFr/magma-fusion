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
class NomenclaturesQueriesTest extends BaseIntegrationTest {

    @BeforeAll
    static void installContainer() {
        GraphDBTestContainerConfig.startContainer();
    }

    @DynamicPropertySource
    static void overrideSpringProperties(DynamicPropertyRegistry registry) {
        GraphDBTestContainerConfig.overrideSpringProperties(registry);
    }


    @Nested
    @DisplayName("codes/nomenclatures")
    class GetClassifications {

        @Test
        @DisplayName("When getNomenclaturesListe, returns 3 classifications")
        void should_return_all_concepts_when_getNomenclaturesList() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/codes/nomenclatures")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            String responseBody = bodyAsString(body);

            JSONAssert.assertEquals(
                    loadExpectedJson("nomenclatures-list-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("codes/{nomenclature}")
    class GetClassification {

        @Test
        @DisplayName("When getNomenclature, returns full classifications")
        void should_return_all_concepts_when_getNomenclature() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/codes/naf4")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            //help for debug
//            String responseBody = bodyAsString(body);

            JSONAssert.assertEquals(
                    loadExpectedJson("nomenclature-naf4-expected.json"),
                    bodyAsString(body),
                    true
            );
        }


        @Test
        @DisplayName("When getNomenclature with unknown nomenclature, returns 404")
        void should_return_404_when_getNomenclature_unknown_nomenclature() {
            restTestClient.get()
                    .uri("/codes/naf9999")
                    .exchange()
                    .expectStatus().isNotFound();
        }

    }

    @Nested
    @DisplayName("codes/{nomenclature}/{niveau}/{code}")
    class GetClassificationByCode {

        @Test
        @DisplayName("When testclassif/niveauA/T01, returns code T01")
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
        @DisplayName("When testclassif2/niveauB/A01, returns code A01")
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