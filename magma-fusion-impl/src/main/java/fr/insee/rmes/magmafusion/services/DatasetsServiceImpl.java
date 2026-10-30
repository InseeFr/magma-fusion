package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.*;
import fr.insee.rmes.magmafusion.queries.parameters.DatasetsRequestParametizer;
import fr.insee.rmes.magmafusion.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static fr.insee.rmes.magmafusion.utils.LabelsUtils.*;

@Service
public class DatasetsServiceImpl implements DatasetsService {

    private final String lg1;
    private final String lg2;
    private final RequestProcessor requestProcessor;

    public DatasetsServiceImpl(
            @Value("${fr.insee.rmes.magmafusion.lg1}") String lg1,
            @Value("${fr.insee.rmes.magmafusion.lg2}") String lg2,
            RequestProcessor requestProcessor
    ) {
        this.lg1 = lg1;
        this.lg2 = lg2;
        this.requestProcessor = requestProcessor;
    }



    @Override
    public List<DatasetForList> convertDatasetDTOsToDataSets(List<DatasetForListDTO> dtos) {
        return dtos.stream().map(this::convertDatasetDTOToDataSetForList).toList();
    }

    private DatasetForList convertDatasetDTOToDataSetForList(DatasetForListDTO dto) {
        DatasetForList dataSet = new DatasetForList();

        dataSet.setId(dto.id());
        dataSet.setUri(dto.uri());
        dataSet.setEtatValidation(dto.statutValidation());
        dataSet.setDateCreationEnregistrementCatalogue(dto.dateCreation() != null ? dto.dateCreation().toString() : null);
        dataSet.setDateMiseAJourEnregistrementCatalogue(dto.catalogRecordModified() != null ? dto.catalogRecordModified().toString() : null);
        dataSet.setTitre(buildLocalisedLabels(
                dto.titreLg1(),
                dto.titreLg2(),
                lg1,lg2));
        return dataSet;
    }

