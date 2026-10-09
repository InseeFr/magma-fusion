package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.queries.parameters.*;
import fr.insee.rmes.magmafusion.model.*;
import fr.insee.rmes.magmafusion.utils.TerritoriesFilterUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class GeoCommuneHandler {

    private final RequestProcessor requestProcessor;
    private final TerritoriesFilterUtils territoriesFilterUtils;

    public GeoCommuneHandler(RequestProcessor requestProcessor, TerritoriesFilterUtils territoriesFilterUtils) {
        this.requestProcessor = requestProcessor;
        this.territoriesFilterUtils = territoriesFilterUtils;
    }


    public ResponseEntity<Commune> get(String code, LocalDate date) {
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireRequestParametizer(code, date, Commune.class, "none"))
                .executeQuery()
                .singleResult(Commune.class).toResponseEntity();
    }

    public ResponseEntity<List<Canton>> listeCantons(String code, LocalDate date) {
        return requestProcessor.queryToFindCantonsOfCommune()
                .with(new TerritoireRequestParametizer(code, date, Commune.class, "none"))
                .executeQuery()
                .listResult(Canton.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireBase>> liste(String date, String filtreNom, Boolean com) {
        String finalFiltreNom = filtreNom == null ? "*" : filtreNom;
        boolean finalcom = (com != null) && com;
        if (date==null) {
            date = LocalDate.now().toString();
        }
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireEtoileRequestParametizer(date, Commune.class, finalFiltreNom, "none", finalcom))
                .executeQuery()
                .listResult(TerritoireBase.class)
                .toResponseEntity();

    }

    public ResponseEntity<List<TerritoireTousAttributs>> descendants( String code, LocalDate date, TypeEnumDescendantsCommune type) {
        String territoriesFilter = this.territoriesFilterUtils.defineTerritoriesFilter(type);
        return requestProcessor.queryforFindAscendantsDescendants()
                .with(new AscendantsDescendantsRequestParametizer(code, date, territoriesFilter, Commune.class, false))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireTousAttributs>> ascendants( String code, LocalDate date, TypeEnumAscendantsCommune type) {
        String territoriesFilter = this.territoriesFilterUtils.defineTerritoriesFilter(type);
        return requestProcessor.queryforFindAscendantsDescendants()
                .with(new AscendantsDescendantsRequestParametizer(code, date, territoriesFilter, Commune.class, true))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireBase>> precedents( String code, LocalDate date) {
        return requestProcessor.queryforFindPrecedentsSuivants()
                .with(new PrecedentsSuivantsRequestParametizer(code, date, Commune.class, true))
                .executeQuery()
                .listResult(TerritoireBase.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireBase>> projetes( String code, LocalDate dateProjection, LocalDate date) {
        //The Boolean previous is based on the dateProjection parameter (required parameter) and on the date parameter set to today's date if absent
        // (optional). Setting the date to today's date in ParameterValueDecoder is not retained outside the method
        // => must set the date here as well//
        if (date == null) {
            date = LocalDate.now();
        }
        boolean previous = !dateProjection.isAfter(date);
        return requestProcessor.queryforFindProjetes()
                .with(new ProjetesRequestParametizer(code, dateProjection, date, Commune.class, previous))
                .executeQuery()
                .listResult(TerritoireBase.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireBase>>  suivants(String code, LocalDate date) {
        return requestProcessor.queryforFindPrecedentsSuivants()
                .with(new PrecedentsSuivantsRequestParametizer(code, date, Commune.class, false))
                .executeQuery()
                .listResult(TerritoireBase.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireBaseRelation>>  intersections (String code, LocalDate date, TypeEnum type) {
        String territoriesFilter = this.territoriesFilterUtils.defineTerritoriesFilter(type);
        return requestProcessor.queryToFindIntersections()
                .with(new TerritoiresLiesRequestParametizer(code, date, territoriesFilter, Commune.class))
                .executeQuery()
                .listResult(TerritoireBaseRelation.class)
                .toResponseEntity();
    }
}



