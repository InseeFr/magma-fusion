package fr.insee.rmes.magmaFusion.api;

import fr.insee.rmes.magmaFusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmaFusion.model.QuartierPrioritaireDeLaPolitiqueDeLaVille2024;
import fr.insee.rmes.magmaFusion.model.TerritoireBaseRelation;
import fr.insee.rmes.magmaFusion.model.TypeEnum;
import fr.insee.rmes.magmaFusion.queries.parameters.TerritoireRequestParametizer;
import fr.insee.rmes.magmaFusion.queries.parameters.TerritoiresLiesRequestParametizer;
import fr.insee.rmes.magmaFusion.utils.TerritoriesFilterUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class GeoQuartierPrioritaireDeLaPolitiqueDeLaVilleEndpoints implements GeoQuartierPrioritaireDeLaPolitiqueDeLaVilleApi {

    private final RequestProcessor requestProcessor;
    private final TerritoriesFilterUtils territoriesFilterUtils;

    public GeoQuartierPrioritaireDeLaPolitiqueDeLaVilleEndpoints(RequestProcessor requestProcessor, TerritoriesFilterUtils territoriesFilterUtils) {
        this.requestProcessor = requestProcessor;
        this.territoriesFilterUtils = territoriesFilterUtils;
    }

    @Override
    public ResponseEntity<QuartierPrioritaireDeLaPolitiqueDeLaVille2024> getcogqpv (String code, LocalDate date) {
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireRequestParametizer(code, date, QuartierPrioritaireDeLaPolitiqueDeLaVille2024.class, "none"))
                .executeQuery()
                .singleResult(QuartierPrioritaireDeLaPolitiqueDeLaVille2024.class).toResponseEntity();
    }

    @Override
    public ResponseEntity<List<QuartierPrioritaireDeLaPolitiqueDeLaVille2024>> getcogqpvliste (LocalDate date) {
         return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireRequestParametizer(date, QuartierPrioritaireDeLaPolitiqueDeLaVille2024.class, "none"))
                .executeQuery()
                .listResult(QuartierPrioritaireDeLaPolitiqueDeLaVille2024.class)
                .toResponseEntity();
    }

    @Override
    public ResponseEntity<List<TerritoireBaseRelation>> getcogqpvintersect (String code, LocalDate date, TypeEnum type) {
        String territoriesFilter = this.territoriesFilterUtils.defineTerritoriesFilter(type);
        return requestProcessor.queryToFindIntersections()
                .with(new TerritoiresLiesRequestParametizer(code, date, territoriesFilter, QuartierPrioritaireDeLaPolitiqueDeLaVille2024.class))
                .executeQuery()
                .listResult(TerritoireBaseRelation.class)
                .toResponseEntity();
    }


}
