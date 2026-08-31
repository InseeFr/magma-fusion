package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.model.Indicateur;
import fr.insee.rmes.magmafusion.model.Operation;
import fr.insee.rmes.magmafusion.model.Serie;
import fr.insee.rmes.magmafusion.utils.IndicateurDTO;
import fr.insee.rmes.magmafusion.utils.OperationDTO;
import fr.insee.rmes.magmafusion.utils.SeriesDTO;

import java.util.List;

public interface SeriesOperationsService {
    Serie convertSeriesDTOToSerieById(SeriesDTO seriesDTO);
    Operation convertOperationDTOToOperation(OperationDTO dto);
    List<Serie> convertSeriesDTOsToSeries(List<SeriesDTO> dtos);
    Indicateur convertIndicateurDTOToIndicateur(IndicateurDTO indicateurDTO);
}