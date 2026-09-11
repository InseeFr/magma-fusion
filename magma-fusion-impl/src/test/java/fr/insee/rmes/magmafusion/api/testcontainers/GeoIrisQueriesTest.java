package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoIrisEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumAscendantsIris;
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
class GeoIrisQueriesTest extends TestContainer {

    @Autowired
    GeoIrisEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/iris/{code}")
    class GetCogIris {

        @Test
        @DisplayName("When getcogiris 990010101 (real IRIS), returns IRIS with typeDIris=H")
        void should_return_real_iris_990010101_when_getcogiris() throws Exception {
            var response = endpoints.getcogiris("990010101", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/iris-990010101-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogiris 990020000 (faux-IRIS, commune non-irisee), returns commune as IRIS")
        void should_return_faux_iris_990020000_when_getcogiris() throws Exception {
            var response = endpoints.getcogiris("990020000", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/iris-990020000-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogiris 990010000 (commune irisee + code 0000), returns 404")
        void should_return_404_when_getcogiris_990010000_irisee_0000() throws Exception {
            mockMvc.perform(get("/geo/iris/990010000")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("When getcogiris 990020101 (commune non-irisee + code non-0000), returns 404")
        void should_return_404_when_getcogiris_990020101_non_irisee_non_0000() throws Exception {
            mockMvc.perform(get("/geo/iris/990020101")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("geo/iris/{code}/ascendants")
    class GetCogIrisAsc {

        @Test
        @DisplayName("When getcogirisasc 990010101 (real IRIS) type null, returns 5 ascendants")
        void should_return_5_ascendants_when_getcogirisasc_990010101_type_null() throws Exception {
            var response = endpoints.getcogirisasc("990010101", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/iris-990010101-ascendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogirisasc 990010101 (real IRIS) type Arrondissement, returns 1 arrondissement")
        void should_return_1_arrondissement_when_getcogirisasc_990010101() throws Exception {
            var response = endpoints.getcogirisasc("990010101", LocalDate.of(2025, 1, 1), TypeEnumAscendantsIris.ARRONDISSEMENT);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/iris-990010101-ascendants-arrondissement-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogirisasc 990020000 (faux-IRIS) type null, returns 7 ascendants")
        void should_return_7_ascendants_when_getcogirisasc_990020000_type_null() throws Exception {
            var response = endpoints.getcogirisasc("990020000", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/iris-990020000-ascendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogirisasc 990020000 (faux-IRIS) type Arrondissement, returns 1 arrondissement")
        void should_return_1_arrondissement_when_getcogirisasc_990020000() throws Exception {
            var response = endpoints.getcogirisasc("990020000", LocalDate.of(2025, 1, 1), TypeEnumAscendantsIris.ARRONDISSEMENT);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/iris-990020000-ascendants-arrondissement-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/iris")
    class GetCogIrisList {

        @Test
        @DisplayName("When getcogirislist com=false (default), returns 3 entries (1 real iris + 2 faux-iris)")
        void should_return_3_entries_when_getcogirislist_com_false() throws Exception {
            var response = endpoints.getcogirislist(LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/iris-liste-com-false-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogirislist com=true, returns 5 entries (1 real iris + 4 faux-iris)")
        void should_return_5_entries_when_getcogirislist_com_true() throws Exception {
            var response = endpoints.getcogirislist(LocalDate.of(2025, 1, 1), true);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/iris-liste-com-true-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }
}
