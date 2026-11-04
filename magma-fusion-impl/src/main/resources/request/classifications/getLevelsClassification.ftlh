#Liste des niveaux d'une nomenclature
SELECT ?id ?idLevel ?level ?depth ?prefLabelLevelLg1 ?prefLabelLevelLg2
WHERE {
	BIND('${LG1}' AS ?lg1)
	BIND('${LG2}' AS ?lg2)
	BIND('${nomenclature}' AS ?nomenclature)
	?classification a skos:ConceptScheme ; xkos:levels/rdf:rest*/rdf:first ?level .
	BIND(REPLACE(STR(?classification), '^http://id.insee.fr/codes/([^/]+).*$','$1' ) AS ?id)
	FILTER (?id=?nomenclature)
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

	BIND(REPLACE( STR(?level), '^.+/([^/]+)$','$1' ) AS ?idLevel )
	OPTIONAL {?level skos:prefLabel ?prefLabelLevelLg1. FILTER(LANG(?prefLabelLevelLg1) = ?lg1) }
	OPTIONAL {?level skos:prefLabel ?prefLabelLevelLg2. FILTER(LANG(?prefLabelLevelLg2) = ?lg2) }

	?level xkos:depth ?depth .
}
ORDER BY ?depth