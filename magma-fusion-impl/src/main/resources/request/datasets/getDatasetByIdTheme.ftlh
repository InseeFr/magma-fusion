SELECT DISTINCT ?uri ?labelThemeLg1 ?labelThemeLg2 ?themeTaxonomy
FROM <${DATASETS_GRAPH}>
FROM <${CONCEPTS_GRAPH}>
WHERE {
    VALUES ?uri {<${uri}>}
   	<${uri}> skos:prefLabel ?labelThemeLg1 .
    FILTER (lang(?labelThemeLg1) = '${LG1}')
	OPTIONAL{
    	<${uri}> skos:prefLabel ?labelThemeLg2 .
        FILTER (lang(?labelThemeLg2) = '${LG2}')
    }
	OPTIONAL{
		<${uri}> skos:inScheme ?themeDetail .
		?themeDetail skos:notation ?themeTaxonomy
    }
}