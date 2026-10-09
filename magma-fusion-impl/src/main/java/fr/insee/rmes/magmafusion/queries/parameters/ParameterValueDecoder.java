package fr.insee.rmes.magmafusion.queries.parameters;

import fr.insee.rmes.magmafusion.model.*;

import java.lang.reflect.RecordComponent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.function.Function;

@FunctionalInterface
public interface ParameterValueDecoder<T> {


    String decode(T value);
    String LOCALE_DATE_CLASS = "java.time.LocalDate";
    String STRING_CLASS = "java.lang.String";
    String BOOLEAN_CLASS = "java.lang.Boolean";
    String BOOLEAN2_CLASS = "boolean";
    String INTEGER_CLASS = "java.lang.Integer";
    String CLASS_CLASS = "java.lang.Class";
    String ENUM_DESCENDANTS_AIREDATTRACTIONDESVILLES_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumDescendantsAireDAttractionDesVilles";
    String ENUM_DESCENDANTS_ARRONDISSEMENT_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumDescendantsArrondissement";
    String ENUM_ASCENDANTS_ARRONDISSEMENT_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumAscendantsArrondissement";
    String ENUM_ASCENDANTS_ARRONDISSEMENTMUNICIPAL_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumAscendantsArrondissementMunicipal";
    String ENUM_DESCENDANTS_BASSINDEVIE_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumDescendantsBassinDeVie";
    String ENUM_ASCENDANTS_CANTON_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCanton";
    String ENUM_ASCENDANTS_CANTONOUVILLE_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCantonOuVille";
    String ENUM_DESCENDANTS_CANTONOUVILLE_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumDescendantsCantonOuVille";
    String ENUM_ASCENDANTS_CIRCONSCRIPTIONTERRITORIALE_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCirconscriptionTerritoriale";
    String ENUM_DESCENDANTS_COLLECTIVITEDOUTREMER_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumDescendantsCollectiviteDOutreMer";
    String ENUM_ASCENDANTS_COMMUNEASSOCIEE_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCommuneAssociee";
    String ENUM_ASCENDANTS_COMMUNEDELEGUEE_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCommuneDeleguee";
    String ENUM_ASCENDANTS_DISTRICT_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumAscendantsDistrict";
    String ENUM_DESCENDANTS_DEPARTEMENT_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumDescendantsDepartement";
    String ENUM_ASCENDANTS_DEPARTEMENT_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumAscendantsDepartement";
    String ENUM_DESCENDANTS_COMMUNE_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumDescendantsCommune";
    String ENUM_ASCENDANTS_COMMUNE_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCommune";
    String ENUM_DESCENDANTS_INTERCOMMUNALITE_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumDescendantsIntercommunalite";
    String ENUM_ASCENDANTS_IRIS_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumAscendantsIris";
    String ENUM_DESCENDANTS_PAYS_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumDescendantsPays";
    String ENUM_DESCENDANTS_REGION_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumDescendantsRegion";
    String ENUM_DESCENDANTS_UNITEURBAINE_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumDescendantsUniteUrbaine";
    String ENUM_DESCENDANTS_ZONEDEMPLOI_CLASS = "fr.insee.rmes.magmafusion.model.TypeEnumDescendantsZoneDEmploi";
    String ENUM_TERRITOIRE_LIE = "fr.insee.rmes.magmafusion.model.TypeEnum";
    /**
     * Class for custom decoder in {@link ParametersForQuery} records such as {@link AscendantsDescendantsRequestParametizer} which
     * need to customize decoder for a type which has yet a standard decoder in the children of ParameterValueDecoder
     * <p>
     * For example {@link AscendantsDescendantsRequestParametizer} needs a custom treatment for Strings for attribute <code>filtreNom</code>.
     * So it overrides the method {@link  ParametersForQuery#findParameterValueDecoder(RecordComponent)} and for the attribute
     * <code>filtreNom</code>, it returns its custom decoder with this kind of code :
     * <code>
     * if ("filtreNom".equals(recordComponent.getName())){
     * return new ParameterValueDecoder.DelegaterDecoder<String>(value -> value ==null?"*": value);
     * }
     * </code>
     * <p>
     * The DelegaterDecoder is never be returned by
     * { @link fr.insee.rmes.magma.queries.parameters.ParameterValueDecoder#of(java.lang.Class)} and can only be
     * used when explicitly instanced in a method such as {@link AscendantsDescendantsRequestParametizer#findParameterValueDecoder(RecordComponent)}
     *
     * @param delegatedDecoder : a function applied to decode the value
     * @param <U>              : the type for which the instance will decode
     */
    record DelegaterDecoder<U>(Function<U, String> delegatedDecoder) implements ParameterValueDecoder<U> {
        @Override
        public String decode(U value) {
            return delegatedDecoder.apply(value);
        }
    }



