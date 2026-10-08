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
public class GeoDepartementHandler {


    private final RequestProcessor requestProcessor;
    private final TerritoriesFilterUtils territoriesFilterUtils;

    public GeoDepartementHandler(RequestProcessor requestProcessor, TerritoriesFilterUtils territoriesFilterUtils) {
        this.requestProcessor = requestProcessor;
        this.territoriesFilterUtils = territoriesFilterUtils;
    }

    public ResponseEntity<List<TerritoireTousAttributs>>  descendants(String code, LocalDate date, TypeEnumDescendantsDepartement type, String filtreNom) {
        String territoriesFilter = this.territoriesFilterUtils.defineTerritoriesFilter(type);
        return requestProcessor.queryforFindAscendantsDescendants()
                .with(new AscendantsDescendantsRequestParametizer(code, date, filtreNom, territoriesFilter, Departement.class))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireTousAttributs>>  ascendants(String code, LocalDate date, TypeEnumAscendantsDepartement type) {
        String territoriesFilter = this.territoriesFilterUtils.defineTerritoriesFilter(type);
        return requestProcessor.queryforFindAscendantsDescendants()
                .with(new AscendantsDescendantsRequestParametizer(code, date, territoriesFilter, Departement.class, true))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireBaseChefLieu>>  precedents(String code, LocalDate date) {
        return requestProcessor.queryforFindPrecedentsSuivants()
                .with(new PrecedentsSuivantsRequestParametizer(code, date, Departement.class, true))
                .executeQuery()
                .listResult(TerritoireBaseChefLieu.class)
                .toResponseEntity();
    }


    public ResponseEntity<List<TerritoireBaseChefLieu>>  projetes(String code, LocalDate dateProjection, LocalDate date) {
        //The Boolean previous is based on the dateProjection parameter (required parameter) and on the date parameter set to today's date if absent
        // (optional). Setting the date to today's date in ParameterValueDecoder is not retained outside the method
        // => must set the date here as well
        if (date == null) {
            date = LocalDate.now();
        }
        boolean previous = !dateProjection.isAfter(date);
        return requestProcessor.queryforFindProjetes()
                .with(new ProjetesRequestParametizer(code, dateProjection, date, Departement.class, previous))
                .executeQuery()
                .listResult(TerritoireBaseChefLieu.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<TerritoireBaseChefLieu>>  suivants(String code, LocalDate date) {
        return requestProcessor.queryforFindPrecedentsSuivants()
                .with(new PrecedentsSuivantsRequestParametizer(code, date, Departement.class, false))
                .executeQuery()
                .listResult(TerritoireBaseChefLieu.class)
                .toResponseEntity();
    }


    public ResponseEntity<Departement> get(String code, LocalDate date) {
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireRequestParametizer(code, date, Departement.class,"prefecture"))
                .executeQuery()
                .singleResult(Departement.class).toResponseEntity();
    }


    public ResponseEntity<List<TerritoireBaseChefLieu>> liste(String date) {
        if (date==null) {
            date = LocalDate.now().toString();
        }
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireEtoileRequestParametizer(date, Departement.class, "prefecture", true))
                .executeQuery()
                .listResult(TerritoireBaseChefLieu.class)
                .toResponseEntity();

    }
 
}
