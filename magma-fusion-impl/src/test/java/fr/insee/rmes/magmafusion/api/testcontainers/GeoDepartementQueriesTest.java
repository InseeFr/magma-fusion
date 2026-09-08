package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoDepartementEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumAscendantsDepartement;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsDepartement;
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
class GeoDepartementQueriesTest extends TestContainer {

    @Autowired
    GeoDepartementEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/departement/{code}/ascendants")
    class GetCogDepAsc {

        @Test
        @DisplayName("When getcogdepasc 10 type null, returns 1 ascendant (Region 99)")
        void should_return_1_region_when_getcogdepasc_10_type_null() throws Exception {
            var response = endpoints.getcogdepasc("10", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/departement-10-ascendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogdepasc 10 type Region, returns 1 ascendant (Region 99)")
        void should_return_1_region_when_getcogdepasc_10_type_region() throws Exception {
            var response = endpoints.getcogdepasc("10", LocalDate.of(2025, 1, 1), TypeEnumAscendantsDepartement.REGION);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/departement-10-ascendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/departement/{code}")
    class GetCogDep {

        @Test
        @DisplayName("When getcogdep 10, returns departement 10")
        void should_return_departement_10_when_getcogdep_10() throws Exception {
            var response = endpoints.getcogdep("10", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/departement-10-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogdep 00 (inexistant), returns 404")
        void should_return_404_when_getcogdep_00_inexistant() {
            var response = endpoints.getcogdep("00", LocalDate.of(2025, 1, 1));
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("geo/departement/{code}/descendants")
    class GetCogDepDesc {

        @Test
        @DisplayName("When getcogdepdesc 10 type Commune filtreNom='Commune test 1', returns 1 commune")
        void should_return_1_commune_when_getcogdepdesc_10_type_commune_filtreNom() throws Exception {
            var response = endpoints.getcogdepdesc("10", LocalDate.of(2025, 1, 1), TypeEnumDescendantsDepartement.COMMUNE, "Commune test 1");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/departement-10-descendants-commune-filtreNom-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogdepdesc 10 type null, returns 11 descendants (3 arr + 2 cov + 3 com + 2 comdel + 1 arrmu)")
        void should_return_11_descendants_when_getcogdepdesc_10_type_null() throws Exception {
            var response = endpoints.getcogdepdesc("10", LocalDate.of(2025, 1, 1), null, null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/departement-10-descendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/departements")
    class GetCogDepts {

        @Test
        @DisplayName("When getcogdepts date=2025-01-01, returns 1 departement actif")
        void should_return_1_departement_when_getcogdepts_date() throws Exception {
            var response = endpoints.getcogdepts("2025-01-01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/departements-liste-date-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogdepts date=*, returns 2 departements (actif + supprime)")
        void should_return_2_departements_when_getcogdepts_etoile() throws Exception {
            var response = endpoints.getcogdepts("*");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/departements-liste-etoile-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/departement/{code}/precedents")
    class GetCogDepPrec {

        @Test
        @DisplayName("When getcogdepprec 10, returns 1 precedent (dept 11)")
        void should_return_1_precedent_when_getcogdepprec_10() throws Exception {
            var response = endpoints.getcogdepprec("10", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/departement-10-precedents-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogdepprec 11 (no precedents), returns 404")
        void should_return_404_when_getcogdepprec_11_no_precedents() {
            var response = endpoints.getcogdepprec("11", LocalDate.of(1995, 1, 1));
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("geo/departement/{code}/projetes")
    class GetCogDepProj {

        @Test
        @DisplayName("When getcogdepproj dateProjection null, returns 400")
        void should_return_400_when_getcogdepproj_dateProjection_null() throws Exception {
            mockMvc.perform(get("/geo/departement/10/projetes")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("When getcogdepproj dateProjection empty, returns 400")
        void should_return_400_when_getcogdepproj_dateProjection_empty() throws Exception {
            mockMvc.perform(get("/geo/departement/10/projetes")
                            .param("dateProjection", "")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("When getcogdepproj 10 dateProjection=1995-01-01, returns projection (dept 11)")
        void should_return_1_projete_when_getcogdepproj_10() throws Exception {
            var response = endpoints.getcogdepproj("10", LocalDate.of(1995, 1, 1), LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/departement-10-projetes-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/departement/{code}/suivants")
    class GetCogDepSuiv {

        @Test
        @DisplayName("When getcogdepsuiv 10 (actif, pas de suivant), returns 404")
        void should_return_404_when_getcogdepsuiv_10_no_suivants() {
            var response = endpoints.getcogdepsuiv("10", LocalDate.of(2025, 1, 1));
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @DisplayName("When getcogdepsuiv 11, returns 1 suivant (dept 10)")
        void should_return_1_suivant_when_getcogdepsuiv_11() throws Exception {
            var response = endpoints.getcogdepsuiv("11", LocalDate.of(1995, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/departement-11-suivants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }
}