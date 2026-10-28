package fr.insee.rmes.magmafusion.utils;

import fr.insee.rmes.magmafusion.model.IdLabel;
import fr.insee.rmes.magmafusion.model.LocalisedContenu;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LabelsUtils {
    private LabelsUtils() {
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

    public static List<LocalisedContenu> buildLocalisedLabels(String lg1Field, String lg2Field) {
        if (lg1Field != null | lg2Field != null){
            return createList(
                    StringUtils.hasText(lg1Field) ? createLangField(lg1Field, "fr") : null,
                    StringUtils.hasText(lg2Field) ? createLangField(lg2Field, "en") : null
            );
        }
        else return null;
    }
}