    static <U> ParameterValueDecoder<U> of(Class<U> type) {
        return switch (type.getName()) {
            case LOCALE_DATE_CLASS -> localDate -> String.valueOf(localDate == null ? LocalDate.now(ZoneId.systemDefault()) : localDate);
            case STRING_CLASS -> s -> s == null ? "" : (String) s;
            case BOOLEAN_CLASS -> b -> b == null ? "false" : String.valueOf(b);
            case BOOLEAN2_CLASS -> bool -> (boolean) bool ? "true" : "false";
            case INTEGER_CLASS -> i -> i == null ? "0" : String.valueOf(i);
            case CLASS_CLASS -> clazz -> ((Class<?>) clazz).getSimpleName();
            case ENUM_DESCENDANTS_AIREDATTRACTIONDESVILLES_CLASS,
                 ENUM_DESCENDANTS_ARRONDISSEMENT_CLASS,
                 ENUM_ASCENDANTS_ARRONDISSEMENT_CLASS,
                 ENUM_ASCENDANTS_ARRONDISSEMENTMUNICIPAL_CLASS,
                 ENUM_DESCENDANTS_BASSINDEVIE_CLASS,
                 ENUM_ASCENDANTS_CANTON_CLASS,
                 ENUM_ASCENDANTS_CANTONOUVILLE_CLASS,
                 ENUM_DESCENDANTS_CANTONOUVILLE_CLASS,
                 ENUM_ASCENDANTS_CIRCONSCRIPTIONTERRITORIALE_CLASS,
                 ENUM_DESCENDANTS_COLLECTIVITEDOUTREMER_CLASS,
                 ENUM_ASCENDANTS_COMMUNE_CLASS,
                 ENUM_DESCENDANTS_COMMUNE_CLASS,
                 ENUM_ASCENDANTS_COMMUNEASSOCIEE_CLASS,
                 ENUM_ASCENDANTS_COMMUNEDELEGUEE_CLASS,
                 ENUM_ASCENDANTS_DISTRICT_CLASS,
                 ENUM_DESCENDANTS_DEPARTEMENT_CLASS,
                 ENUM_ASCENDANTS_DEPARTEMENT_CLASS,
                 ENUM_DESCENDANTS_INTERCOMMUNALITE_CLASS,
                 ENUM_ASCENDANTS_IRIS_CLASS,
                 ENUM_DESCENDANTS_PAYS_CLASS,
                 ENUM_DESCENDANTS_REGION_CLASS,
                 ENUM_DESCENDANTS_UNITEURBAINE_CLASS,
                 ENUM_DESCENDANTS_ZONEDEMPLOI_CLASS,
                 ENUM_TERRITOIRE_LIE ->
                    enumValue -> enumValue == null ? "none" : invokeGetValue(enumValue);
            case String _ when Enum.class.isAssignableFrom(type) -> simpleEnum -> ((Enum<?>) simpleEnum).name();
            default -> throw new IllegalArgumentException("Unsupported type: " + type.getName());
        };
    }

    private static String invokeGetValue(Object enumValue) {
        try {
            return (String) enumValue.getClass().getMethod("getValue").invoke(enumValue);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("No accessible getValue() on " + enumValue.getClass(), e);
        }
    }

}
