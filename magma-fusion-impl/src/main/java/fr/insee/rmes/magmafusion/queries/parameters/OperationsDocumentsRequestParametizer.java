package fr.insee.rmes.magmafusion.queries.parameters;

public record OperationsDocumentsRequestParametizer(String idSims, String idRubric, String LANG)
        implements ParametersForQueryDiffusion<OperationsDocumentsRequestParametizer> {
}

