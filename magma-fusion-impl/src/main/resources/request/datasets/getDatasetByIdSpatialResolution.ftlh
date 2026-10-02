SELECT DISTINCT
?spatialResolutionId ?labelspatialResolutionLg1 ?labelspatialResolutionLg2
FROM <${CODELIST_GRAPH}>

WHERE {
    <${uri}> skos:notation ?spatialResolutionId .
    <${uri}> skos:prefLabel ?labelspatialResolutionLg1 .
    FILTER (lang(?labelspatialResolutionLg1) = '${LG1}') .
    <${uri}> skos:prefLabel ?labelspatialResolutionLg2 .
    FILTER (lang(?labelspatialResolutionLg2) = '${LG2}') .
}