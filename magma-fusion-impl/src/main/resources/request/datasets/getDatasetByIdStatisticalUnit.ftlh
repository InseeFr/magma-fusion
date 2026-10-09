SELECT DISTINCT
?statisticalUnitId ?labelStatisticalUnitLg1  ?labelStatisticalUnitLg2
FROM <${CODELIST_GRAPH}>

WHERE {
    <${uri}> skos:notation ?statisticalUnitId .
    <${uri}> skos:prefLabel ?labelStatisticalUnitLg1 .
    FILTER (lang(?labelStatisticalUnitLg1) = '${LG1}') .
    <${uri}> skos:prefLabel ?labelStatisticalUnitLg2 .
    FILTER (lang(?labelStatisticalUnitLg2) = '${LG2}') .
}