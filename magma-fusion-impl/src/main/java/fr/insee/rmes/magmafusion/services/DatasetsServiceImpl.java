package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.*;
import fr.insee.rmes.magmafusion.queries.parameters.DatasetsRequestParametizer;
import fr.insee.rmes.magmafusion.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static fr.insee.rmes.magmafusion.utils.LabelsUtils.createLangField;
import static fr.insee.rmes.magmafusion.utils.LabelsUtils.createList;

@Service
public class DatasetsServiceImpl implements DatasetsService {

    private final RequestProcessor requestProcessor;
    public DatasetsServiceImpl(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }


    @Override
    public List<Dataset> convertDatasetDTOsToDataSets(List<DatasetDTO> dtos) {
        return dtos.stream().map(this::convertDatasetDTOToDataSet).toList();
    }

    private Dataset convertDatasetDTOToDataSet(DatasetDTO dto) {
        Dataset dataSet = new Dataset();

        dataSet.setPageAccueil(null);
        dataSet.setDateMiseAJour(null);
        dataSet.setDatePublication(null);
        dataSet.setVersion(null);
        dataSet.setSpatioTemporel(null);
        dataSet.setDisseminationStatus(null);
        dataSet.setIdentifier(null);
        dataSet.setDateCreationEnregistrementCatalogue(null);
        dataSet.setDateMiseAJourEnregistrementCatalogue(null);
        dataSet.setCreateurEnregistrementCatalogue(null);
        dataSet.setContributeurEnregistrementCatalogue(null);
        dataSet.setNumObservations(null);
        dataSet.setNumSeries(null);
        dataSet.setSousTitre(null);
        dataSet.setResume(null);
        dataSet.setDescription(null);
        dataSet.setNoteDePortee(null);
        dataSet.setEditeur(null);
        dataSet.setKeyword(null);
        dataSet.setType(null);
        dataSet.setAccessRights(null);
        dataSet.setConfidentialityStatus(null);
        dataSet.setCouvertureSpatiale(null);
        dataSet.setPeriodeTemporelle(null);
        dataSet.setStructure(null);
        dataSet.setCreateur(null);
        dataSet.setaEteGenerePar(null);
        dataSet.setTheme(null);
        dataSet.setRelations(null);
        dataSet.setArchiveUnit(null);
        dataSet.setWasDerivedFrom(null);
        dataSet.setProcessStep(null);
        dataSet.setPeriodiciteDeMiseAJour(null);
        dataSet.setResolutionTemporelle(null);
        dataSet.setResolutionSpatiale(null);
        dataSet.setUniteStatistique(null);

        dataSet.setId(dto.id());
        dataSet.setUri(dto.uri());
        dataSet.setEtatValidation(dto.statutValidation());
        dataSet.setDateCreationEnregistrementCatalogue(dto.dateCreation() != null ? dto.dateCreation().toString() : null);
        dataSet.setDateMiseAJourEnregistrementCatalogue(dto.catalogRecordModified() != null ? dto.catalogRecordModified().toString() : null);
        dataSet.setTitre(createList(
                createLangField(dto.titreLg1(),"fr"),
                createLangField(dto.titreLg2(),"en")));
        return dataSet;
    }

