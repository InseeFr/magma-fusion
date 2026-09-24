package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.Indicateur;
import fr.insee.rmes.magmafusion.model.Operation;
import fr.insee.rmes.magmafusion.model.RapportQualite;
import fr.insee.rmes.magmafusion.model.Serie;
import fr.insee.rmes.magmafusion.queries.parameters.IndicateurRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.OperationRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.OperationRubriquesRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.SeriesOperationsRequestParametizer;
import fr.insee.rmes.magmafusion.services.RapportQualiteService;
import fr.insee.rmes.magmafusion.services.OperationsService;
import fr.insee.rmes.magmafusion.utils.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OperationsEndpoints implements OperationsApi {

    private final RequestProcessor requestProcessor;
    private final OperationsService seriesOperationsService;
    private final RapportQualiteService rapportQualiteService;

    public OperationsEndpoints(RequestProcessor requestProcessor, OperationsService seriesOperationsService, RapportQualiteService rapportQualiteService) {
        this.requestProcessor = requestProcessor;
        this.seriesOperationsService = seriesOperationsService;
        this.rapportQualiteService=rapportQualiteService;
    }

    @Override
    public ResponseEntity<List<Serie>> getAllSeries(String dateMiseAJour) {
        String date = dateMiseAJour != null ? dateMiseAJour : "none";
        List<SeriesDTO> dtos = requestProcessor.queryToFindAllSeries()
                .with(new SeriesOperationsRequestParametizer(null, null, date))
                .executeQuery()
                .listResult(SeriesDTO.class)
                .result();
        List<Serie> series = seriesOperationsService.convertSeriesDTOsToSeries(dtos);
        return ResponseEntity.ok(series);
    }

    @Override
    public ResponseEntity<Serie> getSerieById(String id) {
        SeriesDTO seriesDTO = requestProcessor.queryToFindSerieById()
                .with(new SeriesOperationsRequestParametizer(id, null))
                .executeQuery()
                .singleResult(SeriesDTO.class)
                .result();
        if (seriesDTO == null) {
            return ResponseEntity.notFound().build();
        }
        Serie serieById = seriesOperationsService.convertSeriesDTOToSerieById(seriesDTO);
        return EndpointsUtils.toResponseEntity(serieById);
    }

    @Override
    public ResponseEntity<Operation> getOperationByCode(String id) {
        OperationDTO operationDTO = requestProcessor.queryToFindOperationByCode()
                .with(new SeriesOperationsRequestParametizer(null, id))
                .executeQuery()
                .singleResult(OperationDTO.class)
                .result();
        if (operationDTO == null) {
            return ResponseEntity.notFound().build();
        }
        Operation operation = seriesOperationsService.convertOperationDTOToOperation(operationDTO);
        return EndpointsUtils.toResponseEntity(operation);
    }

    @Override
    public ResponseEntity<Indicateur> getIndicatorById(String id) {
        IndicateurDTO indicateurDTO = requestProcessor.queryToFindIndicatorById()
                .with(new IndicateurRequestParametizer(id))
                .executeQuery()
                .singleResult(IndicateurDTO.class)
                .result();
        if (indicateurDTO == null) {
            return ResponseEntity.notFound().build();
        }
        Indicateur indicateur = seriesOperationsService.convertIndicateurDTOToIndicateur(indicateurDTO);
        return EndpointsUtils.toResponseEntity(indicateur);
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