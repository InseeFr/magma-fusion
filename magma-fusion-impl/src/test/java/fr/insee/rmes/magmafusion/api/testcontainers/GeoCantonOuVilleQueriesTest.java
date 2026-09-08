package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoCantonOuVilleEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCantonOuVille;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsCantonOuVille;
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
class GeoCantonOuVilleQueriesTest extends TestContainer {

    @Autowired
    GeoCantonOuVilleEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/cantonOuVille/{code}")
    class GetCogCanVil {

        @Test
        @DisplayName("When getcogcanvil 7701, returns canton-ou-ville 7701")
        void should_return_cov_7701_when_getcogcanvil_7701() throws Exception {
            var response = endpoints.getcogcanvil("7701", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-ou-ville-7701-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcanvil 7700 (inexistant), returns 404")
        void should_return_404_when_getcogcanvil_7700_inexistant() {
            var response = endpoints.getcogcanvil("7700", LocalDate.of(2025, 1, 1));
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("geo/cantonOuVille/{code}/ascendants")
    class GetCogCanVilAsc {

        @Test
        @DisplayName("When getcogcanvilasc 7701 type null, returns 2 ascendants (dept 10, region 99)")
        void should_return_2_ascendants_when_getcogcanvilasc_7701_type_null() throws Exception {
            var response = endpoints.getcogcanvilasc("7701", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-ou-ville-7701-ascendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcanvilasc 7701 type Region, returns 1 region")
        void should_return_1_region_when_getcogcanvilasc_7701_type_region() throws Exception {
            var response = endpoints.getcogcanvilasc("7701", LocalDate.of(2025, 1, 1), TypeEnumAscendantsCantonOuVille.REGION);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-ou-ville-7701-ascendants-region-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/cantonOuVille/{code}/descendants")
    class GetCogCanVilDes {

        @Test
        @DisplayName("When getcogcanvildes 7701 type null, returns 2 descendants (communes)")
        void should_return_2_descendants_when_getcogcanvildes_7701_type_null() throws Exception {
            var response = endpoints.getcogcanvildes("7701", LocalDate.of(2025, 1, 1), null, null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-ou-ville-7701-descendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcanvildes 7701 type Commune filtreNom='Commune test 3', returns 1 commune")
        void should_return_1_commune_when_getcogcanvildes_7701_type_commune_filtreNom() throws Exception {
            var response = endpoints.getcogcanvildes("7701", LocalDate.of(2025, 1, 1), TypeEnumDescendantsCantonOuVille.COMMUNE, "Commune test 3");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-ou-ville-7701-descendants-commune-filtreNom-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/cantonsEtVilles")
    class GetCogCanVilListe {

        @Test
        @DisplayName("When getcogcanvilliste date=2025-01-01, returns 2 cantons-ou-villes actifs")
        void should_return_2_cov_when_getcogcanvilliste_date() throws Exception {
            var response = endpoints.getcogcanvilliste("2025-01-01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/cantons-et-villes-liste-date-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }

        @Test
        @DisplayName("When getcogcanvilliste date=*, returns 3 cantons-ou-villes")
        void should_return_3_cov_when_getcogcanvilliste_etoile() throws Exception {
            var response = endpoints.getcogcanvilliste("*");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/cantons-et-villes-liste-etoile-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }
    }

    @Nested
    @DisplayName("geo/cantonOuVille/{code}/precedents")
    class GetCogCanVilPrec {

        @Test
        @DisplayName("When getcogcanvilprec 7701, returns 1 precedent (7703)")
        void should_return_1_precedent_when_getcogcanvilprec_7701() throws Exception {
            var response = endpoints.getcogcanvilprec("7701", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-ou-ville-7701-precedents-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcanvilprec 7702 (no precedents), returns 404")
        void should_return_404_when_getcogcanvilprec_7702_no_precedents() {
            var response = endpoints.getcogcanvilprec("7702", LocalDate.of(2025, 1, 1));
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("geo/cantonOuVille/{code}/projetes")
    class GetCogCanVilProj {

        @Test
        @DisplayName("When getcogcanvilproj dateProjection null, returns 400")
        void should_return_400_when_getcogcanvilproj_dateProjection_null() throws Exception {
            mockMvc.perform(get("/geo/cantonOuVille/7701/projetes")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("When getcogcanvilproj dateProjection empty, returns 400")
        void should_return_400_when_getcogcanvilproj_dateProjection_empty() throws Exception {
            mockMvc.perform(get("/geo/cantonOuVille/7701/projetes")
                            .param("dateProjection", "")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("When getcogcanvilproj 7701 dateProjection=2010-01-01, returns projection (7703)")
        void should_return_1_projete_when_getcogcanvilproj_7701() throws Exception {
            var response = endpoints.getcogcanvilproj("7701", LocalDate.of(2010, 1, 1), LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-ou-ville-7701-projetes-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/cantonOuVille/{code}/suivants")
    class GetCogCanVilSuiv {

        @Test
        @DisplayName("When getcogcanvilsuiv 7703, returns 1 suivant (7701)")
        void should_return_1_suivant_when_getcogcanvilsuiv_7703() throws Exception {
            var response = endpoints.getcogcanvilsuiv("7703", LocalDate.of(2000, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/canton-ou-ville-7703-suivants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcanvilsuiv 7701 (actif, pas de suivant), returns 404")
        void should_return_404_when_getcogcanvilsuiv_7701_no_suivants() {
            var response = endpoints.getcogcanvilsuiv("7701", LocalDate.of(2025, 1, 1));
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}