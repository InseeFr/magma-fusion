package fr.insee.rmes.magmaFusion.services;

import fr.insee.rmes.magmaFusion.model.Dataset;
import fr.insee.rmes.magmaFusion.model.Distribution;
import fr.insee.rmes.magmaFusion.utils.DatasetByIdDTO;
import fr.insee.rmes.magmaFusion.utils.DatasetDTO;
import fr.insee.rmes.magmaFusion.utils.DistributionDTO;

import java.util.List;

public interface DatasetsService {

    List<Dataset> convertDatasetDTOsToDataSets(List<DatasetDTO> dtos);

    Dataset convertDatasetByIdDTOToDataSet(DatasetByIdDTO dto);

    List<Distribution> convertDistributionDTOsToDistributions(List<DistributionDTO> dtos);

}