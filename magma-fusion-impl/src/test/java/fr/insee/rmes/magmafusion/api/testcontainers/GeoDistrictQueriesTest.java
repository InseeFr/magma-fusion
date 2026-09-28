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
class GeoDistrictQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/district/{code}")
    class GetCogDis {

        @Test
        @DisplayName("When getcogdis 98610, returns district 98610")
        void should_return_district_98610_when_getcogdis_98610() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/district/{code}?date={date}", "98610", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("district-98610-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogdis 98699 (inexistant), returns 404")
        void should_return_404_when_getcogdis_98699_inexistant() {
            restTestClient.get()
                    .uri("/geo/district/{code}?date={date}", "98699", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/district/{code}/ascendants")
    class GetCogDisAsc {

        @Test
        @DisplayName("When getcogdisasc 98610 type null, returns 1 ascendant (COM 986)")
        void should_return_1_ascendant_when_getcogdisasc_98610_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/district/{code}/ascendants?date={date}", "98610", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("district-98610-ascendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogdisasc 98610 type CollectiviteDOutreMer, returns 1 COM")
        void should_return_1_com_when_getcogdisasc_98610_type_collectiviteDOutreMer() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/district/{code}/ascendants?date={date}&type={type}",
                            "98610", "2025-01-01", "CollectiviteDOutreMer")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("district-98610-ascendants-collectiviteDOutreMer-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}