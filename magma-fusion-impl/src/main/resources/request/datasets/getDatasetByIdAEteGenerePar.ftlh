SELECT DISTINCT ?wasGeneratedById ?labelWasGeneratedByLg1  ?labelWasGeneratedByLg2

WHERE {
    BIND(STRAFTER(STR(<${uri}>), '/operations/') AS ?wasGeneratedByTempId)
    BIND(STRAFTER(STR(?wasGeneratedByTempId), '/') AS ?wasGeneratedById)
    <${uri}> rdf:type ?statisticalOperation .
    <${uri}> skos:prefLabel ?labelWasGeneratedByLg1 .
    FILTER(lang(?labelWasGeneratedByLg1)= '${LG1}') .
    <${uri}> skos:prefLabel ?labelWasGeneratedByLg2 .
    FILTER(lang(?labelWasGeneratedByLg2)= '${LG2}') .
}
