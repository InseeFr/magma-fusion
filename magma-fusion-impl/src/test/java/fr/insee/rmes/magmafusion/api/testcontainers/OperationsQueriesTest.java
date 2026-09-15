package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.OperationsEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "--spring.profiles.active=security.disabled")
@AutoConfigureMockMvc
@Tag("integration")

class OperationsQueriesTest extends TestContainer {

    @Autowired
    OperationsEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("operations/rapportQualite/{id}")
    class GetOperation {

        @Test
        @DisplayName("When getRapportQualiteByCode, returns rapport with all rubrique types (TEXT, DATE, RICH_TEXT, GEOGRAPHY, ORGANIZATION, CODE_LIST)")
        void should_return_rapportQualite_with_all_rubrique_types() throws Exception {
            var response = endpoints.getRapportQualiteByCode("9999");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/rapportQualite-9999-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }

        @Test
        @DisplayName("When getRapportQualiteByCode for rapport without rubriques, returns rapport without rubriques")
        void should_return_rapportQualite_without_rubriques() throws Exception {
            var response = endpoints.getRapportQualiteByCode("9998");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/rapportQualite-9998-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }

        @Test
        @DisplayName("When getRapportQualiteByCode with unknown id, returns 404")
        void should_return_404_when_rapportQualite_inexistant() throws Exception {
            mockMvc.perform(get("/operations/rapportQualite/0000"))
                    .andExpect(status().isNotFound());
        }
    }
}