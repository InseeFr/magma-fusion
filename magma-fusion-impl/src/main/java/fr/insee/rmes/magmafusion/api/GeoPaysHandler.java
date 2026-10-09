package fr.insee.rmes.magmafusion.api;

import fr.insee.rmes.magmafusion.api.requestprocessor.RequestProcessor;
import fr.insee.rmes.magmafusion.model.Pays;
import fr.insee.rmes.magmafusion.model.TerritoireTousAttributs;
import fr.insee.rmes.magmafusion.model.TypeEnumDescendantsPays;
import fr.insee.rmes.magmafusion.queries.parameters.AscendantsDescendantsRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.PrecedentsSuivantsRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.TerritoireEtoileRequestParametizer;
import fr.insee.rmes.magmafusion.queries.parameters.TerritoireRequestParametizer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@Component
public class GeoPaysHandler {

    private final RequestProcessor requestProcessor;

    public GeoPaysHandler(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }


    public ResponseEntity<Pays> get(String code, LocalDate date) {
        return requestProcessor.queryforFindPays()
                .with(new TerritoireRequestParametizer(code, date, Pays.class, "none"))
                .executeQuery()
                .singleResult(Pays.class).toResponseEntity();
    }


    public ResponseEntity<List<TerritoireTousAttributs>> descendants(String code, LocalDate date, TypeEnumDescendantsPays type) {
        return requestProcessor.queryforFindDescendantsPays()
                .with(new AscendantsDescendantsRequestParametizer(code, date, type, Pays.class))
                .executeQuery()
                .listResult(TerritoireTousAttributs.class)
                .toResponseEntity();
    }


    public ResponseEntity<List<Pays>> liste(String date) {
        if (date==null) {
            date = LocalDate.now().toString();
        }
        return requestProcessor.queryforFindPays()
                .with(new TerritoireEtoileRequestParametizer(date, Pays.class, "none"))
                .executeQuery()
                .listResult(Pays.class)
                .toResponseEntity();

    }


    public ResponseEntity<List<Pays>> precedents(String code, LocalDate date) {
        return requestProcessor.queryforFindPaysPrecedents()
                .with(new PrecedentsSuivantsRequestParametizer(code, date, Pays.class, true))
                .executeQuery()
                .listResult(Pays.class)
                .toResponseEntity();
    }

    public ResponseEntity<List<Pays>> suivants(String code, LocalDate date) {
        return requestProcessor.queryforFindPaysSuivants()
                .with(new PrecedentsSuivantsRequestParametizer(code, date, Pays.class, false))
                .executeQuery()
                .listResult(Pays.class)
                .toResponseEntity();
    }
}