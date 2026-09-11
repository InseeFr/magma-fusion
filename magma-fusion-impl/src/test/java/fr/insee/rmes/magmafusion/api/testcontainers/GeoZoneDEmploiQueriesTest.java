package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoZoneDEmploiEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsZoneDEmploi;
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
class GeoZoneDEmploiQueriesTest extends TestContainer {

    @Autowired
    GeoZoneDEmploiEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/zoneDEmploi2020/{code}")
    class GetCogZe {

        @Test
        @DisplayName("When getcogze 9901, returns zone d'emploi 9901")
        void should_return_ze_9901_when_getcogze_9901() throws Exception {
            var response = endpoints.getcogze("9901", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/zone-d-emploi-9901-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogze 9999 (inexistant), returns 404")
        void should_return_404_when_getcogze_9999_inexistant() throws Exception {
            mockMvc.perform(get("/geo/zoneDEmploi2020/9999")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("geo/zoneDEmploi2020/{code}/descendants")
    class GetCogZeDesc {

        @Test
        @DisplayName("When getcogzedesc 9901 type null, returns 2 descendants")
        void should_return_2_descendants_when_getcogzedesc_9901_type_null() throws Exception {
            var response = endpoints.getcogzedesc("9901", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/zone-d-emploi-9901-descendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogzedesc 9901 type Commune, returns 2 communes")
        void should_return_2_communes_when_getcogzedesc_9901_type_commune() throws Exception {
            var response = endpoints.getcogzedesc("9901", LocalDate.of(2025, 1, 1), TypeEnumDescendantsZoneDEmploi.COMMUNE);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/zone-d-emploi-9901-descendants-commune-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/zonesDEmploi2020")
    class GetCogZeListe {

        @Test
        @DisplayName("When getcogzeliste date 2025-01-01, returns 1 zone d'emploi")
        void should_return_1_ze_when_getcogzeliste_date() throws Exception {
            var response = endpoints.getcogzeliste("2025-01-01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/zones-d-emploi-liste-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogzeliste date *, returns 1 zone d'emploi")
        void should_return_1_ze_when_getcogzeliste_date_etoile() throws Exception {
            var response = endpoints.getcogzeliste("*");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/zones-d-emploi-liste-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }
}