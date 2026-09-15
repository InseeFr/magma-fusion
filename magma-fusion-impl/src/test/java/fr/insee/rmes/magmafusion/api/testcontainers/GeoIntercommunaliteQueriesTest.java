package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoIntercommunaliteEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsIntercommunalite;
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
class GeoIntercommunaliteQueriesTest extends TestContainer {

    @Autowired
    GeoIntercommunaliteEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/intercommunalite/{code}")
    class GetCogInterco {

        @Test
        @DisplayName("When getcoginterco 999000001, returns intercommunalite 999000001")
        void should_return_intercommunalite_999000001_when_getcoginterco() throws Exception {
            var response = endpoints.getcoginterco("999000001", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/intercommunalite-999000001-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcoginterco 999000099 (inexistant), returns 404")
        void should_return_404_when_getcoginterco_999000099_inexistant() {
            var response = endpoints.getcoginterco("999000099", LocalDate.of(2025, 1, 1));
            assertNotNull(response);
            assert response.getStatusCode().value() == 404;
        }
    }

    @Nested
    @DisplayName("geo/intercommunalite/{code}/descendants")
    class GetCogIntercoDes {

        @Test
        @DisplayName("When getcogintercodes 999000001 type null, returns 2 descendants (2 communes)")
        void should_return_2_descendants_when_getcogintercodes_999000001_type_null() throws Exception {
            var response = endpoints.getcogintercodes("999000001", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/intercommunalite-999000001-descendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogintercodes 999000001 type Commune, returns 2 communes")
        void should_return_2_communes_when_getcogintercodes_999000001_type_commune() throws Exception {
            var response = endpoints.getcogintercodes("999000001", LocalDate.of(2025, 1, 1), TypeEnumDescendantsIntercommunalite.COMMUNE);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/intercommunalite-999000001-descendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/intercommunalites")
    class GetCogIntercoListe {

        @Test
        @DisplayName("When getcogintercoliste date=2025-01-01 filtreNom='Intercommunalite test 1', returns 1 intercommunalite")
        void should_return_1_intercommunalite_when_getcogintercoliste_date_filtreNom() throws Exception {
            var response = endpoints.getcogintercoliste("2025-01-01", "Intercommunalite test 1");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/intercommunalite-liste-date-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogintercoliste date=* filtreNom='Intercommunalite test', returns 2 intercommunalites (historique)")
        void should_return_2_intercommunalites_when_getcogintercoliste_etoile_filtreNom() throws Exception {
            var response = endpoints.getcogintercoliste("*", "Intercommunalite test");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/intercommunalite-liste-etoile-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/intercommunalite/{code}/precedents")
    class GetCogIntercoPrec {

        @Test
        @DisplayName("When getcogintercoprec 999000001, returns 1 precedent (999000002)")
        void should_return_1_precedent_when_getcogintercoprec_999000001() throws Exception {
            var response = endpoints.getcogintercoprec("999000001", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/intercommunalite-999000001-precedents-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/intercommunalite/{code}/suivants")
    class GetCogIntercoSuiv {

        @Test
        @DisplayName("When getcogintercosuiv 999000002, returns 1 suivant (999000001)")
        void should_return_1_suivant_when_getcogintercosuiv_999000002() throws Exception {
            var response = endpoints.getcogintercosuiv("999000002", LocalDate.of(2005, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/intercommunalite-999000002-suivants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/intercommunalite/{code}/projetes")
    class GetCogIntercoProj {

        @Test
        @DisplayName("When getcogintercoproj 999000001 dateProjection=2005-01-01, returns 1 projete (999000002)")
        void should_return_1_projete_when_getcogintercoproj_999000001() throws Exception {
            var response = endpoints.getcogintercoproj("999000001", LocalDate.of(2005, 1, 1), LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/intercommunalite-999000001-projetes-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogintercoproj dateProjection null, returns 400")
        void should_return_400_when_getcogintercoproj_dateProjection_null() throws Exception {
            mockMvc.perform(get("/geo/intercommunalite/999000001/projetes")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("When getcogintercoproj dateProjection empty, returns 400")
        void should_return_400_when_getcogintercoproj_dateProjection_empty() throws Exception {
            mockMvc.perform(get("/geo/intercommunalite/999000001/projetes")
                            .param("dateProjection", "")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());
        }
    }
}
