package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.model.RapportQualite;
import fr.insee.rmes.magmafusion.utils.RapportQualiteDTO;

public interface RapportQualiteService {
    RapportQualite convertDTOToRapportQualite(RapportQualiteDTO rapportQualiteDTO);
}
