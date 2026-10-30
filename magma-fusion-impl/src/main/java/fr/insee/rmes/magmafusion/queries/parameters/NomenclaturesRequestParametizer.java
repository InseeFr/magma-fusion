package fr.insee.rmes.magmafusion.queries.parameters;

public record NomenclaturesRequestParametizer(String nomenclature,
                                              String niveau,
                                              String code) implements ParametersForQuery<NomenclaturesRequestParametizer> {

    public NomenclaturesRequestParametizer() {
        this("none", "none", "none");
    }

}