    @Override
    public Dataset convertDatasetByIdDTOToDataSet(DatasetByIdDTO dto) {
        Dataset dataSet = new Dataset();

        dataSet.setPageAccueil(null);
        dataSet.setDateMiseAJour(null);
        dataSet.setDatePublication(null);
        dataSet.setVersion(null);
        dataSet.setSpatioTemporel(null);
        dataSet.setDisseminationStatus(null);
        dataSet.setIdentifier(null);
        dataSet.setDateCreationEnregistrementCatalogue(null);
        dataSet.setDateMiseAJourEnregistrementCatalogue(null);
        dataSet.setCreateurEnregistrementCatalogue(null);
        dataSet.setContributeurEnregistrementCatalogue(null);
        dataSet.setNumObservations(null);
        dataSet.setNumSeries(null);
        dataSet.setSousTitre(null);
        dataSet.setResume(null);
        dataSet.setDescription(null);
        dataSet.setNoteDePortee(null);
        dataSet.setEditeur(null);
        dataSet.setKeyword(null);
        dataSet.setType(null);
        dataSet.setAccessRights(null);
        dataSet.setConfidentialityStatus(null);
        dataSet.setSpatioTemporel(null);
        dataSet.setPeriodeTemporelle(null);
        dataSet.setStructure(null);
        dataSet.setCreateur(null);
        dataSet.setaEteGenerePar(null);
        dataSet.setTheme(null);
        dataSet.setRelations(null);
        dataSet.setArchiveUnit(null);
        dataSet.setWasDerivedFrom(null);
        dataSet.setProcessStep(null);
        dataSet.setPeriodiciteDeMiseAJour(null);
        dataSet.setResolutionTemporelle(null);
        dataSet.setResolutionSpatiale(null);
        dataSet.setUniteStatistique(null);

        dataSet.setId(dto.id());
        dataSet.setUri(dto.uri());
        dataSet.setEtatValidation(dto.statutValidation());
        dataSet.setDateMiseAJour(dto.modified());
        dataSet.setDatePublication(dto.issued());
        dataSet.setVersion(dto.version());
        dataSet.setSpatioTemporel(dto.spatialTemporal());
        dataSet.setDisseminationStatus(dto.disseminationStatus());
        dataSet.setIdentifier(dto.identifier());
        dataSet.setDateCreationEnregistrementCatalogue(dto.catalogRecordCreated());
        dataSet.setDateMiseAJourEnregistrementCatalogue(dto.catalogRecordModified());

        if (StringUtils.hasText(dto.numObservations()) ) {
            dataSet.setNumObservations(Integer.parseInt(dto.numObservations()));
        }
        if (StringUtils.hasText(dto.numSeries()) ) {
            dataSet.setNumSeries(Integer.parseInt(dto.numSeries()));
        }

        if (StringUtils.hasText(dto.titleLg1()) ) {
            dataSet.setTitre(createList(
                    createLangField(dto.titleLg1(), "fr"),
                    createLangField(dto.titleLg2(), "en")));
        }
        if (StringUtils.hasText(dto.subtitleLg1())) {
            dataSet.setSousTitre(createList(
                    createLangField(dto.subtitleLg1(),"fr"),
                    createLangField(dto.subtitleLg2(),"en")));
        }
        if (StringUtils.hasText(dto.abstractLg1())){
            dataSet.setResume(createList(
                    createLangField(dto.abstractLg1(),"fr"),
                    createLangField(dto.abstractLg2(),"en")));
        }
        if (StringUtils.hasText(dto.descriptionLg1())){
            dataSet.setDescription(createList(
                    createLangField(dto.descriptionLg1(),"fr"),
                    createLangField(dto.descriptionLg2(),"en")));
        }
        if (StringUtils.hasText(dto.scopeNoteLg1())) {
            dataSet.setNoteDePortee(createList(
                    createLangField(dto.scopeNoteLg1(), "fr"),
                    createLangField(dto.scopeNoteLg2(), "en")));
        }
        if (StringUtils.hasText(dto.landingPageLg1())) {
            dataSet.setPageAccueil(createList(
                    new LocalisedUrl().langue("fr").url(dto.landingPageLg1()),
                    new LocalisedUrl().langue("en").url(dto.landingPageLg2())));
        }

        if (dto.keywordLg1() != null && dto.keywordLg2() != null){
            dataSet.setKeyword(buildKeywords(dto.keywordLg1(), dto.keywordLg2()));
        }

        if (StringUtils.hasText(dto.idPublisher())) {
            dataSet.setEditeur(new IdLabel()
                    .id(dto.idPublisher())
                    .libelle(createList(
                            createLangField(dto.labelPublisherLg1(),"fr"),
                            createLangField(dto.labelPublisherLg2(),"en"))));
        }

        if (StringUtils.hasText(dto.labeltypeLg1())) {
            dataSet.setType(createList(
                    createLangField(dto.labeltypeLg1(),"fr"),
                    createLangField(dto.labeltypeLg2(),"en")));
        }

        if (StringUtils.hasText(dto.labelaccessRightsLg1())) {
            dataSet.setAccessRights(createList(
                    createLangField(dto.labelaccessRightsLg1(),"fr"),
                    createLangField(dto.labelaccessRightsLg2(),"en")));
        }

        if (StringUtils.hasText(dto.labelconfidentialityStatusLg1())) {
            dataSet.setConfidentialityStatus(createList(
                    createLangField(dto.labelconfidentialityStatusLg1(),"fr"),
                    createLangField(dto.labelconfidentialityStatusLg2(),"en")));
        }

        if (StringUtils.hasText(dto.labelaccrualPeriodicityLg1())) {
            List<LocalisedContenu> accrualPeriodicityListTitle = createList(
                    createLangField(dto.labelaccrualPeriodicityLg1(),"fr"),
                    createLangField(dto.labelaccrualPeriodicityLg2(),"en")
            );
            IdLabel accrualPeriodicityIdLabel = new IdLabel();
            accrualPeriodicityIdLabel.setId(null);
            accrualPeriodicityIdLabel.setLibelle(accrualPeriodicityListTitle);
            dataSet.setPeriodiciteDeMiseAJour(accrualPeriodicityIdLabel);

        }

        if (StringUtils.hasText(dto.temporalResolutions())) {
            List<String> urisTemporalResolution = List.of(dto.temporalResolutions().split(","));
            List<Label> temporalResolutionList = getTemporalResolution(urisTemporalResolution);
            dataSet.setResolutionTemporelle(temporalResolutionList);
            }

        if (StringUtils.hasText(dto.spatialResolutions())) {
            List<String> urisSpatialResolution = List.of(dto.spatialResolutions().split(","));
            List<IdLabel> spatialResolutionList = getSpatialResolution(urisSpatialResolution);
            dataSet.setResolutionSpatiale(spatialResolutionList);
        }

        if (StringUtils.hasText(dto.statisticalUnits())) {
            List<String> urisStatisticalUnits = List.of(dto.statisticalUnits().split(","));
            List<IdLabel> statisticalUnitList = getStatisticalUnits(urisStatisticalUnits);
            dataSet.setUniteStatistique(statisticalUnitList);
        }

        if (StringUtils.hasText(dto.themes())) {
            List<String> urisThemes = List.of(dto.themes().split(","));
            List<Theme> themesList = getThemes(urisThemes);
            dataSet.setTheme(themesList);
        }

        if (StringUtils.hasText(dto.spatialId())) {
            dataSet.setCouvertureSpatiale(new IdLabel()
                    .id(dto.spatialId())
                    .libelle(createList(
                            createLangField(dto.labelspatialLg1(),"fr"),
                            createLangField(dto.labelspatialLg2(),"en"))));
        }

        if (StringUtils.hasText(dto.idCatalogRecordCreator())) {
            dataSet.setCreateurEnregistrementCatalogue(new IdLabel()
                    .id(dto.idCatalogRecordCreator())
                    .libelle(createList(
                            createLangField(dto.catalogRecordCreatorLabelLg1(),"fr"),
                            createLangField(dto.catalogRecordCreatorLabelLg2(),"en"))));
        }

        if (StringUtils.hasText(dto.idCatalogRecordContributor())) {
            dataSet.setContributeurEnregistrementCatalogue(new IdLabel()
                    .id(dto.idCatalogRecordContributor())
                    .libelle(createList(
                            createLangField(dto.catalogRecordContributorLabelLg1(),"fr"),
                            createLangField(dto.catalogRecordContributorLabelLg2(),"en"))));
        }


        if (StringUtils.hasText(dto.startPeriod())) {
            dataSet.setPeriodeTemporelle(new Temporal()
                    .debutPeriode(dto.startPeriod())
                    .finPeriode(dto.endPeriod()));
        }


        if (StringUtils.hasText(dto.structureUri())) {
            dataSet.setStructure(new Structure()
                    .uri(dto.structureUri())
                    .id(dto.structureId())
                    .structureDonnees(dto.dsd()));
        }

        if (StringUtils.hasText(dto.creators())) {
            dataSet.setCreateur(Arrays.stream(dto.creators().split("\\|"))
                    .filter(s -> !s.isBlank())
                    .map(raw -> {
                        String[] parts = raw.split("\\$", -1);
                        return new IdLabel()
                                .id(parts.length > 0 ? parts[0] : null)
                                .libelle(createList(
                                        createLangField(parts.length > 1 ? parts[1] : null, "fr"),
                                        createLangField(parts.length > 2 ? parts[2] : null, "en")));
                    })
                    .toList());
        }

        if (StringUtils.hasText(dto.operationStat())) {
            dataSet.setaEteGenerePar(Arrays.stream(dto.operationStat().split(","))
                    .filter(s -> !s.isBlank())
                    .map(uri -> new IdLabel().id(uri))
                    .toList());
        }

        if (StringUtils.hasText(dto.names())) {
            dataSet.setTheme(Arrays.stream(dto.names().split(","))
                    .filter(s -> !s.isBlank())
                    .map(uri -> new Theme().uri(uri))
                    .toList());
        }

        if (StringUtils.hasText(dto.relations())) {
            dataSet.setRelations(Arrays.stream(dto.relations().split(","))
                    .filter(s -> !s.isBlank())
                    .toList());
        }

        if (StringUtils.hasText(dto.archiveUnits())) {
            dataSet.setArchiveUnit(Arrays.stream(dto.archiveUnits().split(","))
                    .filter(s -> !s.isBlank())
                    .map(uri -> new IdLabel().id(uri))
                    .toList());
        }

        if (StringUtils.hasText(dto.wasDerivedFromS())) {
            WasDerivedFrom wasDerivedFrom = new WasDerivedFrom()
                    .datasets(Arrays.stream(dto.wasDerivedFromS().split(","))
                            .filter(s -> !s.isBlank())
                            .toList());
            if (StringUtils.hasText(dto.derivedDescriptionLg1())) {
                wasDerivedFrom.setDescription(createList(
                        createLangField(dto.derivedDescriptionLg1(),"fr"),
                        createLangField(dto.derivedDescriptionLg2(), "en")));
            }
            if (dto.derivedDescriptionLg1() != null && dto.derivedDescriptionLg1().isBlank()) {
                wasDerivedFrom.setDescription(createList(
                        createLangField(dto.derivedDescriptionLg1(),"fr"),
                        createLangField("", "en")));
            }
            dataSet.setWasDerivedFrom(wasDerivedFrom);
        }

        return dataSet;
    }


