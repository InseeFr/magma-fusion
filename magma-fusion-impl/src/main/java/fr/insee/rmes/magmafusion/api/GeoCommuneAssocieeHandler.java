package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.CommuneAssociee;
import fr.insee.rmes.magmafusion.model.TerritoireTousAttributs;
import fr.insee.rmes.magmafusion.model.TypeEnumAscendantsCommuneAssociee;
import fr.insee.rmes.magmafusion.queries.parameters.AscendantsDescendantsRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.TerritoireEtoileRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.TerritoireRequestParametizer;
import fr.insee.rmes.magmafusion.utils.TerritoriesFilterUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@Component
public class GeoCommuneAssocieeHandler {

    private final RequestProcessor requestProcessor;
    private final TerritoriesFilterUtils territoriesFilterUtils;

    public GeoCommuneAssocieeHandler(RequestProcessor requestProcessor, TerritoriesFilterUtils territoriesFilterUtils) {
        this.requestProcessor = requestProcessor;
        this.territoriesFilterUtils = territoriesFilterUtils;
    }


    public ResponseEntity<List<TerritoireTousAttributs>>  ascendants(String code, LocalDate date, TypeEnumAscendantsCommuneAssociee type) {
        String territoriesFilter = this.territoriesFilterUtils.defineTerritoriesFilter(type);
        return requestProcessor.queryforFindAscendantsDescendants()
                .with(new AscendantsDescendantsRequestParametizer(code, date, territoriesFilter, CommuneAssociee.class, true))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }


    public ResponseEntity<CommuneAssociee> get(String code, LocalDate date) {
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireRequestParametizer(code, date, CommuneAssociee.class, "none"))
                .executeQuery()
                .singleResult(CommuneAssociee.class)
                .toResponseEntity();

    }

    public ResponseEntity<List<CommuneAssociee>> liste(String date) {
        if (date==null) {
            date = LocalDate.now().toString();
        }
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireEtoileRequestParametizer(date, CommuneAssociee.class, "none"))
                .executeQuery()
                .listResult(CommuneAssociee.class)
                .toResponseEntity();

    }

}