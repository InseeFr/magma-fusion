package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.model.NomenclatureForList;
import fr.insee.rmes.magmafusion.utils.NomenclatureForListDTO;

public interface NomenclatureService {
    NomenclatureForList convertNomenclatureDtoforList(NomenclatureForListDTO nomenclatureForListDTO);
}
