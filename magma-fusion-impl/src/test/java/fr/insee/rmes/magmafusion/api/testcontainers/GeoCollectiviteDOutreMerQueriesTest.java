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
class GeoCollectiviteDOutreMerQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/collectiviteDOutreMer/{code}")
    class GetCogColl {

        @Test
        @DisplayName("When getcogcoll 986, returns COM 986")
        void should_return_com_986_when_getcogcoll_986() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/collectiviteDOutreMer/{code}?date={date}", "986", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("collectivite-d-outre-mer-986-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcoll 989 (inexistant), returns 404")
        void should_return_404_when_getcogcoll_989_inexistant() {
            restTestClient.get()
                    .uri("/geo/collectiviteDOutreMer/{code}?date={date}", "989", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/collectiviteDOutreMer/{code}/descendants")
    class GetCogCollDes {

        @Test
        @DisplayName("When getcogcolldes 986 type null, returns 2 descendants (CT 98601, district 98610)")
        void should_return_2_descendants_when_getcogcolldes_986_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/collectiviteDOutreMer/{code}/descendants?date={date}", "986", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("collectivite-d-outre-mer-986-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcolldes 986 type CirconscriptionTerritoriale, returns 1 descendant (CT 98601)")
        void should_return_1_descendant_when_getcogcolldes_986_type_circonscriptionTerritoriale() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/collectiviteDOutreMer/{code}/descendants?date={date}&type={type}",
                            "986", "2025-01-01", "CirconscriptionTerritoriale")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("collectivite-d-outre-mer-986-descendants-circonscriptionTerritoriale-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/collectivitesDOutreMer")
    class GetCogCollListe {

        @Test
        @DisplayName("When getcogcollliste date=2025-01-01, returns 1 COM active (986)")
        void should_return_1_com_when_getcogcollliste_date() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/collectivitesDOutreMer?date={date}", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("collectivites-d-outre-mer-liste-date-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogcollliste date=*, returns 2 COMs (986, 987)")
        void should_return_2_coms_when_getcogcollliste_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/collectivitesDOutreMer?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("collectivites-d-outre-mer-liste-etoile-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}