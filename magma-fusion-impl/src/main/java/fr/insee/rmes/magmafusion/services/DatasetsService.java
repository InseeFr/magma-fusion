package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.model.Dataset;
import fr.insee.rmes.magmafusion.model.Distribution;
import fr.insee.rmes.magmafusion.utils.DatasetByIdDTO;
import fr.insee.rmes.magmafusion.utils.DatasetDTO;
import fr.insee.rmes.magmafusion.utils.DistributionDTO;

import java.util.List;

public interface DatasetsService {

    List<Dataset> convertDatasetDTOsToDataSets(List<DatasetDTO> dtos);

    Dataset convertDatasetByIdDTOToDataSet(DatasetByIdDTO dto);

    List<Distribution> convertDistributionDTOsToDistributions(List<DistributionDTO> dtos);

}