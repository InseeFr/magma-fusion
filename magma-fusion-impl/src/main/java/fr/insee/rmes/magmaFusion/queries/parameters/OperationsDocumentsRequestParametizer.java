package fr.insee.rmes.magmaFusion.queries.parameters;

public record OperationsDocumentsRequestParametizer(String idSims, String idRubric, String LANG)
        implements ParametersForQueryDiffusion<OperationsDocumentsRequestParametizer> {
}

