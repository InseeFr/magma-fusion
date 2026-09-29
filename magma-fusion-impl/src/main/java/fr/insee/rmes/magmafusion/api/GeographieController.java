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
    private final GeoArrondissementMunicipalHandler arrondissementMunicipalHandler;
    private final GeoBassinDeVieHandler bassinDeVieHandler;
    private final GeoCantonHandler cantonHandler;
    private final GeoCantonOuVilleHandler cantonOuVilleHandler;

    public GeographieController(GeoAireDAttractionDesVillesHandler aireDAttractionHandler,
                                GeoArrondissementHandler arrondissementHandler, GeoArrondissementMunicipalHandler arrondissementMunicipalHandler, GeoBassinDeVieHandler bassinDeVieHandler, GeoCantonHandler cantonHandler, GeoCantonOuVilleHandler cantonOuVilleHandler) {
        this.aireDAttractionHandler = aireDAttractionHandler;
        this.arrondissementHandler = arrondissementHandler;
        this.arrondissementMunicipalHandler = arrondissementMunicipalHandler;
        this.bassinDeVieHandler = bassinDeVieHandler;
        this.cantonHandler = cantonHandler;
        this.cantonOuVilleHandler = cantonOuVilleHandler;
    }

    /*Aires d'attraction des villes*/

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

    /*Arrondissements*/

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrasc(String code, LocalDate date, TypeEnumAscendantsArrondissement type) {
        return arrondissementHandler.ascendants(code,date,type);
    }

    @Override
    public ResponseEntity<Arrondissement> getcogarr(String code, LocalDate date) {
        return arrondissementHandler.get(code,date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrdes(String code, LocalDate date, TypeEnumDescendantsArrondissement type) {
        return arrondissementHandler.descendants(code,date,type);
    }

    @Override
    public ResponseEntity<List<Arrondissement>> getcogarrliste(String date) {
        return arrondissementHandler.liste(date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrprec(String code, LocalDate date) {
        return arrondissementHandler.precedents(code,date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrproj(String code, LocalDate dateProjection, LocalDate date) {
        return arrondissementHandler.projetes(code,dateProjection,date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrsuiv(String code, LocalDate date) {
        return arrondissementHandler.suivants(code,date);
    }

    /*Arrondissements municipaux*/
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrmuasc(String code, LocalDate date, TypeEnumAscendantsArrondissementMunicipal type) {
        return arrondissementMunicipalHandler.ascendants(code,date,type);
    }

    public ResponseEntity<ArrondissementMunicipal> getcogarrmu(String code, LocalDate date) {
        return arrondissementMunicipalHandler.get(code, date);
    }

    public ResponseEntity<List<ArrondissementMunicipal>> getcogarrmuliste(String date) {
        return arrondissementMunicipalHandler.liste(date);
    }

    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrmuprec(String code, LocalDate date) {
        return arrondissementMunicipalHandler.precedents(code,date);
    }

    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrmuproj(String code, LocalDate dateProjection, LocalDate date) {
        return arrondissementMunicipalHandler.projetes(code, dateProjection, date);
    }

    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrmusuiv(String code, LocalDate date) {
        return arrondissementMunicipalHandler.suivants(code, date);
    }

    /*bassins de vie*/
    public ResponseEntity<BassinDeVie2022> getcogbass(String code, LocalDate date) {
        return bassinDeVieHandler.get(code, date);
    }

    public ResponseEntity<List<TerritoireTousAttributs>>  getcogbassdes (String code, LocalDate date, TypeEnumDescendantsBassinDeVie type) {
        return bassinDeVieHandler.descendants(code, date, type);
    }

    public ResponseEntity<List<BassinDeVie2022>> getcogbassliste (String date, String filtreNom) {
        return bassinDeVieHandler.liste(date, filtreNom);
    }

    /*cantons*/
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanasc(String code, LocalDate date, TypeEnumAscendantsCanton type) {
        return cantonHandler.ascendants(code, date, type);
    }

    public ResponseEntity<Canton> getcogcan(String code, LocalDate date) {
        return cantonHandler.get(code, date);
    }

    public ResponseEntity<List<TerritoireTousAttributs>> getcogcancom(String code, LocalDate date) {
        return cantonHandler.listeCommunes(code, date);
    }

    public ResponseEntity<List<Canton>> getcogcanliste(String date) {
        return cantonHandler.liste(date);
    }

    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanprec(String code, LocalDate date) {
        return cantonHandler.precedents(code, date);
    }

    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanproj(String code, LocalDate dateProjection, LocalDate date) {
        return cantonHandler.projetes(code, dateProjection,date);
    }

    public ResponseEntity<List<TerritoireTousAttributs>> getcogcansuiv(String code, LocalDate date) {
        return cantonHandler.suivants(code, date);
    }

    public ResponseEntity<List<TerritoireBaseRelation>> getcogcanintersect (String code, LocalDate date, TypeEnum type) {
        return cantonHandler.intersections(code, date, type);
    }

    /*canton ou ville*/
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanvilasc(String code, LocalDate date, TypeEnumAscendantsCantonOuVille type) {
        return cantonOuVilleHandler.ascendants(code,date,type);
    }

    public ResponseEntity<CantonOuVille> getcogcanvil(String code, LocalDate date) {
        return cantonOuVilleHandler.get(code,date);
    }

    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanvildes(String code, LocalDate date, TypeEnumDescendantsCantonOuVille type, String filtreNom) {
        return cantonOuVilleHandler.descendants(code,date,type,filtreNom);
    }

    public ResponseEntity<List<CantonOuVille>> getcogcanvilliste(String date) {
        return cantonOuVilleHandler.liste(date);
    }

    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanvilprec(String code, LocalDate date) {
        return cantonOuVilleHandler.precedents(code, date);
    }

    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanvilproj(String code, LocalDate dateProjection, LocalDate date) {
        return cantonOuVilleHandler.projetes(code,dateProjection,date);
    }

    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanvilsuiv(String code, LocalDate date) {
        return cantonOuVilleHandler.suivants(code,date);
    }

}