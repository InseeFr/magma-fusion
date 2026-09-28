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
class GeoCommuneDelegueeQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/communeDeleguee/{code}")
    class GetCogComD {

        @Test
        @DisplayName("When getcogcomd 98001, returns commune deleguee 98001")
        void should_return_commune_deleguee_98001_when_getcogcomd_98001() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/communeDeleguee/{code}?date={date}", "98001", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-deleguee-98001-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomd 98099 (inexistant), returns 404")
        void should_return_404_when_getcogcomd_98099_inexistant() {
            restTestClient.get()
                    .uri("/geo/communeDeleguee/{code}?date={date}", "98099", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/communeDeleguee/{code}/ascendants")
    class GetCogComDAsc {

        @Test
        @DisplayName("When getcogcomdasc 98001 type null, returns 7 ascendants (aav T01, arr 991, bv 88001, cov 7701, com 99002, dep 10, reg 99)")
        void should_return_7_ascendants_when_getcogcomdasc_98001_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/communeDeleguee/{code}/ascendants?date={date}", "98001", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-deleguee-98001-ascendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomdasc 98001 type Departement, returns 1 departement")
        void should_return_1_departement_when_getcogcomdasc_98001_type_departement() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/communeDeleguee/{code}/ascendants?date={date}&type={type}",
                            "98001", "2025-01-01", "Departement")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-deleguee-98001-ascendants-departement-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomdasc 98001 date before creation, returns 404")
        void should_return_404_when_getcogcomdasc_98001_date_before_creation() {
            restTestClient.get()
                    .uri("/geo/communeDeleguee/{code}/ascendants?date={date}", "98001", "2010-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/communesDeleguees")
    class GetCogComDListe {

        @Test
        @DisplayName("When getcogcomdliste date=2025-01-01, returns 2 communes deleguees actives (98001, 98002)")
        void should_return_2_communes_deleguees_when_getcogcomdliste_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/communesDeleguees?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("communes-deleguees-liste-date-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomdliste date=*, returns 3 communes deleguees (98001, 98002, 98003)")
        void should_return_3_communes_deleguees_when_getcogcomdliste_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/communesDeleguees?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("communes-deleguees-liste-etoile-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}