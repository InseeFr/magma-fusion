package fr.insee.rmes.magmafusion.services;


import fr.insee.rmes.magmafusion.model.Concept;
import fr.insee.rmes.magmafusion.model.ConceptForList;
import fr.insee.rmes.magmafusion.utils.ConceptDTO;

public interface ConceptService {
    Concept convertConceptDTOToConcept(ConceptDTO conceptDTO);
    ConceptForList convertConceptDTOToDefinition(ConceptDTO conceptDTO);
}