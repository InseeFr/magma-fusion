package fr.insee.rmes.magmafusion.utils;

public record NomenclatureDTO(
        String classification,
        String id,
        String prefLabelLg1,
        String prefLabelLg2,
        String altLabelsLg1,
        String altLabelsLg2,
        String descriptionLg1,
        String descriptionLg2,
        String issued,
        String lastRefreshedOn
) {
}
