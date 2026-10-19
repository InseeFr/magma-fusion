package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.model.IdUriLabel;
import fr.insee.rmes.magmafusion.model.LocalisedContenu;
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

import static fr.insee.rmes.magmafusion.utils.LabelsUtils.createLangField;
import static fr.insee.rmes.magmafusion.utils.LabelsUtils.createList;


@Service
@Slf4j
public class NomenclatureServiceImpl implements NomenclatureService {

    @Value("${fr.insee.rmes.magmafusion.lg1}")
    private String lg1;

    @Value("${fr.insee.rmes.magmafusion.lg2}")
    private String lg2;

    public NomenclatureForList convertNomenclatureDtoforList(NomenclatureForListDTO nomenclatureForListDTO) {
        NomenclatureForList nomenclatureForList = new NomenclatureForList();
        nomenclatureForList.setNomenclature(nomenclatureForListDTO.id());
        nomenclatureForList.setUri(nomenclatureForListDTO.classification());
        nomenclatureForList.setIntitule(createList(
                        createLangField(nomenclatureForListDTO.prefLabelLg1(), lg1),
                        createLangField(nomenclatureForListDTO.prefLabelLg2(), lg2)
                )
        );
        return nomenclatureForList;
    }

    public Nomenclature convertNomenclatureDto(NomenclatureDTO nomenclatureDTO, List<NiveauDTO> niveauxDTO) {
        Nomenclature nomenclature = new Nomenclature();
        nomenclature.setNomenclature(nomenclatureDTO.id());
        nomenclature.setUri(nomenclatureDTO.classification());
        nomenclature.setIntitule(createList(
                createLangField(nomenclatureDTO.prefLabelLg1(), lg1),
                createLangField(nomenclatureDTO.prefLabelLg2(), lg2))
        );
        if (StringUtils.hasText(nomenclatureDTO.altLabelsLg1())) {
            List<String> altLabelsLg1 = List.of(nomenclatureDTO.altLabelsLg1().split(","));
            List<LocalisedContenu> altLabelsLg1List = getAltLabels(altLabelsLg1, lg1);
            nomenclature.setIntituleAlternatif(altLabelsLg1List);
        }
        if (StringUtils.hasText(nomenclatureDTO.altLabelsLg2())) {
            List<String> altLabelsLg2 = List.of(nomenclatureDTO.altLabelsLg2().split(","));
            List<LocalisedContenu> altLabelsLg2List = getAltLabels(altLabelsLg2, lg2);
            nomenclature.setIntituleAlternatif(altLabelsLg2List);
        }
        if (StringUtils.hasText(nomenclatureDTO.descriptionLg1()) && StringUtils.hasText(nomenclatureDTO.descriptionLg2())) {
            nomenclature.setDescription(createList(
                    createLangField(nomenclatureDTO.descriptionLg1(), lg1),
                    createLangField(nomenclatureDTO.descriptionLg2(), lg2))
            );
        }
        if (StringUtils.hasText(nomenclatureDTO.descriptionLg1()) & !StringUtils.hasText(nomenclatureDTO.descriptionLg2())) {
            nomenclature.setDescription(createList(
                    createLangField(nomenclatureDTO.descriptionLg1(), lg1))
            );
        }
        addNiveaux(niveauxDTO,nomenclature);
        return nomenclature;
    }

    private void addNiveaux(List<NiveauDTO> niveauxDTO, Nomenclature nomenclature) {
        if (niveauxDTO == null || nomenclature == null) {
            return;
        }
        for (NiveauDTO niveauDTO : niveauxDTO) {
                IdUriLabel niveau = new IdUriLabel();
                niveau.setId(niveauDTO.idLevel());
                niveau.setUri(URI.create(niveauDTO.level()));
                if (StringUtils.hasText(niveauDTO.prefLabelLevelLg1()) && StringUtils.hasText(niveauDTO.prefLabelLevelLg2())) {
                    niveau.setLabel(createList(
                        createLangField(niveauDTO.prefLabelLevelLg1(), lg1),
                        createLangField(niveauDTO.prefLabelLevelLg2(), lg2)
                            )
                        );
                };
                if (StringUtils.hasText(niveauDTO.prefLabelLevelLg1()) & !StringUtils.hasText(niveauDTO.prefLabelLevelLg2())) {
                    niveau.setLabel(createList(
                        createLangField(niveauDTO.prefLabelLevelLg1(), lg1)
                        )
                    );
                };
                nomenclature.addNiveauxItem(niveau);
        }
    }

    private List<LocalisedContenu> getAltLabels (List<String> altLabels,String lang){
            List<LocalisedContenu> altLabelList = new ArrayList<>();
            for (String altLabel: altLabels) {
                LocalisedContenu intituleAlternatif = createLangField(altLabel, lang);
                altLabelList.add(intituleAlternatif);
            }
            return altLabelList;
        }
    }

