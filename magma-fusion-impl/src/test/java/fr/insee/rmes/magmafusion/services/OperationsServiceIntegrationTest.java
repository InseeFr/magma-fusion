package fr.insee.rmes.magmafusion.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.insee.rmes.magmafusion.services.utils.BaseIntegrationServiceTest;
import fr.insee.rmes.magmafusion.utils.SeriesDTO;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

class OperationsServiceIntegrationTest extends BaseIntegrationServiceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OperationsServiceImpl service;

    @Test
    void should_return_valid_json_when_convertSeriesDTO() throws Exception {
        var result = service.convertSeriesDTOToSerieById(fullSeriesDTO());
        String actualJson = objectMapper.writeValueAsString(result);

        JSONAssert.assertEquals(
                loadExpectedJson("serie-expected.json"),
                actualJson,
                true
        );
    }

    private SeriesDTO fullSeriesDTO() {
        return new SeriesDTO(
                "s1001",
                "http://id.insee.fr/operations/serie/s1001",
                "Enquête innovation", "Innovation survey",
                "Sigle FR", "Sigle EN",
                "2020",
                "Résumé FR", "Abstract EN",
                "Note historique FR", "History note EN",
                "http://id.insee.fr/concepts/type/E", "E", "Enquête", "Survey",
                "http://id.insee.fr/concepts/periodicity/A", "A", "Annuelle", "Annual",
                "f1001$http://id.insee.fr/operations/famille/f1001$Famille FR$Family EN",
                "s1197$http://id.insee.fr/operations/serie/s1197$Série liée 1$Linked series 1|s1198$http://id.insee.fr/operations/serie/s1198$Série liée 2$Linked series 2",
                "s1010$http://id.insee.fr/operations/serie/s1010$Précédente FR$Previous EN",
                "s1020$http://id.insee.fr/operations/serie/s1020$Suivante FR$Next EN",
                "op2024$http://id.insee.fr/operations/operation/op2024$Opération 2024$Operation 2024|op2023$http://id.insee.fr/operations/operation/op2023$Opération 2023$Operation 2023",
                null,
                "http://id.insee.fr/qualite/rapport/1500", "1500",
                LocalDate.of(2020, 1, 15),
                LocalDate.of(2024, 6, 1),
                "insee$http://id.insee.fr/organisations/insee$Institut national de la statistique$French national institute of statistics",
                "drees$http://id.insee.fr/organisations/drees$DREES$DREES",
                "dares$http://id.insee.fr/organisations/dares$DARES$DARES",
                "dataCollectorId1$dataCollector1$dataCollector1LabelLg1$dataCollector1LabelLg2|dataCollectorId2$dataCollector2$dataCollector2LabelLg1$dataCollector2LabelLg2",
                "Publiée"
        );
    }
}
