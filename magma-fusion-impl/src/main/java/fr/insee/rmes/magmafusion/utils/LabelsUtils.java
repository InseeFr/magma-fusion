package fr.insee.rmes.magmafusion.utils;

import fr.insee.rmes.magmafusion.model.LocalisedContenu;
import fr.insee.rmes.magmafusion.model.LocalisedUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class LabelsUtils {
    private LabelsUtils() {
        /* This utility class should not be instantiated */
    }

    //used to create fields Langue+contenu or langue+url or id+label
    public static <L> List<L> createList(L... langues) {
        log.debug("Création d'une liste à partir de {} éléments", langues.length);
        List<L> result = Arrays.stream(langues)
                .filter(Objects::nonNull)
                .toList();
        log.debug("Liste créée avec {} éléments non nuls", result.size());
        return result;
    }

    public static LocalisedContenu createLangField(String contenu, String langue) {
        log.debug("Création d'un LocalisedContenu: contenu='{}', langue='{}'", contenu, langue);
        if (contenu == null || langue == null) {
            log.warn("Paramètres null détectés : contenu={}, langue={}", contenu, langue);
        }
        LocalisedContenu langueContenu = new LocalisedContenu();
        langueContenu.setContenu(contenu);
        langueContenu.setLangue(langue);
        return langueContenu;
    }

    public static LocalisedUrl createLangUrl(String url, String langue) {
        log.debug("Création d'un LocalisedUrl: url='{}', langue='{}'", url, langue);
        if (url == null || langue == null) {
            log.warn("Paramètres null détectés: url={}, langue={}", url, langue);
        }
        LocalisedUrl langueUrl = new LocalisedUrl();
        langueUrl.setUrl(url);
        langueUrl.setLangue(langue);
        return langueUrl;
    }

    public static List<LocalisedContenu> buildLocalisedLabels(String lg1Field, String lg2Field, String lg1, String lg2) {
        log.debug(
                "Localised labels building : lg1Field='{}', lg2Field='{}', lg1='{}', lg2='{}'",
                lg1Field, lg2Field, lg1, lg2
        );
        List<LocalisedContenu> result = createList(
                StringUtils.hasText(lg1Field) ? createLangField(lg1Field, lg1) : null,
                StringUtils.hasText(lg2Field) ? createLangField(lg2Field, lg2) : null
        );
        if (result.isEmpty()) {
            log.info("No valid localised label generated (lg1Field and lg2Field are empty or null))");
            return null;
        }

        log.debug("{} localised label generated", result.size());
        return result;
    }

    public static List<LocalisedUrl> buildLocalisedUrls(String lg1Field, String lg2Field, String lg1, String lg2) {
        log.debug(
                "Building localised URLs : lg1Field='{}', lg2Field='{}', lg1='{}', lg2='{}'",
                lg1Field, lg2Field, lg1, lg2
        );
        if (lg1Field == null && lg2Field == null) {
            log.info("No localised URLs generated (lg1Field and lg2Field are null)");
            return null;
        }
        List<LocalisedUrl> result = createList(
            StringUtils.hasText(lg1Field) ? createLangUrl(lg1Field, lg1) : null,
            StringUtils.hasText(lg2Field) ? createLangUrl(lg2Field, lg2) : null
            );
        log.debug("{} localised URLs generated", result.size());
        return result;
    }
}