    protected List<Label> getTemporalResolution(List<String> urisTemporalResolution) {
        List<Label> temporalResolution = new ArrayList<>();
        for (String uri : urisTemporalResolution) {
            TemporalResolutionDTO temporalResolutionContenu = this.requestProcessor.queryToFindTemporalResolutionContenu()
                    .with(DatasetsRequestParametizer.ofUri(uri))
                    .executeQuery()
                    .singleResult(TemporalResolutionDTO.class)
                    .result();
            List<LocalisedContenu> temporalResolutionTitles = createList(
                    createLangField(temporalResolutionContenu.labeltemporalResolutionLg1(),"fr"),
                    createLangField(temporalResolutionContenu.labeltemporalResolutionLg2(),"en")
            );
            Label temporalResolutionLabel = new Label();
            temporalResolutionLabel.setLibelle(temporalResolutionTitles);
            temporalResolution.add(temporalResolutionLabel);
        }
        return temporalResolution;
    }

    protected List<IdLabel> getSpatialResolution(List<String> urisSpatialResolution) {
        List<IdLabel> spatialResolution = new ArrayList<>();
        for (String uri : urisSpatialResolution) {
            SpatialResolutionDTO spatialResolutionContenu = this.requestProcessor.queryToFindSpatialResolutionContenu()
                    .with(DatasetsRequestParametizer.ofUri(uri))
                    .executeQuery()
                    .singleResult(SpatialResolutionDTO.class)
                    .result();
            List<LocalisedContenu> spatialResolutionTitles = createList(
                    createLangField(spatialResolutionContenu.labelSpatialResolutionLg1(),"fr"),
                    createLangField(spatialResolutionContenu.labelSpatialResolutionLg2(),"en")
            );
            IdLabel spatialResolutionLabel = new IdLabel();
            spatialResolutionLabel.setLibelle(spatialResolutionTitles);
            spatialResolutionLabel.setId(spatialResolutionContenu.spatialResolutionId());
            spatialResolution.add(spatialResolutionLabel);
        }
        return spatialResolution;
    }

