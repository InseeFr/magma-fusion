package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.*;
import fr.insee.rmes.magmafusion.queries.parameters.OperationsDocumentsRequestParametizer;
import fr.insee.rmes.magmafusion.utils.*;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static fr.insee.rmes.magmafusion.utils.LabelsUtils.*;

@Service
public class OperationsServiceImpl implements OperationsService {

    private final String lg1;
    private final String lg2;
    private final RequestProcessor requestProcessor;

    public OperationsServiceImpl(
            @Value("${fr.insee.rmes.magmafusion.lg1}") String lg1,
            @Value("${fr.insee.rmes.magmafusion.lg2}") String lg2,
            RequestProcessor requestProcessor
    ) {
        this.lg1 = lg1;
        this.lg2 = lg2;
        this.requestProcessor = requestProcessor;
    }



    @Override
    public Serie convertSeriesDTOToSerieById(SeriesDTO dto) {
        Serie serieById = new Serie();

        serieById.setSeriesId(dto.seriesId());
        serieById.setUri(dto.series());
        serieById.setDateCreation(dto.created() != null ? dto.created() : null);
        serieById.setDateMiseAJour(dto.modified() != null ? dto.modified() : null);
        serieById.setStatutValidation(dto.validationState());

        serieById.setLabel(buildLocalisedLabels(
                dto.seriesLabelLg1(),
                dto.seriesLabelLg2(),
                lg1,lg2));

        serieById.setAltLabel(buildLocalisedLabels(
                dto.seriesAltLabelLg1(),
                dto.seriesAltLabelLg2(),
                lg1,lg2));

        serieById.setResume(buildLocalisedLabels(
                dto.seriesAbstractLg1(),
                dto.seriesAbstractLg2(),
                lg1,lg2));

        serieById.setNoteHistorique(buildLocalisedLabels(
                dto.seriesHistoryNoteLg1(),
                dto.seriesHistoryNoteLg2(),
                lg1, lg2));

        if (StringUtils.hasText(dto.type())) {
            IdUriLabel type = new IdUriLabel();
            type.setId(dto.typeID());
            type.setUri(URI.create(dto.type()));
            type.setLabel(buildLocalisedLabels(
                    dto.typeLabelLg1(),
                    dto.typeLabelLg2(),
                    lg1,lg2));
            serieById.setType(type);
        }

        if (StringUtils.hasText(dto.periodicity())) {
            IdUriLabel frequence = new IdUriLabel();
            frequence.setId(dto.periodicityId());
            frequence.setUri(URI.create(dto.periodicity()));
            frequence.setLabel(buildLocalisedLabels(
                    dto.periodicityLabelLg1(),
                    dto.periodicityLabelLg2(),
                    lg1,lg2));
            serieById.setFrequenceCollecte(frequence);
        }

        if (StringUtils.hasText(dto.families())) {
            serieById.setFamille(parseFamille(dto.families()));
        }

        if (StringUtils.hasText(dto.sims())) {
            IdUri rapportQualite = new IdUri();
            rapportQualite.setId(dto.simsId());
            rapportQualite.setUri(URI.create(dto.sims()));
            serieById.setRapportQualite(rapportQualite);
        }

        serieById.setSeriesPrecedentes(parseRefList(dto.previousSeries()));
        serieById.setSeriesSuivantes(parseRefList(dto.nextSeries()));
        serieById.setSeriesLiees(parseRefList(dto.seeAlsoSeries()));
        serieById.setOperations(parseRefList(dto.operations()));
        serieById.setIndicateurs(parseRefList(dto.indicators()));
        serieById.setProprietaires(parseRefList(dto.creators()));
        serieById.setOrganismesResponsables(parseRefList(dto.publishers()));
        serieById.setPartenaires(parseRefList(dto.contributors()));
        serieById.setServicesCollecteurs(parseRefList(dto.dataCollectors()));

        return serieById;
    }

