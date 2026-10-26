SELECT DISTINCT ?uri ?intituleLg1 ?intituleLg2
?definitionLg1 ?definitionLg2 ?noteEditorialeLg1 ?noteEditorialeLg2 ?scopeNoteLg1 ?scopeNoteLg2
 ?dateMiseAJour ?dateCreation ?dateFinDeValidite ?hasLink ?hasIntitulesAlternatifs ?id
FROM <${CONCEPTS_DEFINITIONS_GRAPH}>
WHERE {
			BIND("${id}" AS ?id) .
			?uri skos:inScheme ?uriScheme .
			FILTER(REGEX(STR(?uriScheme),'/concepts/definitions/scheme')) 
			FILTER(REGEX(STR(?uri),'/concepts/definition/${id}')) .

            OPTIONAL {
			    ?uri skos:prefLabel ?intituleLg1 .
			    FILTER(lang(?intituleLg1) = '${LG1}')
            }
            OPTIONAL {
			    ?uri skos:prefLabel ?intituleLg2 .
			    FILTER(lang(?intituleLg2) = '${LG2}')
			}

			BIND(EXISTS{?uri (skos:broader|skos:narrower|skos:related|dcterms:replaces|dcterms:isReplacedBy|skos:closeMatch|dcterms:references) ?repl } AS ?hasLink)
			
			OPTIONAL{ ?uri dcterms:modified ?dateMiseAJour }
			OPTIONAL{ ?uri dcterms:created ?dateCreation  }
			OPTIONAL{ ?uri dcterms:valid ?dateFinDeValidite }
		
			
			OPTIONAL {
				?uri skos:definition ?defLg1 .
				?defLg1 dcterms:language 'fr'^^xsd:language .
				?defLg1 evoc:noteLiteral ?definitionLg1 .
				?defLg1 pav:version ?vdLg1 .
				filter not exists {
					?uri skos:definition ?def2Lg1 .
					?def2Lg1 dcterms:language 'fr'^^xsd:language .
					?def2Lg1 pav:version ?v2dLg1 .
					filter (?v2dLg1 > ?vdLg1)
				}
			}  
			
			OPTIONAL {
				?uri skos:definition ?defLg2 .
				?defLg2 dcterms:language 'en'^^xsd:language .
				?defLg2 evoc:noteLiteral ?definitionLg2 .
				?defLg2 pav:version ?vdLg2 .
				filter not exists {
					?uri skos:definition ?def2Lg2 .
					?def2Lg2 dcterms:language 'en'^^xsd:language .
					?def2Lg2 pav:version ?v2dLg2 .
					filter (?v2dLg2 > ?vdLg2)
				}
			}   

			OPTIONAL {
				?uri skos:scopeNote ?scopeLg1 .
				?scopeLg1 dcterms:language 'fr'^^xsd:language .
				?scopeLg1 evoc:noteLiteral ?scopeNoteLg1 .
				?scopeLg1 pav:version ?vsfr .
				filter not exists {
					?uri skos:scopeNote ?scope2Lg1 .
					?scope2Lg1 dcterms:language 'fr'^^xsd:language .
					?scope2Lg1 pav:version ?v2sLg1 .
					filter (?v2sLg1 > ?vsLg1)
				}				
			}   

			OPTIONAL {
				?uri skos:scopeNote ?scopeLg2 .
				?scopeLg2 dcterms:language 'en'^^xsd:language .
				?scopeLg2 evoc:noteLiteral ?scopeNoteLg2 .
				?scopeLg2 pav:version ?vsLg2 .
				filter not exists {
					?uri skos:scopeNote ?scope2Lg2 .
					?scope2Lg2 dcterms:language 'en'^^xsd:language .
					?scope2Lg2 pav:version ?v2sLg2 .
					filter (?v2sLg2 > ?vsLg2)
				}
			} 

			OPTIONAL {
				?uri skos:editorialNote ?editorialLg1 .
				?editorialLg1 dcterms:language 'fr'^^xsd:language .
				?editorialLg1 evoc:noteLiteral ?noteEditorialeLg1 .
				?editorialLg1 pav:version ?veLg1 .
				filter not exists {
					?uri skos:editorialNote ?editorial2Lg1 .
					?editorial2Lg1 dcterms:language 'fr'^^xsd:language .
					?editorial2Lg1 pav:version ?v2eLg1 .
					filter (?v2eLg1 > ?veLg1)
				}				
			} 

			OPTIONAL {
				?uri skos:editorialNote ?editorialLg2 .
				?editorialLg2 dcterms:language 'en'^^xsd:language .
				?editorialLg2 evoc:noteLiteral ?noteEditorialeLg2 .
				?editorialLg2 pav:version ?veLg2 .
				filter not exists {
					?uri skos:editorialNote ?editorial2Lg2 .
					?editorial2Lg2 dcterms:language 'en'^^xsd:language .
					?editorial2Lg2 pav:version ?v2eLg2 .
					filter (?v2eLg2 > ?veLg2)
				}
			}

			BIND(EXISTS{?uri skos:altLabel ?labelAlt } AS ?hasIntitulesAlternatifs)

}
ORDER BY DESC(?dateMiseAJour) 
LIMIT 1