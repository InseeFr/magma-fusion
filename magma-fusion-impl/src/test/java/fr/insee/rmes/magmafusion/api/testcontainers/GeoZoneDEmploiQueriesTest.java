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
class GeoZoneDEmploiQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/zoneDEmploi2020/{code}")
    class GetCogZe {

        @Test
        @DisplayName("When getcogze 9901, returns zone d'emploi 9901")
        void should_return_ze_9901_when_getcogze_9901() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/zoneDEmploi2020/{code}?date={date}", "9901", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("zone-d-emploi-9901-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogze 9999 (inexistant), returns 404")
        void should_return_404_when_getcogze_9999_inexistant() {
            restTestClient.get()
                    .uri("/geo/zoneDEmploi2020/{code}?date={date}", "9999", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/zoneDEmploi2020/{code}/descendants")
    class GetCogZeDesc {

        @Test
        @DisplayName("When getcogzedesc 9901 type null, returns 2 descendants")
        void should_return_2_descendants_when_getcogzedesc_9901_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/zoneDEmploi2020/{code}/descendants?date={date}", "9901", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("zone-d-emploi-9901-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogzedesc 9901 type Commune, returns 2 communes")
        void should_return_2_communes_when_getcogzedesc_9901_type_commune() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/zoneDEmploi2020/{code}/descendants?date={date}&type={type}",
                            "9901", "2025-01-01", "Commune")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("zone-d-emploi-9901-descendants-commune-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/zonesDEmploi2020")
    class GetCogZeListe {

        @Test
        @DisplayName("When getcogzeliste date 2025-01-01, returns 1 zone d'emploi")
        void should_return_1_ze_when_getcogzeliste_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/zonesDEmploi2020?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("zones-d-emploi-liste-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogzeliste date *, returns 1 zone d'emploi")
        void should_return_1_ze_when_getcogzeliste_date_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/zonesDEmploi2020?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("zones-d-emploi-liste-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}