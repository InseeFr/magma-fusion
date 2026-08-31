package fr.insee.rmes.magmaFusion.utils;

import fr.insee.rmes.magmaFusion.model.TypeEnumDescendantsZoneDEmploi;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class TypeEnumDescendantsZoneDEmploiConverter implements Converter<String, TypeEnumDescendantsZoneDEmploi> {
    @Override
    public TypeEnumDescendantsZoneDEmploi convert(String source) {
        for (TypeEnumDescendantsZoneDEmploi type : TypeEnumDescendantsZoneDEmploi.values()) {
            if (type.getValue().equalsIgnoreCase(source)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value for TypeEnumDescendantsZoneDEmploi: " + source);
    }
}
