package fr.insee.rmes.magmaFusion.services;

import fr.insee.rmes.magmaFusion.model.Indicateur;
import fr.insee.rmes.magmaFusion.model.Operation;
import fr.insee.rmes.magmaFusion.model.Serie;
import fr.insee.rmes.magmaFusion.utils.IndicateurDTO;
import fr.insee.rmes.magmaFusion.utils.OperationDTO;
import fr.insee.rmes.magmaFusion.utils.SeriesDTO;

import java.util.List;

public interface SeriesOperationsService {
    Serie transformSeriesDTOToSerieById(SeriesDTO seriesDTO);
    Operation transformOperationDTOToOperation(OperationDTO dto);
    List<Serie> transformSeriesDTOsToSeries(List<SeriesDTO> dtos);
    Indicateur transformIndicateurDTOToIndicateur(IndicateurDTO indicateurDTO);
}