package fr.insee.rmes.magmaFusion.queries.parameters;

public record ClassificationRequestParametizer(String nomenclature,
                                               String niveau,
                                               String code) implements ParametersForQueryDiffusion<ClassificationRequestParametizer> {

}
