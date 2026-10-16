SELECT DISTINCT ?classification ?id ?prefLabelLg1 ?prefLabelLg2
WHERE {
	BIND('${LG1}' AS ?lg1)
	BIND('${LG2}' AS ?lg2)
	?classification a skos:ConceptScheme ; xkos:levels/rdf:rest*/rdf:first ?level .
	BIND(REPLACE(STR(?classification), '^http://id.insee.fr/codes/([^/]+).*$','$1' ) AS ?id)
	?classification dcterms:issued ?issued
	{
		SELECT ?id (MAX(?issued) AS ?maxIssued)
		WHERE {
			?classification a skos:ConceptScheme ; xkos:levels/rdf:rest*/rdf:first ?level .
			BIND(REPLACE(STR(?classification), '^http://id.insee.fr/codes/([^/]+).*$','$1' ) AS ?id)
			?classification dcterms:issued ?issued
		}
		GROUP BY ?id
	}
	FILTER(STR(?issued) = STR(?maxIssued))
	OPTIONAL {?classification skos:prefLabel ?prefLabelLg1. FILTER(LANG(?prefLabelLg1) = ?lg1) }
	OPTIONAL {?classification skos:prefLabel ?prefLabelLg2. FILTER(LANG(?prefLabelLg2) = ?lg2) }
}
ORDER BY ?id