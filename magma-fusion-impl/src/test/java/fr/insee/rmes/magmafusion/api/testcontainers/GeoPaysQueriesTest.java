package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoPaysEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsPays;
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
class GeoPaysQueriesTest extends TestContainer {

    @Autowired
    GeoPaysEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/pays/{code}")
    class GetCogPays {

        @Test
        @DisplayName("When getcogpays 99901, returns pays 99901")
        void should_return_pays_99901_when_getcogpays() throws Exception {
            var response = endpoints.getcogpays("99901", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/pays-99901-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogpays 99999 (inexistant), returns 404")
        void should_return_404_when_getcogpays_99999_inexistant() {
            var response = endpoints.getcogpays("99999", LocalDate.of(2025, 1, 1));
            assertNotNull(response);
            assert response.getStatusCode().value() == 404;
        }
    }

    @Nested
    @DisplayName("geo/pays/{code}/descendants")
    class GetCogPaysDes {

        @Test
        @DisplayName("When getcogpaysdesc 99901 type null, returns 1 territoire")
        void should_return_1_territoire_when_getcogpaysdesc_99901_type_null() throws Exception {
            var response = endpoints.getcogpaysdesc("99901", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/pays-99901-descendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogpaysdesc 99901 type Territoire, returns 1 territoire")
        void should_return_1_territoire_when_getcogpaysdesc_99901_type_territoire() throws Exception {
            var response = endpoints.getcogpaysdesc("99901", LocalDate.of(2025, 1, 1), TypeEnumDescendantsPays.TERRITOIRE);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/pays-99901-descendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogpaysdesc type Region, returns 400")
        void should_return_400_when_getcogpaysdesc_type_region() throws Exception {
            mockMvc.perform(get("/geo/pays/99901/descendants")
                            .param("date", "2025-01-01")
                            .param("type", "Region"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("geo/pays")
    class GetCogPaysListe {

        @Test
        @DisplayName("When getcogpayslist date=2025-01-01, returns 1 pays actif")
        void should_return_1_pays_when_getcogpayslist_date() throws Exception {
            var response = endpoints.getcogpayslist("2025-01-01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/pays-liste-date-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogpayslist date=*, returns 2 pays (historique)")
        void should_return_2_pays_when_getcogpayslist_etoile() throws Exception {
            var response = endpoints.getcogpayslist("*");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/pays-liste-etoile-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/pays/{code}/precedents")
    class GetCogPaysPrec {

        @Test
        @DisplayName("When getcogpaysprec 99901, returns 1 precedent (99902)")
        void should_return_1_precedent_when_getcogpaysprec_99901() throws Exception {
            var response = endpoints.getcogpaysprec("99901", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/pays-99901-precedents-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/pays/{code}/suivants")
    class GetCogPaysSuiv {

        @Test
        @DisplayName("When getcogpayssuiv 99902, returns 1 suivant (99901)")
        void should_return_1_suivant_when_getcogpayssuiv_99902() throws Exception {
            var response = endpoints.getcogpayssuiv("99902", LocalDate.of(2005, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/pays-99902-suivants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }
}
