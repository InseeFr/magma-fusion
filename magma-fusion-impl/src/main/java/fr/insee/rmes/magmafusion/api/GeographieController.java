package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.model.*;
import fr.insee.rmes.magmafusion.queries.parameters.AscendantsDescendantsRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.TerritoireEtoileRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.TerritoireRequestParametizer;
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
    public final GeoCirconscriptionTerritorialeHandler circonscriptionTerritorialeHandler;
    public final GeoCollectiviteDOutreMerHandler collectiviteDOutreMerHandler;
    public final GeoCommuneHandler communeHandler;
    public final GeoCommuneAssocieeHandler communeAssocieeHandler;
    public final GeoCommuneDelegueeHandler communeDelegueeHandler;
    public final GeoDepartementHandler departementHandler;
    public final GeoDistrictHandler districHandler;
    public final GeoIntercommunaliteHandler intercommunaliteHandler;
    public final GeoIrisHandler irisHandler;
    public final GeoPaysHandler paysHandler;
    public final GeoQuartierPrioritaireDeLaPolitiqueDeLaVilleHandler quartierPrioritaireDeLaPolitiqueDeLaVilleHandler;
    public final GeoRegionHandler regionHandler;
    public final GeoUniteUrbaineHandler uniteUrbaineHandler;
    public final GeoZoneDEmploiHandler zoneDEmploiHandler;

    public GeographieController(GeoAireDAttractionDesVillesHandler aireDAttractionHandler,
                                GeoArrondissementHandler arrondissementHandler, GeoArrondissementMunicipalHandler arrondissementMunicipalHandler, GeoBassinDeVieHandler bassinDeVieHandler, GeoCantonHandler cantonHandler, GeoCantonOuVilleHandler cantonOuVilleHandler, GeoCirconscriptionTerritorialeHandler circonscriptionTerritorialeHandler, GeoCollectiviteDOutreMerHandler collectiviteDOutreMerHandler, GeoCommuneHandler communeHandler, GeoCommuneAssocieeHandler communeAssocieeHandler, GeoCommuneDelegueeHandler communeDelegueeHandler, GeoDepartementHandler departementHandler, GeoDistrictHandler districHandler, GeoIntercommunaliteHandler intercommunaliteHandler, GeoIrisHandler irisHandler, GeoPaysHandler paysHandler, GeoQuartierPrioritaireDeLaPolitiqueDeLaVilleHandler quartierPrioritaireDeLaPolitiqueDeLaVilleHandler, GeoRegionHandler regionHandler, GeoUniteUrbaineHandler uniteUrbaineHandler, GeoZoneDEmploiHandler zoneDEmploiHandler) {
        this.aireDAttractionHandler = aireDAttractionHandler;
        this.arrondissementHandler = arrondissementHandler;
        this.arrondissementMunicipalHandler = arrondissementMunicipalHandler;
        this.bassinDeVieHandler = bassinDeVieHandler;
        this.cantonHandler = cantonHandler;
        this.cantonOuVilleHandler = cantonOuVilleHandler;
        this.circonscriptionTerritorialeHandler = circonscriptionTerritorialeHandler;
        this.collectiviteDOutreMerHandler = collectiviteDOutreMerHandler;
        this.communeHandler = communeHandler;
        this.communeAssocieeHandler = communeAssocieeHandler;
        this.communeDelegueeHandler = communeDelegueeHandler;
        this.departementHandler = departementHandler;
        this.districHandler = districHandler;
        this.intercommunaliteHandler = intercommunaliteHandler;
        this.irisHandler = irisHandler;
        this.paysHandler = paysHandler;
        this.quartierPrioritaireDeLaPolitiqueDeLaVilleHandler = quartierPrioritaireDeLaPolitiqueDeLaVilleHandler;
        this.regionHandler = regionHandler;
        this.uniteUrbaineHandler = uniteUrbaineHandler;
        this.zoneDEmploiHandler = zoneDEmploiHandler;
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

    /*Arrondissements municipaux*/
    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrmuasc(String code, LocalDate date, TypeEnumAscendantsArrondissementMunicipal type) {
        return arrondissementMunicipalHandler.ascendants(code, date, type);
    }

    @Override
    public ResponseEntity<ArrondissementMunicipal> getcogarrmu(String code, LocalDate date) {
        return arrondissementMunicipalHandler.get(code, date);
    }

    @Override
    public ResponseEntity<List<ArrondissementMunicipal>> getcogarrmuliste(String date) {
        return arrondissementMunicipalHandler.liste(date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrmuprec(String code, LocalDate date) {
        return arrondissementMunicipalHandler.precedents(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrmuproj(String code, LocalDate dateProjection, LocalDate date) {
        return arrondissementMunicipalHandler.projetes(code, dateProjection, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogarrmusuiv(String code, LocalDate date) {
        return arrondissementMunicipalHandler.suivants(code, date);
    }

    /*bassins de vie*/
    @Override
    public ResponseEntity<BassinDeVie2022> getcogbass(String code, LocalDate date) {
        return bassinDeVieHandler.get(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogbassdes(String code, LocalDate date, TypeEnumDescendantsBassinDeVie type) {
        return bassinDeVieHandler.descendants(code, date, type);
    }

    @Override
    public ResponseEntity<List<BassinDeVie2022>> getcogbassliste(String date, String filtreNom) {
        return bassinDeVieHandler.liste(date, filtreNom);
    }

    /*cantons*/
    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanasc(String code, LocalDate date, TypeEnumAscendantsCanton type) {
        return cantonHandler.ascendants(code, date, type);
    }

    @Override
    public ResponseEntity<Canton> getcogcan(String code, LocalDate date) {
        return cantonHandler.get(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcancom(String code, LocalDate date) {
        return cantonHandler.listeCommunes(code, date);
    }

    @Override
    public ResponseEntity<List<Canton>> getcogcanliste(String date) {
        return cantonHandler.liste(date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanprec(String code, LocalDate date) {
        return cantonHandler.precedents(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanproj(String code, LocalDate dateProjection, LocalDate date) {
        return cantonHandler.projetes(code, dateProjection, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcansuiv(String code, LocalDate date) {
        return cantonHandler.suivants(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireBaseRelation>> getcogcanintersect(String code, LocalDate date, TypeEnum type) {
        return cantonHandler.intersections(code, date, type);
    }

    /*canton ou ville*/
    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanvilasc(String code, LocalDate date, TypeEnumAscendantsCantonOuVille type) {
        return cantonOuVilleHandler.ascendants(code, date, type);
    }

    @Override
    public ResponseEntity<CantonOuVille> getcogcanvil(String code, LocalDate date) {
        return cantonOuVilleHandler.get(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanvildes(String code, LocalDate date, TypeEnumDescendantsCantonOuVille type, String filtreNom) {
        return cantonOuVilleHandler.descendants(code, date, type, filtreNom);
    }

    @Override
    public ResponseEntity<List<CantonOuVille>> getcogcanvilliste(String date) {
        return cantonOuVilleHandler.liste(date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanvilprec(String code, LocalDate date) {
        return cantonOuVilleHandler.precedents(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanvilproj(String code, LocalDate dateProjection, LocalDate date) {
        return cantonOuVilleHandler.projetes(code, dateProjection, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcanvilsuiv(String code, LocalDate date) {
        return cantonOuVilleHandler.suivants(code, date);
    }

    /*circonscription territoriale*/
    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcirasc(String code, LocalDate date, TypeEnumAscendantsCirconscriptionTerritoriale type) {
        return circonscriptionTerritorialeHandler.ascendants(code, date, type);
    }

    @Override
    public ResponseEntity<CirconscriptionTerritoriale> getcogcir(String code, LocalDate date) {
        return circonscriptionTerritorialeHandler.get(code, date);
    }

    /*collectivité d'outre-mer*/
    @Override
    public ResponseEntity<CollectiviteDOutreMer> getcogcoll(String code, LocalDate date) {
        return collectiviteDOutreMerHandler.get(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcolldes(String code, LocalDate date, TypeEnumDescendantsCollectiviteDOutreMer type, String filtreNom) {
        return collectiviteDOutreMerHandler.descendants(code, date, type, filtreNom);
    }

    @Override
    public ResponseEntity<List<CollectiviteDOutreMer>> getcogcollliste(String date) {
        return collectiviteDOutreMerHandler.liste(date);
    }

    @Override
    public ResponseEntity<Commune> getcogcom(String code, LocalDate date) {
        return communeHandler.get(code, date);
    }

    @Override
    public ResponseEntity<List<Canton>> getcogcomcan(String code, LocalDate date) {
        return communeHandler.listeCantons(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireBase>> getcogcomliste(String date, String filtreNom, Boolean com) {
        return communeHandler.liste(date, filtreNom, com);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcomdesc(String code, LocalDate date, TypeEnumDescendantsCommune type) {
        return communeHandler.descendants(code, date, type);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcomasc(String code, LocalDate date, TypeEnumAscendantsCommune type) {
        return communeHandler.ascendants(code, date, type);
    }

    @Override
    public ResponseEntity<List<TerritoireBase>> getcogcomprec(String code, LocalDate date) {
        return communeHandler.precedents(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireBase>> getcogcomproj(String code, LocalDate dateProjection, LocalDate date) {
        return communeHandler.projetes(code, dateProjection, date);
    }

    @Override
    public ResponseEntity<List<TerritoireBase>> getcogcomsuiv(String code, LocalDate date) {
        return communeHandler.suivants(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireBaseRelation>> getcogcomintersect(String code, LocalDate date, TypeEnum type) {
        return communeHandler.intersections(code, date, type);
    }

    /*communes associées*/
    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcomaasc(String code, LocalDate date, TypeEnumAscendantsCommuneAssociee type) {
        return communeAssocieeHandler.ascendants(code, date, type);
    }

    @Override
    public ResponseEntity<CommuneAssociee> getcogcoma(String code, LocalDate date) {
        return communeAssocieeHandler.get(code, date);
    }

    @Override
    public ResponseEntity<List<CommuneAssociee>> getcogcomaliste(String date) {
        return communeAssocieeHandler.liste(date);
    }

    /*communes déléguées*/
    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogcomdasc(String code, LocalDate date, TypeEnumAscendantsCommuneDeleguee type) {
        return communeDelegueeHandler.ascendants(code, date, type);
    }

    @Override
    public ResponseEntity<CommuneDeleguee> getcogcomd(String code, LocalDate date) {
        return communeDelegueeHandler.get(code, date);
    }

    @Override
    public ResponseEntity<List<CommuneDeleguee>> getcogcomdliste(String date) {
        return communeDelegueeHandler.liste(date);
    }

    /*departements*/
    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogdepdesc(String code, LocalDate date, TypeEnumDescendantsDepartement type, String filtreNom) {
        return departementHandler.descendants(code, date, type,filtreNom);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogdepasc(String code, LocalDate date, TypeEnumAscendantsDepartement type) {
        return departementHandler.ascendants(code, date, type);
    }

    @Override
    public ResponseEntity<List<TerritoireBaseChefLieu>> getcogdepprec(String code, LocalDate date) {
        return departementHandler.precedents(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireBaseChefLieu>> getcogdepproj(String code, LocalDate dateProjection, LocalDate date) {
        return departementHandler.projetes(code, dateProjection, date);
    }

    @Override
    public ResponseEntity<List<TerritoireBaseChefLieu>> getcogdepsuiv(String code, LocalDate date) {
        return departementHandler.suivants(code, date);
    }

    @Override
    public ResponseEntity<Departement> getcogdep(String code, LocalDate date) {
        return departementHandler.get(code, date);
    }

    @Override
    public ResponseEntity<List<TerritoireBaseChefLieu>> getcogdepts(String date) {
        return departementHandler.liste(date);
    }

    /*districts*/
    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogdisasc(String code, LocalDate date, TypeEnumAscendantsDistrict type) {
        return districHandler.ascendants(code, date, type);
    }

    @Override
    public ResponseEntity<District> getcogdis(String code, LocalDate date) {
        return districHandler.get(code, date);
    }

    /*intercommunalités*/
    @Override
    public ResponseEntity<Intercommunalite> getcoginterco(String code, LocalDate date) {
        return intercommunaliteHandler.get(code,date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogintercodes(String code, LocalDate date, TypeEnumDescendantsIntercommunalite type) {
        return intercommunaliteHandler.descendants(code,date,type);
    }

    @Override
    public ResponseEntity<List<Intercommunalite>> getcogintercoliste(String date, String filtreNom) {
         return intercommunaliteHandler.liste(date,filtreNom);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogintercoprec(String code, LocalDate date) {
        return intercommunaliteHandler.precedents(code,date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogintercoproj(String code, LocalDate dateProjection, LocalDate date) {
        return intercommunaliteHandler.projetes(code,dateProjection,date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogintercosuiv(String code, LocalDate date) {
        return intercommunaliteHandler.suivants(code,date);
    }

    /*iris*/
    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogirisasc (String code, LocalDate date, TypeEnumAscendantsIris type) {
        return irisHandler.ascendants(code,date,type);
    }

    @Override
    public ResponseEntity<Iris> getcogiris(String code, LocalDate date) {
        return irisHandler.get(code,date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogirislist (LocalDate date, Boolean com) {
        return irisHandler.liste(date, com);
    }

    /*pays*/
    @Override
    public ResponseEntity<Pays> getcogpays(String code, LocalDate date) {
        return paysHandler.get(code,date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogpaysdesc(String code, LocalDate date, TypeEnumDescendantsPays type) {
        return paysHandler.descendants(code,date,type);
    }

    @Override
    public ResponseEntity<List<Pays>> getcogpayslist(String date) {
        return paysHandler.liste(date);

    }

    @Override
    public ResponseEntity<List<Pays>> getcogpaysprec(String code, LocalDate date) {
        return paysHandler.precedents(code,date);
    }

    @Override
    public ResponseEntity<List<Pays>> getcogpayssuiv(String code, LocalDate date) {
        return paysHandler.suivants(code,date);
    }

    /*quartiers prioritaires de la politique de la ville*/
    @Override
    public ResponseEntity<QuartierPrioritaireDeLaPolitiqueDeLaVille2024> getcogqpv (String code, LocalDate date) {
        return quartierPrioritaireDeLaPolitiqueDeLaVilleHandler.get(code,date);
    }

    @Override
    public ResponseEntity<List<QuartierPrioritaireDeLaPolitiqueDeLaVille2024>> getcogqpvliste (LocalDate date) {
        return quartierPrioritaireDeLaPolitiqueDeLaVilleHandler.liste(date);
    }

    @Override
    public ResponseEntity<List<TerritoireBaseRelation>> getcogqpvintersect (String code, LocalDate date, TypeEnum type) {
        return quartierPrioritaireDeLaPolitiqueDeLaVilleHandler.intersections(code,date,type);
    }

    /*regions*/
    @Override
    public ResponseEntity<Region> getcogreg(String code, LocalDate date) {
        return regionHandler.get(code,date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogregdes(String code, LocalDate date, TypeEnumDescendantsRegion type, String filtreNom) {
        return regionHandler.descendants(code,date,type,filtreNom);
    }

    @Override
    public ResponseEntity<List<Region>> getcogregliste(String date) {
        return regionHandler.liste(date);

    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogregprec(String code, LocalDate date) {
        return regionHandler.precedents(code,date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogregproj(String code, LocalDate dateProjection, LocalDate date) {
        return regionHandler.projetes(code,dateProjection,date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>> getcogregsuiv(String code, LocalDate date) {
        return regionHandler.suivants(code,date);
    }

    /*unités urbaines*/
    @Override
    public ResponseEntity<UniteUrbaine2020> getcoguu(String code, LocalDate date) {
        return uniteUrbaineHandler.get(code,date);
    }

    @Override
    public ResponseEntity<List<TerritoireTousAttributs>>  getcoguudes (String code, LocalDate date, TypeEnumDescendantsUniteUrbaine type) {
        return uniteUrbaineHandler.descendants(code,date,type);
    }

    @Override
    public ResponseEntity<List<UniteUrbaine2020>> getcoguuliste (String date) {
        return uniteUrbaineHandler.liste(date);
    }

    /*zones d'emploi*/
    public ResponseEntity<ZoneDEmploi2020> getcogze(String code, LocalDate date) {
        return zoneDEmploiHandler.get(code,date);
    }


    public ResponseEntity<List<TerritoireTousAttributs>>  getcogzedesc(String code, LocalDate date, TypeEnumDescendantsZoneDEmploi type) {
        return zoneDEmploiHandler.descendants(code,date,type);
    }

    public ResponseEntity<List<ZoneDEmploi2020>> getcogzeliste (String date) {
        return zoneDEmploiHandler.liste(date);
    }
}
