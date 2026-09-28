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
class GeoCommuneAssocieeQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/communeAssociee/{code}")
    class GetCogComA {

        @Test
        @DisplayName("When getcogcoma 99101, returns commune associee 99101")
        void should_return_commune_associee_99101_when_getcogcoma_99101() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/communeAssociee/{code}?date={date}", "99101", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-associee-99101-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcoma 99100 (inexistant), returns 404")
        void should_return_404_when_getcogcoma_99100_inexistant() {
            restTestClient.get()
                    .uri("/geo/communeAssociee/{code}?date={date}", "99100", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/communeAssociee/{code}/ascendants")
    class GetCogComAAsc {

        @Test
        @DisplayName("When getcogcomaasc 99101 type null, returns 5 ascendants (aav T01, arr 991, com 99001, dep 10, reg 99)")
        void should_return_5_ascendants_when_getcogcomaasc_99101_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/communeAssociee/{code}/ascendants?date={date}", "99101", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-associee-99101-ascendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomaasc 99101 type Departement, returns 1 departement")
        void should_return_1_departement_when_getcogcomaasc_99101_type_departement() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/communeAssociee/{code}/ascendants?date={date}&type={type}",
                            "99101", "2025-01-01", "Departement")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-associee-99101-ascendants-departement-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/communesAssociees")
    class GetCogComAListe {

        @Test
        @DisplayName("When getcogcomaliste date=2025-01-01, returns 1 commune associee active (99101)")
        void should_return_1_commune_associee_when_getcogcomaliste_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/communesAssociees?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("communes-associees-liste-date-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomaliste date=*, returns 2 communes associees (99101, 99102)")
        void should_return_2_communes_associees_when_getcogcomaliste_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/communesAssociees?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("communes-associees-liste-etoile-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}