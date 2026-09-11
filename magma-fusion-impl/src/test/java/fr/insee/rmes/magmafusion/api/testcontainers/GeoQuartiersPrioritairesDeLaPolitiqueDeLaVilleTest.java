package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoQuartierPrioritaireDeLaPolitiqueDeLaVilleEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnum;
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
class GeoQuartiersPrioritairesDeLaPolitiqueDeLaVilleTest extends TestContainer {

    @Autowired
    GeoQuartierPrioritaireDeLaPolitiqueDeLaVilleEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/quartierPrioritaireDeLaPolitiqueDeLaVille2024/{code}")
    class GetCogQpv {

        @Test
        @DisplayName("When getcogqpv QN01001M, returns QPV QN01001M")
        void should_return_qpv_QN01001M_when_getcogqpv_QN01001M() throws Exception {
            var response = endpoints.getcogqpv("QN01001M", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/qpv-QN01001M-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogqpv QN01099M (inexistant), returns 404")
        void should_return_404_when_getcogqpv_QN01099M_inexistant() throws Exception {
            mockMvc.perform(get("/geo/quartierPrioritaireDeLaPolitiqueDeLaVille2024/QN01099M")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("When getcogqpv QJ01001M (code invalide), returns 400")
        void should_return_400_when_getcogqpv_QJ01001M_code_invalide() throws Exception {
            mockMvc.perform(get("/geo/quartierPrioritaireDeLaPolitiqueDeLaVille2024/QJ01001M")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("geo/quartiersPrioritairesDeLaPolitiqueDeLaVille2024")
    class GetCogQpvListe {

        @Test
        @DisplayName("When getcogqpvliste date=2025-01-01, returns 1 QPV")
        void should_return_1_qpv_when_getcogqpvliste_date() throws Exception {
            var response = endpoints.getcogqpvliste(LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/qpv-liste-date-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/quartierPrioritaireDeLaPolitiqueDeLaVille2024/{code}/intersections")
    class GetCogQpvIntersect {

        @Test
        @DisplayName("When getcogqpvintersect QN01001M type null, returns 1 intersection (commune 99001)")
        void should_return_1_intersection_when_getcogqpvintersect_QN01001M_type_null() throws Exception {
            var response = endpoints.getcogqpvintersect("QN01001M", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/qpv-QN01001M-intersections-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogqpvintersect QN01001M type Commune, returns 1 commune")
        void should_return_1_commune_when_getcogqpvintersect_QN01001M_type_commune() throws Exception {
            var response = endpoints.getcogqpvintersect("QN01001M", LocalDate.of(2025, 1, 1), TypeEnum.COMMUNE);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/qpv-QN01001M-intersections-commune-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }
}