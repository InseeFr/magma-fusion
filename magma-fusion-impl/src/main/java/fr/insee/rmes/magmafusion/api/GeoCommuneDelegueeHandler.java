package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.CommuneDeleguee;
import fr.insee.rmes.magmafusion.model.TerritoireTousAttributs;
import fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCommuneDeleguee;
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
public class GeoCommuneDelegueeHandler {

    private final RequestProcessor requestProcessor;
    private final TerritoriesFilterUtils territoriesFilterUtils;

    public GeoCommuneDelegueeHandler(RequestProcessor requestProcessor, TerritoriesFilterUtils territoriesFilterUtils) {
        this.requestProcessor = requestProcessor;
        this.territoriesFilterUtils = territoriesFilterUtils;
    }


    public ResponseEntity<List<TerritoireTousAttributs>>  ascendants(String code, LocalDate date, TypeEnumAscendantsCommuneDeleguee type) {
        String territoriesFilter = this.territoriesFilterUtils.defineTerritoriesFilter(type);
        return requestProcessor.queryforFindAscendantsDescendants()
                .with(new AscendantsDescendantsRequestParametizer(code, date, territoriesFilter, CommuneDeleguee.class, true))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }

    public ResponseEntity<CommuneDeleguee> get (String code, LocalDate date) {
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireRequestParametizer(code, date, CommuneDeleguee.class, "none"))
                .executeQuery()
                .singleResult(CommuneDeleguee.class)
                .toResponseEntity();

    }

    public ResponseEntity<List<CommuneDeleguee>> liste (String date) {
        if (date==null) {
            date = LocalDate.now().toString();
        }
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireEtoileRequestParametizer(date, CommuneDeleguee.class, "none"))
                .executeQuery()
                .listResult(CommuneDeleguee.class)
                .toResponseEntity();

    }

}
