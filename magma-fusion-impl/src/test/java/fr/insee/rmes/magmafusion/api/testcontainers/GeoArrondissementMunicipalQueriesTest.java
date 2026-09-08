package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoArrondissementMunipalEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumAscendantsArrondissementMunicipal;
import org.junit.jupiter.api.DisplayName;
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
class GeoArrondissementMunicipalQueriesTest extends TestContainer {

    @Autowired
    GeoArrondissementMunipalEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    // =========================================================
    //   geo/arrondissementMunicipal/{code}
    // =========================================================

    @Test
    @DisplayName("When getcogarrmu 75101, returns ArrMun 75101")
    void should_return_arrmu_75101_when_getcogarrmu_75101() throws Exception {
        var response = endpoints.getcogarrmu("75101", LocalDate.of(2025, 1, 1));
        var result = response.getBody();

        assertNotNull(result);
        String data = objectMapper.writeValueAsString(result);
        String expected = new String(
                Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("testcontainers/arrondissement-municipal-75101-expected.json"))
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
        JSONAssert.assertEquals(expected, data, true);
    }

    @Test
    @DisplayName("When getcogarrmu 75199 (inexistant), returns 404")
    void should_return_404_when_getcogarrmu_75199_inexistant() {
        var response = endpoints.getcogarrmu("75199", LocalDate.of(2025, 1, 1));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // =========================================================
    //   geo/arrondissementMunicipal/{code}/ascendants
    // =========================================================

    @Test
    @DisplayName("When getcogarrmuasc 75101 type null, returns 5 ascendants")
    void should_return_5_ascendants_when_getcogarrmuasc_75101_type_null() throws Exception {
        var response = endpoints.getcogarrmuasc("75101", LocalDate.of(2025, 1, 1), null);
        var result = response.getBody();

        assertNotNull(result);
        String data = objectMapper.writeValueAsString(result);
        String expected = new String(
                Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("testcontainers/arrondissement-municipal-75101-ascendants-expected.json"))
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
        JSONAssert.assertEquals(expected, data, true);
    }

    @Test
    @DisplayName("When getcogarrmuasc 75101 type Commune, returns 1 commune")
    void should_return_1_commune_when_getcogarrmuasc_75101_type_commune() throws Exception {
        var response = endpoints.getcogarrmuasc("75101", LocalDate.of(2025, 1, 1), TypeEnumAscendantsArrondissementMunicipal.COMMUNE);
        var result = response.getBody();

        assertNotNull(result);
        String data = objectMapper.writeValueAsString(result);
        String expected = new String(
                Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("testcontainers/arrondissement-municipal-75101-ascendants-commune-expected.json"))
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
        JSONAssert.assertEquals(expected, data, true);
    }

    // =========================================================
    //   geo/arrondissementsMunicipaux
    // =========================================================

    @Test
    @DisplayName("When getcogarrmuliste date=2025-01-01, returns 1 ArrMun actif")
    void should_return_1_arrmu_when_getcogarrmuliste_date() throws Exception {
        var response = endpoints.getcogarrmuliste("2025-01-01");
        var result = response.getBody();

        assertNotNull(result);
        String data = objectMapper.writeValueAsString(result);
        String expected = new String(
                Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("testcontainers/arrondissements-municipaux-liste-date-expected.json"))
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
        JSONAssert.assertEquals(expected, data, false);
    }

    @Test
    @DisplayName("When getcogarrmuliste date=*, returns 2 ArrMun")
    void should_return_2_arrmu_when_getcogarrmuliste_etoile() throws Exception {
        var response = endpoints.getcogarrmuliste("*");
        var result = response.getBody();

        assertNotNull(result);
        String data = objectMapper.writeValueAsString(result);
        String expected = new String(
                Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("testcontainers/arrondissements-municipaux-liste-etoile-expected.json"))
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
        JSONAssert.assertEquals(expected, data, false);
    }

    // =========================================================
    //   geo/arrondissementMunicipal/{code}/precedents
    // =========================================================

    @Test
    @DisplayName("When getcogarrmuprec 75101, returns 1 precedent (75102)")
    void should_return_1_precedent_when_getcogarrmuprec_75101() throws Exception {
        var response = endpoints.getcogarrmuprec("75101", LocalDate.of(2025, 1, 1));
        var result = response.getBody();

        assertNotNull(result);
        String data = objectMapper.writeValueAsString(result);
        String expected = new String(
                Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("testcontainers/arrondissement-municipal-75101-precedents-expected.json"))
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
        JSONAssert.assertEquals(expected, data, true);
    }

    @Test
    @DisplayName("When getcogarrmuprec 75102 (no precedents), returns 404")
    void should_return_404_when_getcogarrmuprec_75102_no_precedents() {
        var response = endpoints.getcogarrmuprec("75102", LocalDate.of(1995, 1, 1));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // =========================================================
    //   geo/arrondissementMunicipal/{code}/projetes
    // =========================================================

    @Test
    @DisplayName("When getcogarrmuproj dateProjection null, returns 400")
    void should_return_400_when_getcogarrmuproj_dateProjection_null() throws Exception {
        mockMvc.perform(get("/geo/arrondissementMunicipal/75101/projetes")
                        .param("date", "2025-01-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When getcogarrmuproj dateProjection empty, returns 400")
    void should_return_400_when_getcogarrmuproj_dateProjection_empty() throws Exception {
        mockMvc.perform(get("/geo/arrondissementMunicipal/75101/projetes")
                        .param("dateProjection", "")
                        .param("date", "2025-01-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When getcogarrmuproj 75101 dateProjection=1995-01-01, returns projection (75102)")
    void should_return_1_projete_when_getcogarrmuproj_75101() throws Exception {
        var response = endpoints.getcogarrmuproj("75101", LocalDate.of(1995, 1, 1), LocalDate.of(2025, 1, 1));
        var result = response.getBody();

        assertNotNull(result);
        String data = objectMapper.writeValueAsString(result);
        String expected = new String(
                Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("testcontainers/arrondissement-municipal-75101-projetes-expected.json"))
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
        JSONAssert.assertEquals(expected, data, true);
    }

    // =========================================================
    //   geo/arrondissementMunicipal/{code}/suivants
    // =========================================================

    @Test
    @DisplayName("When getcogarrmusuiv 75101 (actif, pas de suivant), returns 404")
    void should_return_404_when_getcogarrmusuiv_75101_no_suivants() {
        var response = endpoints.getcogarrmusuiv("75101", LocalDate.of(2025, 1, 1));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("When getcogarrmusuiv 75102, returns 1 suivant (75101)")
    void should_return_1_suivant_when_getcogarrmusuiv_75102() throws Exception {
        var response = endpoints.getcogarrmusuiv("75102", LocalDate.of(1995, 1, 1));
        var result = response.getBody();

        assertNotNull(result);
        String data = objectMapper.writeValueAsString(result);
        String expected = new String(
                Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("testcontainers/arrondissement-municipal-75102-suivants-expected.json"))
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
        JSONAssert.assertEquals(expected, data, true);
    }
}