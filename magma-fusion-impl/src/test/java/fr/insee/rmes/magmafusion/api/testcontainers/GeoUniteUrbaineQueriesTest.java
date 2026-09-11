package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoUniteUrbaineEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsUniteUrbaine;
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
class GeoUniteUrbaineQueriesTest extends TestContainer {

    @Autowired
    GeoUniteUrbaineEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/uniteUrbaine2020/{code}")
    class GetCogUu {

        @Test
        @DisplayName("When getcoguu 99101, returns unite urbaine 99101")
        void should_return_uu_99101_when_getcoguu_99101() throws Exception {
            var response = endpoints.getcoguu("99101", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/unite-urbaine-99101-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcoguu 99199 (inexistant), returns 404")
        void should_return_404_when_getcoguu_99199_inexistant() throws Exception {
            mockMvc.perform(get("/geo/uniteUrbaine2020/99199")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("geo/uniteUrbaine2020/{code}/descendants")
    class GetCogUuDes {

        @Test
        @DisplayName("When getcoguudes 99101 type null, returns 2 descendants")
        void should_return_2_descendants_when_getcoguudes_99101_type_null() throws Exception {
            var response = endpoints.getcoguudes("99101", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/unite-urbaine-99101-descendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcoguudes 99101 type Commune, returns 2 communes")
        void should_return_2_communes_when_getcoguudes_99101_type_commune() throws Exception {
            var response = endpoints.getcoguudes("99101", LocalDate.of(2025, 1, 1), TypeEnumDescendantsUniteUrbaine.COMMUNE);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/unite-urbaine-99101-descendants-commune-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/unitesUrbaines2020")
    class GetCogUuListe {

        @Test
        @DisplayName("When getcoguuliste date 2025-01-01, returns 1 unite urbaine")
        void should_return_1_uu_when_getcoguuliste_date() throws Exception {
            var response = endpoints.getcoguuliste("2025-01-01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/unites-urbaines-liste-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcoguuliste date *, returns 1 unite urbaine")
        void should_return_1_uu_when_getcoguuliste_date_etoile() throws Exception {
            var response = endpoints.getcoguuliste("*");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/unites-urbaines-liste-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }
}