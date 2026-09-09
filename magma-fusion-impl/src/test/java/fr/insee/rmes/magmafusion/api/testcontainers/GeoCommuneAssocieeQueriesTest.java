package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoCommuneAssocieeEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCommuneAssociee;
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
class GeoCommuneAssocieeQueriesTest extends TestContainer {

    @Autowired
    GeoCommuneAssocieeEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/communeAssociee/{code}")
    class GetCogComA {

        @Test
        @DisplayName("When getcogcoma 99101, returns commune associee 99101")
        void should_return_commune_associee_99101_when_getcogcoma_99101() throws Exception {
            var response = endpoints.getcogcoma("99101", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/commune-associee-99101-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcoma 99100 (inexistant), returns 404")
        void should_return_404_when_getcogcoma_99100_inexistant() throws Exception {
            mockMvc.perform(get("/geo/communeAssociee/99100")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("geo/communeAssociee/{code}/ascendants")
    class GetCogComAAsc {

        @Test
        @DisplayName("When getcogcomaasc 99101 type null, returns 5 ascendants (aav T01, arr 991, com 99001, dep 10, reg 99)")
        void should_return_5_ascendants_when_getcogcomaasc_99101_type_null() throws Exception {
            var response = endpoints.getcogcomaasc("99101", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/commune-associee-99101-ascendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogcomaasc 99101 type Departement, returns 1 departement")
        void should_return_1_departement_when_getcogcomaasc_99101_type_departement() throws Exception {
            var response = endpoints.getcogcomaasc("99101", LocalDate.of(2025, 1, 1), TypeEnumAscendantsCommuneAssociee.DEPARTEMENT);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/commune-associee-99101-ascendants-departement-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/communesAssociees")
    class GetCogComAListe {

        @Test
        @DisplayName("When getcogcomaliste date=2025-01-01, returns 1 commune associee active (99101)")
        void should_return_1_commune_associee_when_getcogcomaliste_date() throws Exception {
            var response = endpoints.getcogcomaliste("2025-01-01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/communes-associees-liste-date-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }

        @Test
        @DisplayName("When getcogcomaliste date=*, returns 2 communes associees (99101, 99102)")
        void should_return_2_communes_associees_when_getcogcomaliste_etoile() throws Exception {
            var response = endpoints.getcogcomaliste("*");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/communes-associees-liste-etoile-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }
    }
}