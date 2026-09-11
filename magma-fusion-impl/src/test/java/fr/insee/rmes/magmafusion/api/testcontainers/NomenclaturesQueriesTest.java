package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.NomenclaturesEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
class NomenclaturesQueriesTest extends TestContainer {

    @Autowired
    NomenclaturesEndpoints endpoints;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("codes/{nomenclature}/{niveau}/{code}")
    class GetClassificationByCode {

        @Test
        @DisplayName("When testclassif/niveauA/T01, returns concept T01")
        void should_return_concept_T01_when_testclassif_niveauA_T01() throws Exception {
            var response = endpoints.getClassificationByCode("testclassif", "niveauA", "T01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/nomenclature-testclassif-T01-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When testclassif2/niveauB/A01, returns concept A01")
        void should_return_concept_A01_when_testclassif2_niveauB_A01() throws Exception {
            var response = endpoints.getClassificationByCode("testclassif2", "niveauB", "A01");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/nomenclature-testclassif2-A01-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When testclassif/niveauA/T99 (code inexistant), returns 404")
        void should_return_404_when_code_inexistant() throws Exception {
            mockMvc.perform(get("/codes/testclassif/niveauA/T99"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("When inexistant/niveauA/T01 (nomenclature inexistante), returns 404")
        void should_return_404_when_nomenclature_inexistante() throws Exception {
            mockMvc.perform(get("/codes/inexistant/niveauA/T01"))
                    .andExpect(status().isNotFound());
        }
    }
}