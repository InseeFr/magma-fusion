package fr.insee.rmes.magmaFusion.services;

import fr.insee.rmes.magmaFusion.model.RapportQualite;
import fr.insee.rmes.magmaFusion.utils.RapportQualiteDTO;

public interface RapportQualiteService {
    RapportQualite convertDTOToRapportQualite(RapportQualiteDTO rapportQualiteDTO);
}
