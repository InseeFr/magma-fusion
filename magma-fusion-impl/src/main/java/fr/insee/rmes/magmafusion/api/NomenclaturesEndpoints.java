package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.Nomenclature;
import fr.insee.rmes.magmafusion.model.NomenclatureByCode;
import fr.insee.rmes.magmafusion.model.NomenclatureForList;
import fr.insee.rmes.magmafusion.queries.parameters.NomenclaturesRequestParametizer;
import fr.insee.rmes.magmafusion.services.NomenclatureService;
import fr.insee.rmes.magmafusion.utils.EndpointsUtils;
import fr.insee.rmes.magmafusion.utils.NiveauDTO;
import fr.insee.rmes.magmafusion.utils.NomenclatureDTO;
import fr.insee.rmes.magmafusion.utils.NomenclatureForListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NomenclaturesEndpoints implements NomenclaturesApi {

    private final RequestProcessor requestProcessor;
    private final NomenclatureService nomenclatureService;

    public NomenclaturesEndpoints(RequestProcessor requestProcessor, NomenclatureService nomenclatureService) {
        this.requestProcessor = requestProcessor;
        this.nomenclatureService = nomenclatureService;
    }


    @Override
    public ResponseEntity<List<NomenclatureForList>> getnomenclliste(){
        List<NomenclatureForListDTO> listNomenclaturesDto = requestProcessor.queryToFindClassifications()
                .with(new NomenclaturesRequestParametizer())
                .executeQuery()
                .listResult(NomenclatureForListDTO.class)
                .result();

        List<NomenclatureForList> nomenclatures = listNomenclaturesDto.stream()
                .map(nomenclatureService::convertNomenclatureDtoforList)
                .toList();

        return EndpointsUtils.toResponseEntity(nomenclatures);

    }

    @Override
    public ResponseEntity<Nomenclature> getClassification(String nomenclature){
        NomenclatureDTO nomenclatureDTO = requestProcessor.queryToFindClassification()
                .with(new NomenclaturesRequestParametizer(nomenclature))
                .executeQuery()
                .singleResult(NomenclatureDTO.class)
                .result();
        List<NiveauDTO> niveauxDTO = requestProcessor.queryToFindLevelsClassification()
                .with(new NomenclaturesRequestParametizer(nomenclature))
                .executeQuery()
                .listResult(NiveauDTO.class)
                .result();
        Nomenclature nomencl = nomenclatureService.convertNomenclatureDto(nomenclatureDTO,niveauxDTO);
        return EndpointsUtils.toResponseEntity(nomencl);
    }

    @Override
    public ResponseEntity<NomenclatureByCode> getClassificationByCode(String nomenclature, String niveau, String code){
        return requestProcessor.queryToFindClassificationByCode()
                .with(new NomenclaturesRequestParametizer(nomenclature, niveau, code))
                .executeQuery()
                .singleResult(NomenclatureByCode.class)
                .toResponseEntity();
    }
}
