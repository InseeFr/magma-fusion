package fr.insee.rmes.magmaFusion.api;

import fr.insee.rmes.magmaFusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmaFusion.model.RapportQualite;
import fr.insee.rmes.magmaFusion.queries.parameters.OperationRequestParametizer;
import fr.insee.rmes.magmaFusion.queries.parameters.OperationRubriquesRequestParametizer;
import fr.insee.rmes.magmaFusion.services.RapportQualiteService;
import fr.insee.rmes.magmaFusion.utils.RapportQualiteDTO;
import fr.insee.rmes.magmaFusion.utils.RubriqueDTO;
import fr.insee.rmes.magmaFusion.utils.EndpointsUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class OperationsEndpoints implements OperationsApi {

    private final RequestProcessor requestProcessor;
    private final RapportQualiteService rapportQualiteService;

    public OperationsEndpoints(RequestProcessor requestProcessor, RapportQualiteService rapportQualiteService) {
        this.requestProcessor = requestProcessor;
        this.rapportQualiteService = rapportQualiteService;
    }


    @Override
    public ResponseEntity<RapportQualite> getRapportQualiteByCode(String idSims) {
        RapportQualiteDTO rapportQualiteDTO = requestProcessor.queryToFindRapportQualite()
                .with(new OperationRequestParametizer(idSims))
                .executeQuery()
                .singleResult(RapportQualiteDTO.class)
                .result();

        if (rapportQualiteDTO == null){
            return ResponseEntity.notFound().build();
        }

        String LG1_CL = "http://id.insee.fr/codes/langue/fr";
        String LG2_CL = "http://id.insee.fr/codes/langue/en";

        List<RubriqueDTO> rubriqueList = requestProcessor.queryToFindRubriques()
                .with(new OperationRubriquesRequestParametizer(rapportQualiteDTO.id(), LG1_CL, LG2_CL))
                .executeQuery()
                .listResult(RubriqueDTO.class)
                .result();
        rapportQualiteDTO = rapportQualiteDTO.withRubriqueDTOList(rubriqueList);



        RapportQualite rapportQualite = rapportQualiteService.convertDTOToRapportQualite(rapportQualiteDTO);

        return EndpointsUtils.toResponseEntity(rapportQualite);

        }

    }