    @Override
    public Operation convertOperationDTOToOperation(OperationDTO dto) {
        Operation operation = new Operation();

        operation.setId(dto.operationId());
        operation.setUri(dto.operation());
        operation.setMillesime(dto.temporal());
        operation.setDateCreation(dto.created() != null ? dto.created() : null);
        operation.setDateMiseAJour(dto.modified() != null ? dto.modified() : null);
        operation.setStatutValidation(dto.validationState());

        operation.setLabel(buildLocalisedLabels(
                dto.operationLabelLg1(),
                dto.operationLabelLg2(),
                lg1,lg2));

        operation.setAltLabel(buildLocalisedLabels(
                dto.operationAltLabelLg1(),
                dto.operationAltLabelLg2(),
                lg1,lg2));

        if (StringUtils.hasText(dto.series())) {
            SerieRef serie = new SerieRef();
            serie.setId(dto.seriesId());
            serie.setUri(dto.series());
            serie.setLabel(buildLocalisedLabels(
                    dto.seriesLabelLg1(),
                    dto.seriesLabelLg2(),
                    lg1,lg2));
            operation.setSerie(serie);
        }

        if (StringUtils.hasText(dto.simsId())) {
            IdUri rapportQualite = new IdUri();
            rapportQualite.setId(dto.simsId());
            rapportQualite.setUri(URI.create(dto.sims()));
            operation.setRapportQualite(rapportQualite);
        }

        return operation;
    }

    @Override
    public List<Serie> convertSeriesDTOsToSeries(List<SeriesDTO> dtos) {
        return dtos.stream().map(this::convertSeriesDTOToSeriesItem).toList();
    }

    private Serie convertSeriesDTOToSeriesItem(SeriesDTO dto) {
        Serie serieById = new Serie();
        serieById.setOperations(null);
        serieById.setAltLabel(null);
        serieById.setDateCreation(null);
        serieById.setResume(null);
        serieById.setNoteHistorique(null);
        serieById.setSeriesPrecedentes(null);
        serieById.setSeriesSuivantes(null);
        serieById.setSeriesLiees(null);
        serieById.setIndicateurs(null);
        serieById.setProprietaires(null);
        serieById.setOrganismesResponsables(null);
        serieById.setPartenaires(null);
        serieById.setServicesCollecteurs(null);
        serieById.setSeriesId(dto.seriesId());
        serieById.setUri(dto.series());
        serieById.setLabel(buildLocalisedLabels(
                dto.seriesLabelLg1(),
                dto.seriesLabelLg2(),
                lg1,lg2));
        return serieById;
    }

    private IdUriLabel parseFamille(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String[] parts = raw.split("\\$", -1);
        IdUriLabel famille = new IdUriLabel();
        famille.setId(parts.length > 0 ? parts[0] : null);
        String uriStr = parts.length > 1 ? parts[1] : null;
        famille.setUri(StringUtils.hasText(uriStr) ? URI.create(uriStr) : null);
        famille.setLabel(buildLocalisedLabels(
                parts.length > 2 ? parts[2] : null,
                parts.length > 3 ? parts[3] : null,
                lg1,lg2));
        return famille;
    }

    private IdUriLabel parseSingleRef(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String[] parts = raw.split("\\$", -1);
        IdUriLabel ref = new IdUriLabel();
        ref.setId(parts.length > 0 ? parts[0] : null);
        String uriStr = parts.length > 1 ? parts[1] : null;
        ref.setUri(StringUtils.hasText(uriStr) ? URI.create(uriStr) : null);
        ref.setLabel(buildLocalisedLabels(
                parts.length > 2 ? parts[2] : null,
                parts.length > 3 ? parts[3] : null,
                this.lg1, this.lg2));
        return ref;
    }

