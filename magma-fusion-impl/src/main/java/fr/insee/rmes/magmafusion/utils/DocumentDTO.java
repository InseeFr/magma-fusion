package fr.insee.rmes.magmafusion.utils;

public record DocumentDTO(
        String uri,
        String url,
        String labelLg1,
        String labelLg2,
        String commentLg1,
        String commentLg2,
        String dateMiseAJour,
        String langue,
        String taille
) {

}
