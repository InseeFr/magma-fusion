package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.*;
import fr.insee.rmes.magmafusion.queries.parameters.DatasetsRequestParametizer;
import fr.insee.rmes.magmafusion.utils.*;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${fr.insee.rmes.magmafusion.lg1}")
    private String lg1;

    @Value("${fr.insee.rmes.magmafusion.lg2}")
    private String lg2;

    @Override
    public List<Dataset> convertDatasetDTOsToDataSets(List<DatasetDTO> dtos) {
        return dtos.stream().map(this::convertDatasetDTOToDataSet).toList();
    }

    private Dataset convertDatasetDTOToDataSet(DatasetDTO dto) {
        Dataset dataSet = new Dataset();

        dataSet.setLandingPage(null);
        dataSet.setModified(null);
        dataSet.setIssued(null);
        dataSet.setVersion(null);
        dataSet.setSpatialTemporal(null);
        dataSet.setDisseminationStatus(null);
        dataSet.setIdentifier(null);
        dataSet.setCatalogRecordCreated(null);
        dataSet.setCatalogRecordModified(null);
        dataSet.setCatalogRecordCreator(null);
        dataSet.setCatalogRecordContributor(null);
        dataSet.setNumObservations(null);
        dataSet.setNumSeries(null);
        dataSet.setSubtitle(null);
        dataSet.setAbstract(null);
        dataSet.setDescription(null);
        dataSet.setScopeNote(null);
        dataSet.setPublisher(null);
        dataSet.setKeyword(null);
        dataSet.setType(null);
        dataSet.setAccessRights(null);
        dataSet.setConfidentialityStatus(null);
        dataSet.setSpatial(null);
        dataSet.setTemporal(null);
        dataSet.setStructure(null);
        dataSet.setCreator(null);
        dataSet.setWasGeneratedBy(null);
        dataSet.setTheme(null);
        dataSet.setRelations(null);
        dataSet.setArchiveUnit(null);
        dataSet.setWasDerivedFrom(null);
        dataSet.setProcessStep(null);
        dataSet.setAccrualPeriodicity(null);
        dataSet.setTemporalResolution(null);
        dataSet.setSpatialResolution(null);
        dataSet.setStatisticalUnit(null);

        dataSet.setId(dto.id());
        dataSet.setUri(dto.uri());
        dataSet.setValidationState(dto.statutValidation());
        dataSet.setCatalogRecordCreated(dto.dateCreation() != null ? dto.dateCreation().toString() : null);
        dataSet.setCatalogRecordModified(dto.catalogRecordModified() != null ? dto.catalogRecordModified().toString() : null);
        dataSet.setTitle(createList(
                createLangField(dto.titreLg1(),lg1),
                createLangField(dto.titreLg2(),lg2)));
        return dataSet;
    }

    @Override
    public Dataset convertDatasetByIdDTOToDataSet(DatasetByIdDTO dto) {
        Dataset dataSet = new Dataset();

        dataSet.setLandingPage(null);
        dataSet.setModified(null);
        dataSet.setIssued(null);
        dataSet.setVersion(null);
        dataSet.setSpatialTemporal(null);
        dataSet.setDisseminationStatus(null);
        dataSet.setIdentifier(null);
        dataSet.setCatalogRecordCreated(null);
        dataSet.setCatalogRecordModified(null);
        dataSet.setCatalogRecordCreator(null);
        dataSet.setCatalogRecordContributor(null);
        dataSet.setNumObservations(null);
        dataSet.setNumSeries(null);
        dataSet.setSubtitle(null);
        dataSet.setAbstract(null);
        dataSet.setDescription(null);
        dataSet.setScopeNote(null);
        dataSet.setPublisher(null);
        dataSet.setKeyword(null);
        dataSet.setType(null);
        dataSet.setAccessRights(null);
        dataSet.setConfidentialityStatus(null);
        dataSet.setSpatial(null);
        dataSet.setTemporal(null);
        dataSet.setStructure(null);
        dataSet.setCreator(null);
        dataSet.setWasGeneratedBy(null);
        dataSet.setTheme(null);
        dataSet.setRelations(null);
        dataSet.setArchiveUnit(null);
        dataSet.setWasDerivedFrom(null);
        dataSet.setProcessStep(null);
        dataSet.setAccrualPeriodicity(null);
        dataSet.setTemporalResolution(null);
        dataSet.setSpatialResolution(null);
        dataSet.setStatisticalUnit(null);

        dataSet.setId(dto.id());
        dataSet.setUri(dto.uri());
        dataSet.setValidationState(dto.statutValidation());
        dataSet.setModified(dto.modified());
        dataSet.setIssued(dto.issued());
        dataSet.setVersion(dto.version());
        dataSet.setSpatialTemporal(dto.spatialTemporal());
        dataSet.setDisseminationStatus(dto.disseminationStatus());
        dataSet.setIdentifier(dto.identifier());
        dataSet.setCatalogRecordCreated(dto.catalogRecordCreated());
        dataSet.setCatalogRecordModified(dto.catalogRecordModified());

        if (StringUtils.hasText(dto.numObservations()) ) {
            dataSet.setNumObservations(Integer.parseInt(dto.numObservations()));
        }
        if (StringUtils.hasText(dto.numSeries()) ) {
            dataSet.setNumSeries(Integer.parseInt(dto.numSeries()));
        }

        if (StringUtils.hasText(dto.titleLg1()) ) {
            dataSet.setTitle(createList(
                    createLangField(dto.titleLg1(), lg1),
                    createLangField(dto.titleLg2(), lg2)));
        }
        if (StringUtils.hasText(dto.subtitleLg1())) {
            dataSet.setSubtitle(createList(
                    createLangField(dto.subtitleLg1(),lg1),
                    createLangField(dto.subtitleLg2(),lg2)));
        }
        if (StringUtils.hasText(dto.abstractLg1())){
            dataSet.setAbstract(createList(
                    createLangField(dto.abstractLg1(),lg1),
                    createLangField(dto.abstractLg2(),lg2)));
        }
        if (StringUtils.hasText(dto.descriptionLg1())){
            dataSet.setDescription(createList(
                    createLangField(dto.descriptionLg1(),lg1),
                    createLangField(dto.descriptionLg2(),lg2)));
        }
        if (StringUtils.hasText(dto.scopeNoteLg1())) {
            dataSet.setScopeNote(createList(
                    createLangField(dto.scopeNoteLg1(), lg1),
                    createLangField(dto.scopeNoteLg2(), lg2)));
        }
        if (StringUtils.hasText(dto.landingPageLg1())) {
            dataSet.setLandingPage(createList(
                    new LocalisedUrl().lang(lg1).url(dto.landingPageLg1()),
                    new LocalisedUrl().lang(lg2).url(dto.landingPageLg2())));
        }

        if (dto.keywordLg1() != null && dto.keywordLg2() != null){
            dataSet.setKeyword(buildKeywords(dto.keywordLg1(), dto.keywordLg2()));
        }

        if (StringUtils.hasText(dto.idPublisher())) {
            dataSet.setPublisher(new IdLabel()
                    .id(dto.idPublisher())
                    .label(createList(
                            createLangField(dto.labelPublisherLg1(),lg1),
                            createLangField(dto.labelPublisherLg2(),lg2))));
        }

        if (StringUtils.hasText(dto.labeltypeLg1())) {
            dataSet.setType(createList(
                    createLangField(dto.labeltypeLg1(),lg1),
                    createLangField(dto.labeltypeLg2(),lg2)));
        }

        if (StringUtils.hasText(dto.labelaccessRightsLg1())) {
            dataSet.setAccessRights(createList(
                    createLangField(dto.labelaccessRightsLg1(),lg1),
                    createLangField(dto.labelaccessRightsLg2(),lg2)));
        }

        if (StringUtils.hasText(dto.labelconfidentialityStatusLg1())) {
            dataSet.setConfidentialityStatus(createList(
                    createLangField(dto.labelconfidentialityStatusLg1(),lg1),
                    createLangField(dto.labelconfidentialityStatusLg2(),lg2)));
        }

        if (StringUtils.hasText(dto.labelaccrualPeriodicityLg1())) {
            List<LocalisedContenu> accrualPeriodicityListTitle = createList(
                    createLangField(dto.labelaccrualPeriodicityLg1(),lg1),
                    createLangField(dto.labelaccrualPeriodicityLg2(),lg2)
            );
            IdLabel accrualPeriodicityIdLabel = new IdLabel();
            accrualPeriodicityIdLabel.setId(null);
            accrualPeriodicityIdLabel.setLabel(accrualPeriodicityListTitle);
            dataSet.setAccrualPeriodicity(accrualPeriodicityIdLabel);

        }

        if (StringUtils.hasText(dto.temporalResolutions())) {
            List<String> urisTemporalResolution = List.of(dto.temporalResolutions().split(","));
            List<Label> temporalResolutionList = getTemporalResolution(urisTemporalResolution);
            dataSet.setTemporalResolution(temporalResolutionList);
            }

        if (StringUtils.hasText(dto.spatialResolutions())) {
            List<String> urisSpatialResolution = List.of(dto.spatialResolutions().split(","));
            List<IdLabel> spatialResolutionList = getSpatialResolution(urisSpatialResolution);
            dataSet.setSpatialResolution(spatialResolutionList);
        }

        if (StringUtils.hasText(dto.statisticalUnits())) {
            List<String> urisStatisticalUnits = List.of(dto.statisticalUnits().split(","));
            List<IdLabel> statisticalUnitList = getStatisticalUnits(urisStatisticalUnits);
            dataSet.setStatisticalUnit(statisticalUnitList);
        }

        if (StringUtils.hasText(dto.themes())) {
            List<String> urisThemes = List.of(dto.themes().split(","));
            List<Theme> themesList = getThemes(urisThemes);
            dataSet.setTheme(themesList);
        }

        if (StringUtils.hasText(dto.spatialId())) {
            dataSet.setSpatial(new IdLabel()
                    .id(dto.spatialId())
                    .label(createList(
                            createLangField(dto.labelspatialLg1(),lg1),
                            createLangField(dto.labelspatialLg2(),lg2))));
        }

        if (StringUtils.hasText(dto.idCatalogRecordCreator())) {
            dataSet.setCatalogRecordCreator(new IdLabel()
                    .id(dto.idCatalogRecordCreator())
                    .label(createList(
                            createLangField(dto.catalogRecordCreatorLabelLg1(),lg1),
                            createLangField(dto.catalogRecordCreatorLabelLg2(),lg2))));
        }

        if (StringUtils.hasText(dto.idCatalogRecordContributor())) {
            dataSet.setCatalogRecordContributor(new IdLabel()
                    .id(dto.idCatalogRecordContributor())
                    .label(createList(
                            createLangField(dto.catalogRecordContributorLabelLg1(),lg1),
                            createLangField(dto.catalogRecordContributorLabelLg2(),lg2))));
        }


        if (StringUtils.hasText(dto.startPeriod())) {
            dataSet.setTemporal(new Temporal()
                    .startPeriod(dto.startPeriod())
                    .endPeriod(dto.endPeriod()));
        }


        if (StringUtils.hasText(dto.structureUri())) {
            dataSet.setStructure(new Structure()
                    .uri(dto.structureUri())
                    .id(dto.structureId())
                    .dsd(dto.dsd()));
        }

        if (StringUtils.hasText(dto.creators())) {
            dataSet.setCreator(Arrays.stream(dto.creators().split("\\|"))
                    .filter(s -> !s.isBlank())
                    .map(raw -> {
                        String[] parts = raw.split("\\$", -1);
                        return new IdLabel()
                                .id(parts.length > 0 ? parts[0] : null)
                                .label(createList(
                                        createLangField(parts.length > 1 ? parts[1] : null, lg1),
                                        createLangField(parts.length > 2 ? parts[2] : null, lg2)));
                    })
                    .toList());
        }

        if (StringUtils.hasText(dto.operationStat())) {
            dataSet.setWasGeneratedBy(Arrays.stream(dto.operationStat().split(","))
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
                        createLangField(dto.derivedDescriptionLg1(),lg1),
                        createLangField(dto.derivedDescriptionLg2(), lg2)));
            }
            if (dto.derivedDescriptionLg1() != null && dto.derivedDescriptionLg1().isBlank()) {
                wasDerivedFrom.setDescription(createList(
                        createLangField(dto.derivedDescriptionLg1(),lg1),
                        createLangField("", lg2)));
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
                    createLangField(temporalResolutionContenu.labeltemporalResolutionLg1(),lg1),
                    createLangField(temporalResolutionContenu.labeltemporalResolutionLg2(),lg2)
            );
            Label temporalResolutionLabel = new Label();
            temporalResolutionLabel.setLabel(temporalResolutionTitles);
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
                    createLangField(spatialResolutionContenu.labelSpatialResolutionLg1(),lg1),
                    createLangField(spatialResolutionContenu.labelSpatialResolutionLg2(),lg2)
            );
            IdLabel spatialResolutionLabel = new IdLabel();
            spatialResolutionLabel.setLabel(spatialResolutionTitles);
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
                    createLangField(statisticalUnitsContenu.labelStatisticalUnitLg1(),lg1),
                    createLangField(statisticalUnitsContenu.labelStatisticalUnitLg2(),lg2)
            );
            IdLabel statisticalUnitLabel = new IdLabel();
            statisticalUnitLabel.setLabel(statisticalUnitTitles);
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
                    createLangField(themeDTO.labelThemeLg1(),lg1),
                    createLangField(themeDTO.labelThemeLg2(),lg2)
            );
            Theme themeLabel = new Theme();
            themeLabel.setLabel(themeTitles);
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
                        createLangField(first.titleLg1(), lg1),
                        createLangField(first.titleLg2(), lg2)));
            }
            if (StringUtils.hasText(first.descriptionLg1())) {
                d.setDescription(createList(
                        createLangField(first.descriptionLg1(), lg1),
                        createLangField(first.descriptionLg2(), lg2)));
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