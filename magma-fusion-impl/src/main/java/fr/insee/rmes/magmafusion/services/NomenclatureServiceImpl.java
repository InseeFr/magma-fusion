package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.model.LocalisedContenu;
import fr.insee.rmes.magmafusion.model.NiveauUriIntitule;
import fr.insee.rmes.magmafusion.model.Nomenclature;
import fr.insee.rmes.magmafusion.model.NomenclatureForList;
import fr.insee.rmes.magmafusion.utils.NiveauDTO;
import fr.insee.rmes.magmafusion.utils.NomenclatureDTO;
import fr.insee.rmes.magmafusion.utils.NomenclatureForListDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static fr.insee.rmes.magmafusion.utils.DatesUtils.tryParseDateToLocalDate;
import static fr.insee.rmes.magmafusion.utils.LocatingFieldsConverter.buildLocalisedLabels;
import static fr.insee.rmes.magmafusion.utils.LocatingFieldsConverter.createLangField;


@Service
@Slf4j
public class NomenclatureServiceImpl implements NomenclatureService{

    private final String lg1;
    private final String lg2;

    public NomenclatureServiceImpl(
            @Value("${fr.insee.rmes.magmafusion.lg1}") String lg1,
            @Value("${fr.insee.rmes.magmafusion.lg2}") String lg2
    ) {
        this.lg1 = lg1;
        this.lg2 = lg2;
    }

    public NomenclatureForList convertNomenclatureDtoforList(NomenclatureForListDTO nomenclatureForListDTO){
        NomenclatureForList nomenclatureForList = new NomenclatureForList();
        nomenclatureForList.setNomenclature(nomenclatureForListDTO.id());
        nomenclatureForList.setUri(nomenclatureForListDTO.classification());
        nomenclatureForList.setIntitule(buildLocalisedLabels(nomenclatureForListDTO.prefLabelLg1(),nomenclatureForListDTO.prefLabelLg2(),this.lg1, this.lg2));
        return nomenclatureForList;
    }

    public Nomenclature convertNomenclatureDto(NomenclatureDTO nomenclatureDTO, List<NiveauDTO> niveauxDTO) {
        Nomenclature nomenclature = new Nomenclature();
        nomenclature.setNomenclature(nomenclatureDTO.id());
        nomenclature.setUri(nomenclatureDTO.classification());
        setIntitules(nomenclature, nomenclatureDTO, lg1, lg2);
        setAlternativeLabels(nomenclature, nomenclatureDTO, lg1, lg2);

        nomenclature.setDescription(
            Optional.ofNullable(buildLocalisedLabels(nomenclatureDTO.descriptionLg1(),nomenclatureDTO.descriptionLg2(), lg1, lg2))
                .map(list -> list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList()))
                .orElse(null)
        );

        nomenclature.setDatePublication(tryParseDateToLocalDate(nomenclatureDTO.issued()));
        nomenclature.setDateMiseAJour(tryParseDateToLocalDate(nomenclatureDTO.lastRefreshedOn()));
        addNiveaux(niveauxDTO,nomenclature);
        return nomenclature;
    }


    protected void setIntitules(Nomenclature nomenclature, NomenclatureDTO nomenclatureDTO, String lg1, String lg2){
        List<LocalisedContenu> intituleslList = new ArrayList<>();

        processIntitules(nomenclatureDTO.prefLabelLg1(), lg1, intituleslList);
        processIntitules(nomenclatureDTO.prefLabelLg2(), lg2, intituleslList);

        if (!intituleslList.isEmpty()) {
            nomenclature.setIntitule(intituleslList);
        }
    }
    private void processIntitules(String intitules, String language, List<LocalisedContenu> targetList) {
        if (StringUtils.hasText(intitules)) {
            for (String intitle : intitules.split(",")) {
                LocalisedContenu intitule = createLangField(intitle.trim(), language);
                targetList.add(intitule);
            }
        }
    }

    protected void setAlternativeLabels(Nomenclature nomenclature, NomenclatureDTO nomenclatureDTO, String lg1, String lg2){
        List<LocalisedContenu> altLabelList = new ArrayList<>();

        processAltLabels(nomenclatureDTO.altLabelsLg1(), lg1, altLabelList);
        processAltLabels(nomenclatureDTO.altLabelsLg2(), lg2, altLabelList);

        if (!altLabelList.isEmpty()) {
            nomenclature.setIntituleAlternatif(altLabelList);
        }
    }

    private void processAltLabels(String altLabels, String language, List<LocalisedContenu> targetList) {
        if (StringUtils.hasText(altLabels)) {
            for (String altLabel : altLabels.split(",")) {
                LocalisedContenu intituleAlternatif = createLangField(altLabel.trim(), language);
                targetList.add(intituleAlternatif);
            }
        }
    }

    protected void addNiveaux(List<NiveauDTO> niveauxDTO, Nomenclature nomenclature) {
        if (niveauxDTO == null || nomenclature == null) {
            return;
        }
        for (NiveauDTO niveauDTO : niveauxDTO) {
            NiveauUriIntitule niveau = new NiveauUriIntitule();
            niveau.setNiveau(niveauDTO.idLevel());
            niveau.setUri(URI.create(niveauDTO.level()));
            niveau.setIntitule(buildLocalisedLabels(niveauDTO.prefLabelLevelLg1(),niveauDTO.prefLabelLevelLg2(),lg1,lg2)
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())
            );
        nomenclature.addNiveauxItem(niveau);
        }
    }
}


