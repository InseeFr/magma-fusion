package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoCollectiviteDOutreMerEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsCollectiviteDOutreMer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
class GeoCollectiviteDOutreMerQueriesTest extends TestContainer {

    @Autowired
    GeoCollectiviteDOutreMerEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/collectiviteDOutreMer/{code}")
    class GetCogColl {

        @Test
        @DisplayName("When getcogcoll 986, returns COM 986")
        void should_return_com_986_when_getcogcoll_986() throws Exception {
            var response = endpoints.getcogcoll("986", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/collectivite-d-outre-mer-986-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcoll 989 (inexistant), returns 404")
        void should_return_404_when_getcogcoll_989_inexistant() throws Exception {
            mockMvc.perform(get("/geo/collectiviteDOutreMer/989")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("geo/collectiviteDOutreMer/{code}/descendants")
    class GetCogCollDes {

        @Test
        @DisplayName("When getcogcolldes 986 type null, returns 1 descendant (CT 98601)")
        void should_return_1_descendant_when_getcogcolldes_986_type_null() throws Exception {
            var response = endpoints.getcogcolldes("986", LocalDate.of(2025, 1, 1), null, null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/collectivite-d-outre-mer-986-descendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcolldes 986 type CirconscriptionTerritoriale, returns 1 descendant (CT 98601)")
        void should_return_1_descendant_when_getcogcolldes_986_type_circonscriptionTerritoriale() throws Exception {
            var response = endpoints.getcogcolldes("986", LocalDate.of(2025, 1, 1), TypeEnumDescendantsCollectiviteDOutreMer.CIRCONSCRIPTION_TERRITORIALE, null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/collectivite-d-outre-mer-986-descendants-circonscriptionTerritoriale-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/collectivitesDOutreMer")
    class GetCogCollListe {

        @Test
        @DisplayName("When getcogcollliste date=2025-01-01, returns 1 COM active (986)")
        void should_return_1_com_when_getcogcollliste_date() throws Exception {
            var response = endpoints.getcogcollliste("2025-01-01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/collectivites-d-outre-mer-liste-date-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }

        @Test
        @DisplayName("When getcogcollliste date=*, returns 2 COMs (986, 987)")
        void should_return_2_coms_when_getcogcollliste_etoile() throws Exception {
            var response = endpoints.getcogcollliste("*");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/collectivites-d-outre-mer-liste-etoile-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }
    }
}