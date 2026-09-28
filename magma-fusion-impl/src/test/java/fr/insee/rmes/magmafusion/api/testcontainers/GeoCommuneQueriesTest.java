package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.web.util.UriBuilder;

import java.net.URI;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
class GeoCommuneQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/commune/{code}/ascendants")
    class GetCogComAsc {

        @Test
        @DisplayName("When getcogcomasc 99001 type null, returns 4 ascendants (arr, dept, region, aav)")
        void should_return_4_ascendants_when_getcogcomasc_99001_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/commune/{code}/ascendants?date={date}", "99001", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-99001-ascendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomasc 99001 type Departement, returns 1 ascendant (dept)")
        void should_return_1_departement_when_getcogcomasc_99001_type_departement() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/commune/{code}/ascendants?date={date}&type={type}",
                            "99001", "2025-01-01", "Departement")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-99001-ascendants-departement-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomasc 99001 type Arrondissement before creation, returns 404")
        void should_return_404_when_getcogcomasc_99001_type_arrondissement_before_creation() {
            restTestClient.get()
                    .uri("/geo/commune/{code}/ascendants?date={date}&type={type}",
                            "99001", "2005-01-01", "Arrondissement")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/commune/{code}")
    class GetCogCom {

        @Test
        @DisplayName("When getcogcom 99001, returns commune 99001")
        void should_return_commune_99001_when_getcogcom_99001() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/commune/{code}?date={date}", "99001", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-99001-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcom 99999 (inexistant), returns 404")
        void should_return_404_when_getcogcom_99999() {
            restTestClient.get()
                    .uri("/geo/commune/{code}?date={date}", "99999", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/commune/{code}/cantons")
    class GetCogComCan {

        @Test
        @DisplayName("When getcogcomcan 99001, returns 2 cantons (9901, 9902)")
        void should_return_2_cantons_when_getcogcomcan_99001() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/commune/{code}/cantons?date={date}", "99001", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-99001-cantons-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/commune/{code}/descendants")
    class GetCogComDesc {

        @Test
        @DisplayName("When getcogcomdesc 99001 type null, returns 4 descendants (1 arrmu + 1 comas + 1 iris + 1 qpv)")
        void should_return_4_descendants_when_getcogcomdesc_99001_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/commune/{code}/descendants?date={date}", "99001", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-99001-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomdesc 99002 type Iris, returns 404")
        void should_return_404_when_getcogcomdesc_99002_type_iris() {
            restTestClient.get()
                    .uri("/geo/commune/{code}/descendants?date={date}&type={type}",
                            "99002", "2025-01-01", "Iris")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/communes")
    class GetCogComListe {

        private URI buildListUri(UriBuilder uriBuilder, String date, String filtreNom, Boolean com) {
            uriBuilder.path("/geo/communes");
            if (date != null) {
                uriBuilder.queryParam("date", date);
            }
            if (filtreNom != null) {
                uriBuilder.queryParam("filtreNom", filtreNom);
            }
            if (com != null) {
                uriBuilder.queryParam("com", com);
            }
            return uriBuilder.build();
        }

        @Test
        @DisplayName("When getcogcomliste filtreNom='Commune test', returns 3 communes actives")
        void should_return_3_communes_when_getcogcomliste_filtreNom() throws Exception {
            byte[] body = restTestClient.get()
                    .uri(uriBuilder -> buildListUri(uriBuilder, "2025-01-01", "Commune test", false))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("communes-liste-filtreNom-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomliste date=*, returns 5 communes (actives + supprimees)")
        void should_return_5_communes_when_getcogcomliste_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri(uriBuilder -> buildListUri(uriBuilder, "*", null, null))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("communes-liste-etoile-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/commune/{code}/precedents")
    class GetCogComPrec {

        @Test
        @DisplayName("When getcogcomprec 99003, returns 2 precedents (99004, 99005)")
        void should_return_2_precedents_when_getcogcomprec_99003() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/commune/{code}/precedents?date={date}", "99003", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-99003-precedents-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomprec 99001 (no precedents), returns 404")
        void should_return_404_when_getcogcomprec_99001_no_precedents() {
            restTestClient.get()
                    .uri("/geo/commune/{code}/precedents?date={date}", "99001", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/commune/{code}/projetes")
    class GetCogComProj {

        @Test
        @DisplayName("When getcogcomproj 99003 dateProjection=2010-01-01, returns 2 projetes (99004, 99005)")
        void should_return_2_projetes_when_getcogcomproj_99003() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/commune/{code}/projetes?dateProjection={dateProjection}&date={date}",
                            "99003", "2010-01-01", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-99003-projetes-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomproj dateProjection null, returns 400")
        void should_return_400_when_getcogcomproj_dateProjection_null() {
            restTestClient.get()
                    .uri("/geo/commune/99001/projetes?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("When getcogcomproj dateProjection empty, returns 400")
        void should_return_400_when_getcogcomproj_dateProjection_empty() {
            restTestClient.get()
                    .uri("/geo/commune/99001/projetes?dateProjection=&date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }
    }

    @Nested
    @DisplayName("geo/commune/{code}/suivants")
    class GetCogComSuiv {

        @Test
        @DisplayName("When getcogcomsuiv 99004, returns 1 suivant (99003)")
        void should_return_1_suivant_when_getcogcomsuiv_99004() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/commune/{code}/suivants?date={date}", "99004", "2010-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-99004-suivants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomsuiv 99001 (active, no suivants), returns 404")
        void should_return_404_when_getcogcomsuiv_99001_no_suivants() {
            restTestClient.get()
                    .uri("/geo/commune/{code}/suivants?date={date}", "99001", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/commune/{code}/intersections")
    class GetCogComIntersect {

        @Test
        @DisplayName("When getcogcomintersect 99001 type null, returns 8 intersections")
        void should_return_8_intersections_when_getcogcomintersect_99001_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/commune/{code}/intersections?date={date}", "99001", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-99001-intersections-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcomintersect 99001 type Canton, returns 2 cantons")
        void should_return_2_cantons_when_getcogcomintersect_99001_type_canton() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/commune/{code}/intersections?date={date}&type={type}",
                            "99001", "2025-01-01", "Canton")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("commune-99001-intersections-canton-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}