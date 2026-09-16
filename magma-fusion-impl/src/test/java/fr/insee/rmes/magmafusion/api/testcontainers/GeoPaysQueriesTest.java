package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsPays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestTestClient
@Tag("integration")
class GeoPaysQueriesTest extends TestContainer {

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
    @DisplayName("geo/pays/{code}")
    class GetCogPays {

        @Test
        @DisplayName("When getcogpays 99901, returns pays 99901")
        void should_return_pays_99901_when_getcogpays() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/pays/{code}?date={date}", "99901", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("pays-99901-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogpays 99999 (inexistant), returns 404")
        void should_return_404_when_getcogpays_99999_inexistant() {
            restTestClient.get()
                    .uri("/geo/pays/{code}?date={date}", "99999", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/pays/{code}/descendants")
    class GetCogPaysDes {

        @Test
        @DisplayName("When getcogpaysdesc 99901 type null, returns 1 territoire")
        void should_return_1_territoire_when_getcogpaysdesc_99901_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/pays/{code}/descendants?date={date}", "99901", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("pays-99901-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogpaysdesc 99901 type Territoire, returns 1 territoire")
        void should_return_1_territoire_when_getcogpaysdesc_99901_type_territoire() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/pays/{code}/descendants?date={date}&type={type}",
                            "99901", "2025-01-01", "Territoire")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("pays-99901-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogpaysdesc type Region, returns 400")
        void should_return_400_when_getcogpaysdesc_type_region() {
            restTestClient.get()
                    .uri("/geo/pays/99901/descendants?date=2025-01-01&type=Region")
                    .exchange()
                    .expectStatus().isBadRequest();
        }
    }

    @Nested
    @DisplayName("geo/pays")
    class GetCogPaysListe {

        @Test
        @DisplayName("When getcogpayslist date=2025-01-01, returns 1 pays actif")
        void should_return_1_pays_when_getcogpayslist_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/pays?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("pays-liste-date-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogpayslist date=*, returns 2 pays (historique)")
        void should_return_2_pays_when_getcogpayslist_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/pays?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("pays-liste-etoile-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/pays/{code}/precedents")
    class GetCogPaysPrec {

        @Test
        @DisplayName("When getcogpaysprec 99901, returns 1 precedent (99902)")
        void should_return_1_precedent_when_getcogpaysprec_99901() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/pays/{code}/precedents?date={date}", "99901", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("pays-99901-precedents-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/pays/{code}/suivants")
    class GetCogPaysSuiv {

        @Test
        @DisplayName("When getcogpayssuiv 99902, returns 1 suivant (99901)")
        void should_return_1_suivant_when_getcogpayssuiv_99902() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/pays/{code}/suivants?date={date}", "99902", "2005-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("pays-99902-suivants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}