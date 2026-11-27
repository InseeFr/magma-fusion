package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.Document;
import fr.insee.rmes.magmafusion.model.Indicateur;
import fr.insee.rmes.magmafusion.model.Operation;
import fr.insee.rmes.magmafusion.model.RapportQualite;
import fr.insee.rmes.magmafusion.model.Serie;
import fr.insee.rmes.magmafusion.queries.parameters.IndicateurRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.OperationRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.OperationRubriquesRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.SeriesOperationsRequestParametizer;
import fr.insee.rmes.magmafusion.services.OperationsService;
import fr.insee.rmes.magmafusion.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OperationsEndpoints implements OperationsApi {

    private final RequestProcessor requestProcessor;
    private final OperationsService seriesOperationsService;

    private final String documentsBaseUri;
    private final String linksBaseUri;
    private final String codesLangueLg1BaseUri;
    private final String codesLangueLg2BaseUri;

    public OperationsEndpoints(RequestProcessor requestProcessor, OperationsService seriesOperationsService,
                               @Value("${fr.insee.rmes.magmafusion.gestion.baseURI:http://id.insee.fr/}") String baseUri,
                               @Value("${fr.insee.rmes.magmafusion.documents.baseURI}") String documentsPath,
                               @Value("${fr.insee.rmes.magmafusion.links.baseURI}") String linksPath,
                               @Value("${fr.insee.rmes.magmafusion.lg1}") String lg1,
                               @Value("${fr.insee.rmes.magmafusion.lg2}") String lg2) {
        this.requestProcessor = requestProcessor;
        this.seriesOperationsService = seriesOperationsService;
        this.documentsBaseUri = baseUri + documentsPath + "/";
        this.linksBaseUri = baseUri + linksPath + "/";
        this.codesLangueLg1BaseUri = baseUri + "codes/langue/" + lg1;
        this.codesLangueLg2BaseUri = baseUri + "codes/langue/" + lg2;
    }

    @Override
    public ResponseEntity<Document> getDocumentById(String id) {
        return EndpointsUtils.toResponseEntity(seriesOperationsService.findDocumentByUri(documentsBaseUri + id));
    }

    @Override
    public ResponseEntity<Document> getLienById(String id) {
        return EndpointsUtils.toResponseEntity(seriesOperationsService.findDocumentByUri(linksBaseUri + id));
    }

    @Override
    public ResponseEntity<List<Serie>> getAllSeries(String dateMiseAJour) {
        String date = dateMiseAJour != null ? dateMiseAJour : "none";
        List<SeriesDTO> dtos = requestProcessor.queryToFindAllSeries()
                .with(new SeriesOperationsRequestParametizer(null, null, date))
                .executeQuery()
                .listResult(SeriesDTO.class)
                .result();
        if (dtos == null) {
            return ResponseEntity.notFound().build();
        }
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


        List<RubriqueDTO> rubriqueList = requestProcessor.queryToFindRubriques()
                .with(new OperationRubriquesRequestParametizer(rapportQualiteDTO.id(), codesLangueLg1BaseUri, codesLangueLg2BaseUri))
                .executeQuery()
                .listResult(RubriqueDTO.class)
                .result();
        rapportQualiteDTO = rapportQualiteDTO.withRubriqueDTOList(rubriqueList);


        RapportQualite rapportQualite = seriesOperationsService.convertDTOToRapportQualite(rapportQualiteDTO);

        return EndpointsUtils.toResponseEntity(rapportQualite);

    }


}