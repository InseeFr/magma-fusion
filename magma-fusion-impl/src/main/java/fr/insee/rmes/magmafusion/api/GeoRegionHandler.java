package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.Region;
import fr.insee.rmes.magmafusion.model.TerritoireTousAttributs;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsRegion;
import fr.insee.rmes.magmafusion.queries.parameters.*;
import fr.insee.rmes.magmafusion.utils.TerritoriesFilterUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class GeoRegionHandler {

    private final RequestProcessor requestProcessor;
    private final TerritoriesFilterUtils territoriesFilterUtils;

    public GeoRegionHandler(RequestProcessor requestProcessor, TerritoriesFilterUtils territoriesFilterUtils) {
        this.requestProcessor = requestProcessor;
        this.territoriesFilterUtils = territoriesFilterUtils;
    }


    public ResponseEntity<Region> get(String code, LocalDate date) {
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireRequestParametizer(code, date, Region.class, "prefectureDeRegion"))
                .executeQuery()
                .singleResult(Region.class).toResponseEntity();
    }

    public ResponseEntity<List<TerritoireTousAttributs>> descendants(String code, LocalDate date, TypeEnumDescendantsRegion type, String filtreNom) {
        String territoriesFilter = this.territoriesFilterUtils.defineTerritoriesFilter(type);
        return requestProcessor.queryforFindAscendantsDescendants()
                .with(new AscendantsDescendantsRequestParametizer(code, date, filtreNom, territoriesFilter, Region.class))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }


    public ResponseEntity<List<Region>> liste(String date) {
        if (date==null) {
            date = LocalDate.now().toString();
        }
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireEtoileRequestParametizer(date, Region.class, "prefectureDeRegion"))
                .executeQuery()
                .listResult(Region.class)
                .toResponseEntity();

    }

    public ResponseEntity<List<TerritoireTousAttributs>> precedents(String code, LocalDate date) {
        return requestProcessor.queryforFindPrecedentsSuivants()
                .with(new PrecedentsSuivantsRequestParametizer(code, date, Region.class, true))
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
                .with(new ProjetesRequestParametizer(code, dateProjection, date, Region.class, previous))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireTousAttributs>> suivants(String code, LocalDate date) {
        return requestProcessor.queryforFindPrecedentsSuivants()
                .with(new PrecedentsSuivantsRequestParametizer(code, date, Region.class, false))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }
}
