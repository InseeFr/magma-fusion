package fr.insee.rmes.magmafusion.queries.parameters;

import java.lang.reflect.RecordComponent;
import java.time.LocalDate;

public record PrecedentsSuivantsRequestParametizer(String code,
                                                   LocalDate date,
                                                   Class<?> typeOrigine,
                                                   boolean previous) implements ParametersForQuery<PrecedentsSuivantsRequestParametizer> {


    @Override
    public ParameterValueDecoder<Object> findParameterValueDecoder(RecordComponent recordComponent) {
        return ParametersForQuery.super.findParameterValueDecoder(recordComponent);
    }
}

