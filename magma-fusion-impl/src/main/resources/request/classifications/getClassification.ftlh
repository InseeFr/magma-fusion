SELECT DISTINCT ?classification ?id ?prefLabelLg1 ?prefLabelLg2
(GROUP_CONCAT(DISTINCT ?altLabelLg1; SEPARATOR=",") AS ?altLabelsLg1)
(GROUP_CONCAT(DISTINCT ?altLabelLg2; SEPARATOR=",") AS ?altLabelsLg2)
?descriptionLg1 ?descriptionLg2
?issued ?lastRefreshedOn
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
	OPTIONAL {?classification skos:prefLabel ?prefLabelLg1. FILTER(LANG(?prefLabelLg1) = ?lg1) }
	OPTIONAL {?classification skos:prefLabel ?prefLabelLg2. FILTER(LANG(?prefLabelLg2) = ?lg2) }

	OPTIONAL {?classification skos:altLabel ?altLabelLg1. FILTER(LANG(?altLabelLg1) = ?lg1) }
	OPTIONAL {?classification skos:altLabel ?altLabelLg2. FILTER(LANG(?altLabelLg2) = ?lg2) }

	OPTIONAL {?classification dc:description ?descriptionLg1. FILTER(LANG(?descriptionLg1) = ?lg1) }
	OPTIONAL {?classification dc:description ?descriptionLg2. FILTER(LANG(?descriptionLg2) = ?lg2) }

	OPTIONAL {?classification dcterms:issued ?issued. }
	OPTIONAL {?classification pav:lastRefreshedOn ?lastRefreshedOn. }
}
GROUP BY ?classification ?id ?prefLabelLg1 ?prefLabelLg2 ?descriptionLg1 ?descriptionLg2
?issued ?lastRefreshedOn
ORDER BY ?id