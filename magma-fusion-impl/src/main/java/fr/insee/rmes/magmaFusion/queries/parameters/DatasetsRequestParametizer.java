package fr.insee.rmes.magmaFusion.queries.parameters;

public record DatasetsRequestParametizer(String id, String date) implements ParametersForQueryGestion<DatasetsRequestParametizer> {

    // for getListDatasets (no filter)
    public DatasetsRequestParametizer() {
        this(null, null);
    }

    // for getListDatasetsFilterByDate
    public DatasetsRequestParametizer(String date) {
        this(null, date);
    }
}