    private List<IdUriLabel> parseRefList(String raw) {
        List<IdUriLabel> list = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return list;
        }
        for (String item : raw.split("\\|")) {
            IdUriLabel ref = parseSingleRef(item);
            if (ref != null) {
                list.add(ref);
            }
        }
        return list;
    }

    @Override
    public Indicateur convertIndicateurDTOToIndicateur(IndicateurDTO dto) {
        Indicateur indicateur = new Indicateur();

        indicateur.setAltLabel(null);
        indicateur.setResume(null);
        indicateur.setNoteHistorique(null);
        indicateur.setFrequenceCollecte(null);
        indicateur.setRapportQualite(null);
        indicateur.setSeriesContributrices(null);
        indicateur.setSeriesLiees(null);
        indicateur.setIndicateursLies(null);
        indicateur.setDateCreation(null);
        indicateur.setDateMiseAJour(null);
        indicateur.setProprietaires(null);
        indicateur.setOrganismesResponsables(null);
        indicateur.setPartenaires(null);

        indicateur.setId(dto.indicatorId());
        indicateur.setUri(dto.indicator());
        indicateur.setDateCreation(dto.created() != null ? dto.created().toString() : null);
        indicateur.setDateMiseAJour(dto.modified() != null ? dto.modified().toString() : null);
        indicateur.setStatuValidation(dto.validationState());

        indicateur.setLabel(buildLocalisedLabels(
                dto.indicatorLabelLg1(),
                dto.indicatorLabelLg2(),
                lg1,lg2));

        indicateur.setAltLabel(buildLocalisedLabels(
                dto.indicatorAltLabelLg1(),
                dto.indicatorAltLabelLg2(),
                lg1,lg2));

        indicateur.setResume(buildLocalisedLabels(
                dto.indicatorAbstractLg1(),
                dto.indicatorAbstractLg2(),
                lg1,lg2));

        indicateur.setNoteHistorique(buildLocalisedLabels(
                dto.indicatorHistoryNoteLg1(),
                dto.indicatorHistoryNoteLg2(),
                lg1,lg2));

        if (dto.periodicity() != null && !dto.periodicity().isBlank()) {
            IdUriLabel frequence = new IdUriLabel(dto.periodicityId());
            frequence.setUri(toUri(dto.periodicity()));
            frequence.setLabel(buildLocalisedLabels(
                    dto.periodicityLabelLg1(),
                    dto.periodicityLabelLg2(),
                    lg1,lg2));
            indicateur.setFrequenceCollecte(frequence);
        }

        if (dto.simsId() != null && !dto.simsId().isBlank()) {
            IdUri rapportQualite = new IdUri();
            rapportQualite.setId(dto.simsId());
            rapportQualite.setUri(URI.create(dto.sims()));
            indicateur.setRapportQualite(rapportQualite);
        }

        indicateur.setSeriesContributrices(parseIdUriLabelList(dto.wasGeneratedBySeries()));
        indicateur.setSeriesLiees(parseIdUriLabelList(dto.seeAlsoSeries()));
        indicateur.setIndicateursLies(parseIdUriLabelList(dto.seeAlsoIndicators()));
        indicateur.setProprietaires(parseIdUriLabelList(dto.creators()));
        indicateur.setOrganismesResponsables(parseIdUriLabelList(dto.publishers()));
        indicateur.setPartenaires(parseIdUriLabelList(dto.contributors()));

        return indicateur;
    }

    private List<IdUriLabel> parseIdUriLabelList(String raw) {
        List<IdUriLabel> list = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return null;
        }
        for (String item : raw.split("\\|")) {
            String[] parts = item.split("\\$", -1);
            String id = parts.length > 0 ? parts[0] : null;
            if (id == null || id.isBlank()) {
                continue;
            }
            IdUriLabel ref = new IdUriLabel(id);
            if (parts.length > 1 && !parts[1].isBlank()) {
                ref.setUri(toUri(parts[1]));
            }
            ref.setLabel(buildLocalisedLabels(
                    parts.length > 2 ? parts[2] : null,
                    parts.length > 3 ? parts[3] : null,
                    lg1,lg2));
            list.add(ref);
        }
        return list;
    }

    private URI toUri(String uriString) {
        if (uriString == null || uriString.isBlank()) {
            return null;
        }
        try {
            return URI.create(uriString);
        } catch (IllegalArgumentException _) {
            return null;
        }
    }


    @Override
    public RapportQualite convertDTOToRapportQualite(RapportQualiteDTO rapportQualiteDTO) {
        RapportQualite rapportQualite = new RapportQualite();
        rapportQualite.setId(rapportQualiteDTO.id());
        rapportQualite.setUri(URI.create(rapportQualiteDTO.uri()));
        List<LocalisedContenu> label = buildLocalisedLabels(
                rapportQualiteDTO.labelLg1(),
                rapportQualiteDTO.labelLg2(),
                lg1,lg2);
        rapportQualite.setLabel(label);

        rapportQualite.setRubriques(null);

        if (rapportQualiteDTO.rubriqueDTOList() != null) {
            for (RubriqueDTO rubDTO : rapportQualiteDTO.rubriqueDTOList()) {
                Rubrique rubrique = convertRubrique(rubDTO, rapportQualite);
                if (rubrique != null) {
                    rapportQualite.addRubriquesItem(rubrique);
                }
            }
        }

        return rapportQualite;
    }

    private Rubrique convertRubrique(RubriqueDTO rubriqueDTO, RapportQualite rapportQualite) {
        Rubrique rubrique = createRubrique(rubriqueDTO);

        List<LocalisedContenu> titre = buildLocalisedLabels(
                rubriqueDTO.titreLg1(),
                rubriqueDTO.titreLg2(),
                lg1,lg2);
        rubrique.setTitre(titre);


        switch (rubriqueDTO.type()) {
            case "DATE":
                rubrique.setDate(rubriqueDTO.valeurSimple());
                break;
            case "CODE_LIST":
                rubrique = addCodeList(rubriqueDTO, rubrique, rapportQualite);
                break;
            case "RICH_TEXT":
                addRichText(rubriqueDTO, rubrique, rapportQualite);
                break;
            case "TEXT":
                List<LocalisedContenu> label = buildLocalisedLabels(
                        rubriqueDTO.labelLg1(),
                        rubriqueDTO.labelLg2(),
                        lg1,lg2);
                rubrique.setLabel(label);
                break;
            case "GEOGRAPHY":
                rubrique.setTerritoire(createUriLabel(rubriqueDTO, rubriqueDTO.geoUri()));
                break;
            case "ORGANIZATION":
                rubrique.setOrganisme(createUriLabel(rubriqueDTO, rubriqueDTO.organisationUri()));
                break;
            default:
                break;
        }

        return rubrique;
    }

    private static Rubrique createRubrique(RubriqueDTO rubriqueDTO) {
        Rubrique rubrique = new Rubrique();
        rubrique.setId(rubriqueDTO.id());
        rubrique.setUri(rubriqueDTO.uri());
        rubrique.setIdParent(rubriqueDTO.idParent());
        rubrique.setType(rubriqueDTO.type());
        rubrique.setLabel(null);
        rubrique.setContenus(null);
        rubrique.setCodes(null);
        return rubrique;
    }

    private IdUriLabel createUriLabel(RubriqueDTO rubriqueDTO, String uri) {
        IdUriLabel rubriqueWithIdUriLabel = new IdUriLabel();
        rubriqueWithIdUriLabel.setId(rubriqueDTO.valeurSimple());
        rubriqueWithIdUriLabel.setUri(URI.create(uri));
        List<LocalisedContenu> label = buildLocalisedLabels(
                rubriqueDTO.labelObjLg1(),
                rubriqueDTO.labelObjLg2(),
                lg1,lg2);
        rubriqueWithIdUriLabel.setLabel(label);
    return rubriqueWithIdUriLabel;
    }

    private void addRichText(RubriqueDTO rubriqueDTO, Rubrique rubrique, RapportQualite rapportQualite) {
        Contenu contenuLg1 = new Contenu();
        contenuLg1.setDocuments(null);
        if (StringUtils.hasText(rubriqueDTO.labelLg1())) {
            contenuLg1.setTexte(rubriqueDTO.labelLg1());
        } else {
            contenuLg1.setTexte("");
        }
        contenuLg1.setLangue(lg1);
        if (Boolean.TRUE.equals(rubriqueDTO.hasDocLg1())) {
            contenuLg1.setDocuments(findDocuments(rapportQualite.getId(), rubriqueDTO.id(), lg1));
        }
        rubrique.addContenusItem(contenuLg1);

        if (isConditionForContenuLg2(rubriqueDTO)) {
            Contenu contenuLg2 = new Contenu();
            contenuLg2.setDocuments(null);
            if (StringUtils.hasText(rubriqueDTO.labelLg2())) {
                contenuLg2.setTexte(rubriqueDTO.labelLg2());
            } else {
                contenuLg2.setTexte("");
            }
            contenuLg2.setLangue(lg2);
            if (Boolean.TRUE.equals(rubriqueDTO.hasDocLg2())) {
                contenuLg2.setDocuments(findDocuments(rapportQualite.getId(), rubriqueDTO.id(), lg2));
            }
            rubrique.addContenusItem(contenuLg2);
        }
    }

    private static boolean isConditionForContenuLg2(RubriqueDTO rubriqueDTO) {
        return rubriqueDTO.hasDocLg2() != null &&
                (StringUtils.hasText(rubriqueDTO.labelLg2()) || rubriqueDTO.hasDocLg2());
    }

    private List<Document> findDocuments(String rapportQualiteId, String rubriqueDTOId, String lang) {
        List<DocumentDTO> documentsDTO = this.requestProcessor.queryToFindDocuments()
                .with(new OperationsDocumentsRequestParametizer(rapportQualiteId, rubriqueDTOId, lang))
                .executeQuery()
                .listResult(DocumentDTO.class)
                .result();
        List<Document> documents = new ArrayList<>();
        for (DocumentDTO documentDTO : documentsDTO) {
            Document document = new Document();
            List<LocalisedContenu> label = buildLocalisedLabels(
                    documentDTO.labelLg1(),
                    documentDTO.labelLg2(),
                    lg1,lg2);
            document.label(label);
            document.setDateMiseAJour(documentDTO.dateMiseAJour());
            document.setLangue(documentDTO.langue());
            document.setUrl(documentDTO.url());
            documents.add(document);
        }
        return documents;
    }

    private Rubrique addCodeList(RubriqueDTO rubriqueDTO, Rubrique rubrique, RapportQualite rapportQualite) {
        IdUriLabel rubriqueCodeList = new IdUriLabel();
        rubriqueCodeList.setId(rubriqueDTO.valeurSimple());
        rubriqueCodeList.setUri(URI.create(rubriqueDTO.codeUri()));
        rubriqueCodeList.setLabel(buildLocalisedLabels(
                rubriqueDTO.labelObjLg1(),
                rubriqueDTO.labelObjLg2(),
                lg1,lg2));

        if (rapportQualite.getRubriques() != null) {
            boolean rubricExist = rapportQualite.getRubriques().stream()
                    .filter(Objects::nonNull)
                    .anyMatch(r -> r.getId().equals(rubriqueDTO.id()));

            if (rubricExist) {
                Rubrique existingRubric = rapportQualite.getRubriques().stream()
                        .filter(r -> r.getId().equals(rubriqueDTO.id()))
                        .findFirst()
                        .orElseThrow();
                existingRubric.addCodesItem(rubriqueCodeList);
                return null;
            }
        }
        rubrique.addCodesItem(rubriqueCodeList);
        return rubrique;
    }
}