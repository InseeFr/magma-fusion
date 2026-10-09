package fr.insee.rmes.magmafusion.queries.parameters;

public record SeriesOperationsRequestParametizer(String idSeries, String operationId, String date)
        implements ParametersForQuery<SeriesOperationsRequestParametizer> {

    public SeriesOperationsRequestParametizer(String idSeries, String operationId) {
        this(idSeries, operationId, "");
    }
}