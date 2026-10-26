SELECT ?id ?uri ?intituleLg1 ?hasLink ?collectionUri
FROM <http://rdf.insee.fr/graphes/concepts/definitions>
WHERE {
  ?uri skos:inScheme ?conceptScheme .
  FILTER(REGEX(STR(?conceptScheme),'/concepts/definitions/scheme'))

<#if collection != "">
  OPTIONAL {
    ?collectionUri skos:member ?uri ; a skos:Collection .
  }
  FILTER (
    STRENDS(STR(?collectionUri), '/${collection}')
  )
</#if>

  ?uri skos:notation ?id .
  ?uri skos:prefLabel ?intitule .
  FILTER(lang(?intitule) = 'fr')
  FILTER(CONTAINS(LCASE(STR(?intitule)), LCASE("${libelle}")))
  BIND(STR(?intitule) AS ?intituleLg1)
  BIND(EXISTS{?uri (skos:broader|skos:narrower|skos:related|dcterms:replaces|dcterms:isReplacedBy|skos:closeMatch|dcterms:references) ?repl } AS ?hasLink)
}
ORDER BY ?intituleLg1