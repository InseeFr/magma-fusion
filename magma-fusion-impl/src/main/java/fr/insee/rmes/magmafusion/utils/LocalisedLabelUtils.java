package fr.insee.rmes.magmafusion.utils;

import fr.insee.rmes.magmafusion.model.LocalisedUrl;
import fr.insee.rmes.magmafusion.model.LocalisedContenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LocalisedLabelUtils {
    private LocalisedLabelUtils() {
        /* This utility class should not be instantiated */
    }

    //used to create fiels Langue+contenu or langue+url
    public static <L> List<L> createListLangField(L... langues) {
        return Arrays.stream(langues)
                .filter(Objects::nonNull)
                .toList();
    }

    public static LocalisedContenu createLangField(String contenu, String langue) {
        LocalisedContenu langueContenu = new LocalisedContenu();
        langueContenu.setContenu(contenu);
        langueContenu.setLangue(langue);
        return langueContenu;
    }
}
