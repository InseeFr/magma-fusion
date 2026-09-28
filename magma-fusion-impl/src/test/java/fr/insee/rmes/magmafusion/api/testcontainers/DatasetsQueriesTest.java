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
class DatasetsQueriesTest extends TestContainer {

    static final String DATASET_ID = "idDatasetTest";
    static final String DATASET_ID_2 = "idDatasetTest2";

    @Nested
    @DisplayName("dataset/{id}")
    class GetDataSetById {

        @Test
        @DisplayName("When getDataSetById, returns full dataset")
        void should_return_full_dataset_when_getDataSetById() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/dataset/{id}", DATASET_ID)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("dataset-idDatasetTest-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getDataSetById with unknown id, returns 404")
        void should_return_404_when_getDataSetById_unknown_id() {
            restTestClient.get()
                    .uri("/dataset/id-inconnu")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("datasets/list")
    class GetListDatasets {

        @Test
        @DisplayName("When getListDatasets, returns datasets with correct fields")
        void should_return_dataset_list_with_correct_fields() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/datasets/list")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("dataset-list-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getListDatasets with dateMiseAJour before modified date, returns one of the datasets of the database")
        void should_return_one_dataset_when_dateMiseAJour_is_before_modified_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/datasets/list?dateMiseAJour={dateMiseAJour}", "2025-01-01T00:00:00.000")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("dataset-list2-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getListDatasets with dateMiseAJour after modified date, returns empty list")
        void should_return_empty_list_when_dateMiseAJour_is_after_modified_date() {
            restTestClient.get()
                    .uri("/datasets/list?dateMiseAJour={dateMiseAJour}", "2026-12-10T00:00:00.000")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.length()").isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("dataset/{id}/distributions")
    class GetDataSetDistributionsById {

        @Test
        @DisplayName("When getDataSetDistributionsById, returns distributions for the dataset")
        void should_return_full_distributions_when_getDataSetDistributionsById() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/dataset/{id}/distributions", DATASET_ID)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("dataset-idDatasetTest-distributions-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getDataSetDistributionsById, returns empty list for dataset with no distributions")
        void should_return_empty_list_when_getDataSetDistributionsById_dataset_without_distributions() {
            restTestClient.get()
                    .uri("/dataset/{id}/distributions", DATASET_ID_2)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.length()").isEqualTo(0);
        }
    }
}