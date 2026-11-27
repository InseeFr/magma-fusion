SELECT DISTINCT ?concept
WHERE {
    ?rubrique insee:additionalMaterial/rdf:rest*/rdf:first <${uri}> .
    ?attribute a sdmx-mm:ReportedAttribute ;
               ?property ?rubrique .
    ?property sdmx-mm:concept ?concept .
}
