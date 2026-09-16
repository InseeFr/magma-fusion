package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@SpringBootTest(properties = "--spring.profiles.active=security.disabled")
@AutoConfigureMockMvc
@Tag("integration")
class SeriesOperationsQueriesTest extends TestContainer {

    static final String SERIE_ID = "idSeriePrincipaleTest";
    static final String OPERATION_ID = "idOperationTest";

    @Nested
    @DisplayName("operations/serie/{id}")
    class GetSerieById {

        @Test
        @DisplayName("When getSerieById (/operations/serie/{id} end-point), returns full serie")
        void should_return_serieById_idSeriePrincipaleTest_when_getSerieById_idSeriePrincipaleTest() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/operations/serie/{id}", SERIE_ID)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("serie-idSeriePrincipaleTest-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getSerieById (/operations/serie/{id} end-point) with unknown id, returns 404")
        void should_return_404_when_getSerieById_unknown_id() {
            restTestClient.get()
                    .uri("/operations/serie/{id}", "serieInconnue")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("operations/operation/{id}")
    class GetOperationById {

        @Test
        @DisplayName("When getOperationByCode (/operations/operation/{id} end-point), returns full operation")
        void should_return_operationById_idOperationTest_when_getOperationByCode_idOperationTest() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/operations/operation/{id}", OPERATION_ID)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("operation-idOperationTest-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getOperationByCode (/operations/serie/{id} end-point) with unknown id, returns 404")
        void should_return_404_when_getOperationById_unknown_id() {
            restTestClient.get()
                    .uri("/operations/operation/{id}", "serieInconnue")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("operations/series")
    class GetSeries {

        @Test
        @DisplayName("When getAllSeries (/operations/series end-point) without date filter, returns all series")
        void should_return_all_series_when_getAllSeries_without_dateFilter() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/operations/series")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("serie-list-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getAllSeries (/operations/series end-point) with date 2025-01-01, returns 1 filtered serie")
        void should_return_filtered_series_when_getAllSeries_with_date_2025_01_01() {
            restTestClient.get()
                    .uri("/operations/series?dateMiseAJour={dateMiseAJour}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.length()").isEqualTo(1)
                    .jsonPath("$[0].seriesId").isEqualTo("idSerieTest")
                    .jsonPath("$[0].uri").isEqualTo("http://bauhaus/operations/serie/idSerieTest");
        }

        @Test
        @DisplayName("When getAllSeries (/operations/series end-point) with date 2024-01-01, returns 2 filtered series")
        void should_return_filtered_series_when_getAllSeries_with_date_2024_01_01() {
            restTestClient.get()
                    .uri("/operations/series?dateMiseAJour={dateMiseAJour}", "2024-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.length()").isEqualTo(2)
                    .jsonPath("$[0].seriesId").isEqualTo("idSeriePrincipaleTest")
                    .jsonPath("$[1].seriesId").isEqualTo("idSerieTest");
        }

        @Test
        @DisplayName("When getAllSeries (/operations/series end-point), returns 200")
        void should_return_200_when_getAllSeries() {
            restTestClient.get()
                    .uri("/operations/series")
                    .exchange()
                    .expectStatus().isOk();
        }
    }

    @Nested
    @DisplayName("operations/indicateur/{id}")
    class GetIndicateurById {

        @Test
        @DisplayName("When getIndicatorById, returns full indicator")
        void should_return_full_indicateur_when_getIndicatorById_idIndicateurTest() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/operations/indicateur/{id}", "idIndicateurTest")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("indicateur-idIndicateurTest-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getIndicatorById with unknown id, returns 404")
        void should_return_404_when_getIndicatorById_unknown_id() {
            restTestClient.get()
                    .uri("/operations/indicateur/{id}", "indicateurInconnu")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }
}