package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoAireDAttractionDesVillesEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsAireDAttractionDesVilles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
class GeoAireDAttractionDesVillesQueriesTest extends TestContainer {

    @Autowired
    GeoAireDAttractionDesVillesEndpoints endpoints;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/aireDAttractionDesVilles2020/{code}")
    class GetCogAav {

        @Test
        @DisplayName("When getcogaav T01, returns AAV T01")
        void should_return_aav_T01_when_getcogaav_T01() throws Exception {
            var response = endpoints.getcogaav("T01", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/aav-T01-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogaav T99 (inexistant), returns 404")
        void should_return_404_when_getcogaav_T99_inexistant() {
            var response = endpoints.getcogaav("T99", LocalDate.of(2025, 1, 1));
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("geo/aireDAttractionDesVilles2020/{code}/descendants")
    class GetCogAavDesc {

        @Test
        @DisplayName("When getcogaavdesc T01 type null, returns 9 descendants")
        void should_return_9_descendants_when_getcogaavdesc_T01_type_null() throws Exception {
            var response = endpoints.getcogaavdesc("T01", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/aav-T01-descendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogaavdesc T01 type Commune, returns 3 communes")
        void should_return_3_communes_when_getcogaavdesc_T01_type_commune() throws Exception {
            var response = endpoints.getcogaavdesc("T01", LocalDate.of(2025, 1, 1), TypeEnumDescendantsAireDAttractionDesVilles.COMMUNE);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/aav-T01-descendants-commune-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/airesDAttractionDesVilles2020")
    class GetCogAavListe {

        @Test
        @DisplayName("When getcogaavliste date=2025-01-01, returns 1 AAV active")
        void should_return_1_aav_when_getcogaavliste_date() throws Exception {
            var response = endpoints.getcogaavliste("2025-01-01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/aav-liste-date-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }

        @Test
        @DisplayName("When getcogaavliste date=*, returns 2 AAVs")
        void should_return_2_aav_when_getcogaavliste_etoile() throws Exception {
            var response = endpoints.getcogaavliste("*");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/aav-liste-etoile-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }
    }
}