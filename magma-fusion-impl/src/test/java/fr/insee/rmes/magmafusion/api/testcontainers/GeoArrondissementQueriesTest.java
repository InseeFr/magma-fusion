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
class GeoArrondissementQueriesTest extends TestContainer {

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
    @DisplayName("geo/arrondissement/{code}/ascendants")
    class GetCogArrAsc {

        @Test
        @DisplayName("When getcogarrasc 991 type null, returns 2 ascendants (Dept + Region)")
        void should_return_2_ascendants_when_getcogarrasc_991_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissement/{code}/ascendants?date={date}", "991", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-991-ascendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogarrasc 991 type Region, returns 1 ascendant (Region)")
        void should_return_1_region_when_getcogarrasc_991_type_region() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissement/{code}/ascendants?date={date}&type={type}",
                            "991", "2025-01-01", "Region")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-991-ascendants-region-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/arrondissement/{code}")
    class GetCogArr {

        @Test
        @DisplayName("When getcogarr 991, returns arrondissement 991")
        void should_return_arrondissement_991_when_getcogarr_991() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissement/{code}?date={date}", "991", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-991-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/arrondissement/{code}/descendants")
    class GetCogArrDes {

        @Test
        @DisplayName("When getcogarrdes 991 type CommuneDeleguee, returns 2 communes deleguees")
        void should_return_2_communeDeleguee_when_getcogarrdes_991_type_communeDeleguee() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissement/{code}/descendants?type={type}",
                            "991", "CommuneDeleguee")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-991-descendants-communeDeleguee-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogarrdes 991 type null, returns 8 descendants (1 arrmu + 2 com + 1 comas + 2 comdel + 1 iris + 1 qpv)")
        void should_return_8_descendants_when_getcogarrdes_991_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissement/{code}/descendants?date={date}", "991", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-991-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/arrondissements")
    class GetCogArrListe {

        @Test
        @DisplayName("When getcogarrliste date=2025-01-01, returns 3 active arrondissements")
        void should_return_3_arrondissements_when_getcogarrliste_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissements?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissements-liste-date-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogarrliste date=*, returns 7 arrondissements (actifs + supprimes)")
        void should_return_7_arrondissements_when_getcogarrliste_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissements?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissements-liste-etoile-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/arrondissement/{code}/precedents")
    class GetCogArrPrec {

        @Test
        @DisplayName("When getcogarrprec 991, returns 2 precedents (994, 995)")
        void should_return_2_precedents_when_getcogarrprec_991() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissement/{code}/precedents?date={date}", "991", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-991-precedents-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogarrprec 992 (no precedents), returns 404")
        void should_return_404_when_getcogarrprec_992_no_precedents() {
            restTestClient.get()
                    .uri("/geo/arrondissement/{code}/precedents?date={date}", "992", "2005-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/arrondissement/{code}/projetes")
    class GetCogArrProj {

        @Test
        @DisplayName("When getcogarrproj dateProjection null, returns 400")
        void should_return_400_when_getcogarrproj_dateProjection_null() {
            restTestClient.get()
                    .uri("/geo/arrondissement/991/projetes?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("When getcogarrproj dateProjection empty, returns 400")
        void should_return_400_when_getcogarrproj_dateProjection_empty() {
            restTestClient.get()
                    .uri("/geo/arrondissement/991/projetes?dateProjection=&date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("When getcogarrproj 991 dateProjection=2005-01-01, returns projections via predecessors chain")
        void should_return_projetes_when_getcogarrproj_991() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissement/{code}/projetes?dateProjection={dateProjection}&date={date}",
                            "991", "2005-01-01", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-991-projetes-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/arrondissement/{code}/suivants")
    class GetCogArrSuiv {

        @Test
        @DisplayName("When getcogarrsuiv 991 (actif, pas de suivants), returns 404")
        void should_return_404_when_getcogarrsuiv_991_no_suivants() {
            restTestClient.get()
                    .uri("/geo/arrondissement/{code}/suivants?date={date}", "991", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }

        @Test
        @DisplayName("When getcogarrsuiv 996, returns 3 suivants (994, 995, 997)")
        void should_return_3_suivants_when_getcogarrsuiv_996() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/arrondissement/{code}/suivants?date={date}", "996", "2008-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("arrondissement-996-suivants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}