    @Override
    public Dataset convertDatasetByIdDTOToDataSet(DatasetByIdDTO dto) {
        Dataset dataSet = initDataset();

        dataSet.setId(dto.id());
        dataSet.setUri(dto.uri());
        dataSet.setEtatValidation(dto.statutValidation());
        dataSet.setDateMiseAJour(dto.modified() != null ? dto.modified() : null);
        dataSet.setDatePublication(dto.issued() != null ? dto.issued() : null);
        dataSet.setVersion(dto.version() != null ? dto.version() : null);
        dataSet.setSpatioTemporel(dto.spatialTemporal() != null ? dto.spatialTemporal() : null);
        dataSet.setDisseminationStatus(dto.disseminationStatus() != null ? dto.disseminationStatus() : null);
        dataSet.setIdentifier(dto.identifier() != null ? dto.identifier() : null);
        dataSet.setDateCreationEnregistrementCatalogue(dto.catalogRecordCreated() != null ? dto.catalogRecordCreated() : null);
        dataSet.setDateMiseAJourEnregistrementCatalogue(dto.catalogRecordModified() != null ? dto.catalogRecordModified() : null);

        if (StringUtils.hasText(dto.numObservations()) ) {
            dataSet.setNumObservations(Integer.parseInt(dto.numObservations()));
        }
        if (StringUtils.hasText(dto.numSeries()) ) {
            dataSet.setNumSeries(Integer.parseInt(dto.numSeries()));
        }

        dataSet.setTitre(buildLocalisedLabels(
            dto.titleLg1(),
            dto.titleLg2(),
            lg1,lg2));

        dataSet.setSousTitre(buildLocalisedLabels(
            dto.subtitleLg1(),
            dto.subtitleLg2(),
            lg1,lg2));

        dataSet.setResume(buildLocalisedLabels(
            dto.abstractLg1(),
            dto.abstractLg2(),
            lg1, lg2));

        dataSet.setDescription(buildLocalisedLabels(
            dto.descriptionLg1(),
            dto.descriptionLg2(),
            lg1,lg2));


        dataSet.setNoteDePortee(buildLocalisedLabels(
            dto.scopeNoteLg1(),
            dto.scopeNoteLg2(),
            lg1, lg2));

        dataSet.setPageAccueil(buildLocalisedUrls(
            dto.landingPageLg1(),
            dto.landingPageLg2(),
            lg1,lg2));

        if (dto.keywordLg1() != null && dto.keywordLg2() != null){
            dataSet.setKeyword(buildKeywords(dto.keywordLg1(), dto.keywordLg2()));
        }

        if (StringUtils.hasText(dto.idPublisher())) {
            dataSet.setEditeur(new IdLabel()
                    .id(dto.idPublisher())
                    .libelle(buildLocalisedLabels(
                            dto.labelPublisherLg1(),
                            dto.labelPublisherLg2(),
                            lg1,lg2))
            );
        }

        dataSet.setType(buildLocalisedLabels(
            dto.labeltypeLg1(),
            dto.labeltypeLg2(),
            lg1, lg2));

        dataSet.setAccessRights(buildLocalisedLabels(
            dto.labelaccessRightsLg1(),
            dto.labelaccessRightsLg2(),
            lg1, lg2));

        dataSet.setConfidentialityStatus(buildLocalisedLabels(
            dto.labelconfidentialityStatusLg1(),
            dto.labelconfidentialityStatusLg2(),
            lg1,lg2));

        List<LocalisedContenu> accrualPeriodicityListTitle = buildLocalisedLabels(
            dto.labelaccrualPeriodicityLg1(),
            dto.labelaccrualPeriodicityLg2(),
            lg1, lg2);
        IdLabel accrualPeriodicityIdLabel = new IdLabel();
        accrualPeriodicityIdLabel.setId(null);
        accrualPeriodicityIdLabel.setLibelle(accrualPeriodicityListTitle);
        dataSet.setPeriodiciteDeMiseAJour(accrualPeriodicityIdLabel);

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
                    .libelle(buildLocalisedLabels(
                            dto.labelspatialLg1(),
                            dto.labelspatialLg2(),
                            lg1,lg2)));
        }

        if (StringUtils.hasText(dto.idCatalogRecordCreator())) {
            dataSet.setCreateurEnregistrementCatalogue(new IdLabel()
                    .id(dto.idCatalogRecordCreator())
                    .libelle(buildLocalisedLabels(
                            dto.catalogRecordCreatorLabelLg1(),
                            dto.catalogRecordCreatorLabelLg2(),
                            lg1, lg2)));
        }

        if (StringUtils.hasText(dto.idCatalogRecordContributor())) {
            dataSet.setContributeurEnregistrementCatalogue(new IdLabel()
                    .id(dto.idCatalogRecordContributor())
                    .libelle(buildLocalisedLabels(
                            dto.catalogRecordContributorLabelLg1(),
                            dto.catalogRecordContributorLabelLg2(),
                            lg1, lg2)));
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
                                        createLangField(parts.length > 1 ? parts[1] : null, lg1),
                                        createLangField(parts.length > 2 ? parts[2] : null, lg2)));
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
            wasDerivedFrom.setDescription(buildLocalisedLabels(
                    dto.derivedDescriptionLg1(),
                    dto.derivedDescriptionLg2(),
                    lg1,lg2));
            dataSet.setWasDerivedFrom(wasDerivedFrom);
        }

        return dataSet;
    }


    private Dataset initDataset(){
        Dataset dataSet = new Dataset();
        dataSet.setPageAccueil(null);
        dataSet.setNumObservations(null);
        dataSet.setNumSeries(null);
        dataSet.setKeyword(null);
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
            List<LocalisedContenu> temporalResolutionTitles = buildLocalisedLabels(
                    temporalResolutionContenu.labeltemporalResolutionLg1(),
                    temporalResolutionContenu.labeltemporalResolutionLg2(),
                    lg1,lg2
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
            List<LocalisedContenu> spatialResolutionTitles = buildLocalisedLabels(
                    spatialResolutionContenu.labelSpatialResolutionLg1(),
                    spatialResolutionContenu.labelSpatialResolutionLg2(),
                    lg1,lg2
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
            List<LocalisedContenu> statisticalUnitTitles = buildLocalisedLabels(
                    statisticalUnitsContenu.labelStatisticalUnitLg1(),
                    statisticalUnitsContenu.labelStatisticalUnitLg2(),
                    lg1,lg2
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
            List<LocalisedContenu> themeTitles = buildLocalisedLabels(
                    themeDTO.labelThemeLg1(),
                    themeDTO.labelThemeLg2(),
                    lg1,lg2
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
                d.setTitle(buildLocalisedLabels(
                        first.titleLg1(),
                        first.titleLg2(),
                        lg1, lg2));
            }
            if (StringUtils.hasText(first.descriptionLg1())) {
                d.setDescription(buildLocalisedLabels(
                        first.descriptionLg1(),
                        first.descriptionLg2(),
                        lg1, lg2));
            }
            return d;
        }).toList();
    }

    private List<LocalisedContenu> buildKeywords(String kwLg1, String kwLg2) {
        List<LocalisedContenu> keywords = new java.util.ArrayList<>();
        if (StringUtils.hasText(kwLg1)) {
            Arrays.stream(kwLg1.split(","))
                    .filter(s -> !s.isBlank())
                    .forEach(kw -> keywords.add(new LocalisedContenu().langue(lg1).contenu(kw.trim())));
        }
        if (StringUtils.hasText(kwLg2)) {
            Arrays.stream(kwLg2.split(","))
                    .filter(s -> !s.isBlank())
                    .forEach(kw -> keywords.add(new LocalisedContenu().langue(lg2).contenu(kw.trim())));
        }
        return keywords;
    }
}