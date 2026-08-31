package fr.insee.rmes.magmaFusion.queries.parameters;


import java.lang.reflect.RecordComponent;

public interface ParametersForQueryDiffusion<E extends Record & ParametersForQuery<E>> extends ParametersForQuery<E> {

    default ParameterValueDecoder<?> findParameterValueDecoder(RecordComponent recordComponent){
        return ParameterValueDecoderDiffusion.of(recordComponent.getType());
    }


}
