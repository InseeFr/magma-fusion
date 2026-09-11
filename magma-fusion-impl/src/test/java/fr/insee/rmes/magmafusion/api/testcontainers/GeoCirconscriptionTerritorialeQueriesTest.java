package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoCirconscriptionTerritorialeEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCirconscriptionTerritoriale;
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
class GeoCirconscriptionTerritorialeQueriesTest extends TestContainer {

    @Autowired
    GeoCirconscriptionTerritorialeEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/circonscriptionTerritoriale/{code}")
    class GetCogCir {

        @Test
        @DisplayName("When getcogcir 98601, returns CT 98601")
        void should_return_ct_98601_when_getcogcir_98601() throws Exception {
            var response = endpoints.getcogcir("98601", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/circonscription-territoriale-98601-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcir 98600 (inexistant), returns 404")
        void should_return_404_when_getcogcir_98600_inexistant() throws Exception {
            mockMvc.perform(get("/geo/circonscriptionTerritoriale/98600")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("geo/circonscriptionTerritoriale/{code}/ascendants")
    class GetCogCirAsc {

        @Test
        @DisplayName("When getcogcirasc 98601 type null, returns 1 ascendant (COM 986)")
        void should_return_1_ascendant_when_getcogcirasc_98601_type_null() throws Exception {
            var response = endpoints.getcogcirasc("98601", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/circonscription-territoriale-98601-ascendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcirasc 98601 type CollectiviteDOutreMer, returns 1 ascendant (COM 986)")
        void should_return_1_ascendant_when_getcogcirasc_98601_type_collectiviteDOutreMer() throws Exception {
            var response = endpoints.getcogcirasc("98601", LocalDate.of(2025, 1, 1), TypeEnumAscendantsCirconscriptionTerritoriale.COLLECTIVITE_D_OUTRE_MER);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/circonscription-territoriale-98601-ascendants-collectiviteDOutreMer-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }
}