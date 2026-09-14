package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.ConceptsEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
class ConceptsQueriesTest extends TestContainer {

    @Autowired
    ConceptsEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("concepts/definition/{id}")
    class GetConceptById {

        @ParameterizedTest(name = "When getConceptById with {1}, returns full concept")
        @CsvSource({
                "c0001, conceptsSuivants",
                "c0002, conceptsPrecedents and conceptsReferences",
                "c0003, intitulesAlternatifs"
        })
        void should_return_full_concept_when_getConceptById(String conceptId, String description) throws Exception {
            var response = endpoints.getconcept(conceptId);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/concept-" + conceptId + "-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
        
        @Test
        @DisplayName("When getConceptById with unknown id, returns 404")
        void should_return_404_when_getConceptById_unknown_id() throws Exception {
            mockMvc.perform(get("/geo/concepts/definition/c9999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("concepts/definitions")
    class GetConceptsList {

        @Test
        @DisplayName("When getConceptsList with libelle filter, returns matching concepts")
        void should_return_matching_concepts_when_getConceptsList_libelle_concept_test() throws Exception {
            var response = endpoints.getconceptsliste("concept test", null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/concepts-list-libelle-concept-test-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getConceptsList with collection filter, returns concepts in collection")
        void should_return_collection_concepts_when_getConceptsList_collection_idCollectionTest() throws Exception {
            var response = endpoints.getconceptsliste(null, "idCollectionTest");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/concepts-list-collection-idCollectionTest-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getConceptsList with libelle and collection, returns filtered concepts")
        void should_return_filtered_concepts_when_getConceptsList_libelle_and_collection() throws Exception {
            var response = endpoints.getconceptsliste("peuplement", "idCollectionTest");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/concepts-list-peuplement-collection-idCollectionTest-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getConceptsList with no filter, returns 13 concepts")
        void should_return_all_concepts_when_getConceptsList_no_filter() {
            var response = endpoints.getconceptsliste("", null);
            var result = response.getBody();

            assertNotNull(result);
            assertEquals(13, result.size(), "Should contain exactly 13 test concepts, got " + result.size());
        }
    }
}