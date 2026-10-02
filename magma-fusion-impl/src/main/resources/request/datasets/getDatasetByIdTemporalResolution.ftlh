SELECT DISTINCT
?labeltemporalResolutionLg1 ?labeltemporalResolutionLg2
FROM <${CODELIST_GRAPH}>

WHERE {
    <${uri}> skos:prefLabel ?labeltemporalResolutionLg1 .
    FILTER (lang(?labeltemporalResolutionLg1) = '${LG1}') .
    <${uri}> skos:prefLabel ?labeltemporalResolutionLg2 .
    FILTER (lang(?labeltemporalResolutionLg2) = '${LG2}') .
}