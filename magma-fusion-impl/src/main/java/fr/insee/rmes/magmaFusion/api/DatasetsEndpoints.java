package fr.insee.rmes.magmaFusion.api;

import fr.insee.rmes.magmaFusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmaFusion.model.Dataset;
import fr.insee.rmes.magmaFusion.model.Distribution;
import fr.insee.rmes.magmaFusion.queries.parameters.DatasetsRequestParametizer;
import fr.insee.rmes.magmaFusion.services.DatasetsService;
import fr.insee.rmes.magmaFusion.utils.DatasetByIdDTO;
import fr.insee.rmes.magmaFusion.utils.DatasetDTO;
import fr.insee.rmes.magmaFusion.utils.DistributionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DatasetsEndpoints implements DatasetsApi {

    private final RequestProcessor requestProcessor;
    private final DatasetsService datasetsService;

    public DatasetsEndpoints(RequestProcessor requestProcessor, DatasetsService datasetsService) {
        this.requestProcessor = requestProcessor;
        this.datasetsService = datasetsService;
    }

    @Override
    public ResponseEntity<List<Dataset>> getListDatasets(@Nullable String dateMiseAJour) {
        List<DatasetDTO> dtos = requestProcessor.queryToFindAllDatasets()
                .with(new DatasetsRequestParametizer(dateMiseAJour))
                .executeQuery()
                .listResult(DatasetDTO.class)
                .result();

        List<Dataset> dataSets = datasetsService.transformDatasetDTOsToDataSets(dtos);
        return ResponseEntity.ok(dataSets);
    }

    @Override
    public ResponseEntity<Dataset> getDataSetById(String id) {
        DatasetByIdDTO dto = requestProcessor.queryToFindDatasetById()
                .with(new DatasetsRequestParametizer(id, null))
                .executeQuery()
                .singleResult(DatasetByIdDTO.class)
                .result();
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(datasetsService.transformDatasetByIdDTOToDataSet(dto));
    }

    @Override
    public ResponseEntity<List<Distribution>> getDataSetDistributionsById(String id) {
        List<DistributionDTO> dtos = requestProcessor.queryToFindDistributionsByDatasetId()
                .with(new DatasetsRequestParametizer(id, null))
                .executeQuery()
                .listResult(DistributionDTO.class)
                .result();
        return ResponseEntity.ok(datasetsService.transformDistributionDTOsToDistributions(dtos));
    }
}
