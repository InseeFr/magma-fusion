package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoCommuneDelegueeEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCommuneDeleguee;
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
class GeoCommuneDelegueeQueriesTest extends TestContainer {

    @Autowired
    GeoCommuneDelegueeEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/communeDeleguee/{code}")
    class GetCogComD {

        @Test
        @DisplayName("When getcogcomd 98001, returns commune deleguee 98001")
        void should_return_commune_deleguee_98001_when_getcogcomd_98001() throws Exception {
            var response = endpoints.getcogcomd("98001", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/commune-deleguee-98001-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcomd 98099 (inexistant), returns 404")
        void should_return_404_when_getcogcomd_98099_inexistant() throws Exception {
            mockMvc.perform(get("/geo/communeDeleguee/98099")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("geo/communeDeleguee/{code}/ascendants")
    class GetCogComDAsc {

        @Test
        @DisplayName("When getcogcomdasc 98001 type null, returns 7 ascendants (aav T01, arr 991, bv 88001, cov 7701, com 99002, dep 10, reg 99)")
        void should_return_7_ascendants_when_getcogcomdasc_98001_type_null() throws Exception {
            var response = endpoints.getcogcomdasc("98001", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/commune-deleguee-98001-ascendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcomdasc 98001 type Departement, returns 1 departement")
        void should_return_1_departement_when_getcogcomdasc_98001_type_departement() throws Exception {
            var response = endpoints.getcogcomdasc("98001", LocalDate.of(2025, 1, 1), TypeEnumAscendantsCommuneDeleguee.DEPARTEMENT);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/commune-deleguee-98001-ascendants-departement-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcomdasc 98001 date before creation, returns 404")
        void should_return_404_when_getcogcomdasc_98001_date_before_creation() throws Exception {
            mockMvc.perform(get("/geo/communeDeleguee/98001/ascendants")
                            .param("date", "2010-01-01"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("geo/communesDeleguees")
    class GetCogComDListe {

        @Test
        @DisplayName("When getcogcomdliste date=2025-01-01, returns 2 communes deleguees actives (98001, 98002)")
        void should_return_2_communes_deleguees_when_getcogcomdliste_date() throws Exception {
            var response = endpoints.getcogcomdliste("2025-01-01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/communes-deleguees-liste-date-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }

        @Test
        @DisplayName("When getcogcomdliste date=*, returns 3 communes deleguees (98001, 98002, 98003)")
        void should_return_3_communes_deleguees_when_getcogcomdliste_etoile() throws Exception {
            var response = endpoints.getcogcomdliste("*");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/communes-deleguees-liste-etoile-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }
    }
}