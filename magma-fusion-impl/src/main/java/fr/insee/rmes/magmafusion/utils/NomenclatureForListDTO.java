package fr.insee.rmes.magmafusion.utils;

import java.net.URI;

public record NomenclatureForListDTO (
    String id,
    URI classification,
    String prefLabelLg1,
    String prefLabelLg2
){

        }
