package fr.insee.rmes.magmafusion.queries.parameters;

public record ClassificationRequestParametizer(String nomenclature,
                                               String niveau,
                                               String code) implements ParametersForQuery<ClassificationRequestParametizer> {

}
