package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class GeographieController implements GeographieApi {

    private final GeoAireDAttractionDesVillesHandler aireDAttractionHandler;
    private final GeoArrondissementHandler arrondissementHandler;

    public GeographieController(GeoAireDAttractionDesVillesHandler aireDAttractionHandler,
                                GeoArrondissementHandler arrondissementHandler) {
        this.aireDAttractionHandler = aireDAttractionHandler;
        this.arrondissementHandler = arrondissementHandler;
    }

    @Override
    public ResponseEntity<AireDAttractionDesVilles2020> getcogaav(String code, LocalDate date) {
        return aireDAttractionHandler.get(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogaavdesc(String code, LocalDate date, TypeEnumDescendantsAireDAttractionDesVilles type) {
        return aireDAttractionHandler.descendants(code, date, type);
    }

    @Override
    public ResponseEntity<List<AireDAttractionDesVilles2020>> getcogaavliste(String date) {
        return aireDAttractionHandler.liste(date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrasc(String code, LocalDate date, TypeEnumAscendantsArrondissement type) {
        return arrondissementHandler.ascendants(code, date, type);
    }

    @Override
    public ResponseEntity<Arrondissement> getcogarr(String code, LocalDate date) {
        return arrondissementHandler.get(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrdes(String code, LocalDate date, TypeEnumDescendantsArrondissement type) {
        return arrondissementHandler.descendants(code, date, type);
    }

    @Override
    public ResponseEntity<List<Arrondissement>> getcogarrliste(String date) {
        return arrondissementHandler.liste(date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrprec(String code, LocalDate date) {
        return arrondissementHandler.precedents(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrproj(String code, LocalDate dateProjection, LocalDate date) {
        return arrondissementHandler.projetes(code, dateProjection, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrsuiv(String code, LocalDate date) {
        return arrondissementHandler.suivants(code, date);
    }
}