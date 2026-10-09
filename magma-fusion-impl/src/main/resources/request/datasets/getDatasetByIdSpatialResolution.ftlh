SELECT DISTINCT
?spatialResolutionId ?labelSpatialResolutionLg1 ?labelSpatialResolutionLg2
FROM <${CODELIST_GRAPH}>

WHERE {
    <${uri}> skos:notation ?spatialResolutionId .
    <${uri}> skos:prefLabel ?labelSpatialResolutionLg1 .
    FILTER (lang(?labelSpatialResolutionLg1) = '${LG1}') .
    <${uri}> skos:prefLabel ?labelSpatialResolutionLg2 .
    FILTER (lang(?labelSpatialResolutionLg2) = '${LG2}') .
}