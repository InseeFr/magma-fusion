package fr.insee.rmes.magmaFusion.queries.parameters;

import java.lang.reflect.RecordComponent;
import java.time.LocalDate;


public record TerritoiresLiesRequestParametizer(String code,
                                                LocalDate date,
                                                String territoriesFilter,
                                                Class<?> typeOrigine) implements ParametersForQueryDiffusion<TerritoiresLiesRequestParametizer> {


    @Override
    public ParameterValueDecoder<?> findParameterValueDecoder(RecordComponent recordComponent) {
        return ParametersForQueryDiffusion.super.findParameterValueDecoder(recordComponent);
    }
}