package fr.insee.rmes.magmaFusion.queries.parameters;

public record OperationRubriquesRequestParametizer(String idSims, String LG1_CL, String LG2_CL)
        implements ParametersForQueryDiffusion<OperationRubriquesRequestParametizer> {

}