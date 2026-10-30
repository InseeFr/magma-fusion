package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.model.NomenclatureForList;
import fr.insee.rmes.magmafusion.utils.NomenclatureForListDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static fr.insee.rmes.magmafusion.utils.LabelsUtils.*;


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
}