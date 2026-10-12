package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.NomenclatureForList;
import fr.insee.rmes.magmafusion.model.Nomenclature;
import fr.insee.rmes.magmafusion.queries.parameters.NomenclaturesRequestParametizer;
import fr.insee.rmes.magmafusion.services.NomenclatureService;
import fr.insee.rmes.magmafusion.utils.EndpointsUtils;
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

    };

    @Override
    public ResponseEntity<Nomenclature> getClassificationByCode(String nomenclature, String niveau, String code){
        return requestProcessor.queryToFindClassification()
                .with(new NomenclaturesRequestParametizer(nomenclature, niveau, code))
                .executeQuery()
                .singleResult(Nomenclature.class)
                .toResponseEntity();
    }
}
