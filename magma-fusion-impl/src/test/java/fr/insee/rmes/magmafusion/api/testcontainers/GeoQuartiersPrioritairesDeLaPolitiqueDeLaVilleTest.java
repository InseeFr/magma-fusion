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
class GeoQuartiersPrioritairesDeLaPolitiqueDeLaVilleTest extends TestContainer {

    @Nested
    @DisplayName("geo/quartierPrioritaireDeLaPolitiqueDeLaVille2024/{code}")
    class GetCogQpv {

        @Test
        @DisplayName("When getcogqpv QN01001M, returns QPV QN01001M")
        void should_return_qpv_QN01001M_when_getcogqpv_QN01001M() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/quartierPrioritaireDeLaPolitiqueDeLaVille2024/{code}?date={date}",
                            "QN01001M", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("qpv-QN01001M-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogqpv QN01099M (inexistant), returns 404")
        void should_return_404_when_getcogqpv_QN01099M_inexistant() {
            restTestClient.get()
                    .uri("/geo/quartierPrioritaireDeLaPolitiqueDeLaVille2024/{code}?date={date}",
                            "QN01099M", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }

        @Test
        @DisplayName("When getcogqpv QJ01001M (code invalide), returns 400")
        void should_return_400_when_getcogqpv_QJ01001M_code_invalide() {
            restTestClient.get()
                    .uri("/geo/quartierPrioritaireDeLaPolitiqueDeLaVille2024/{code}?date={date}",
                            "QJ01001M", "2025-01-01")
                    .exchange()
                    .expectStatus().isBadRequest();
        }
    }

    @Nested
    @DisplayName("geo/quartiersPrioritairesDeLaPolitiqueDeLaVille2024")
    class GetCogQpvListe {

        @Test
        @DisplayName("When getcogqpvliste date=2025-01-01, returns 1 QPV")
        void should_return_1_qpv_when_getcogqpvliste_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/quartiersPrioritairesDeLaPolitiqueDeLaVille2024?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("qpv-liste-date-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/quartierPrioritaireDeLaPolitiqueDeLaVille2024/{code}/intersections")
    class GetCogQpvIntersect {

        @Test
        @DisplayName("When getcogqpvintersect QN01001M type null, returns 1 intersection (commune 99001)")
        void should_return_1_intersection_when_getcogqpvintersect_QN01001M_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/quartierPrioritaireDeLaPolitiqueDeLaVille2024/{code}/intersections?date={date}",
                            "QN01001M", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("qpv-QN01001M-intersections-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogqpvintersect QN01001M type Commune, returns 1 commune")
        void should_return_1_commune_when_getcogqpvintersect_QN01001M_type_commune() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/quartierPrioritaireDeLaPolitiqueDeLaVille2024/{code}/intersections?date={date}&type={type}",
                            "QN01001M", "2025-01-01", "Commune")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("qpv-QN01001M-intersections-commune-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}