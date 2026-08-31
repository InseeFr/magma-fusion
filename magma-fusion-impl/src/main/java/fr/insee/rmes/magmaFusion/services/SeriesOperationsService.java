package fr.insee.rmes.magmaFusion.services;

import fr.insee.rmes.magmaFusion.model.Indicateur;
import fr.insee.rmes.magmaFusion.model.Operation;
import fr.insee.rmes.magmaFusion.model.Serie;
import fr.insee.rmes.magmaFusion.utils.IndicateurDTO;
import fr.insee.rmes.magmaFusion.utils.OperationDTO;
import fr.insee.rmes.magmaFusion.utils.SeriesDTO;

import java.util.List;

public interface SeriesOperationsService {
    Serie convertSeriesDTOToSerieById(SeriesDTO seriesDTO);
    Operation convertOperationDTOToOperation(OperationDTO dto);
    List<Serie> convertSeriesDTOsToSeries(List<SeriesDTO> dtos);
    Indicateur convertIndicateurDTOToIndicateur(IndicateurDTO indicateurDTO);
}