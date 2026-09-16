package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.DatasetsEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
class DatasetsQueriesTest extends TestContainer {

    static final String DATASET_ID = "idDatasetTest";
    static final String DATASET_ID_2 = "idDatasetTest2";

    @Autowired
    DatasetsEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("dataset/{id}")
    class GetDataSetById {

        @Test
        @DisplayName("When getDataSetById, returns full dataset")
        void should_return_full_dataset_when_getDataSetById() throws Exception {
            var response = endpoints.getDataSetById(DATASET_ID);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/dataset-idDatasetTest-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getDataSetById with unknown id, returns 404")
        void should_return_404_when_getDataSetById_unknown_id() throws Exception {
            mockMvc.perform(get("/dataset/id-inconnu"))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("datasets/list")
    class GetListDatasets {

        @Test
        @DisplayName("When getListDatasets, returns datasets with correct fields")
        void should_return_dataset_list_with_correct_fields() throws Exception {
            var response = endpoints.getListDatasets(null);
            var result = response.getBody();

            assertNotNull(result);

            String data = objectMapper.writeValueAsString(result);

            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/dataset-list-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );

            JSONAssert.assertEquals(expected, data, false);
        }

        @Test
        @DisplayName("When getListDatasets with dateMiseAJour before modified date, returns one of the datasets")
        void should_return_one_dataset_when_dateMiseAJour_is_before_modified_date() throws IOException, JSONException {
            var response = endpoints.getListDatasets("2025-01-01T00:00:00.000");
            var result = response.getBody();

            String data = objectMapper.writeValueAsString(result);

            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/dataset-list2-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );

            JSONAssert.assertEquals(expected, data, false);

        }

        @Test
        @DisplayName("When getListDatasets with dateMiseAJour after modified date, returns empty list")
        void should_return_empty_list_when_dateMiseAJour_is_after_modified_date() {
            var response = endpoints.getListDatasets("2026-12-10T00:00:00.000");
            var result = response.getBody();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("dataset/{id}/distributions")
    class GetDataSetDistributionsById {

        @Test
        @DisplayName("When getDataSetDistributionsById, returns distributions for the dataset")
        void should_return_full_distributions_when_getDataSetDistributionsById() throws Exception {
            var response = endpoints.getDataSetDistributionsById(DATASET_ID);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/dataset-idDatasetTest-distributions-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getDataSetDistributionsById, returns empty list for dataset with no distributions")
        void should_return_empty_list_when_getDataSetDistributionsById_dataset_without_distributions() {
            var response = endpoints.getDataSetDistributionsById(DATASET_ID_2);
            var result = response.getBody();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}