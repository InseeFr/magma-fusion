package fr.insee.rmes.magmafusion.queries.parameters;

public record OperationRubriquesRequestParametizer(String idSims, String LG1_CL, String LG2_CL)
        implements ParametersForQueryDiffusion<OperationRubriquesRequestParametizer> {

}