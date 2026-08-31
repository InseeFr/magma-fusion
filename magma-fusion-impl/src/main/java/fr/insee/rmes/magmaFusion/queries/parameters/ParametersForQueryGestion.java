package fr.insee.rmes.magmaFusion.queries.parameters;

import java.lang.reflect.RecordComponent;

public interface ParametersForQueryGestion <E extends Record & ParametersForQuery<E>> extends ParametersForQuery<E> {

        default ParameterValueDecoder<?> findParameterValueDecoder(RecordComponent recordComponent){
            return ParameterValueDecoderGestion.of(recordComponent.getType());
        }


    }

