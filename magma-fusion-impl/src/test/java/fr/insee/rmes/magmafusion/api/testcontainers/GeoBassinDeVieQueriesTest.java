package fr.insee.rmes.magmafusion.api.testcontainers;

import fr.insee.rmes.magmafusion.api.GeoBassinDeVieEndpoints;
import fr.insee.rmes.magmafusion.api.testcontainers.config.TestContainer;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsBassinDeVie;
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
class GeoBassinDeVieQueriesTest extends TestContainer {

    @Autowired
    GeoBassinDeVieEndpoints endpoints;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("geo/bassinDeVie2022/{code}")
    class GetCogBass {

        @Test
        @DisplayName("When getcogbass 88001, returns BV 88001")
        void should_return_bv_88001_when_getcogbass_88001() throws Exception {
            var response = endpoints.getcogbass("88001", LocalDate.of(2025, 1, 1));
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/bassin-de-vie-88001-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogbass 88099 (inexistant), returns 404")
        void should_return_404_when_getcogbass_88099_inexistant() {
            var response = endpoints.getcogbass("88099", LocalDate.of(2025, 1, 1));
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("geo/bassinDeVie2022/{code}/descendants")
    class GetCogBassDes {

        @Test
        @DisplayName("When getcogbassdes 88001 type null, returns 4 descendants (2 communes + 2 comdel)")
        void should_return_4_descendants_when_getcogbassdes_88001_type_null() throws Exception {
            var response = endpoints.getcogbassdes("88001", LocalDate.of(2025, 1, 1), null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/bassin-de-vie-88001-descendants-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }

        @Test
        @DisplayName("When getcogbassdes 88001 type Commune, returns 2 communes")
        void should_return_2_communes_when_getcogbassdes_88001_type_commune() throws Exception {
            var response = endpoints.getcogbassdes("88001", LocalDate.of(2025, 1, 1), TypeEnumDescendantsBassinDeVie.COMMUNE);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/bassin-de-vie-88001-descendants-commune-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, true);
        }
    }

    @Nested
    @DisplayName("geo/bassinsDeVie2022")
    class GetCogBassListe {

        @Test
        @DisplayName("When getcogbassliste date=2025-01-01 filtreNom='Bassin de vie test 1', returns 1 BV")
        void should_return_1_bv_when_getcogbassliste_filtre_nom() throws Exception {
            var response = endpoints.getcogbassliste("2025-01-01", "Bassin de vie test 1");
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/bassins-de-vie-liste-filtre-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }

        @Test
        @DisplayName("When getcogbassliste date=*, returns 2 BV")
        void should_return_2_bv_when_getcogbassliste_etoile() throws Exception {
            var response = endpoints.getcogbassliste("*", null);
            var result = response.getBody();

            assertNotNull(result);
            String data = objectMapper.writeValueAsString(result);
            String expected = new String(
                    Objects.requireNonNull(getClass().getClassLoader()
                                    .getResourceAsStream("testcontainers/bassins-de-vie-liste-etoile-expected.json"))
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            JSONAssert.assertEquals(expected, data, false);
        }
    }
}