package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.BassinDeVie2022;
import fr.insee.rmes.magmafusion.model.TerritoireTousAttributs;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsBassinDeVie;
import fr.insee.rmes.magmafusion.queries.parameters.AscendantsDescendantsRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.TerritoireEtoileRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.TerritoireRequestParametizer;
import fr.insee.rmes.magmafusion.utils.TerritoriesFilterUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@Component
public class GeoBassinDeVieHandler {

    private final RequestProcessor requestProcessor;
    private final TerritoriesFilterUtils territoriesFilterUtils;

    public GeoBassinDeVieHandler(RequestProcessor requestProcessor, TerritoriesFilterUtils territoriesFilterUtils) {
        this.requestProcessor = requestProcessor;
        this.territoriesFilterUtils = territoriesFilterUtils;
    }

    public ResponseEntity<BassinDeVie2022> get(String code, LocalDate date) {
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireRequestParametizer(code, date, BassinDeVie2022.class, "none"))
                .executeQuery()
                .singleResult(BassinDeVie2022.class).toResponseEntity();
    }

    public ResponseEntity<List<TerritoireTousAttributs>>  descendants(String code, LocalDate date, TypeEnumDescendantsBassinDeVie type) {
        String territoriesFilter = this.territoriesFilterUtils.defineTerritoriesFilter(type);
        return requestProcessor.queryforFindAscendantsDescendants()
                .with(new AscendantsDescendantsRequestParametizer(code, date, territoriesFilter, BassinDeVie2022.class, false))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<BassinDeVie2022>> liste(String date, String filtreNom) {
        String finalFiltreNom = filtreNom == null ? "*" : filtreNom;
        if (date==null) {
            date = LocalDate.now().toString();
        }
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireEtoileRequestParametizer(date, BassinDeVie2022.class, finalFiltreNom,"none", true))
                .executeQuery()
                .listResult(BassinDeVie2022.class)
                .toResponseEntity();

    }

}