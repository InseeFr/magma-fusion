package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.model.Nomenclature;
import fr.insee.rmes.magmafusion.utils.NiveauDTO;
import fr.insee.rmes.magmafusion.utils.NomenclatureDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NomenclatureServiceImplTest {

    @Mock
    private NomenclatureDTO nomenclatureDTO;

    @Mock
    private NiveauDTO niveauDTO;

    @InjectMocks
    private NomenclatureServiceImpl service; // Remplacez par la classe qui contient convertNomenclatureDto

    private static final String LG1 = "fr";
    private static final String LG2 = "en";

    @BeforeEach
    void setUp() {
        // Initialisation commune
        when(nomenclatureDTO.id()).thenReturn("nomenclature-123");
        when(nomenclatureDTO.classification()).thenReturn("http://example.com/classification");
        when(nomenclatureDTO.prefLabelLg1()).thenReturn("Libellé FR");
        when(nomenclatureDTO.prefLabelLg2()).thenReturn("Label EN");
        when(nomenclatureDTO.issued()).thenReturn(String.valueOf(LocalDate.of(2023, 1, 1)));
        when(nomenclatureDTO.lastRefreshedOn()).thenReturn(String.valueOf(LocalDate.of(2023, 6, 15)));
        ReflectionTestUtils.setField(service, "lg1", "fr");
        ReflectionTestUtils.setField(service, "lg2", "en");
    }

    @Test
    void convertNomenclatureDto_WithAllFields_ShouldReturnCompleteNomenclature() {
        // Given
        when(nomenclatureDTO.altLabelsLg1()).thenReturn("alt1,alt2,alt3");
        when(nomenclatureDTO.altLabelsLg2()).thenReturn("altA,altB");
        when(nomenclatureDTO.descriptionLg1()).thenReturn("Description FR");
        when(nomenclatureDTO.descriptionLg2()).thenReturn("Description EN");

        List<NiveauDTO> niveauxDTO = List.of(niveauDTO);
        when(niveauDTO.idLevel()).thenReturn("level-1");
        when(niveauDTO.level()).thenReturn("http://example.com/level1");
        when(niveauDTO.prefLabelLevelLg1()).thenReturn("Niveau FR");
        when(niveauDTO.prefLabelLevelLg2()).thenReturn("Level EN");

        // When
        Nomenclature result = service.convertNomenclatureDto(nomenclatureDTO, niveauxDTO);

        // Then
        assertNotNull(result);
        assertEquals("nomenclature-123", result.getNomenclature());
        assertEquals("http://example.com/classification", result.getUri());
        assertEquals(2, result.getIntitule().size());
        assertEquals("Libellé FR", result.getIntitule().get(0).getContenu());
        assertEquals(LG1, result.getIntitule().get(0).getLangue());
        assertEquals("Label EN", result.getIntitule().get(1).getContenu());
        assertEquals(LG2, result.getIntitule().get(1).getLangue());

        // Vérification des altLabels
        assertNotNull(result.getIntituleAlternatif());
        assertEquals(5, result.getIntituleAlternatif().size()); // 3 de lg1 + 2 de lg2

        // Vérification de la description
        assertNotNull(result.getDescription());
        assertEquals(2, result.getDescription().size());

        // Vérification des dates
        assertEquals(String.valueOf(LocalDate.of(2023, 1, 1)), result.getDatePublication());
        assertEquals(String.valueOf(LocalDate.of(2023, 6, 15)), result.getDateMiseAJour());

        // Vérification des niveaux
        assertNotNull(result.getNiveaux());
        assertEquals(1, result.getNiveaux().size());
        assertEquals("level-1", result.getNiveaux().get(0).getNiveau());
        assertEquals(URI.create("http://example.com/level1"), result.getNiveaux().get(0).getUri());
    }

    @Test
    void convertNomenclatureDto_WithOnlyLg1AltLabels_ShouldSetAltLabels() {
        // Given
        when(nomenclatureDTO.altLabelsLg1()).thenReturn("alt1,alt2");
        when(nomenclatureDTO.altLabelsLg2()).thenReturn(null);

        // When
        Nomenclature result = service.convertNomenclatureDto(nomenclatureDTO, new ArrayList<>());

        // Then
        assertNotNull(result.getIntituleAlternatif());
        assertEquals(2, result.getIntituleAlternatif().size());
        assertEquals("alt1", result.getIntituleAlternatif().get(0).getContenu());
        assertEquals(LG1, result.getIntituleAlternatif().get(0).getLangue());
    }

    @Test
    void convertNomenclatureDto_WithOnlyLg2AltLabels_ShouldSetAltLabels() {
        // Given
        when(nomenclatureDTO.altLabelsLg1()).thenReturn(null);
        when(nomenclatureDTO.altLabelsLg2()).thenReturn("altA,altB");

        // When
        Nomenclature result = service.convertNomenclatureDto(nomenclatureDTO, new ArrayList<>());

        // Then
        assertNotNull(result.getIntituleAlternatif());
        assertEquals(2, result.getIntituleAlternatif().size());
        assertEquals("altA", result.getIntituleAlternatif().get(0).getContenu());
        assertEquals(LG2, result.getIntituleAlternatif().get(0).getLangue());
    }

    @Test
    void convertNomenclatureDto_WithNoAltLabels_ShouldNotSetAltLabels() {
        // Given
        when(nomenclatureDTO.altLabelsLg1()).thenReturn(null);
        when(nomenclatureDTO.altLabelsLg2()).thenReturn(null);

        // When
        Nomenclature result = service.convertNomenclatureDto(nomenclatureDTO, new ArrayList<>());

        // Then
        assertNotNull(result.getIntituleAlternatif());
        assertTrue(result.getIntituleAlternatif().isEmpty());
    }

    @Test
    void convertNomenclatureDto_WithBothDescriptions_ShouldSetDescription() {
        // Given
        when(nomenclatureDTO.descriptionLg1()).thenReturn("Description FR");
        when(nomenclatureDTO.descriptionLg2()).thenReturn("Description EN");

        // When
        Nomenclature result = service.convertNomenclatureDto(nomenclatureDTO, new ArrayList<>());

        // Then
        assertNotNull(result.getDescription());
        assertEquals(2, result.getDescription().size());
    }

    @Test
    void convertNomenclatureDto_WithOnlyLg1Description_ShouldSetDescription() {
        // Given
        when(nomenclatureDTO.descriptionLg1()).thenReturn("Description FR");
        when(nomenclatureDTO.descriptionLg2()).thenReturn(null);

        // When
        Nomenclature result = service.convertNomenclatureDto(nomenclatureDTO, new ArrayList<>());

        // Then
        assertNotNull(result.getDescription());
        assertEquals(1, result.getDescription().size());
        assertEquals("Description FR", result.getDescription().get(0).getContenu());
        assertEquals(LG1, result.getDescription().get(0).getLangue());
    }

    @Test
    void convertNomenclatureDto_WithNoDescriptions_ShouldNotSetDescription() {
        // Given
        when(nomenclatureDTO.descriptionLg1()).thenReturn(null);
        when(nomenclatureDTO.descriptionLg2()).thenReturn(null);

        // When
        Nomenclature result = service.convertNomenclatureDto(nomenclatureDTO, new ArrayList<>());

        // Then
        assertNotNull(result.getDescription());
        assertTrue(result.getDescription().isEmpty());
    }

    @Test
    void convertNomenclatureDto_WithEmptyNiveaux_ShouldNotAddNiveaux() {
        // When
        Nomenclature result = service.convertNomenclatureDto(nomenclatureDTO, new ArrayList<>());

        // Then
        assertNotNull(result.getNiveaux());
        assertTrue(result.getNiveaux().isEmpty());
    }

    @Test
    void convertNomenclatureDto_WithNullNiveaux_ShouldNotAddNiveaux() {
        // When
        Nomenclature result = service.convertNomenclatureDto(nomenclatureDTO, null);

        // Then
        assertNotNull(result.getNiveaux());
        assertTrue(result.getNiveaux().isEmpty());
    }

    @Test
    void convertNomenclatureDto_WithNiveauOnlyLg1Label_ShouldSetNiveauIntitule() {
        // Given
        List<NiveauDTO> niveauxDTO = List.of(niveauDTO);
        when(niveauDTO.idLevel()).thenReturn("level-1");
        when(niveauDTO.level()).thenReturn("http://example.com/level1");
        when(niveauDTO.prefLabelLevelLg1()).thenReturn("Niveau FR");
        when(niveauDTO.prefLabelLevelLg2()).thenReturn(null);

        // When
        Nomenclature result = service.convertNomenclatureDto(nomenclatureDTO, niveauxDTO);

        // Then
        assertNotNull(result.getNiveaux());
        assertEquals(1, result.getNiveaux().size());
        assertNotNull(result.getNiveaux().get(0).getIntitule());
        assertEquals(1, result.getNiveaux().get(0).getIntitule().size());
        assertEquals("Niveau FR", result.getNiveaux().get(0).getIntitule().get(0).getContenu());
        assertEquals(LG1, result.getNiveaux().get(0).getIntitule().get(0).getLangue());
    }

    @Test
    void addNiveaux_WithNullParameters_ShouldNotThrowException() {
        // Given & when
        assertDoesNotThrow(() -> service.addNiveaux(null, null));
    }

    @Test
    void addNiveaux_WithNullNiveauxDTO_ShouldNotAddNiveaux() {
        // Given
        Nomenclature nomenclature = new Nomenclature();

        // When
        service.addNiveaux(null, nomenclature);

        // Then
        assertNotNull(nomenclature.getNiveaux());
        assertTrue(nomenclature.getNiveaux().isEmpty());
    }

    @Test
    void addNiveaux_WithNullNomenclature_ShouldNotAddNiveaux() {
        // Given
        List<NiveauDTO> niveauxDTO = List.of(niveauDTO);

        // When & Then
        assertDoesNotThrow(() -> service.addNiveaux(niveauxDTO, null));
    }
}
