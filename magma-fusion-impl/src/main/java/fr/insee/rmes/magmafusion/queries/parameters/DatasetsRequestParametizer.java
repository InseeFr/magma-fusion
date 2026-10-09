package fr.insee.rmes.magmafusion.queries.parameters;

public record DatasetsRequestParametizer(String id, String uri, String date) implements ParametersForQuery<DatasetsRequestParametizer> {

    // for getListDatasets (no filter)
    public DatasetsRequestParametizer() {
        this(null, null, null);
    }

    // for getListDatasetsFilterByDate
    public static DatasetsRequestParametizer ofDate(String date){
        return new DatasetsRequestParametizer(null, null, date);
    }

    public static DatasetsRequestParametizer ofId(String id){
        return new DatasetsRequestParametizer(id, null, null);
    }

    // for getDatasetByIdTemporalResolution
    public static DatasetsRequestParametizer ofUri(String uri){
        return new DatasetsRequestParametizer(null, uri, null);
    }
}