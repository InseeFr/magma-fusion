package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoCantonEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnum;
import fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCanton;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
class GeoCantonQueriesTest extends TestContainer {

    @Autowired
    GeoCantonEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/canton/{code}")
    class GetCogCan {

        @Test
        @DisplayName("When getcogcan 9901, returns canton 9901")
        void should_return_canton_9901_when_getcogcan_9901() throws Exception {
            var response = endpoints.getcogcan("9901", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-9901-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcan 9900 (inexistant), returns 404")
        void should_return_404_when_getcogcan_9900_inexistant() throws Exception {
            mockMvc.perform(get("/geo/canton/9900")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("geo/canton/{code}/ascendants")
    class GetCogCanAsc {

        @Test
        @DisplayName("When getcogcanasc 9901 type null, returns 2 ascendants (dep 10, reg 99)")
        void should_return_2_ascendants_when_getcogcanasc_9901_type_null() throws Exception {
            var response = endpoints.getcogcanasc("9901", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-9901-ascendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcanasc 9901 type Departement, returns 1 departement")
        void should_return_1_departement_when_getcogcanasc_9901_type_departement() throws Exception {
            var response = endpoints.getcogcanasc("9901", LocalDate.of(2025, 1, 1), TypeEnumAscendantsCanton.DEPARTEMENT);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-9901-ascendants-departement-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/canton/{code}/communes")
    class GetCogCanCom {

        @Test
        @DisplayName("When getcogcancom 9901, returns 2 communes (99001, 99002)")
        void should_return_2_communes_when_getcogcancom_9901() throws Exception {
            var response = endpoints.getcogcancom("9901", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-9901-communes-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/cantons")
    class GetCogCanListe {

        @Test
        @DisplayName("When getcogcanliste date=2025-01-01, returns 2 cantons actifs")
        void should_return_2_cantons_when_getcogcanliste_date() throws Exception {
            var response = endpoints.getcogcanliste("2025-01-01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/cantons-liste-date-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }

        @Test
        @DisplayName("When getcogcanliste date=*, returns 3 cantons")
        void should_return_3_cantons_when_getcogcanliste_etoile() throws Exception {
            var response = endpoints.getcogcanliste("*");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/cantons-liste-etoile-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }
    }

    @Nested
    @DisplayName("geo/canton/{code}/precedents")
    class GetCogCanPrec {

        @Test
        @DisplayName("When getcogcanprec 9901, returns 1 precedent (9903)")
        void should_return_1_precedent_when_getcogcanprec_9901() throws Exception {
            var response = endpoints.getcogcanprec("9901", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-9901-precedents-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcanprec 9902 (no precedents), returns 404")
        void should_return_404_when_getcogcanprec_9902_no_precedents() {
            var response = endpoints.getcogcanprec("9902", LocalDate.of(2025, 1, 1));
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("geo/canton/{code}/projetes")
    class GetCogCanProj {

        @Test
        @DisplayName("When getcogcanproj 9901 dateProjection=1985-01-01, returns 1 projete (9903)")
        void should_return_1_projete_when_getcogcanproj_9901() throws Exception {
            var response = endpoints.getcogcanproj("9901", LocalDate.of(1985, 1, 1), LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-9901-projetes-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcanproj dateProjection null, returns 400")
        void should_return_400_when_getcogcanproj_dateProjection_null() throws Exception {
            mockMvc.perform(get("/geo/canton/9901/projetes")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("When getcogcanproj dateProjection empty, returns 400")
        void should_return_400_when_getcogcanproj_dateProjection_empty() throws Exception {
            mockMvc.perform(get("/geo/canton/9901/projetes")
                            .param("dateProjection", "")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("geo/canton/{code}/suivants")
    class GetCogCanSuiv {

        @Test
        @DisplayName("When getcogcansuiv 9903, returns 1 suivant (9901)")
        void should_return_1_suivant_when_getcogcansuiv_9903() throws Exception {
            var response = endpoints.getcogcansuiv("9903", LocalDate.of(1985, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-9903-suivants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcansuiv 9901 (actif, pas de suivant), returns 404")
        void should_return_404_when_getcogcansuiv_9901_no_suivants() {
            var response = endpoints.getcogcansuiv("9901", LocalDate.of(2025, 1, 1));
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("geo/canton/{code}/intersections")
    class GetCogCanIntersect {

        @Test
        @DisplayName("When getcogcanintersect 9901 type Commune, returns 2 communes")
        void should_return_2_communes_when_getcogcanintersect_9901_type_commune() throws Exception {
            var response = endpoints.getcogcanintersect("9901", LocalDate.of(2025, 1, 1), TypeEnum.COMMUNE);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-9901-intersections-commune-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }
}