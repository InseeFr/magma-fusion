package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestTestClient
@Tag("integration")
class GeoArrondissementMunicipalQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/arrondissementMunicipal/{code}")
    class GetCogArrMu {

        @Test
        @DisplayName("When getcogarrmu 75101, returns ArrMun 75101")
        void should_return_arrmu_75101_when_getcogarrmu_75101() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissementMunicipal/{code}?date={date}", "75101", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-municipal-75101-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogarrmu 75199 (inexistant), returns 404")
        void should_return_404_when_getcogarrmu_75199_inexistant() {
            restTestClient.get()
                    .uri("/geo/arrondissementMunicipal/{code}?date={date}", "75199", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/arrondissementMunicipal/{code}/ascendants")
    class GetCogArrMuAsc {

        @Test
        @DisplayName("When getcogarrmuasc 75101 type null, returns 5 ascendants")
        void should_return_5_ascendants_when_getcogarrmuasc_75101_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissementMunicipal/{code}/ascendants?date={date}", "75101", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-municipal-75101-ascendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogarrmuasc 75101 type Commune, returns 1 commune")
        void should_return_1_commune_when_getcogarrmuasc_75101_type_commune() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissementMunicipal/{code}/ascendants?date={date}&type={type}",
                            "75101", "2025-01-01", "Commune")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-municipal-75101-ascendants-commune-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/arrondissementsMunicipaux")
    class GetCogArrMuListe {

        @Test
        @DisplayName("When getcogarrmuliste date=2025-01-01, returns 1 ArrMun actif")
        void should_return_1_arrmu_when_getcogarrmuliste_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissementsMunicipaux?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissements-municipaux-liste-date-expected.json"),
                    bodyAsString(body),
                    false
            );
        }

        @Test
        @DisplayName("When getcogarrmuliste date=*, returns 2 ArrMun")
        void should_return_2_arrmu_when_getcogarrmuliste_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissementsMunicipaux?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissements-municipaux-liste-etoile-expected.json"),
                    bodyAsString(body),
                    false
            );
        }
    }

    @Nested
    @DisplayName("geo/arrondissementMunicipal/{code}/precedents")
    class GetCogArrMuPrec {

        @Test
        @DisplayName("When getcogarrmuprec 75101, returns 1 precedent (75102)")
        void should_return_1_precedent_when_getcogarrmuprec_75101() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissementMunicipal/{code}/precedents?date={date}", "75101", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-municipal-75101-precedents-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogarrmuprec 75102 (no precedents), returns 404")
        void should_return_404_when_getcogarrmuprec_75102_no_precedents() {
            restTestClient.get()
                    .uri("/geo/arrondissementMunicipal/{code}/precedents?date={date}", "75102", "1995-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/arrondissementMunicipal/{code}/projetes")
    class GetCogArrMuProj {

        @Test
        @DisplayName("When getcogarrmuproj dateProjection null, returns 400")
        void should_return_400_when_getcogarrmuproj_dateProjection_null() {
            restTestClient.get()
                    .uri("/geo/arrondissementMunicipal/75101/projetes?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("When getcogarrmuproj dateProjection empty, returns 400")
        void should_return_400_when_getcogarrmuproj_dateProjection_empty() {
            restTestClient.get()
                    .uri("/geo/arrondissementMunicipal/75101/projetes?dateProjection=&date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("When getcogarrmuproj 75101 dateProjection=1995-01-01, returns projection (75102)")
        void should_return_1_projete_when_getcogarrmuproj_75101() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissementMunicipal/{code}/projetes?dateProjection={dateProjection}&date={date}",
                            "75101", "1995-01-01", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-municipal-75101-projetes-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/arrondissementMunicipal/{code}/suivants")
    class GetCogArrMuSuiv {

        @Test
        @DisplayName("When getcogarrmusuiv 75101 (actif, pas de suivant), returns 404")
        void should_return_404_when_getcogarrmusuiv_75101_no_suivants() {
            restTestClient.get()
                    .uri("/geo/arrondissementMunicipal/{code}/suivants?date={date}", "75101", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }

        @Test
        @DisplayName("When getcogarrmusuiv 75102, returns 1 suivant (75101)")
        void should_return_1_suivant_when_getcogarrmusuiv_75102() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissementMunicipal/{code}/suivants?date={date}", "75102", "1995-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-municipal-75102-suivants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}