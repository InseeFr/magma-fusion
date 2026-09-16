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
class GeoIntercommunaliteQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/intercommunalite/{code}")
    class GetCogInterco {

        @Test
        @DisplayName("When getcoginterco 999000001, returns intercommunalite 999000001")
        void should_return_intercommunalite_999000001_when_getcoginterco() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/intercommunalite/{code}?date={date}", "999000001", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("intercommunalite-999000001-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcoginterco 999000099 (inexistant), returns 404")
        void should_return_404_when_getcoginterco_999000099_inexistant() {
            restTestClient.get()
                    .uri("/geo/intercommunalite/{code}?date={date}", "999000099", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/intercommunalite/{code}/descendants")
    class GetCogIntercoDes {

        @Test
        @DisplayName("When getcogintercodes 999000001 type null, returns 2 descendants (2 communes)")
        void should_return_2_descendants_when_getcogintercodes_999000001_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/intercommunalite/{code}/descendants?date={date}", "999000001", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("intercommunalite-999000001-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogintercodes 999000001 type Commune, returns 2 communes")
        void should_return_2_communes_when_getcogintercodes_999000001_type_commune() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/intercommunalite/{code}/descendants?date={date}&type={type}",
                            "999000001", "2025-01-01", "Commune")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("intercommunalite-999000001-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/intercommunalites")
    class GetCogIntercoListe {

        @Test
        @DisplayName("When getcogintercoliste date=2025-01-01 filtreNom='Intercommunalite test 1', returns 1 intercommunalite")
        void should_return_1_intercommunalite_when_getcogintercoliste_date_filtreNom() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/intercommunalites?date={date}&filtreNom={filtreNom}",
                            "2025-01-01", "Intercommunalite test 1")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("intercommunalite-liste-date-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogintercoliste date=* filtreNom='Intercommunalite test', returns 2 intercommunalites (historique)")
        void should_return_2_intercommunalites_when_getcogintercoliste_etoile_filtreNom() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/intercommunalites?date={date}&filtreNom={filtreNom}",
                            "*", "Intercommunalite test")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("intercommunalite-liste-etoile-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/intercommunalite/{code}/precedents")
    class GetCogIntercoPrec {

        @Test
        @DisplayName("When getcogintercoprec 999000001, returns 1 precedent (999000002)")
        void should_return_1_precedent_when_getcogintercoprec_999000001() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/intercommunalite/{code}/precedents?date={date}", "999000001", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("intercommunalite-999000001-precedents-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/intercommunalite/{code}/suivants")
    class GetCogIntercoSuiv {

        @Test
        @DisplayName("When getcogintercosuiv 999000002, returns 1 suivant (999000001)")
        void should_return_1_suivant_when_getcogintercosuiv_999000002() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/intercommunalite/{code}/suivants?date={date}", "999000002", "2005-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("intercommunalite-999000002-suivants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/intercommunalite/{code}/projetes")
    class GetCogIntercoProj {

        @Test
        @DisplayName("When getcogintercoproj 999000001 dateProjection=2005-01-01, returns 1 projete (999000002)")
        void should_return_1_projete_when_getcogintercoproj_999000001() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/intercommunalite/{code}/projetes?dateProjection={dateProjection}&date={date}",
                            "999000001", "2005-01-01", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("intercommunalite-999000001-projetes-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogintercoproj dateProjection null, returns 400")
        void should_return_400_when_getcogintercoproj_dateProjection_null() {
            restTestClient.get()
                    .uri("/geo/intercommunalite/999000001/projetes?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("When getcogintercoproj dateProjection empty, returns 400")
        void should_return_400_when_getcogintercoproj_dateProjection_empty() {
            restTestClient.get()
                    .uri("/geo/intercommunalite/999000001/projetes?dateProjection=&date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }
    }
}