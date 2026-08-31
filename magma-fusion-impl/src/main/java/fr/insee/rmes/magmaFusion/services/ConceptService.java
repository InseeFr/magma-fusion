package fr.insee.rmes.magmaFusion.services;


import fr.insee.rmes.magmaFusion.model.Concept;
import fr.insee.rmes.magmaFusion.model.ConceptForList;
import fr.insee.rmes.magmaFusion.utils.ConceptDTO;

public interface ConceptService {
    Concept convertConceptDTOToConcept(ConceptDTO conceptDTO);
    ConceptForList convertConceptDTOToDefinition(ConceptDTO conceptDTO);
}