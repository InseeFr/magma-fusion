package fr.insee.rmes.magmafusion.utils;

import fr.insee.rmes.magmafusion.model.LocalisedContenu;
import fr.insee.rmes.magmafusion.model.NearbyConcept;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j


public record ConceptDTO(

    String id,
    String uri,
    String intituleLg1,
    String intituleLg2,
    String definitionLg1,
    String definitionLg2,
    String scopeNoteLg1,
    String scopeNoteLg2,
    String noteEditorialeLg1,
    String noteEditorialeLg2,
    String dateMiseAJour,
    String dateCreation,
    String dateFinDeValidite,
    @Getter
    Boolean hasLink,
    @Getter
    Boolean hasIntitulesAlternatifs,
    @Getter
    List<NearbyConcept> nearbyConcepts,
    List<LocalisedContenu> intitulesAlternatifs
)
{

   public ConceptDTO withDateCreation(String dateCreation) {
       return new ConceptDTO(id, uri, intituleLg1, intituleLg2, definitionLg1, definitionLg2, scopeNoteLg1, scopeNoteLg2, noteEditorialeLg1, noteEditorialeLg2, dateMiseAJour, dateCreation, dateFinDeValidite, hasLink, hasIntitulesAlternatifs, nearbyConcepts, intitulesAlternatifs);
   }

    public ConceptDTO withNearbyConcepts(List<NearbyConcept> nearbyConcepts) {
        return new ConceptDTO(id, uri, intituleLg1, intituleLg2, definitionLg1, definitionLg2, scopeNoteLg1, scopeNoteLg2, noteEditorialeLg1, noteEditorialeLg2, dateMiseAJour, dateCreation, dateFinDeValidite, hasLink, hasIntitulesAlternatifs, nearbyConcepts, intitulesAlternatifs);
    }

    public ConceptDTO withIntitulesAlternatifs(List<LocalisedContenu> intitulesAlternatifs) {
        return new ConceptDTO(id, uri, intituleLg1, intituleLg2, definitionLg1, definitionLg2, scopeNoteLg1, scopeNoteLg2, noteEditorialeLg1, noteEditorialeLg2, dateMiseAJour, dateCreation, dateFinDeValidite, hasLink, hasIntitulesAlternatifs, nearbyConcepts, intitulesAlternatifs);
    }

    public boolean hasLinkValue() {
        return Boolean.TRUE.equals(hasLink);
    }

    public boolean hasIntitulesAlternatifsValue(){
       return Boolean.TRUE.equals(hasIntitulesAlternatifs);
    }
}
