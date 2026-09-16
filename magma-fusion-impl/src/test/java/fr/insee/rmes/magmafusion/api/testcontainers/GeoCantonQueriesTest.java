package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnum;
import fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCanton;
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
class GeoCantonQueriesTest extends TestContainer {

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
    @DisplayName("geo/canton/{code}")
    class GetCogCan {

        @Test
        @DisplayName("When getcogcan 9901, returns canton 9901")
        void should_return_canton_9901_when_getcogcan_9901() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/canton/{code}?date={date}", "9901", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-9901-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcan 9900 (inexistant), returns 404")
        void should_return_404_when_getcogcan_9900_inexistant() {
            restTestClient.get()
                    .uri("/geo/canton/{code}?date={date}", "9900", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/canton/{code}/ascendants")
    class GetCogCanAsc {

        @Test
        @DisplayName("When getcogcanasc 9901 type null, returns 2 ascendants (dep 10, reg 99)")
        void should_return_2_ascendants_when_getcogcanasc_9901_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/canton/{code}/ascendants?date={date}", "9901", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-9901-ascendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcanasc 9901 type Departement, returns 1 departement")
        void should_return_1_departement_when_getcogcanasc_9901_type_departement() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/canton/{code}/ascendants?date={date}&type={type}",
                            "9901", "2025-01-01", "Departement")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-9901-ascendants-departement-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/canton/{code}/communes")
    class GetCogCanCom {

        @Test
        @DisplayName("When getcogcancom 9901, returns 2 communes (99001, 99002)")
        void should_return_2_communes_when_getcogcancom_9901() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/canton/{code}/communes?date={date}", "9901", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-9901-communes-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/cantons")
    class GetCogCanListe {

        @Test
        @DisplayName("When getcogcanliste date=2025-01-01, returns 2 cantons actifs")
        void should_return_2_cantons_when_getcogcanliste_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/cantons?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("cantons-liste-date-expected.json"),
                    bodyAsString(body),
                    false
            );
        }

        @Test
        @DisplayName("When getcogcanliste date=*, returns 3 cantons")
        void should_return_3_cantons_when_getcogcanliste_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/cantons?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("cantons-liste-etoile-expected.json"),
                    bodyAsString(body),
                    false
            );
        }
    }

    @Nested
    @DisplayName("geo/canton/{code}/precedents")
    class GetCogCanPrec {

        @Test
        @DisplayName("When getcogcanprec 9901, returns 1 precedent (9903)")
        void should_return_1_precedent_when_getcogcanprec_9901() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/canton/{code}/precedents?date={date}", "9901", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-9901-precedents-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcanprec 9902 (no precedents), returns 404")
        void should_return_404_when_getcogcanprec_9902_no_precedents() {
            restTestClient.get()
                    .uri("/geo/canton/{code}/precedents?date={date}", "9902", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/canton/{code}/projetes")
    class GetCogCanProj {

        @Test
        @DisplayName("When getcogcanproj 9901 dateProjection=1985-01-01, returns 1 projete (9903)")
        void should_return_1_projete_when_getcogcanproj_9901() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/canton/{code}/projetes?dateProjection={dateProjection}&date={date}",
                            "9901", "1985-01-01", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-9901-projetes-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcanproj dateProjection null, returns 400")
        void should_return_400_when_getcogcanproj_dateProjection_null() {
            restTestClient.get()
                    .uri("/geo/canton/9901/projetes?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("When getcogcanproj dateProjection empty, returns 400")
        void should_return_400_when_getcogcanproj_dateProjection_empty() {
            restTestClient.get()
                    .uri("/geo/canton/9901/projetes?dateProjection=&date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }
    }

    @Nested
    @DisplayName("geo/canton/{code}/suivants")
    class GetCogCanSuiv {

        @Test
        @DisplayName("When getcogcansuiv 9903, returns 1 suivant (9901)")
        void should_return_1_suivant_when_getcogcansuiv_9903() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/canton/{code}/suivants?date={date}", "9903", "1985-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-9903-suivants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcansuiv 9901 (actif, pas de suivant), returns 404")
        void should_return_404_when_getcogcansuiv_9901_no_suivants() {
            restTestClient.get()
                    .uri("/geo/canton/{code}/suivants?date={date}", "9901", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/canton/{code}/intersections")
    class GetCogCanIntersect {

        @Test
        @DisplayName("When getcogcanintersect 9901 type Commune, returns 2 communes")
        void should_return_2_communes_when_getcogcanintersect_9901_type_commune() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/canton/{code}/intersections?date={date}&type={type}",
                            "9901", "2025-01-01", "Commune")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("canton-9901-intersections-commune-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}