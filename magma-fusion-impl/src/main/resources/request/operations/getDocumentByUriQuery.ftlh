SELECT ?uri ?url ?labelLg1 ?labelLg2 ?commentLg1 ?commentLg2 ?dateMiseAJour ?langue ?taille
FROM <http://rdf.insee.fr/graphes/qualite/documents>
WHERE {
    BIND(<${uri}> AS ?uri)
    ?uri rdf:type foaf:Document ;
         schema:url ?url .

    OPTIONAL { ?uri rdfs:label ?labelLg1 . FILTER(lang(?labelLg1) = '${LG1}') }
    OPTIONAL { ?uri rdfs:label ?labelLg2 . FILTER(lang(?labelLg2) = '${LG2}') }
    OPTIONAL { ?uri rdfs:comment ?commentLg1 . FILTER(lang(?commentLg1) = '${LG1}') }
    OPTIONAL { ?uri rdfs:comment ?commentLg2 . FILTER(lang(?commentLg2) = '${LG2}') }
    OPTIONAL { ?uri pav:lastRefreshedOn ?dateMiseAJour . }
    OPTIONAL { ?uri dc:language ?langue . }
    OPTIONAL { ?uri dcterms:extent ?taille . }
}
LIMIT 1
