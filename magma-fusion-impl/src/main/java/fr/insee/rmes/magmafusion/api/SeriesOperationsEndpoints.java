package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.Indicateur;
import fr.insee.rmes.magmafusion.model.Operation;
import fr.insee.rmes.magmafusion.model.Serie;
import fr.insee.rmes.magmafusion.queries.parameters.IndicateurRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.SeriesOperationsRequestParametizer;
import fr.insee.rmes.magmafusion.services.SeriesOperationsService;
import fr.insee.rmes.magmafusion.utils.IndicateurDTO;
import fr.insee.rmes.magmafusion.utils.OperationDTO;
import fr.insee.rmes.magmafusion.utils.SeriesDTO;
import fr.insee.rmes.magmafusion.utils.EndpointsUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SeriesOperationsEndpoints implements SeriesOperationsApi {

    private final RequestProcessor requestProcessor;
    private final SeriesOperationsService seriesOperationsService;

    public SeriesOperationsEndpoints(RequestProcessor requestProcessor, SeriesOperationsService seriesOperationsService) {
        this.requestProcessor = requestProcessor;
        this.seriesOperationsService = seriesOperationsService;
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

}