    protected List<IdLabel> getStatisticalUnits(List<String> urisStatisticalUnits) {
        List<IdLabel> statisticalUnits = new ArrayList<>();
        for (String uri : urisStatisticalUnits) {
            StatisticalUnitDTO statisticalUnitsContenu = this.requestProcessor.queryToFindStatisticalUnits()
                    .with(DatasetsRequestParametizer.ofUri(uri))
                    .executeQuery()
                    .singleResult(StatisticalUnitDTO.class)
                    .result();
            List<LocalisedContenu> statisticalUnitTitles = createList(
                    createLangField(statisticalUnitsContenu.labelStatisticalUnitLg1(),"fr"),
                    createLangField(statisticalUnitsContenu.labelStatisticalUnitLg2(),"en")
            );
            IdLabel statisticalUnitLabel = new IdLabel();
            statisticalUnitLabel.setLibelle(statisticalUnitTitles);
            statisticalUnitLabel.setId(statisticalUnitsContenu.statisticalUnitId());
            statisticalUnits.add(statisticalUnitLabel);
        }
        return statisticalUnits;
    }

    protected List<Theme> getThemes(List<String> urisThemes) {
        List<Theme> themes = new ArrayList<>();
        for (String uri : urisThemes) {
            ThemeDTO themeDTO = this.requestProcessor.queryToFindThemes()
                    .with(DatasetsRequestParametizer.ofUri(uri))
                    .executeQuery()
                    .singleResult(ThemeDTO.class)
                    .result();
            List<LocalisedContenu> themeTitles = createList(
                    createLangField(themeDTO.labelThemeLg1(),"fr"),
                    createLangField(themeDTO.labelThemeLg2(),"en")
            );
            Theme themeLabel = new Theme();
            themeLabel.setLibelle(themeTitles);
            themeLabel.setUri(themeDTO.uri());
            themes.add(themeLabel);
        }
        return themes;
    }

