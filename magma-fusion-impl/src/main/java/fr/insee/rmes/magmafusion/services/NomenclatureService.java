package fr.insee.rmes.magmafusion.services;

import fr.insee.rmes.magmafusion.model.Nomenclature;
import fr.insee.rmes.magmafusion.model.NomenclatureForList;
import fr.insee.rmes.magmafusion.utils.NiveauDTO;
import fr.insee.rmes.magmafusion.utils.NomenclatureDTO;
import fr.insee.rmes.magmafusion.utils.NomenclatureForListDTO;

import java.util.List;

public interface NomenclatureService {
    NomenclatureForList convertNomenclatureDtoforList(NomenclatureForListDTO nomenclatureForListDTO);
    Nomenclature convertNomenclatureDto(NomenclatureDTO nomenclatureDTO, List<NiveauDTO> niveauxDTO);
}
