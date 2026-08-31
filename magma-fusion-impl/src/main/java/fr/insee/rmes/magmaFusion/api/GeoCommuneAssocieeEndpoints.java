package fr.insee.rmes.magmaFusion.api;

import fr.insee.rmes.magmaFusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmaFusion.model.CommuneAssociee;
import fr.insee.rmes.magmaFusion.model.TerritoireTousAttributs;
import fr.insee.rmes.magmaFusion.model.TypeEnumAscendantsCommuneAssociee;
import fr.insee.rmes.magmaFusion.queries.parameters.AscendantsDescendantsRequestParametizer;
import fr.insee.rmes.magmaFusion.queries.parameters.TerritoireEtoileRequestParametizer;
import fr.insee.rmes.magmaFusion.queries.parameters.TerritoireRequestParametizer;
import fr.insee.rmes.magmaFusion.utils.TerritoriesFilterUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@RestController
public class GeoCommuneAssocieeEndpoints implements GeoCommuneAssocieeApi{

    private final RequestProcessor requestProcessor;
    private final TerritoriesFilterUtils territoriesFilterUtils;

    public GeoCommuneAssocieeEndpoints(RequestProcessor requestProcessor, TerritoriesFilterUtils territoriesFilterUtils) {
        this.requestProcessor = requestProcessor;
        this.territoriesFilterUtils = territoriesFilterUtils;
    }


    @Override
    public ResponseEntity<List<TerritoireTousAttributs>>  getcogcomaasc (String code, LocalDate date, TypeEnumAscendantsCommuneAssociee type) {
        String territoriesFilter = this.territoriesFilterUtils.defineTerritoriesFilter(type);
        return requestProcessor.queryforFindAscendantsDescendants()
                .with(new AscendantsDescendantsRequestParametizer(code, date, territoriesFilter, CommuneAssociee.class, true))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }

    @Override
    public ResponseEntity<CommuneAssociee> getcogcoma(String code, LocalDate date) {
        return requestProcessor.queryforFindTerritoire()
                .with(new TerritoireRequestParametizer(code, date, CommuneAssociee.class, "none"))
                .executeQuery()
                .singleResult(CommuneAssociee.class)
                .toResponseEntity();

    }

    @Override
    public ResponseEntity<List<CommuneAssociee>> getcogcomaliste (String date) {
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