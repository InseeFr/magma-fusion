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
class GeoAireDAttractionDesVillesQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/aireDAttractionDesVilles2020/{code}")
    class GetCogAav {

        @Test
        @DisplayName("When getcogaav T01, returns AAV T01")
        void should_return_aav_T01_when_getcogaav_T01() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/aireDAttractionDesVilles2020/{code}?date={date}", "T01", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("aav-T01-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogaav T99 (inexistant), returns 404")
        void should_return_404_when_getcogaav_T99_inexistant() {
            restTestClient.get()
                    .uri("/geo/aireDAttractionDesVilles2020/{code}?date={date}", "T99", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/aireDAttractionDesVilles2020/{code}/descendants")
    class GetCogAavDesc {

        @Test
        @DisplayName("When getcogaavdesc T01 type null, returns 9 descendants")
        void should_return_9_descendants_when_getcogaavdesc_T01_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/aireDAttractionDesVilles2020/{code}/descendants?date={date}", "T01", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("aav-T01-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogaavdesc T01 type Commune, returns 3 communes")
        void should_return_3_communes_when_getcogaavdesc_T01_type_commune() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/aireDAttractionDesVilles2020/{code}/descendants?date={date}&type={type}",
                            "T01", "2025-01-01", "Commune")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("aav-T01-descendants-commune-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/airesDAttractionDesVilles2020")
    class GetCogAavListe {

        @Test
        @DisplayName("When getcogaavliste date=2025-01-01, returns 1 AAV active")
        void should_return_1_aav_when_getcogaavliste_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/airesDAttractionDesVilles2020?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("aav-liste-date-expected.json"),
                    bodyAsString(body),
                    false
            );
        }

        @Test
        @DisplayName("When getcogaavliste date=*, returns 2 AAVs")
        void should_return_2_aav_when_getcogaavliste_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/airesDAttractionDesVilles2020?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("aav-liste-etoile-expected.json"),
                    bodyAsString(body),
                    false
            );
        }
    }
}