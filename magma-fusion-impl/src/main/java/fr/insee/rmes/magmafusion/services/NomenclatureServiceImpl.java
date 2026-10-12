package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.model.NomenclatureForList;
import fr.insee.rmes.magmafusion.utils.NomenclatureForListDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static fr.insee.rmes.magmafusion.utils.LabelsUtils.createLangField;
import static fr.insee.rmes.magmafusion.utils.LabelsUtils.createList;


@Service
@Slf4j
public class NomenclatureServiceImpl implements NomenclatureService{

    @Value("${fr.insee.rmes.magmafusion.lg1}")
    private String lg1;

    @Value("${fr.insee.rmes.magmafusion.lg2}")
    private String lg2;

    public NomenclatureForList convertNomenclatureDtoforList(NomenclatureForListDTO nomenclatureForListDTO){
        NomenclatureForList nomenclatureForList = new NomenclatureForList();
        nomenclatureForList.setNomenclature(nomenclatureForListDTO.id());
        nomenclatureForList.setUri(nomenclatureForListDTO.classification());
        nomenclatureForList.setIntitule(createList(
                createLangField(nomenclatureForListDTO.prefLabelLg1(),lg1),
                createLangField(nomenclatureForListDTO.prefLabelLg2(),lg2)
                )
        );
        return nomenclatureForList;
    }
}