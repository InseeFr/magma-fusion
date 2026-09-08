package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoRegionEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsRegion;
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
class GeoRegionQueriesTest extends TestContainer {

    @Autowired
    GeoRegionEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/region/{code}")
    class GetCogReg {

        @Test
        @DisplayName("When getcogreg 99, returns region 99")
        void should_return_region_99_when_getcogreg_99() throws Exception {
            var response = endpoints.getcogreg("99", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/region-99-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogreg 00 (inexistant), returns 404")
        void should_return_404_when_getcogreg_00_inexistant() {
            var response = endpoints.getcogreg("00", LocalDate.of(2025, 1, 1));
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("geo/region/{code}/descendants")
    class GetCogRegDes {

        @Test
        @DisplayName("When getcogregdes 99 type null, returns 12 descendants")
        void should_return_12_descendants_when_getcogregdes_99_type_null() throws Exception {
            var response = endpoints.getcogregdes("99", LocalDate.of(2025, 1, 1), null, null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/region-99-descendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogregdes 99 type Departement filtreNom='Departement test', returns 1 departement")
        void should_return_1_departement_when_getcogregdes_99_type_departement_filtreNom() throws Exception {
            var response = endpoints.getcogregdes("99", LocalDate.of(2025, 1, 1), TypeEnumDescendantsRegion.DEPARTEMENT, "Departement test");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/region-99-descendants-departement-filtreNom-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/regions")
    class GetCogRegListe {

        @Test
        @DisplayName("When getcogregliste date=2025-01-01, returns 1 region active")
        void should_return_1_region_when_getcogregliste_date() throws Exception {
            var response = endpoints.getcogregliste("2025-01-01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/regions-liste-date-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }

        @Test
        @DisplayName("When getcogregliste date=*, returns 2 regions")
        void should_return_2_regions_when_getcogregliste_etoile() throws Exception {
            var response = endpoints.getcogregliste("*");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/regions-liste-etoile-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }
    }

    @Nested
    @DisplayName("geo/region/{code}/precedents")
    class GetCogRegPrec {

        @Test
        @DisplayName("When getcogregprec 99, returns 1 precedent (region 88)")
        void should_return_1_precedent_when_getcogregprec_99() throws Exception {
            var response = endpoints.getcogregprec("99", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/region-99-precedents-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogregprec 88 (no precedents), returns 404")
        void should_return_404_when_getcogregprec_88_no_precedents() {
            var response = endpoints.getcogregprec("88", LocalDate.of(1995, 1, 1));
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("geo/region/{code}/projetes")
    class GetCogRegProj {

        @Test
        @DisplayName("When getcogregproj dateProjection null, returns 400")
        void should_return_400_when_getcogregproj_dateProjection_null() throws Exception {
            mockMvc.perform(get("/geo/region/99/projetes")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("When getcogregproj dateProjection empty, returns 400")
        void should_return_400_when_getcogregproj_dateProjection_empty() throws Exception {
            mockMvc.perform(get("/geo/region/99/projetes")
                            .param("dateProjection", "")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("When getcogregproj 99 dateProjection=1995-01-01, returns projection (region 88)")
        void should_return_1_projete_when_getcogregproj_99() throws Exception {
            var response = endpoints.getcogregproj("99", LocalDate.of(1995, 1, 1), LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/region-99-projetes-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/region/{code}/suivants")
    class GetCogRegSuiv {

        @Test
        @DisplayName("When getcogregsuiv 99 (actif, pas de suivant), returns 404")
        void should_return_404_when_getcogregsuiv_99_no_suivants() {
            var response = endpoints.getcogregsuiv("99", LocalDate.of(2025, 1, 1));
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @DisplayName("When getcogregsuiv 88, returns 1 suivant (region 99)")
        void should_return_1_suivant_when_getcogregsuiv_88() throws Exception {
            var response = endpoints.getcogregsuiv("88", LocalDate.of(1995, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/region-88-suivants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }
}