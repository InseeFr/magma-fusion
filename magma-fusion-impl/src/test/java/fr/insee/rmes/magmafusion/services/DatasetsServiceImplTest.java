package fr.insee.rmes.magmafusion.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.IdLabel;
import fr.insee.rmes.magmafusion.model.Label;
import fr.insee.rmes.magmafusion.queries.parameters.DatasetsRequestParametizer;
import fr.insee.rmes.magmafusion.utils.*;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class DatasetsServiceImplTest {

    private RequestProcessor requestProcessor;

    private DatasetsServiceImpl service;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        requestProcessor = mock(RequestProcessor.class, RETURNS_DEEP_STUBS);
        service = new DatasetsServiceImpl("fr", "en", requestProcessor);
    }

    // =========================================================
    //   convertDatasetDTOsToDataSets
    // =========================================================

    @Test
    void should_map_list_of_dtos_to_datasets() throws IOException, JSONException {
        var dtos = List.of(
                new DatasetDTO("id1", "http://bauhaus/ds/id1", "Titre 1 FR", "Title 1 EN",
                        LocalDate.of(2024, 1, 10), "Publiée", LocalDate.of(2023, 6, 1)),
                new DatasetDTO("id2", "http://bauhaus/ds/id2", "Titre 2 FR", "Title 2 EN",
                        null, "Provisoire, jamais publiée", null)
        );

        var result = service.convertDatasetDTOsToDataSets(dtos);

        assertNotNull(result, "Result should not be null");

        String data = objectMapper.writeValueAsString(result);
        String expected = new String(
                Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("services/datasets-list-expected.json"))
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
        JSONAssert.assertEquals(expected, data, JSONCompareMode.LENIENT);

    }

    // =========================================================
    //   convertDatasetByIdDTOToDataSet
    // =========================================================

    @Test
    void should_map_basic_fields() throws IOException, JSONException {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());
        assertNotNull(result, "Result should not be null");

        String data = objectMapper.writeValueAsString(result);
        String expected = new String(
                Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("services/full-dataset-expected.json"))
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
        JSONAssert.assertEquals(expected, data, JSONCompareMode.LENIENT);
    }


    @Test
    void should_map_landingPage_when_present() {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());

        assertAll(
                () -> assertNotNull(result.getPageAccueil()),
                () -> assertEquals(2, result.getPageAccueil().size()),
                () -> assertEquals("fr", result.getPageAccueil().getFirst().getLangue()),
                () -> assertEquals("https://example.fr/page", result.getPageAccueil().getFirst().getUrl()),
                () -> assertEquals("en", result.getPageAccueil().get(1).getLangue()),
                () -> assertEquals("https://example.en/page", result.getPageAccueil().get(1).getUrl())
        );
    }

    @Test
    void should_not_set_landingPage_when_null() {
        assertNull(service.convertDatasetByIdDTOToDataSet(minimalDatasetByIdDTO()).getPageAccueil());
    }

    @Test
    void should_map_publisher_when_idPublisher_present() {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());

        assertAll(
                () -> assertNotNull(result.getEditeur()),
                () -> assertEquals("DG75-F001", result.getEditeur().getId()),
                () -> assertEquals(2, result.getEditeur().getLibelle().size()),
                () -> assertEquals("Éditeur FR", result.getEditeur().getLibelle().getFirst().getContenu()),
                () -> assertEquals("Publisher EN", result.getEditeur().getLibelle().get(1).getContenu())
        );
    }

    @Test
    void should_not_set_publisher_when_idPublisher_is_null() {
        assertNull(service.convertDatasetByIdDTOToDataSet(minimalDatasetByIdDTO()).getEditeur());
    }

    @Test
    void should_map_spatial_when_spatialId_present() {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());

        assertAll(
                () -> assertNotNull(result.getCouvertureSpatiale()),
                () -> assertEquals("France", result.getCouvertureSpatiale().getId()),
                () -> assertEquals("France métropolitaine", result.getCouvertureSpatiale().getLibelle().getFirst().getContenu()),
                () -> assertEquals("Metropolitan France", result.getCouvertureSpatiale().getLibelle().get(1).getContenu())
        );
    }

    @Test
    void should_not_set_spatial_when_spatialId_is_null() {
        assertNull(service.convertDatasetByIdDTOToDataSet(minimalDatasetByIdDTO()).getCouvertureSpatiale());
    }

    @Test
    void should_map_temporal_when_startPeriod_present() {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());

        assertAll(
                () -> assertNotNull(result.getPeriodeTemporelle()),
                () -> assertEquals("2010", result.getPeriodeTemporelle().getDebutPeriode()),
                () -> assertEquals("2024", result.getPeriodeTemporelle().getFinPeriode())
        );
    }

    @Test
    void should_not_set_temporal_when_startPeriod_is_null() {
        assertNull(service.convertDatasetByIdDTOToDataSet(minimalDatasetByIdDTO()).getPeriodeTemporelle());
    }

    @Test
    void should_map_structure_when_structureUri_present() {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());

        assertAll(
                () -> assertNotNull(result.getStructure()),
                () -> assertEquals("http://bauhaus/dsd/dsd1000", result.getStructure().getUri()),
                () -> assertEquals("dsd1000", result.getStructure().getId()),
                () -> assertEquals("DSD_1000", result.getStructure().getStructureDonnees())
        );
    }

    @Test
    void should_not_set_structure_when_structureUri_is_null() {
        assertNull(service.convertDatasetByIdDTOToDataSet(minimalDatasetByIdDTO()).getStructure());
    }

    @Test
    void should_parse_numObservations_and_numSeries_as_integers() {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());

        assertAll(
                () -> assertEquals(42000, result.getNumObservations()),
                () -> assertEquals(12, result.getNumSeries())
        );
    }

    @Test
    void should_not_set_numObservations_when_null() {
        var result = service.convertDatasetByIdDTOToDataSet(minimalDatasetByIdDTO());
        assertNull(result.getNumObservations());
        assertNull(result.getNumSeries());
    }

    @Test
    void should_parse_single_creator_from_pipe_dollar_format() {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());

        assertAll(
                () -> assertEquals(1, result.getCreateur().size()),
                () -> assertEquals("DG75-E330", result.getCreateur().getFirst().getId()),
                () -> assertEquals(2, result.getCreateur().getFirst().getLibelle().size()),
                () -> assertEquals("fr", result.getCreateur().getFirst().getLibelle().getFirst().getLangue()),
                () -> assertEquals("Division ESPRI", result.getCreateur().getFirst().getLibelle().getFirst().getContenu()),
                () -> assertEquals("en", result.getCreateur().getFirst().getLibelle().get(1).getLangue()),
                () -> assertEquals("ESPRI Division", result.getCreateur().getFirst().getLibelle().get(1).getContenu())
        );
    }

    @Test
    void should_parse_multiple_creators_separated_by_pipe() {
        var dto = datasetByIdDTOWithCreators("DG75-A$Label A FR$Label A EN|DG75-B$Label B FR$Label B EN");
        var result = service.convertDatasetByIdDTOToDataSet(dto);

        assertAll(
                () -> assertEquals(2, result.getCreateur().size()),
                () -> assertEquals("DG75-A", result.getCreateur().get(0).getId()),
                () -> assertEquals("Label A FR", result.getCreateur().get(0).getLibelle().getFirst().getContenu()),
                () -> assertEquals("Label A EN", result.getCreateur().get(0).getLibelle().get(1).getContenu()),
                () -> assertEquals("DG75-B", result.getCreateur().get(1).getId()),
                () -> assertEquals("Label B FR", result.getCreateur().get(1).getLibelle().getFirst().getContenu())
        );
    }

    @Test
    void should_not_set_creator_when_creators_is_null() {
        var result = service.convertDatasetByIdDTOToDataSet(minimalDatasetByIdDTO());
        assertNull(result.getCreateur());
    }

    @Test
    void should_parse_operationStat_as_uri_list() {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());

        assertAll(
                () -> assertEquals(1, result.getaEteGenerePar().size()),
                () -> assertEquals("http://bauhaus/operations/serie/s1223", result.getaEteGenerePar().getFirst().getId())
        );
    }

    @Test
    void should_return_null_wasGeneratedBy_when_operationStat_is_null() {
        assertNull(service.convertDatasetByIdDTOToDataSet(minimalDatasetByIdDTO()).getaEteGenerePar());
    }

    @Test
    void should_parse_theme_as_uri_list() {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());

        assertAll(
                () -> assertEquals(2, result.getTheme().size()),
                () -> assertEquals("http://theme/emploi", result.getTheme().get(0).getUri()),
                () -> assertEquals("http://theme/chomage", result.getTheme().get(1).getUri())
        );
    }

    @Test
    void should_parse_keywords_lg1_and_lg2() {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());

        assertAll(
                () -> assertEquals(3, result.getKeyword().size()),
                () -> assertEquals("fr", result.getKeyword().get(0).getLangue()),
                () -> assertEquals("emploi", result.getKeyword().get(0).getContenu()),
                () -> assertEquals("fr", result.getKeyword().get(1).getLangue()),
                () -> assertEquals("chômage", result.getKeyword().get(1).getContenu()),
                () -> assertEquals("en", result.getKeyword().get(2).getLangue()),
                () -> assertEquals("employment", result.getKeyword().get(2).getContenu())
        );
    }

    @Test
    void should_parse_relations() {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());

        assertAll(
                () -> assertEquals(2, result.getRelations().size()),
                () -> assertEquals("http://ds/related1", result.getRelations().get(0)),
                () -> assertEquals("http://ds/related2", result.getRelations().get(1))
        );
    }

    @Test
    void should_parse_archiveUnits() {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());

        assertAll(
                () -> assertEquals(1, result.getArchiveUnit().size()),
                () -> assertEquals("http://archive/unit1", result.getArchiveUnit().getFirst().getId())
        );
    }

    @Test
    void should_map_wasDerivedFrom_with_description_when_present() {
        var result = service.convertDatasetByIdDTOToDataSet(fullDatasetByIdDTO());

        assertAll(
                () -> assertNotNull(result.getWasDerivedFrom()),
                () -> assertEquals(1, result.getWasDerivedFrom().getDatasets().size()),
                () -> assertEquals("ds-source-1", result.getWasDerivedFrom().getDatasets().getFirst()),
                () -> assertNotNull(result.getWasDerivedFrom().getDescription()),
                () -> assertEquals("Dérivé de FR", result.getWasDerivedFrom().getDescription().getFirst().getContenu()),
                () -> assertEquals("Derived from EN", result.getWasDerivedFrom().getDescription().get(1).getContenu())
        );
    }

    @Test
    void should_not_set_wasDerivedFrom_when_null() {
        assertNull(service.convertDatasetByIdDTOToDataSet(minimalDatasetByIdDTO()).getWasDerivedFrom());
    }


    // =========================================================
    //   getTemporalResolution
    // =========================================================

    @Test
    void getTemporalResolution_shouldReturnLabelsWithLocalisedTitles() {
        // Given
        TemporalResolutionDTO annualDto = new TemporalResolutionDTO(
                "Annuelle",
                "Annual"
        );

        TemporalResolutionDTO monthlyDto = new TemporalResolutionDTO(
                "Mensuelle",
                "Monthly"
        );

        when(requestProcessor.queryToFindTemporalResolutionContenu()
                .with(any(DatasetsRequestParametizer.class))
                .executeQuery()
                .singleResult(TemporalResolutionDTO.class)
                .result())
                .thenReturn(annualDto)  // Premier appel (pour "http://bauhaus/codes/frequence/A")
                .thenReturn(monthlyDto); // Second appel (pour "http://bauhaus/codes/frequence/M")

        List<String> uris = List.of(
                "http://bauhaus/codes/frequence/A",
                "http://bauhaus/codes/frequence/M"
        );

        // When
        List<Label> result = service.getTemporalResolution(uris);

        // Then
        assertThat(result).hasSize(2);

        Label annualResolution = result.get(0);
        assertThat(annualResolution.getLibelle()).hasSize(2);
        assertThat(annualResolution.getLibelle().get(0).getLangue()).isEqualTo("fr");
        assertThat(annualResolution.getLibelle().get(0).getContenu()).isEqualTo("Annuelle");
        assertThat(annualResolution.getLibelle().get(1).getLangue()).isEqualTo("en");
        assertThat(annualResolution.getLibelle().get(1).getContenu()).isEqualTo("Annual");

        Label monthlyResolution = result.get(1);
        assertThat(monthlyResolution.getLibelle()).hasSize(2);
        assertThat(monthlyResolution.getLibelle().get(0).getLangue()).isEqualTo("fr");
        assertThat(monthlyResolution.getLibelle().get(0).getContenu()).isEqualTo("Mensuelle");
        assertThat(monthlyResolution.getLibelle().get(1).getLangue()).isEqualTo("en");
        assertThat(monthlyResolution.getLibelle().get(1).getContenu()).isEqualTo("Monthly");
    }

    @Test
    void getTemporalResolution_shouldReturnEmptyListForEmptyInput() {
        // Given
        List<String> uris = List.of();

        // When
        List<Label> result = service.getTemporalResolution(uris);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getTemporalResolution_shouldHandleNullLabelsInDTO() {
        // Given
        TemporalResolutionDTO dtoWithNullLabels = new TemporalResolutionDTO(
                null,  // labeltemporalResolutionLg1 = null
                null   // labeltemporalResolutionLg2 = null
        );

          when(requestProcessor.queryToFindTemporalResolutionContenu()
                .with(any(DatasetsRequestParametizer.class))
                .executeQuery()
                .singleResult(TemporalResolutionDTO.class)
                .result())
                .thenReturn(dtoWithNullLabels);

        List<String> uris = List.of("http://bauhaus/codes/frequence/A");

        // When
        List<Label> result = service.getTemporalResolution(uris);

        // Then
        assertThat(result).hasSize(1);
        Label resolution = result.get(0);
        assertThat(resolution.getLibelle()).isNull();
    }

    @Test
    void getTemporalResolution_shouldHandleMissingLabelsInDTO() {
        // Given
        TemporalResolutionDTO dtoWithMissingLabels = new TemporalResolutionDTO(
                "",    // labeltemporalResolutionLg1 = vide
                null   // labeltemporalResolutionLg2 = null
        );

        when(requestProcessor.queryToFindTemporalResolutionContenu()
                .with(any(DatasetsRequestParametizer.class))
                .executeQuery()
                .singleResult(TemporalResolutionDTO.class)
                .result())
                .thenReturn(dtoWithMissingLabels);

        List<String> uris = List.of("http://bauhaus/codes/frequence/A");

        // When
        List<Label> result = service.getTemporalResolution(uris);

        // Then
        assertThat(result).hasSize(1);
        Label resolution = result.get(0);

        assertThat(resolution.getLibelle()).isEmpty();
    }


    // =========================================================
    //   getSpatialResolution
    // =========================================================

    @Test
    void getSpatialResolution_shouldReturnIdLabelsWithLocalisedTitles() {
        // Given
        SpatialResolutionDTO spatialResolution1Dto = new SpatialResolutionDTO(
                "SpatialResolution1Id" ,
                "labelSpatialResolution1Lg1",
                "labelSpatialResolution1Lg2"
        );

        SpatialResolutionDTO spatialResolution2Dto = new SpatialResolutionDTO(
                "SpatialResolution2Id",
                "labelSpatialResolution2Lg1",
                "labelSpatialResolution2Lg2"
        );

        when(requestProcessor.queryToFindSpatialResolutionContenu()
                .with(any(DatasetsRequestParametizer.class))
                .executeQuery()
                .singleResult(SpatialResolutionDTO.class)
                .result())
                .thenReturn(spatialResolution1Dto)
                .thenReturn(spatialResolution2Dto);

        List<String> uris = List.of(
                "http://bauhaus/codes/frequence/A",
                "http://bauhaus/codes/frequence/M"
        );

        // When
        List<IdLabel> result = service.getSpatialResolution(uris);

        // Then
        assertThat(result).hasSize(2);

        IdLabel firstResolution = result.get(0);
        assertThat(firstResolution.getLibelle()).hasSize(2);
        assertThat(firstResolution.getLibelle().get(0).getLangue()).isEqualTo("fr");
        assertThat(firstResolution.getLibelle().get(0).getContenu()).isEqualTo("labelSpatialResolution1Lg1");
        assertThat(firstResolution.getLibelle().get(1).getLangue()).isEqualTo("en");
        assertThat(firstResolution.getLibelle().get(1).getContenu()).isEqualTo("labelSpatialResolution1Lg2");

        IdLabel secondResolution = result.get(1);
        assertThat(secondResolution.getLibelle()).hasSize(2);
        assertThat(secondResolution.getLibelle().get(0).getLangue()).isEqualTo("fr");
        assertThat(secondResolution.getLibelle().get(0).getContenu()).isEqualTo("labelSpatialResolution2Lg1");
        assertThat(secondResolution.getLibelle().get(1).getLangue()).isEqualTo("en");
        assertThat(secondResolution.getLibelle().get(1).getContenu()).isEqualTo("labelSpatialResolution2Lg2");
    }

    // =========================================================
    //   getStatistiaclUnits
    // =========================================================

    @Test
    void getStaitsticalUnits_shouldReturnIdLabelsWithLocalisedTitles() {
        // Given
        StatisticalUnitDTO statisticalUnit1DTO = new StatisticalUnitDTO(
                "statisticalUnit1Id" ,
                "labelStatisticalUnit1Lg1",
                "labelStatisticalUnit1Lg2"
        );

        StatisticalUnitDTO statisticalUnit2DTO = new StatisticalUnitDTO(
                "statisticalUnit2DTO",
                "labelStatisticalUnit2Lg1",
                "labelStatisticalUnit2Lg2"
        );

        when(requestProcessor.queryToFindStatisticalUnits()
                .with(any(DatasetsRequestParametizer.class))
                .executeQuery()
                .singleResult(StatisticalUnitDTO.class)
                .result())
                .thenReturn(statisticalUnit1DTO)
                .thenReturn(statisticalUnit2DTO);

        List<String> uris = List.of(
                "http://uriStatisticalUnit1",
                "http://uriStatisticalUnit2"
        );

        // When
        List<IdLabel> result = service.getStatisticalUnits(uris);

        // Then
        assertThat(result).hasSize(2);

        IdLabel firstStatiticalUnit = result.get(0);
        assertThat(firstStatiticalUnit.getLibelle()).hasSize(2);
        assertThat(firstStatiticalUnit.getLibelle().get(0).getLangue()).isEqualTo("fr");
        assertThat(firstStatiticalUnit.getLibelle().get(0).getContenu()).isEqualTo("labelStatisticalUnit1Lg1");
        assertThat(firstStatiticalUnit.getLibelle().get(1).getLangue()).isEqualTo("en");
        assertThat(firstStatiticalUnit.getLibelle().get(1).getContenu()).isEqualTo("labelStatisticalUnit1Lg2");

        IdLabel secondStatiticalUnit = result.get(1);
        assertThat(secondStatiticalUnit.getLibelle()).hasSize(2);
        assertThat(secondStatiticalUnit.getLibelle().get(0).getLangue()).isEqualTo("fr");
        assertThat(secondStatiticalUnit.getLibelle().get(0).getContenu()).isEqualTo("labelStatisticalUnit2Lg1");
        assertThat(secondStatiticalUnit.getLibelle().get(1).getLangue()).isEqualTo("en");
        assertThat(secondStatiticalUnit.getLibelle().get(1).getContenu()).isEqualTo("labelStatisticalUnit2Lg2");
    }


    // =========================================================
    //   Fixtures
    // =========================================================

    private DatasetByIdDTO fullDatasetByIdDTO() {
        return new DatasetByIdDTO(
                "http://bauhaus/ds/25baaf1f",    // uri
                "25baaf1f",                      // id
                "Provisoire, jamais publiée",    // statutValidation
                "Titre FR", "Title EN",          // titleLg1, titleLg2
                "Sous-titre FR", "Subtitle EN",  // subtitleLg1, subtitleLg2
                "Résumé FR", "Abstract EN",      // abstractLg1, abstractLg2
                "Description FR", "Description EN", // descriptionLg1, descriptionLg2
                "Note FR", "Note EN",            // scopeNoteLg1, scopeNoteLg2
                "https://example.fr/page", "https://example.en/page", // landingPageLg1, landingPageLg2
                "id catalogRecordCreator",
                "catalogRecordCreatorLabelLg1",
                "catalogRecordCreatorLabelLg2",// catalogRecordCreator
                "id catalogRecordContributor",
                "catalogRecordContributorLabelLg1",
                "catalogRecordContributorLabelLg2",// catalogRecordContributor
                "2024-12-09T12:00:00",           // catalogRecordModified
                "2024-12-09T12:00:00",           // catalogRecordCreated
                "2024-11-01",                    // modified
                "2023-05-15",                    // issued
                "2.0",                           // version
                "2025-01-01",                            // spatialTemporal
                "2010",                          // startPeriod
                "2024",                          // endPeriod
                "Dérivé de FR", "Derived from EN", // derivedDescriptionLg1, derivedDescriptionLg2
                "DG75-F001", "Éditeur FR", "Publisher EN", // idPublisher, labelPublisherLg1, labelPublisherLg2
                "label Type FR", "label Type EN",                      // labeltypeLg1, labeltypeLg2
                "label Access Rights FR", "label Access Rights EN",                      // labelaccessRightsLg1, labelaccessRightsLg2
                "label Condidentiality Status FR", "label Condidentiality Status EN",                      // labelconfidentialityStatusLg1, labelconfidentialityStatusLg2
                "label Accrual Periodicity FR", "label Accrual Periodicity EN",                      // labelaccrualPeriodicityLg1, labelaccrualPeriodicityLg2
                "France", "France métropolitaine", "Metropolitan France", // spatialId, labelspatialLg1, labelspatialLg2
                "code process",                            // codeProcessStep
                "dissemination status",                            // disseminationStatus
                "DD_EEC_SERIES",                 // identifier
                "http://bauhaus/dsd/dsd1000", "dsd1000", "DSD_1000", null, // structureUri, structureId, dsd, isDataStructureDefinition
                "42000", "12",                   // numObservations, numSeries
                "DG75-E330$Division ESPRI$ESPRI Division", // creators
                "http://bauhaus/operations/serie/s1223", // operationStat
                "http://theme/emploi,http://theme/chomage", // names
                "ds-source-1",                   // wasDerivedFromS
                "http://ds/related1,http://ds/related2", // relations
                "emploi,chômage", "employment",  // keywordLg1, keywordLg2
                "http://archive/unit1",          // archiveUnits
                null, // temporalResolutions
                null,   // spatialResolutions
                null,  //statiticalUnits
                null //themes
        );
    }

    /** DTO avec tous les champs optionnels à null */
    private DatasetByIdDTO minimalDatasetByIdDTO() {
        return new DatasetByIdDTO(
                "http://bauhaus/ds/min", "min-id", null,
                "Titre FR", "Title EN",
                null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null, null, null,
                null, null, null, null,
                null, null,
                null, null,
                null, null, null,
                null, null,
                null, null,
                null, null,
                null, null,
                null, null, null,
                null, null, null,
                null, null, null, null,
                null, null,
                null, null, null, null,
                null, null, null,
                null, null, null,
                null,
                null,
                null, null, null, null
        );
    }

    private DatasetByIdDTO datasetByIdDTOWithCreators(String creators) {
        return new DatasetByIdDTO(
                "http://bauhaus/ds/min", "min-id", null,
                "Titre FR", "Title EN",
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, creators, null, null, null, null,
                null,null, null, null, null, null, null
        );
    }
}