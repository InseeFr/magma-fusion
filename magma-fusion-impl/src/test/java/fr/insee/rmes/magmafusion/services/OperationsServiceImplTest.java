package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.RapportQualite;
import fr.insee.rmes.magmafusion.model.Rubrique;
import fr.insee.rmes.magmafusion.queries.parameters.OperationsDocumentsRequestParametizer;
import fr.insee.rmes.magmafusion.utils.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OperationsServiceImplTest {
    private OperationsServiceImpl service;
    private RequestProcessor requestProcessor;

    @BeforeEach
    void setUp() {
        service = new OperationsServiceImpl(requestProcessor);
        ReflectionTestUtils.setField(service, "lg1", "fr");
        ReflectionTestUtils.setField(service, "lg2", "en");
    }


    @Test
    void convertDTOToRapportQualite_shouldMapBasicFields() {
        // Given
        RapportQualiteDTO dto = new RapportQualiteDTO("rubrique-001","http://example.com/rubrique-001","Rapport qualité","Quality report", null,null,null,null,null);

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        assertThat(result.getId()).isEqualTo("rubrique-001");
        assertThat(result.getUri()).isEqualTo(URI.create("http://example.com/rubrique-001"));
        assertThat(result.getLabel())
                .hasSize(2)
                .satisfies(labels -> {
                    assertThat(labels.get(0).getLangue()).isEqualTo("fr");
                    assertThat(labels.get(0).getContenu()).isEqualTo("Rapport qualité");
                    assertThat(labels.get(1).getLangue()).isEqualTo("en");
                    assertThat(labels.get(1).getContenu()).isEqualTo("Quality report");
                });
    }

    @Test
    void convertDTOToRapportQualite_shouldHandleOnlyFrenchLabel() {
        // Given
        RapportQualiteDTO dto = new RapportQualiteDTO("rubrique-002","http://example.com/rubrique-002","Rapport qualité",null,null,null,null,null,null);

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        assertThat(result.getLabel()).hasSize(2);
        assertThat(result.getLabel().get(0).getContenu()).isEqualTo("Rapport qualité");
        assertThat(result.getLabel().get(1).getContenu()).isEmpty();
    }

    @Test
    void convertDTOToRapportQualite_shouldHandleNullRubriqueList() {
        // Given
        RapportQualiteDTO dto = createBasicDTO();
        dto.withRubriqueDTOList(null);

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        assertThat(result.getId()).isEqualTo("rubrique-test");
        assertThat(result.getRubriques()).isNull();
    }

    @Test
    void convertRubrique_shouldConvertDateType() {
        // Given
        RapportQualiteDTO dto = createBasicDTO();
        RubriqueDTO rubriqueDTO = createRubriqueDTO("rubrique-date", "DATE");
        rubriqueDTO = rubriqueDTO.withValeurSimple("2024-01-15");
        rubriqueDTO = rubriqueDTO.withTitreLg1("Date de publication");
        rubriqueDTO = rubriqueDTO.withTitreLg2("Publication date");
        dto = dto.withRubriqueDTOList(List.of(rubriqueDTO));

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        assertThat(result.getRubriques()).hasSize(1);
        Rubrique rubrique = result.getRubriques().getFirst();
        assertThat(rubrique.getId()).isEqualTo("rubrique-date");
        assertThat(rubrique.getType()).isEqualTo("DATE");
        assertThat(rubrique.getDate()).isEqualTo("2024-01-15");
        assertThat(rubrique.getTitre()).hasSize(2);
        assertThat(rubrique.getTitre().getFirst().getContenu()).isEqualTo("Date de publication");
        assertThat(rubrique.getTitre().get(1).getContenu()).isEqualTo("Publication date");
    }

    @Test
    void convertRubrique_shouldConvertTextType() {
        // Given
        RapportQualiteDTO dto = createBasicDTO();
        RubriqueDTO rubriqueDTO = createRubriqueDTO("rubrique-text", "TEXT");
        rubriqueDTO = rubriqueDTO.withLabelLg1("Texte français");
        rubriqueDTO = rubriqueDTO.withLabelLg2("English text");
        dto = dto.withRubriqueDTOList(List.of(rubriqueDTO));

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        assertThat(result.getRubriques()).hasSize(1);
        Rubrique rubrique = result.getRubriques().getFirst();
        assertThat(rubrique.getType()).isEqualTo("TEXT");
        assertThat(rubrique.getLabel()).hasSize(2);
        assertThat(rubrique.getLabel().get(0).getContenu()).isEqualTo("Texte français");
        assertThat(rubrique.getLabel().get(1).getContenu()).isEqualTo("English text");
    }

    @Test
    void convertRubrique_shouldConvertGeographyType() {
        // Given
        RapportQualiteDTO dto = createBasicDTO();
        RubriqueDTO rubriqueDTO = createRubriqueDTO("rubrique-geo", "GEOGRAPHY");
        rubriqueDTO = rubriqueDTO.withValeurSimple("FR");
        rubriqueDTO = rubriqueDTO.withGeoUri("http://example.com/geo/fr");
        rubriqueDTO = rubriqueDTO.withLabelObjLg1("France");
        rubriqueDTO = rubriqueDTO.withLabelObjLg2("France2");
        dto = dto.withRubriqueDTOList(List.of(rubriqueDTO));

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        assertThat(result.getRubriques()).hasSize(1);
        Rubrique rubrique = result.getRubriques().getFirst();
        assertThat(rubrique.getType()).isEqualTo("GEOGRAPHY");
        assertThat(rubrique.getTerritoire()).isNotNull();
        assertThat(rubrique.getTerritoire().getId()).isEqualTo("FR");
        assertThat(rubrique.getTerritoire().getUri()).isEqualTo(URI.create("http://example.com/geo/fr"));
        assertThat(rubrique.getTerritoire().getLabel()).hasSize(2);
        assertThat(rubrique.getTerritoire().getLabel().getFirst().getContenu()).isEqualTo("France");
        assertThat(rubrique.getTerritoire().getLabel().get(1).getContenu()).isEqualTo("France2");
    }

    @Test
    void convertRubrique_shouldConvertGeographyType_withOnlyFrenchLabel() {
        // Given
        RapportQualiteDTO dto = createBasicDTO();
        RubriqueDTO rubriqueDTO = createRubriqueDTO("rubrique-geo", "GEOGRAPHY");
        rubriqueDTO = rubriqueDTO.withValeurSimple("FR");
        rubriqueDTO = rubriqueDTO.withGeoUri("http://example.com/geo/fr");
        rubriqueDTO = rubriqueDTO.withLabelObjLg1("France métropolitaine");
        rubriqueDTO = rubriqueDTO.withLabelObjLg2(null);
        dto = dto.withRubriqueDTOList(List.of(rubriqueDTO));

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        Rubrique rubrique = result.getRubriques().getFirst();
        Assertions.assertNotNull(rubrique.getTerritoire());
        assertThat(rubrique.getTerritoire().getLabel()).hasSize(1);
        assertThat(rubrique.getTerritoire().getUri()).isEqualTo(URI.create("http://example.com/geo/fr"));
        assertThat(rubrique.getTerritoire().getLabel().getFirst().getContenu()).isEqualTo("France métropolitaine");
    }

    @Test
    void convertRubrique_shouldConvertOrganizationType() {
        // Given
        RapportQualiteDTO dto = createBasicDTO();
        RubriqueDTO rubriqueDTO = createRubriqueDTO("rubrique-org", "ORGANIZATION");
        rubriqueDTO = rubriqueDTO.withValeurSimple("INSEE");
        rubriqueDTO = rubriqueDTO.withOrganisationUri("http://example.com/org/insee");
        rubriqueDTO = rubriqueDTO.withLabelObjLg1("Institut national de la statistique");
        rubriqueDTO = rubriqueDTO.withLabelObjLg2("National Institute of Statistics");
        dto = dto.withRubriqueDTOList(List.of(rubriqueDTO));

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        assertThat(result.getRubriques()).hasSize(1);
        Rubrique rubrique = result.getRubriques().getFirst();
        assertThat(rubrique.getType()).isEqualTo("ORGANIZATION");
        assertThat(rubrique.getOrganisme()).isNotNull();
        assertThat(rubrique.getOrganisme().getId()).isEqualTo("INSEE");
        assertThat(rubrique.getOrganisme().getUri()).isEqualTo(URI.create("http://example.com/org/insee"));
        assertThat(rubrique.getOrganisme().getLabel()).hasSize(2);
    }

    @Test
    void convertRubrique_shouldConvertCodeListType_singleCode() {
        // Given
        RapportQualiteDTO dto = createBasicDTO();
        RubriqueDTO rubriqueDTO = createRubriqueDTO("rubrique-code", "CODE_LIST");
        rubriqueDTO = rubriqueDTO.withValeurSimple("CODE-001");
        rubriqueDTO = rubriqueDTO.withCodeUri("http://example.com/code/code-001");
        rubriqueDTO = rubriqueDTO.withLabelObjLg1("Code français");
        rubriqueDTO = rubriqueDTO.withLabelObjLg2("English code");
        rubriqueDTO = rubriqueDTO.withMaxOccurs(null);
        dto = dto.withRubriqueDTOList(List.of(rubriqueDTO));

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        assertThat(result.getRubriques()).hasSize(1);
        Rubrique rubrique = result.getRubriques().getFirst();
        assertThat(rubrique.getType()).isEqualTo("CODE_LIST");
        assertThat(rubrique.getCodes()).hasSize(1);
        assertThat(rubrique.getCodes().getFirst().getId()).isEqualTo("CODE-001");
        assertThat(rubrique.getCodes().getFirst().getUri()).isEqualTo(URI.create("http://example.com/code/code-001"));
        assertThat(rubrique.getCodes().getFirst().getLabel()).hasSize(2);
    }

    @Test
    void convertRubrique_shouldConvertCodeListType_multipleCodesInSameRubrique() {
        // Given
        RapportQualiteDTO dto = createBasicDTO();

        RubriqueDTO rubrique1 = createRubriqueDTO("rubrique-code", "CODE_LIST");
        rubrique1 = rubrique1.withValeurSimple("CODE-001");
        rubrique1 = rubrique1.withCodeUri("http://example.com/code/code-001");
        rubrique1 = rubrique1.withLabelObjLg1("Code 1");
        rubrique1 = rubrique1.withLabelObjLg2("Code 1 EN");
        rubrique1 = rubrique1.withMaxOccurs("unbounded");

        RubriqueDTO rubrique2 = createRubriqueDTO("rubrique-code", "CODE_LIST");
        rubrique2 = rubrique2.withValeurSimple("CODE-002");
        rubrique2 = rubrique2.withCodeUri("http://example.com/code/code-002");
        rubrique2 = rubrique2.withLabelObjLg1("Code 2");
        rubrique2 = rubrique2.withLabelObjLg2("Code 2 EN");
        rubrique2 = rubrique2.withMaxOccurs("unbounded");

        RubriqueDTO rubrique3 = createRubriqueDTO("rubrique-code", "CODE_LIST");
        rubrique3 = rubrique3.withValeurSimple("CODE-003");
        rubrique3 = rubrique3.withCodeUri("http://example.com/code/code-003");
        rubrique3 = rubrique3.withLabelObjLg1("Code 3");
        rubrique3 = rubrique3.withMaxOccurs("unbounded");

        dto = dto.withRubriqueDTOList(List.of(rubrique1, rubrique2, rubrique3));

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        assertThat(result.getRubriques())
                .hasSize(1)
                .first()
                .satisfies(rubrique -> {
                    assertThat(rubrique.getId()).isEqualTo("rubrique-code");
                    assertThat(rubrique.getCodes()).hasSize(3);
                    assertThat(rubrique.getCodes().get(0).getId()).isEqualTo("CODE-001");
                    assertThat(rubrique.getCodes().get(1).getId()).isEqualTo("CODE-002");
                    assertThat(rubrique.getCodes().get(2).getId()).isEqualTo("CODE-003");
                });
    }

    @Test
    void convertRubrique_shouldConvertRichTextType_withoutDocuments() {
        // Given
        RapportQualiteDTO dto = createBasicDTO();
        RubriqueDTO rubriqueDTO = createRubriqueDTO("rubrique-rich", "RICH_TEXT");
        rubriqueDTO = rubriqueDTO.withLabelLg1("Texte riche français");
        rubriqueDTO = rubriqueDTO.withLabelLg2("English rich text");
        rubriqueDTO = rubriqueDTO.withHasDocLg1(false);
        rubriqueDTO = rubriqueDTO.withHasDocLg2(false);
        dto = dto.withRubriqueDTOList(List.of(rubriqueDTO));

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        assertThat(result.getRubriques()).hasSize(1);
        Rubrique rubrique = result.getRubriques().getFirst();
        assertThat(rubrique.getType()).isEqualTo("RICH_TEXT");
        assertThat(rubrique.getContenus()).hasSize(2);
        assertThat(rubrique.getContenus().getFirst().getLangue()).isEqualTo("fr");
        assertThat(rubrique.getContenus().get(0).getTexte()).isEqualTo("Texte riche français");
        assertThat(rubrique.getContenus().get(0).getDocuments()).isNull();
        assertThat(rubrique.getContenus().get(1).getLangue()).isEqualTo("en");
        assertThat(rubrique.getContenus().get(1).getTexte()).isEqualTo("English rich text");
    }

    @Test
    void convertRubrique_shouldConvertRichTextType_withMultipleDocuments() {
        // Given
        DocumentDTO doc1Fr = new DocumentDTO("http://doc1.fr", "Label doc 1 FR", "Label doc 1 EN", "2024-01-01", "fr");
        DocumentDTO doc2Fr = new DocumentDTO("http://doc2.fr", "Label doc 2 FR", null, "2024-02-01", "fr");
        DocumentDTO doc1En = new DocumentDTO("http://doc1.en", "Label doc 1 EN", "Label doc 1 EN", "2024-01-01", "en");

        RequestProcessor mockProcessor = mock(RequestProcessor.class, RETURNS_DEEP_STUBS);
        when(mockProcessor.queryToFindDocuments()
                .with(any(OperationsDocumentsRequestParametizer.class))
                .executeQuery()
                .listResult(DocumentDTO.class)
                .result())
                .thenReturn(List.of(doc1Fr, doc2Fr))
                .thenReturn(List.of(doc1En));
        //Double thenReturn chaîné : le premier appel à findDocuments (pour "fr") retourne 2 documents, le second (pour "en") en retourne 1.

        OperationsServiceImpl serviceWithDocs = new OperationsServiceImpl(mockProcessor);

        RapportQualiteDTO dto = createBasicDTO();
        RubriqueDTO rubriqueDTO = createRubriqueDTO("rubrique-rich", "RICH_TEXT");
        rubriqueDTO = rubriqueDTO.withLabelLg1("Texte riche français");
        rubriqueDTO = rubriqueDTO.withLabelLg2("English rich text");
        rubriqueDTO = rubriqueDTO.withHasDocLg1(true);
        rubriqueDTO = rubriqueDTO.withHasDocLg2(true);
        dto = dto.withRubriqueDTOList(List.of(rubriqueDTO));

        // When
        RapportQualite result = serviceWithDocs.convertDTOToRapportQualite(dto);

        // Then
        assertThat(result.getRubriques()).hasSize(1);
        Rubrique rubrique = result.getRubriques().getFirst();
        assertThat(rubrique.getType()).isEqualTo("RICH_TEXT");
        assertThat(rubrique.getContenus()).hasSize(2);

        assertThat(rubrique.getContenus().get(0).getLangue()).isEqualTo("fr");
        assertThat(rubrique.getContenus().get(0).getTexte()).isEqualTo("Texte riche français");
        assertThat(rubrique.getContenus().get(0).getDocuments())
                .hasSize(2)
                .satisfies(docs -> {
                    assertThat(docs.get(0).getUrl()).isEqualTo("http://doc1.fr");
                    assertThat(docs.get(0).getDateMiseAJour()).isEqualTo("2024-01-01");
                    assertThat(docs.get(1).getUrl()).isEqualTo("http://doc2.fr");
                    assertThat(docs.get(1).getDateMiseAJour()).isEqualTo("2024-02-01");
                });

        assertThat(rubrique.getContenus().get(1).getLangue()).isEqualTo("en");
        assertThat(rubrique.getContenus().get(1).getTexte()).isEqualTo("English rich text");
        assertThat(rubrique.getContenus().get(1).getDocuments())
                .hasSize(1)
                .satisfies(docs -> assertThat(docs.get(0).getUrl()).isEqualTo("http://doc1.en"));
    }

    @Test
    void convertRubrique_shouldConvertRichTextType_onlyFrenchContent() {
        // Given
        RapportQualiteDTO dto = createBasicDTO();
        RubriqueDTO rubriqueDTO = createRubriqueDTO("rubrique-rich", "RICH_TEXT");
        rubriqueDTO = rubriqueDTO.withLabelLg1("Texte français uniquement");
        rubriqueDTO = rubriqueDTO.withLabelLg2(null);
        rubriqueDTO = rubriqueDTO.withHasDocLg1(false);
        rubriqueDTO = rubriqueDTO.withHasDocLg2(false);
        dto = dto.withRubriqueDTOList(List.of(rubriqueDTO));

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        Rubrique rubrique = result.getRubriques().getFirst();
        assertThat(rubrique.getContenus()).hasSize(1);
        assertThat(rubrique.getContenus().getFirst().getLangue()).isEqualTo("fr");
    }

    @Test
    void convertRubrique_shouldHandleMixedRubriqueTypes() {
        // Given
        RapportQualiteDTO dto = createBasicDTO();

        RubriqueDTO dateRubrique = createRubriqueDTO("rubrique-date", "DATE");
        dateRubrique = dateRubrique.withValeurSimple("2024-01-15");

        RubriqueDTO textRubrique = createRubriqueDTO("rubrique-text", "TEXT");
        textRubrique = textRubrique.withLabelLg1("Texte");
        textRubrique = textRubrique.withLabelLg2("Text");

        RubriqueDTO geoRubrique = createRubriqueDTO("rubrique-geo", "GEOGRAPHY");
        geoRubrique = geoRubrique.withValeurSimple("FR");
        geoRubrique = geoRubrique.withGeoUri("http://example.com/geo/fr");
        geoRubrique = geoRubrique.withLabelObjLg1("France");

        dto = dto.withRubriqueDTOList(List.of(dateRubrique, textRubrique, geoRubrique));

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        assertThat(result.getRubriques()).hasSize(3);
        assertThat(result.getRubriques().get(0).getType()).isEqualTo("DATE");
        assertThat(result.getRubriques().get(1).getType()).isEqualTo("TEXT");
        assertThat(result.getRubriques().get(2).getType()).isEqualTo("GEOGRAPHY");
    }

    @Test
    void convertRubrique_shouldPreserveRubriqueOrder() {
        // Given
        RapportQualiteDTO dto = createBasicDTO();
        RubriqueDTO rub1 = createRubriqueDTO("rubrique-1", "TEXT");
        RubriqueDTO rub2 = createRubriqueDTO("rubrique-2", "TEXT");
        RubriqueDTO rub3 = createRubriqueDTO("rubrique-3", "TEXT");
        dto = dto.withRubriqueDTOList(List.of(rub1, rub2, rub3));

        // When
        RapportQualite result = service.convertDTOToRapportQualite(dto);

        // Then
        assertThat(result.getRubriques())
                .extracting(Rubrique::getId)
                .containsExactly("rubrique-1", "rubrique-2", "rubrique-3");
    }

    private RapportQualiteDTO createBasicDTO() {
        return new RapportQualiteDTO("rubrique-test","http://example.com/rubrique-test","Test rapport","Test report",null,null,null,null,null);
    }

    private RubriqueDTO createRubriqueDTO(String id, String type) {
        return new RubriqueDTO(id,"http://example.com/" + id, null, type, null,null,null,null,null,null,null,false,false,null,null,null,null);
    }


    // =========================================================
    //   convertSeriesDTOToSerieById
    // =========================================================

    @Test
    void should_map_identifiers_and_dates_when_convertSeriesDTO() {
        var result = service.convertSeriesDTOToSerieById(fullSeriesDTO());

        assertAll(
                () -> assertEquals("s1001", result.getSeriesId()),
                () -> assertEquals("http://id.insee.fr/operations/serie/s1001", result.getUri()),
                () -> assertEquals(LocalDate.of(2020, 1, 15), result.getDateCreation()),
                () -> assertEquals(LocalDate.of(2024, 6, 1), result.getDateMiseAJour()),
                () -> assertEquals("Publiée", result.getStatutValidation())
        );
    }

    @Test
    void should_map_null_dates_as_null_when_convertSeriesDTO() {
        var result = service.convertSeriesDTOToSerieById(minimalSeriesDTO());

        assertNull(result.getDateCreation());
        assertNull(result.getDateMiseAJour());
    }

    @Test
    void should_map_multilingual_labels_when_convertSeriesDTO() {
        var result = service.convertSeriesDTOToSerieById(fullSeriesDTO());

        assertAll(
                () -> assertEquals(2, result.getLabel().size()),
                () -> assertEquals("fr", result.getLabel().getFirst().getLangue()),
                () -> assertEquals("Enquête innovation", result.getLabel().getFirst().getContenu()),
                () -> assertEquals("en", result.getLabel().get(1).getLangue()),
                () -> assertEquals("Innovation survey", result.getLabel().get(1).getContenu()),

                () -> assertEquals("Sigle FR", result.getAltLabel().getFirst().getContenu()),
                () -> assertEquals("Sigle EN", result.getAltLabel().get(1).getContenu()),

                () -> assertEquals("Résumé FR", result.getResume().getFirst().getContenu()),
                () -> assertEquals("Abstract EN", result.getResume().get(1).getContenu()),

                () -> assertEquals("Note historique FR", result.getNoteHistorique().getFirst().getContenu()),
                () -> assertEquals("History note EN", result.getNoteHistorique().get(1).getContenu())
        );
    }

    @Test
    void should_map_type_when_type_uri_is_present() {
        var result = service.convertSeriesDTOToSerieById(fullSeriesDTO());

        assertAll(
                () -> assertNotNull(result.getType()),
                () -> assertEquals("E", result.getType().getId()),
                () -> assertEquals(URI.create("http://id.insee.fr/concepts/type/E"), result.getType().getUri()),
                () -> assertEquals("Enquête", result.getType().getLabel().get(0).getContenu()),
                () -> assertEquals("Survey", result.getType().getLabel().get(1).getContenu())
        );
    }

    @Test
    void should_not_set_type_when_type_is_null() {
        var result = service.convertSeriesDTOToSerieById(minimalSeriesDTO());
        assertNull(result.getType());
    }

    @Test
    void should_not_set_type_when_type_is_blank() {
        var dto = new SeriesDTO(
                "s1001", "http://id.insee.fr/operations/serie/s1001",
                "Label FR", "Label EN", null, null, null, null, null, null,
                "   ", null, null, null,
                null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null
        );
        assertNull(service.convertSeriesDTOToSerieById(dto).getType());
    }

    @Test
    void should_map_periodicity_when_present() {
        var result = service.convertSeriesDTOToSerieById(fullSeriesDTO());

        assertAll(
                () -> assertNotNull(result.getFrequenceCollecte()),
                () -> assertEquals("A", result.getFrequenceCollecte().getId()),
                () -> assertEquals(URI.create("http://id.insee.fr/concepts/periodicity/A"), result.getFrequenceCollecte().getUri()),
                () -> assertEquals("Annuelle", result.getFrequenceCollecte().getLabel().get(0).getContenu()),
                () -> assertEquals("Annual", result.getFrequenceCollecte().getLabel().get(1).getContenu())
        );
    }

    @Test
    void should_not_set_periodicity_when_null() {
        assertNull(service.convertSeriesDTOToSerieById(minimalSeriesDTO()).getFrequenceCollecte());
    }

    @Test
    void should_parse_famille_from_dollar_separated_string() {
        var result = service.convertSeriesDTOToSerieById(fullSeriesDTO());

        assertAll(
                () -> assertNotNull(result.getFamille()),
                () -> assertEquals("f1001", result.getFamille().getId()),
                () -> assertEquals(URI.create("http://id.insee.fr/operations/famille/f1001"), result.getFamille().getUri()),
                () -> assertEquals("Famille FR", result.getFamille().getLabel().get(0).getContenu()),
                () -> assertEquals("Family EN", result.getFamille().getLabel().get(1).getContenu())
        );
    }

    @Test
    void should_not_set_famille_when_families_is_null() {
        assertNull(service.convertSeriesDTOToSerieById(minimalSeriesDTO()).getFamille());
    }

    @Test
    void should_map_rapportQualite_when_simsId_present() {
        var result = service.convertSeriesDTOToSerieById(fullSeriesDTO());

        assertAll(
                () -> assertNotNull(result.getRapportQualite()),
                () -> assertEquals("1500", result.getRapportQualite().getId()),
                () -> assertEquals(URI.create("http://id.insee.fr/qualite/rapport/1500"), result.getRapportQualite().getUri())
        );
    }

    @Test
    void should_not_set_rapportQualite_when_simsId_is_null() {
        assertNull(service.convertSeriesDTOToSerieById(minimalSeriesDTO()).getRapportQualite());
    }

    @Test
    void should_parse_single_ref_in_previousSeries() {
        var result = service.convertSeriesDTOToSerieById(fullSeriesDTO());

        assertAll(
                () -> assertEquals(1, result.getSeriesPrecedentes().size()),
                () -> assertEquals("s1010", result.getSeriesPrecedentes().get(0).getId()),
                () -> assertEquals(URI.create("http://id.insee.fr/operations/serie/s1010"), result.getSeriesPrecedentes().get(0).getUri()),
                () -> assertEquals("Précédente FR", result.getSeriesPrecedentes().get(0).getLabel().get(0).getContenu()),
                () -> assertEquals("Previous EN", result.getSeriesPrecedentes().get(0).getLabel().get(1).getContenu())
        );
    }

    @Test
    void should_parse_multiple_refs_separated_by_pipe_in_seeAlsoSeries() {
        var result = service.convertSeriesDTOToSerieById(fullSeriesDTO());

        assertAll(
                () -> assertEquals(2, result.getSeriesLiees().size()),
                () -> assertEquals("s1197", result.getSeriesLiees().getFirst().getId()),
                () -> assertEquals("s1198", result.getSeriesLiees().get(1).getId())
        );
    }

    @Test
    void should_parse_multiple_refs_in_operations() {
        var result = service.convertSeriesDTOToSerieById(fullSeriesDTO());

        assertAll(
                () -> assertEquals(2, result.getOperations().size()),
                () -> assertEquals("op2024", result.getOperations().getFirst().getId()),
                () -> assertEquals("op2023", result.getOperations().get(1).getId())
        );
    }

    @Test
    void should_return_empty_list_when_refList_field_is_null() {
        var result = service.convertSeriesDTOToSerieById(minimalSeriesDTO());

        assertAll(
                () -> assertNotNull(result.getOperations()),
                () -> assertTrue(result.getOperations().isEmpty()),
                () -> assertTrue(result.getIndicateurs().isEmpty()),
                () -> assertTrue(result.getSeriesPrecedentes().isEmpty()),
                () -> assertTrue(result.getSeriesSuivantes().isEmpty()),
                () -> assertTrue(result.getSeriesLiees().isEmpty()),
                () -> assertTrue(result.getProprietaires().isEmpty()),
                () -> assertTrue(result.getOrganismesResponsables().isEmpty()),
                () -> assertTrue(result.getPartenaires().isEmpty()),
                () -> assertTrue(result.getServicesCollecteurs().isEmpty())
        );
    }

    // =========================================================
    //   convertSeriesDTOsToSeries
    // =========================================================

    @Test
    void should_return_empty_list_when_input_is_empty() {
        var result = service.convertSeriesDTOsToSeries(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void should_map_seriesId_and_uri_for_each_dto() {
        var dtos = List.of(
                minimalSeriesDTO(),
                new SeriesDTO(
                        "s1002", "http://id.insee.fr/operations/serie/s1002",
                        "Enquête 2 FR", "Survey 2 EN", null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null)
        );

        var result = service.convertSeriesDTOsToSeries(dtos);

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals("s1001", result.getFirst().getSeriesId()),
                () -> assertEquals("http://id.insee.fr/operations/serie/s1001", result.getFirst().getUri()),
                () -> assertEquals("s1002", result.get(1).getSeriesId()),
                () -> assertEquals("http://id.insee.fr/operations/serie/s1002", result.get(1).getUri())
        );
    }

    @Test
    void should_map_multilingual_labels_for_each_serie() {
        var dtos = List.of(minimalSeriesDTO());

        var result = service.convertSeriesDTOsToSeries(dtos);
        var label = result.getFirst().getLabel();

        assertAll(
                () -> assertEquals(2, label.size()),
                () -> assertEquals("fr", label.getFirst().getLangue()),
                () -> assertEquals("Label FR", label.getFirst().getContenu()),
                () -> assertEquals("en", label.get(1).getLangue()),
                () -> assertEquals("Label EN", label.get(1).getContenu())
        );
    }

    @Test
    void should_only_populate_seriesId_uri_and_label_not_detail_fields() {
        var result = service.convertSeriesDTOsToSeries(List.of(fullSeriesDTO()));
        var serie = result.getFirst();

        assertAll(
                () -> assertNull(serie.getDateCreation()),
                () -> assertNull(serie.getDateMiseAJour()),
                () -> assertNull(serie.getStatutValidation()),
                () -> assertNull(serie.getType()),
                () -> assertNull(serie.getFrequenceCollecte()),
                () -> assertNull(serie.getFamille()),
                () -> assertNull(serie.getAltLabel()),
                () -> assertNull(serie.getResume()),
                () -> assertNull(serie.getNoteHistorique())
        );
    }

    // =========================================================
    //   convertOperationDTOToOperationById
    // =========================================================

    @Test
    void should_map_identifiers_when_convertOperationDTO() {
        var result = service.convertOperationDTOToOperation(fullOperationDTO());

        assertAll(
                () -> assertEquals("s2193", result.getId()),
                () -> assertEquals("http://id.insee.fr/operations/operation/s2193", result.getUri()),
                () -> assertEquals("2024", result.getMillesime()),
                () -> assertEquals(LocalDate.of(2025, 1, 22), result.getDateCreation()),
                () -> assertEquals(LocalDate.of(2025, 4, 2), result.getDateMiseAJour()),
                () -> assertEquals("Publiée", result.getStatutValidation())
        );
    }

    @Test
    void should_map_multilingual_labels_when_convertOperationDTO() {
        var result = service.convertOperationDTOToOperation(fullOperationDTO());

        assertAll(
                () -> assertEquals(2, result.getLabel().size()),
                () -> assertEquals("fr", result.getLabel().getFirst().getLangue()),
                () -> assertEquals("Enquête innovation 2024", result.getLabel().getFirst().getContenu()),
                () -> assertEquals("en", result.getLabel().get(1).getLangue()),
                () -> assertEquals("Innovation survey 2024", result.getLabel().get(1).getContenu()),

                () -> assertEquals("CIS 2024", result.getAltLabel().getFirst().getContenu()),
                () -> assertEquals("CIS 2024", result.getAltLabel().get(1).getContenu())
        );
    }

    @Test
    void should_map_serie_when_seriesId_is_present() {
        var result = service.convertOperationDTOToOperation(fullOperationDTO());

        assertAll(
                () -> assertNotNull(result.getSerie()),
                () -> assertEquals("s1001", result.getSerie().getId()),
                () -> assertEquals("http://id.insee.fr/operations/serie/s1001", result.getSerie().getUri()),
                () -> assertEquals("Enquête innovation", result.getSerie().getLabel().getFirst().getContenu()),
                () -> assertEquals("Innovation survey", result.getSerie().getLabel().get(1).getContenu())
        );
    }

    @Test
    void should_not_set_serie_when_seriesId_is_null() {
        var dto = new OperationDTO(
                "s2193", "http://id.insee.fr/operations/operation/s2193",
                "Label FR", "Label EN", null, null,
                null,
                null, null, null, null,
                null, null, null, null, null
        );
        assertNull(service.convertOperationDTOToOperation(dto).getSerie());
    }

    @Test
    void should_not_set_serie_when_seriesId_is_blank() {
        var dto = new OperationDTO(
                "s2193", "http://id.insee.fr/operations/operation/s2193",
                "Label FR", "Label EN", null, null,
                null,
                "  ", null, null, null,
                null, null, null, null, null
        );
        assertNull(service.convertOperationDTOToOperation(dto).getSerie());
    }

    @Test
    void should_map_rapportQualite_when_simsId_present_for_operation() {
        var result = service.convertOperationDTOToOperation(fullOperationDTO());

        assertAll(
                () -> assertNotNull(result.getRapportQualite()),
                () -> assertEquals("2203", result.getRapportQualite().getId()),
                () -> assertEquals(URI.create("http://id.insee.fr/qualite/rapport/2203"), result.getRapportQualite().getUri())
        );
    }

    @Test
    void should_not_set_rapportQualite_when_simsId_is_null_for_operation() {
        var dto = new OperationDTO(
                "s2193", "http://id.insee.fr/operations/operation/s2193",
                "Label FR", "Label EN", null, null,
                null,
                "s1001", "http://id.insee.fr/operations/serie/s1001", null, null,
                null, null, null, null, null
        );
        assertNull(service.convertOperationDTOToOperation(dto).getRapportQualite());
    }

    @Test
    void should_map_null_dates_as_null_when_convertOperationDTO() {
        var dto = new OperationDTO(
                "s2193", "http://id.insee.fr/operations/operation/s2193",
                "Label FR", "Label EN", null, null,
                null, null, null, null, null,
                null, null, null, null, null
        );
        var result = service.convertOperationDTOToOperation(dto);

        assertNull(result.getDateCreation());
        assertNull(result.getDateMiseAJour());
    }

    // =========================================================
    //   convertIndicateurDTOToIndicateurById
    // =========================================================

    @Test
    void should_map_identifiers_and_dates_when_convertIndicateurDTO() {
        var result = service.convertIndicateurDTOToIndicateur(fullIndicateurDTO());

        assertAll(
                () -> assertEquals("p1001", result.getId()),
                () -> assertEquals("http://id.insee.fr/produits/indicateur/p1001", result.getUri()),
                () -> assertEquals("2019-05-10", result.getDateCreation()),
                () -> assertEquals("2023-03-15", result.getDateMiseAJour()),
                () -> assertEquals("Publiée", result.getStatuValidation())
        );
    }

    @Test
    void should_map_null_dates_as_null_when_convertIndicateurDTO() {
        var result = service.convertIndicateurDTOToIndicateur(minimalIndicateurDTO());

        assertNull(result.getDateCreation());
        assertNull(result.getDateMiseAJour());
    }

    @Test
    void should_map_multilingual_labels_when_convertIndicateurDTO() {
        var result = service.convertIndicateurDTOToIndicateur(fullIndicateurDTO());

        assertAll(
                () -> assertEquals(2, result.getLabel().size()),
                () -> assertEquals("fr", result.getLabel().getFirst().getLangue()),
                () -> assertEquals("Taux de chômage", result.getLabel().getFirst().getContenu()),
                () -> assertEquals("en", result.getLabel().get(1).getLangue()),
                () -> assertEquals("Unemployment rate", result.getLabel().get(1).getContenu()),

                () -> assertEquals("TxChom", result.getAltLabel().getFirst().getContenu()),
                () -> assertEquals("UnemployRate", result.getAltLabel().get(1).getContenu()),

                () -> assertEquals("Résumé indicateur FR", result.getResume().getFirst().getContenu()),
                () -> assertEquals("Indicator abstract EN", result.getResume().get(1).getContenu()),

                () -> assertEquals("Note historique indicateur FR", result.getNoteHistorique().getFirst().getContenu()),
                () -> assertEquals("Indicator history note EN", result.getNoteHistorique().get(1).getContenu())
        );
    }

    @Test
    void should_map_periodicity_indicateur_when_present() {
        var result = service.convertIndicateurDTOToIndicateur(fullIndicateurDTO());

        assertAll(
                () -> assertNotNull(result.getFrequenceCollecte()),
                () -> assertEquals("T", result.getFrequenceCollecte().getId()),
                () -> assertEquals("http://id.insee.fr/concepts/periodicity/T", result.getFrequenceCollecte().getUri().toString()),
                () -> assertEquals("Trimestrielle", result.getFrequenceCollecte().getLabel().getFirst().getContenu()),
                () -> assertEquals("Quarterly", result.getFrequenceCollecte().getLabel().get(1).getContenu())
        );
    }

    @Test
    void should_not_set_periodicity_indicateur_when_null() {
        assertNull(service.convertIndicateurDTOToIndicateur(minimalIndicateurDTO()).getFrequenceCollecte());
    }

    @Test
    void should_not_set_periodicity_when_blank() {
        var dto = new IndicateurDTO(
                "p1001", "http://id.insee.fr/produits/indicateur/p1001",
                "Label FR", "Label EN", null, null, null, null, null, null,
                "   ", null, null, null,
                null, null, null, null, null,
                null, null, null, null, null, null
        );
        assertNull(service.convertIndicateurDTOToIndicateur(dto).getFrequenceCollecte());
    }

    @Test
    void should_map_rapportQualite_indicateur_when_simsId_present() {
        var result = service.convertIndicateurDTOToIndicateur(fullIndicateurDTO());

        assertAll(
                () -> assertNotNull(result.getRapportQualite()),
                () -> assertEquals("3500", result.getRapportQualite().getId()),
                () -> assertEquals(URI.create("http://id.insee.fr/qualite/rapport/3500"), result.getRapportQualite().getUri())
        );
    }

    @Test
    void should_not_set_rapportQualite_indicateur_when_simsId_is_null() {
        assertNull(service.convertIndicateurDTOToIndicateur(minimalIndicateurDTO()).getRapportQualite());
    }

    @Test
    void should_not_set_rapportQualite_when_simsId_is_blank() {
        var dto = new IndicateurDTO(
                "p1001", "http://id.insee.fr/produits/indicateur/p1001",
                "Label FR", "Label EN", null, null, null, null, null, null,
                null, null, null, null,
                null, null, null,
                "http://id.insee.fr/qualite/rapport/3500", "   ",
                null, null, null, null, null, null
        );
        assertNull(service.convertIndicateurDTOToIndicateur(dto).getRapportQualite());
    }

    @Test
    void should_parse_seriesContributrices_from_dollar_separated_string() {
        var result = service.convertIndicateurDTOToIndicateur(fullIndicateurDTO());

        assertAll(
                () -> assertNotNull(result.getSeriesContributrices()),
                () -> assertEquals(1, result.getSeriesContributrices().size()),
                () -> assertEquals("s1001", result.getSeriesContributrices().getFirst().getId()),
                () -> assertEquals("http://id.insee.fr/operations/serie/s1001", result.getSeriesContributrices().getFirst().getUri().toString()),
                () -> assertEquals("Série contributrice FR", result.getSeriesContributrices().getFirst().getLabel().getFirst().getContenu()),
                () -> assertEquals("Contributing series EN", result.getSeriesContributrices().getFirst().getLabel().get(1).getContenu())
        );
    }

    @Test
    void should_parse_seriesLiees_from_dollar_separated_string() {
        var result = service.convertIndicateurDTOToIndicateur(fullIndicateurDTO());

        assertAll(
                () -> assertNotNull(result.getSeriesLiees()),
                () -> assertEquals(1, result.getSeriesLiees().size()),
                () -> assertEquals("s1197", result.getSeriesLiees().getFirst().getId()),
                () -> assertEquals("Série liée FR", result.getSeriesLiees().getFirst().getLabel().getFirst().getContenu())
        );
    }

    @Test
    void should_parse_indicateursLies_from_dollar_separated_string() {
        var result = service.convertIndicateurDTOToIndicateur(fullIndicateurDTO());

        assertAll(
                () -> assertNotNull(result.getIndicateursLies()),
                () -> assertEquals(1, result.getIndicateursLies().size()),
                () -> assertEquals("p1002", result.getIndicateursLies().getFirst().getId()),
                () -> assertEquals("http://id.insee.fr/produits/indicateur/p1002", result.getIndicateursLies().getFirst().getUri().toString()),
                () -> assertEquals("Indicateur lié FR", result.getIndicateursLies().getFirst().getLabel().getFirst().getContenu()),
                () -> assertEquals("Related indicator EN", result.getIndicateursLies().getFirst().getLabel().get(1).getContenu())
        );
    }

    @Test
    void should_parse_multiple_items_separated_by_pipe() {
        var dto = new IndicateurDTO(
                "p1001", "http://id.insee.fr/produits/indicateur/p1001",
                "Label FR", "Label EN", null, null, null, null, null, null,
                null, null, null, null,
                "s1001$http://id.insee.fr/operations/serie/s1001$Série 1 FR$Series 1 EN|s1002$http://id.insee.fr/operations/serie/s1002$Série 2 FR$Series 2 EN",
                null, null, null, null,
                null, null, null, null, null, null
        );
        var result = service.convertIndicateurDTOToIndicateur(dto);

        assertAll(
                () -> assertEquals(2, result.getSeriesContributrices().size()),
                () -> assertEquals("s1001", result.getSeriesContributrices().getFirst().getId()),
                () -> assertEquals("s1002", result.getSeriesContributrices().get(1).getId())
        );
    }

    @Test
    void should_map_proprietaires_organismesResponsables_partenaires() {
        var result = service.convertIndicateurDTOToIndicateur(fullIndicateurDTO());

        assertAll(
                () -> assertEquals(1, result.getProprietaires().size()),
                () -> assertEquals("insee", result.getProprietaires().getFirst().getId()),
                () -> assertEquals("Institut national", result.getProprietaires().getFirst().getLabel().getFirst().getContenu()),

                () -> assertEquals(1, result.getOrganismesResponsables().size()),
                () -> assertEquals("drees", result.getOrganismesResponsables().getFirst().getId()),

                () -> assertEquals(1, result.getPartenaires().size()),
                () -> assertEquals("dares", result.getPartenaires().getFirst().getId())
        );
    }

    @Test
    void should_return_null_for_list_fields_when_raw_is_null() {
        var result = service.convertIndicateurDTOToIndicateur(minimalIndicateurDTO());

        assertAll(
                () -> assertNull(result.getSeriesContributrices()),
                () -> assertNull(result.getSeriesLiees()),
                () -> assertNull(result.getIndicateursLies()),
                () -> assertNull(result.getProprietaires()),
                () -> assertNull(result.getOrganismesResponsables()),
                () -> assertNull(result.getPartenaires())
        );
    }

    @Test
    void should_skip_item_when_id_part_is_blank() {
        var dto = new IndicateurDTO(
                "p1001", "http://id.insee.fr/produits/indicateur/p1001",
                "Label FR", "Label EN", null, null, null, null, null, null,
                null, null, null, null,
                "$http://id.insee.fr/operations/serie/s1001$Label FR$Label EN|s1002$http://id.insee.fr/operations/serie/s1002$Série 2 FR$Series 2 EN",
                null, null, null, null,
                null, null, null, null, null, null
        );
        var result = service.convertIndicateurDTOToIndicateur(dto);

        assertAll(
                () -> assertEquals(1, result.getSeriesContributrices().size()),
                () -> assertEquals("s1002", result.getSeriesContributrices().getFirst().getId())
        );
    }

    // =========================================================
    //   Fixtures
    // =========================================================

    private SeriesDTO fullSeriesDTO() {
        return new SeriesDTO(
                "s1001",
                "http://id.insee.fr/operations/serie/s1001",
                "Enquête innovation", "Innovation survey",
                "Sigle FR", "Sigle EN",
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
                null,
                "Publiée"
        );
    }

    /** DTO avec tous les champs optionnels à null */
    private SeriesDTO minimalSeriesDTO() {
        return new SeriesDTO(
                "s1001", "http://id.insee.fr/operations/serie/s1001",
                "Label FR", "Label EN", null, null, null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null
        );
    }

    private IndicateurDTO fullIndicateurDTO() {
        return new IndicateurDTO(
                "p1001",
                "http://id.insee.fr/produits/indicateur/p1001",
                "Taux de chômage", "Unemployment rate",
                "TxChom", "UnemployRate",
                "Résumé indicateur FR", "Indicator abstract EN",
                "Note historique indicateur FR", "Indicator history note EN",
                "http://id.insee.fr/concepts/periodicity/T", "T", "Trimestrielle", "Quarterly",
                "s1001$http://id.insee.fr/operations/serie/s1001$Série contributrice FR$Contributing series EN",
                "s1197$http://id.insee.fr/operations/serie/s1197$Série liée FR$Related series EN",
                "p1002$http://id.insee.fr/produits/indicateur/p1002$Indicateur lié FR$Related indicator EN",
                "http://id.insee.fr/qualite/rapport/3500", "3500",
                LocalDate.of(2019, 5, 10),
                LocalDate.of(2023, 3, 15),
                "insee$http://id.insee.fr/organisations/insee$Institut national$National institute",
                "drees$http://id.insee.fr/organisations/drees$DREES$DREES",
                "dares$http://id.insee.fr/organisations/dares$DARES$DARES",
                "Publiée"
        );
    }

    private IndicateurDTO minimalIndicateurDTO() {
        return new IndicateurDTO(
                "p1001", "http://id.insee.fr/produits/indicateur/p1001",
                "Taux FR", "Rate EN",
                null, null, null, null, null, null,
                null, null, null, null,
                null, null, null,
                null, null,
                null, null,
                null, null, null,
                null
        );
    }

    private OperationDTO fullOperationDTO() {
        return new OperationDTO(
                "s2193",
                "http://id.insee.fr/operations/operation/s2193",
                "Enquête innovation 2024", "Innovation survey 2024",
                "CIS 2024", "CIS 2024",
                "2024",
                "s1001", "http://id.insee.fr/operations/serie/s1001",
                "Enquête innovation", "Innovation survey",
                "2203", "http://id.insee.fr/qualite/rapport/2203",
                LocalDate.of(2025, 1, 22),
                LocalDate.of(2025, 4, 2),
                "Publiée"
        );
    }


}
