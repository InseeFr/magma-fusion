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
class GeoRegionQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/region/{code}")
    class GetCogReg {

        @Test
        @DisplayName("When getcogreg 99, returns region 99")
        void should_return_region_99_when_getcogreg_99() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/region/{code}?date={date}", "99", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("region-99-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogreg 00 (inexistant), returns 404")
        void should_return_404_when_getcogreg_00_inexistant() {
            restTestClient.get()
                    .uri("/geo/region/{code}?date={date}", "00", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/region/{code}/descendants")
    class GetCogRegDes {

        @Test
        @DisplayName("When getcogregdes 99 type null, returns 17 descendants")
        void should_return_17_descendants_when_getcogregdes_99_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/region/{code}/descendants?date={date}", "99", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("region-99-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogregdes 99 type Departement filtreNom='Departement test', returns 1 departement")
        void should_return_1_departement_when_getcogregdes_99_type_departement_filtreNom() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/region/{code}/descendants?date={date}&type={type}&filtreNom={filtreNom}",
                            "99", "2025-01-01", "Departement", "Departement test")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("region-99-descendants-departement-filtreNom-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/regions")
    class GetCogRegListe {

        @Test
        @DisplayName("When getcogregliste date=2025-01-01, returns 1 region active")
        void should_return_1_region_when_getcogregliste_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/regions?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("regions-liste-date-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogregliste date=*, returns 2 regions")
        void should_return_2_regions_when_getcogregliste_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/regions?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("regions-liste-etoile-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/region/{code}/precedents")
    class GetCogRegPrec {

        @Test
        @DisplayName("When getcogregprec 99, returns 1 precedent (region 88)")
        void should_return_1_precedent_when_getcogregprec_99() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/region/{code}/precedents?date={date}", "99", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("region-99-precedents-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogregprec 88 (no precedents), returns 404")
        void should_return_404_when_getcogregprec_88_no_precedents() {
            restTestClient.get()
                    .uri("/geo/region/{code}/precedents?date={date}", "88", "1995-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/region/{code}/projetes")
    class GetCogRegProj {

        @Test
        @DisplayName("When getcogregproj dateProjection null, returns 400")
        void should_return_400_when_getcogregproj_dateProjection_null() {
            restTestClient.get()
                    .uri("/geo/region/99/projetes?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("When getcogregproj dateProjection empty, returns 400")
        void should_return_400_when_getcogregproj_dateProjection_empty() {
            restTestClient.get()
                    .uri("/geo/region/99/projetes?dateProjection=&date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("When getcogregproj 99 dateProjection=1995-01-01, returns projection (region 88)")
        void should_return_1_projete_when_getcogregproj_99() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/region/{code}/projetes?dateProjection={dateProjection}&date={date}",
                            "99", "1995-01-01", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("region-99-projetes-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/region/{code}/suivants")
    class GetCogRegSuiv {

        @Test
        @DisplayName("When getcogregsuiv 99 (actif, pas de suivant), returns 404")
        void should_return_404_when_getcogregsuiv_99_no_suivants() {
            restTestClient.get()
                    .uri("/geo/region/{code}/suivants?date={date}", "99", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }

        @Test
        @DisplayName("When getcogregsuiv 88, returns 1 suivant (region 99)")
        void should_return_1_suivant_when_getcogregsuiv_88() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/region/{code}/suivants?date={date}", "88", "1995-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("region-88-suivants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}