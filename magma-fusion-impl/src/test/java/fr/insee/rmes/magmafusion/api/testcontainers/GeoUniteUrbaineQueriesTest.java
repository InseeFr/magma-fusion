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
class GeoUniteUrbaineQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/uniteUrbaine2020/{code}")
    class GetCogUu {

        @Test
        @DisplayName("When getcoguu 99101, returns unite urbaine 99101")
        void should_return_uu_99101_when_getcoguu_99101() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/uniteUrbaine2020/{code}?date={date}", "99101", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("unite-urbaine-99101-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcoguu 99199 (inexistant), returns 404")
        void should_return_404_when_getcoguu_99199_inexistant() {
            restTestClient.get()
                    .uri("/geo/uniteUrbaine2020/{code}?date={date}", "99199", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/uniteUrbaine2020/{code}/descendants")
    class GetCogUuDes {

        @Test
        @DisplayName("When getcoguudes 99101 type null, returns 2 descendants")
        void should_return_2_descendants_when_getcoguudes_99101_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/uniteUrbaine2020/{code}/descendants?date={date}", "99101", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("unite-urbaine-99101-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcoguudes 99101 type Commune, returns 2 communes")
        void should_return_2_communes_when_getcoguudes_99101_type_commune() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/uniteUrbaine2020/{code}/descendants?date={date}&type={type}",
                            "99101", "2025-01-01", "Commune")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("unite-urbaine-99101-descendants-commune-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/unitesUrbaines2020")
    class GetCogUuListe {

        @Test
        @DisplayName("When getcoguuliste date 2025-01-01, returns 1 unite urbaine")
        void should_return_1_uu_when_getcoguuliste_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/unitesUrbaines2020?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("unites-urbaines-liste-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcoguuliste date *, returns 1 unite urbaine")
        void should_return_1_uu_when_getcoguuliste_date_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/unitesUrbaines2020?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("unites-urbaines-liste-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}