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
class GeoDepartementQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/departement/{code}/ascendants")
    class GetCogDepAsc {

        @Test
        @DisplayName("When getcogdepasc 10 type null, returns 1 ascendant (Region 99)")
        void should_return_1_region_when_getcogdepasc_10_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/departement/{code}/ascendants?date={date}", "10", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("departement-10-ascendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogdepasc 10 type Region, returns 1 ascendant (Region 99)")
        void should_return_1_region_when_getcogdepasc_10_type_region() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/departement/{code}/ascendants?date={date}&type={type}",
                            "10", "2025-01-01", "Region")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("departement-10-ascendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/departement/{code}")
    class GetCogDep {

        @Test
        @DisplayName("When getcogdep 10, returns departement 10")
        void should_return_departement_10_when_getcogdep_10() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/departement/{code}?date={date}", "10", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("departement-10-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogdep 00 (inexistant), returns 404")
        void should_return_404_when_getcogdep_00_inexistant() {
            restTestClient.get()
                    .uri("/geo/departement/{code}?date={date}", "00", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/departement/{code}/descendants")
    class GetCogDepDesc {

        @Test
        @DisplayName("When getcogdepdesc 10 type Commune filtreNom='Commune test 1', returns 1 commune")
        void should_return_1_commune_when_getcogdepdesc_10_type_commune_filtreNom() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/departement/{code}/descendants?date={date}&type={type}&filtreNom={filtreNom}",
                            "10", "2025-01-01", "Commune", "Commune test 1")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("departement-10-descendants-commune-filtreNom-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogdepdesc 10 type null, returns 16 descendants (3 arr + 1 arrmu + 2 can + 2 cov + 3 com + 1 comas + 2 comdel + 1 iris + 1 qpv)")
        void should_return_16_descendants_when_getcogdepdesc_10_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/departement/{code}/descendants?date={date}", "10", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("departement-10-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/departements")
    class GetCogDepts {

        @Test
        @DisplayName("When getcogdepts date=2025-01-01, returns 1 departement actif")
        void should_return_1_departement_when_getcogdepts_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/departements?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("departements-liste-date-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogdepts date=*, returns 2 departements (actif + supprime)")
        void should_return_2_departements_when_getcogdepts_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/departements?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("departements-liste-etoile-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/departement/{code}/precedents")
    class GetCogDepPrec {

        @Test
        @DisplayName("When getcogdepprec 10, returns 1 precedent (dept 11)")
        void should_return_1_precedent_when_getcogdepprec_10() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/departement/{code}/precedents?date={date}", "10", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("departement-10-precedents-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogdepprec 11 (no precedents), returns 404")
        void should_return_404_when_getcogdepprec_11_no_precedents() {
            restTestClient.get()
                    .uri("/geo/departement/{code}/precedents?date={date}", "11", "1995-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/departement/{code}/projetes")
    class GetCogDepProj {

        @Test
        @DisplayName("When getcogdepproj dateProjection null, returns 400")
        void should_return_400_when_getcogdepproj_dateProjection_null() {
            restTestClient.get()
                    .uri("/geo/departement/10/projetes?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("When getcogdepproj dateProjection empty, returns 400")
        void should_return_400_when_getcogdepproj_dateProjection_empty() {
            restTestClient.get()
                    .uri("/geo/departement/10/projetes?dateProjection=&date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("When getcogdepproj 10 dateProjection=1995-01-01, returns projection (dept 11)")
        void should_return_1_projete_when_getcogdepproj_10() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/departement/{code}/projetes?dateProjection={dateProjection}&date={date}",
                            "10", "1995-01-01", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("departement-10-projetes-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/departement/{code}/suivants")
    class GetCogDepSuiv {

        @Test
        @DisplayName("When getcogdepsuiv 10 (actif, pas de suivant), returns 404")
        void should_return_404_when_getcogdepsuiv_10_no_suivants() {
            restTestClient.get()
                    .uri("/geo/departement/{code}/suivants?date={date}", "10", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }

        @Test
        @DisplayName("When getcogdepsuiv 11, returns 1 suivant (dept 10)")
        void should_return_1_suivant_when_getcogdepsuiv_11() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/departement/{code}/suivants?date={date}", "11", "1995-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("departement-11-suivants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}