package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoArrondissementEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumAscendantsArrondissement;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsArrondissement;
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
class GeoArrondissementQueriesTest extends TestContainer {

    @Autowired
    GeoArrondissementEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/arrondissement/{code}/ascendants")
    class GetCogArrAsc {

        @Test
        @DisplayName("When getcogarrasc 991 type null, returns 2 ascendants (Dept + Region)")
        void should_return_2_ascendants_when_getcogarrasc_991_type_null() throws Exception {
            var response = endpoints.getcogarrasc("991", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/arrondissement-991-ascendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogarrasc 991 type Region, returns 1 ascendant (Region)")
        void should_return_1_region_when_getcogarrasc_991_type_region() throws Exception {
            var response = endpoints.getcogarrasc("991", LocalDate.of(2025, 1, 1), TypeEnumAscendantsArrondissement.REGION);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/arrondissement-991-ascendants-region-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/arrondissement/{code}")
    class GetCogArr {

        @Test
        @DisplayName("When getcogarr 991, returns arrondissement 991")
        void should_return_arrondissement_991_when_getcogarr_991() throws Exception {
            var response = endpoints.getcogarr("991", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/arrondissement-991-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/arrondissement/{code}/descendants")
    class GetCogArrDes {

        @Test
        @DisplayName("When getcogarrdes 991 type CommuneDeleguee, returns 2 communes deleguees")
        void should_return_2_communeDeleguee_when_getcogarrdes_991_type_communeDeleguee() throws Exception {
            var response = endpoints.getcogarrdes("991", null, TypeEnumDescendantsArrondissement.COMMUNE_DELEGUEE);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/arrondissement-991-descendants-communeDeleguee-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogarrdes 991 type null, returns 7 descendants (1 arrmu + 2 com + 1 comas + 2 comdel + 1 iris)")
        void should_return_7_descendants_when_getcogarrdes_991_type_null() throws Exception {
            var response = endpoints.getcogarrdes("991", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/arrondissement-991-descendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/arrondissements")
    class GetCogArrListe {

        @Test
        @DisplayName("When getcogarrliste date=2025-01-01, returns 3 active arrondissements")
        void should_return_3_arrondissements_when_getcogarrliste_date() throws Exception {
            var response = endpoints.getcogarrliste("2025-01-01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/arrondissements-liste-date-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogarrliste date=*, returns 7 arrondissements (actifs + supprimes)")
        void should_return_7_arrondissements_when_getcogarrliste_etoile() throws Exception {
            var response = endpoints.getcogarrliste("*");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/arrondissements-liste-etoile-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/arrondissement/{code}/precedents")
    class GetCogArrPrec {

        @Test
        @DisplayName("When getcogarrprec 991, returns 2 precedents (994, 995)")
        void should_return_2_precedents_when_getcogarrprec_991() throws Exception {
            var response = endpoints.getcogarrprec("991", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/arrondissement-991-precedents-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogarrprec 992 (no precedents), returns 404")
        void should_return_404_when_getcogarrprec_992_no_precedents() throws Exception {
            mockMvc.perform(get("/geo/arrondissement/992/precedents")
                            .param("date", "2005-01-01"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("geo/arrondissement/{code}/projetes")
    class GetCogArrProj {

        @Test
        @DisplayName("When getcogarrproj dateProjection null, returns 400")
        void should_return_400_when_getcogarrproj_dateProjection_null() throws Exception {
            mockMvc.perform(get("/geo/arrondissement/991/projetes")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("When getcogarrproj dateProjection empty, returns 400")
        void should_return_400_when_getcogarrproj_dateProjection_empty() throws Exception {
            mockMvc.perform(get("/geo/arrondissement/991/projetes")
                            .param("dateProjection", "")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("When getcogarrproj 991 dateProjection=2005-01-01, returns projections via predecessors chain")
        void should_return_projetes_when_getcogarrproj_991() throws Exception {
            var response = endpoints.getcogarrproj("991", LocalDate.of(2005, 1, 1), LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/arrondissement-991-projetes-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/arrondissement/{code}/suivants")
    class GetCogArrSuiv {

        @Test
        @DisplayName("When getcogarrsuiv 991 (actif, pas de suivants), returns 404")
        void should_return_404_when_getcogarrsuiv_991_no_suivants() throws Exception {
            mockMvc.perform(get("/geo/arrondissement/991/suivants")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("When getcogarrsuiv 996, returns 3 suivants (994, 995, 997)")
        void should_return_3_suivants_when_getcogarrsuiv_996() throws Exception {
            var response = endpoints.getcogarrsuiv("996", LocalDate.of(2008, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/arrondissement-996-suivants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }
}