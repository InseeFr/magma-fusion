package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestTestClient
@Tag("integration")
class GeoCantonOuVilleQueriesTest extends TestContainer {

    @Autowired
    private RestTestClient restTestClient;

    private String loadExpectedJson(String resourceName) throws IOException {
        return new String(
                Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("testcontainers/" + resourceName))
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
    }

    private String bodyAsString(byte[] body) {
        return new String(body, StandardCharsets.UTF_8);
    }

    @Nested
    @DisplayName("geo/cantonOuVille/{code}")
    class GetCogCanVil {

        @Test
        @DisplayName("When getcogcanvil 7701, returns canton-ou-ville 7701")
        void should_return_cov_7701_when_getcogcanvil_7701() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/cantonOuVille/{code}?date={date}", "7701", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-ou-ville-7701-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcanvil 7700 (inexistant), returns 404")
        void should_return_404_when_getcogcanvil_7700_inexistant() {
            restTestClient.get()
                    .uri("/geo/cantonOuVille/{code}?date={date}", "7700", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/cantonOuVille/{code}/ascendants")
    class GetCogCanVilAsc {

        @Test
        @DisplayName("When getcogcanvilasc 7701 type null, returns 2 ascendants (dept 10, region 99)")
        void should_return_2_ascendants_when_getcogcanvilasc_7701_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/cantonOuVille/{code}/ascendants?date={date}", "7701", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-ou-ville-7701-ascendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcanvilasc 7701 type Region, returns 1 region")
        void should_return_1_region_when_getcogcanvilasc_7701_type_region() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/cantonOuVille/{code}/ascendants?date={date}&type={type}",
                            "7701", "2025-01-01", "Region")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-ou-ville-7701-ascendants-region-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/cantonOuVille/{code}/descendants")
    class GetCogCanVilDes {

        @Test
        @DisplayName("When getcogcanvildes 7701 type null, returns 4 descendants (2 communes + 2 comdel)")
        void should_return_4_descendants_when_getcogcanvildes_7701_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/cantonOuVille/{code}/descendants?date={date}", "7701", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-ou-ville-7701-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcanvildes 7701 type Commune filtreNom='Commune test 3', returns 1 commune")
        void should_return_1_commune_when_getcogcanvildes_7701_type_commune_filtreNom() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/cantonOuVille/{code}/descendants?date={date}&type={type}&filtreNom={filtreNom}",
                            "7701", "2025-01-01", "Commune", "Commune test 3")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-ou-ville-7701-descendants-commune-filtreNom-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/cantonsEtVilles")
    class GetCogCanVilListe {

        @Test
        @DisplayName("When getcogcanvilliste date=2025-01-01, returns 2 cantons-ou-villes actifs")
        void should_return_2_cov_when_getcogcanvilliste_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/cantonsEtVilles?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("cantons-et-villes-liste-date-expected.json"),
                    bodyAsString(body),
                    false
            );
        }

        @Test
        @DisplayName("When getcogcanvilliste date=*, returns 3 cantons-ou-villes")
        void should_return_3_cov_when_getcogcanvilliste_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/cantonsEtVilles?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("cantons-et-villes-liste-etoile-expected.json"),
                    bodyAsString(body),
                    false
            );
        }
    }

    @Nested
    @DisplayName("geo/cantonOuVille/{code}/precedents")
    class GetCogCanVilPrec {

        @Test
        @DisplayName("When getcogcanvilprec 7701, returns 1 precedent (7703)")
        void should_return_1_precedent_when_getcogcanvilprec_7701() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/cantonOuVille/{code}/precedents?date={date}", "7701", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-ou-ville-7701-precedents-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcanvilprec 7702 (no precedents), returns 404")
        void should_return_404_when_getcogcanvilprec_7702_no_precedents() {
            restTestClient.get()
                    .uri("/geo/cantonOuVille/{code}/precedents?date={date}", "7702", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/cantonOuVille/{code}/projetes")
    class GetCogCanVilProj {

        @Test
        @DisplayName("When getcogcanvilproj dateProjection null, returns 400")
        void should_return_400_when_getcogcanvilproj_dateProjection_null() {
            restTestClient.get()
                    .uri("/geo/cantonOuVille/7701/projetes?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("When getcogcanvilproj dateProjection empty, returns 400")
        void should_return_400_when_getcogcanvilproj_dateProjection_empty() {
            restTestClient.get()
                    .uri("/geo/cantonOuVille/7701/projetes?dateProjection=&date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("When getcogcanvilproj 7701 dateProjection=2010-01-01, returns projection (7703)")
        void should_return_1_projete_when_getcogcanvilproj_7701() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/cantonOuVille/{code}/projetes?dateProjection={dateProjection}&date={date}",
                            "7701", "2010-01-01", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-ou-ville-7701-projetes-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/cantonOuVille/{code}/suivants")
    class GetCogCanVilSuiv {

        @Test
        @DisplayName("When getcogcanvilsuiv 7703, returns 1 suivant (7701)")
        void should_return_1_suivant_when_getcogcanvilsuiv_7703() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/cantonOuVille/{code}/suivants?date={date}", "7703", "2000-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-ou-ville-7703-suivants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcanvilsuiv 7701 (actif, pas de suivant), returns 404")
        void should_return_404_when_getcogcanvilsuiv_7701_no_suivants() {
            restTestClient.get()
                    .uri("/geo/cantonOuVille/{code}/suivants?date={date}", "7701", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }
}