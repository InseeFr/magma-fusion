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
class GeoIrisQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/iris/{code}")
    class GetCogIris {

        @Test
        @DisplayName("When getcogiris 990010101 (real IRIS), returns IRIS with typeDIris=H")
        void should_return_real_iris_990010101_when_getcogiris() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/iris/{code}?date={date}", "990010101", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("iris-990010101-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogiris 990020000 (faux-IRIS, commune non-irisee), returns commune as IRIS")
        void should_return_faux_iris_990020000_when_getcogiris() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/iris/{code}?date={date}", "990020000", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("iris-990020000-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogiris 990010000 (commune irisee + code 0000), returns 404")
        void should_return_404_when_getcogiris_990010000_irisee_0000() {
            restTestClient.get()
                    .uri("/geo/iris/{code}?date={date}", "990010000", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }

        @Test
        @DisplayName("When getcogiris 990020101 (commune non-irisee + code non-0000), returns 404")
        void should_return_404_when_getcogiris_990020101_non_irisee_non_0000() {
            restTestClient.get()
                    .uri("/geo/iris/{code}?date={date}", "990020101", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/iris/{code}/ascendants")
    class GetCogIrisAsc {

        @Test
        @DisplayName("When getcogirisasc 990010101 (real IRIS) type null, returns 5 ascendants")
        void should_return_5_ascendants_when_getcogirisasc_990010101_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/iris/{code}/ascendants?date={date}", "990010101", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("iris-990010101-ascendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogirisasc 990010101 (real IRIS) type Arrondissement, returns 1 arrondissement")
        void should_return_1_arrondissement_when_getcogirisasc_990010101() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/iris/{code}/ascendants?date={date}&type={type}",
                            "990010101", "2025-01-01", "Arrondissement")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("iris-990010101-ascendants-arrondissement-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogirisasc 990020000 (faux-IRIS) type null, returns 7 ascendants")
        void should_return_7_ascendants_when_getcogirisasc_990020000_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/iris/{code}/ascendants?date={date}", "990020000", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("iris-990020000-ascendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogirisasc 990020000 (faux-IRIS) type Arrondissement, returns 1 arrondissement")
        void should_return_1_arrondissement_when_getcogirisasc_990020000() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/iris/{code}/ascendants?date={date}&type={type}",
                            "990020000", "2025-01-01", "Arrondissement")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("iris-990020000-ascendants-arrondissement-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/iris")
    class GetCogIrisList {

        private URI buildListUri(UriBuilder uriBuilder, String date, Boolean com) {
            uriBuilder.path("/geo/iris");
            if (date != null) {
                uriBuilder.queryParam("date", date);
            }
            if (com != null) {
                uriBuilder.queryParam("com", com);
            }
            return uriBuilder.build();
        }

        @Test
        @DisplayName("When getcogirislist com=false (default), returns 3 entries (1 real iris + 2 faux-iris)")
        void should_return_3_entries_when_getcogirislist_com_false() throws Exception {
            byte[] body = restTestClient.get()
                    .uri(uriBuilder -> buildListUri(uriBuilder, "2025-01-01", null))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("iris-liste-com-false-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogirislist com=true, returns 5 entries (1 real iris + 4 faux-iris)")
        void should_return_5_entries_when_getcogirislist_com_true() throws Exception {
            byte[] body = restTestClient.get()
                    .uri(uriBuilder -> buildListUri(uriBuilder, "2025-01-01", true))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("iris-liste-com-true-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}