     @Override
     public List<Distribution> convertDistributionDTOsToDistributions(List<DistributionDTO> dtos) {
        Map<String, List<DistributionDTO>> grouped = new LinkedHashMap<>();
        for (DistributionDTO dto : dtos) {
            grouped.computeIfAbsent(dto.identifier(), k -> new ArrayList<>()).add(dto);
        }
        return grouped.values().stream().map(rows -> {
            DistributionDTO first = rows.get(0);
            Distribution d = new Distribution();
            d.setIdentifier(first.identifier());
            d.setUri(first.uri());
            d.setByteSize(first.byteSize());
            d.setCreated(first.created());
            d.setModified(first.modified());
            d.setFormat(first.format());
            d.setDownloadURL(rows.stream()
                    .map(DistributionDTO::downloadURL)
                    .filter(StringUtils::hasText)
                    .distinct()
                    .toList());
            if (StringUtils.hasText(first.titleLg1())) {
                d.setTitle(createList(
                        createLangField(first.titleLg1(), "fr"),
                        createLangField(first.titleLg2(), "en")));
            }
            if (StringUtils.hasText(first.descriptionLg1())) {
                d.setDescription(createList(
                        createLangField(first.descriptionLg1(), "fr"),
                        createLangField(first.descriptionLg2(), "en")));
            }
            return d;
        }).toList();
    }

    private List<LocalisedContenu> buildKeywords(String kwLg1, String kwLg2) {
        List<LocalisedContenu> keywords = new java.util.ArrayList<>();
        if (StringUtils.hasText(kwLg1)) {
            Arrays.stream(kwLg1.split(","))
                    .filter(s -> !s.isBlank())
                    .forEach(kw -> keywords.add(new LocalisedContenu().langue("fr").contenu(kw.trim())));
        }
        if (StringUtils.hasText(kwLg2)) {
            Arrays.stream(kwLg2.split(","))
                    .filter(s -> !s.isBlank())
                    .forEach(kw -> keywords.add(new LocalisedContenu().langue("en").contenu(kw.trim())));
        }
        return keywords;
    }
}