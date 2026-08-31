package fr.insee.rmes.magmaFusion.utils;


import fr.insee.rmes.magmaFusion.model.TypeEnumAscendantsIris;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class TypeEnumAscendantsIrisConverter implements Converter<String, TypeEnumAscendantsIris> {
    @Override
    public TypeEnumAscendantsIris convert(String source) {
        for (TypeEnumAscendantsIris type : TypeEnumAscendantsIris.values()) {
            if (type.getValue().equalsIgnoreCase(source)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value for TypeEnumAscendantsIris : " + source);
    }
}