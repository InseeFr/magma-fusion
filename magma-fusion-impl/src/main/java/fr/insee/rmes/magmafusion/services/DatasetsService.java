package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.model.Dataset;
import fr.insee.rmes.magmafusion.model.DatasetForList;
import fr.insee.rmes.magmafusion.model.Distribution;
import fr.insee.rmes.magmafusion.utils.DatasetByIdDTO;
import fr.insee.rmes.magmafusion.utils.DatasetForListDTO;
import fr.insee.rmes.magmafusion.utils.DistributionDTO;

import java.util.List;

public interface DatasetsService {

    List<DatasetForList> convertDatasetDTOsToDataSets(List<DatasetForListDTO> dtos);

    Dataset convertDatasetByIdDTOToDataSet(DatasetByIdDTO dto);

    List<Distribution> convertDistributionDTOsToDistributions(List<DistributionDTO> dtos);

}