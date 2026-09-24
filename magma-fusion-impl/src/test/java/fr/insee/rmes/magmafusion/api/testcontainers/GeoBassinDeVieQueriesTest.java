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
class GeoBassinDeVieQueriesTest extends TestContainer {

    @Nested
    @DisplayName("geo/bassinDeVie2022/{code}")
    class GetCogBass {

        @Test
        @DisplayName("When getcogbass 88001, returns BV 88001")
        void should_return_bv_88001_when_getcogbass_88001() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/bassinDeVie2022/{code}?date={date}", "88001", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("bassin-de-vie-88001-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogbass 88099 (inexistant), returns 404")
        void should_return_404_when_getcogbass_88099_inexistant() {
            restTestClient.get()
                    .uri("/geo/bassinDeVie2022/{code}?date={date}", "88099", "2025-01-01")
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    @DisplayName("geo/bassinDeVie2022/{code}/descendants")
    class GetCogBassDes {

        @Test
        @DisplayName("When getcogbassdes 88001 type null, returns 4 descendants (2 communes + 2 comdel)")
        void should_return_4_descendants_when_getcogbassdes_88001_type_null() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/bassinDeVie2022/{code}/descendants?date={date}", "88001", "2025-01-01")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("bassin-de-vie-88001-descendants-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogbassdes 88001 type Commune, returns 2 communes")
        void should_return_2_communes_when_getcogbassdes_88001_type_commune() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/bassinDeVie2022/{code}/descendants?date={date}&type={type}",
                            "88001", "2025-01-01", "Commune")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("bassin-de-vie-88001-descendants-commune-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }

    @Nested
    @DisplayName("geo/bassinsDeVie2022")
    class GetCogBassListe {

        @Test
        @DisplayName("When getcogbassliste date=2025-01-01 filtreNom='Bassin de vie test 1', returns 1 BV")
        void should_return_1_bv_when_getcogbassliste_filtre_nom() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/bassinsDeVie2022?date={date}&filtreNom={filtreNom}",
                            "2025-01-01", "Bassin de vie test 1")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("bassins-de-vie-liste-filtre-expected.json"),
                    bodyAsString(body),
                    true
            );
        }

        @Test
        @DisplayName("When getcogbassliste date=*, returns 2 BV")
        void should_return_2_bv_when_getcogbassliste_etoile() throws Exception {
            byte[] body = restTestClient.get()
                    .uri("/geo/bassinsDeVie2022?date={date}", "*")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .returnResult()
                    .getResponseBody();

            JSONAssert.assertEquals(
                    loadExpectedJson("bassins-de-vie-liste-etoile-expected.json"),
                    bodyAsString(body),
                    true
            );
        }
    }
}