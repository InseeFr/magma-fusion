package fr.insee.rmes.magmafusion.utils;

import fr.insee.rmes.magmafusion.model.IdLabel;
import fr.insee.rmes.magmafusion.model.LocalisedContenu;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LocalisedLabelUtils {
    private LocalisedLabelUtils() {
        /* This utility class should not be instantiated */
    }

    //used to create fields Langue+contenu or langue+url or id+label
    public static <L> List<L> createList(L... langues) {
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

    public static IdLabel createIdLabelField(String id, List<LocalisedContenu> labels) {
        IdLabel idLabel = new IdLabel();
        idLabel.setId(id);
        idLabel.setLabel(labels);
        return idLabel;
    }
}
