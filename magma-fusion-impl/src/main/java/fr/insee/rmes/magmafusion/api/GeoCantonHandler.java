package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.*;
import fr.insee.rmes.magmafusion.queries.parameters.*;
import fr.insee.rmes.magmafusion.utils.TerritoriesFilterUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class GeoCantonHandler {

    private final RequestProcessor requestProcessor;
    private final TerritoriesFilterUtils territoriesFilterUtils;

    public GeoCantonHandler(RequestProcessor requestProcessor, TerritoriesFilterUtils territoriesFilterUtils) {
        this.requestProcessor = requestProcessor;
        this.territoriesFilterUtils = territoriesFilterUtils;
    }


    public ResponseEntity<List<TerritoireTousAttributs>> ascendants(String code, LocalDate date, TypeEnumAscendantsCanton type) {
        String territoriesFilter = this.territoriesFilterUtils.defineTerritoriesFilter(type);
        return requestProcessor.queryforFindAscendantsDescendants()
                .with(new AscendantsDescendantsRequestParametizer(code, date, territoriesFilter, Canton.class, true))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }

    public ResponseEntity<Canton> get(String code, LocalDate date) {
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireRequestParametizer(code, date, Canton.class, "bureauCentralisateur"))
                .executeQuery()
                .singleResult(Canton.class).toResponseEntity();
    }

    public ResponseEntity<List<TerritoireTousAttributs>> listeCommunes(String code, LocalDate date) {
        return requestProcessor.queryToFindCommunesOfCanton()
                .with(new TerritoireRequestParametizer(code, date, TerritoireTousAttributs.class, "*"))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<Canton>> liste(String date) {
        if (date==null) {
            date = LocalDate.now().toString();
        }
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireEtoileRequestParametizer(date, Canton.class, "bureauCentralisateur"))
                .executeQuery()
                .listResult(Canton.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireTousAttributs>> precedents(String code, LocalDate date) {
        return requestProcessor.queryforFindPrecedentsSuivants()
                .with(new PrecedentsSuivantsRequestParametizer(code, date, Canton.class, true))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireTousAttributs>> projetes(String code, LocalDate dateProjection, LocalDate date) {
        //The Boolean previous is based on the dateProjection parameter (required parameter) and on the date parameter set to today's date if absent
        // (optional). Setting the date to today's date in ParameterValueDecoder is not retained outside the method
        // => must set the date here as well
        if (date == null) {
            date = LocalDate.now();
        }
        boolean previous = !dateProjection.isAfter(date);
        return requestProcessor.queryforFindProjetes()
                .with(new ProjetesRequestParametizer(code, dateProjection, date, Canton.class, previous))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireTousAttributs>> suivants(String code, LocalDate date) {
        return requestProcessor.queryforFindPrecedentsSuivants()
                .with(new PrecedentsSuivantsRequestParametizer(code, date, Canton.class, false))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireBaseRelation>> intersections (String code, LocalDate date, TypeEnum type) {
        String territoriesFilter = this.territoriesFilterUtils.defineTerritoriesFilter(type);
        return requestProcessor.queryToFindIntersections()
                .with(new TerritoiresLiesRequestParametizer(code, date, territoriesFilter, Canton.class))
                .executeQuery()
                .listResult(TerritoireBaseRelation.class)
                .toResponseEntity();
    }

}

