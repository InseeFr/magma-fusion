package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.web.util.UriBuilder;

import java.net.URI;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestTestClient
@Tag("integration")
class ConceptsQueriesTest extends TestContainer {

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
            byte[] body = restTestClient.get()
                    .uri("/concepts/definition/{id}", conceptId)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("concept-" + conceptId + "-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getConceptById with unknown id, returns 404")
        void should_return_404_when_getConceptById_unknown_id() {
            restTestClient.get()
                    .uri("/concepts/definition/c9999")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("concepts/definitions")
    class GetConceptsList {

        private static final String LIST_PATH = "/concepts/definitions";

        private URI buildListUri(UriBuilder uriBuilder, String libelle, String collection) {
            uriBuilder.path(LIST_PATH);
            if (libelle != null) {
                uriBuilder.queryParam("libelle", libelle);
            }
            if (collection != null) {
                uriBuilder.queryParam("collection", collection);
            }
            return uriBuilder.build();
        }

        @ParameterizedTest(name = "{2}")
        @CsvSource({
                "concept test,                 , 'When getConceptsList with libelle filter, returns matching concepts', concepts-list-libelle-concept-test-expected.json",
                "             , idCollectionTest, 'When getConceptsList with collection filter, returns concepts in collection', concepts-list-collection-idCollectionTest-expected.json",
                "peuplement   , idCollectionTest, 'When getConceptsList with libelle and collection, returns filtered concepts', concepts-list-peuplement-collection-idCollectionTest-expected.json"
        })
        void should_return_filtered_concepts_when_getConceptsList(
                String libelle, String collection, String displayName, String expectedFile) throws Exception {

            byte[] body = restTestClient.get()
                    .uri(uriBuilder -> buildListUri(uriBuilder, libelle, collection))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson(expectedFile),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getConceptsList with no filter, returns 13 concepts")
        void should_return_all_concepts_when_getConceptsList_no_filter() {
            restTestClient.get()
                    .uri(uriBuilder -> buildListUri(uriBuilder, "", null))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.length()").isEqualTo(13);
        }
    }
}