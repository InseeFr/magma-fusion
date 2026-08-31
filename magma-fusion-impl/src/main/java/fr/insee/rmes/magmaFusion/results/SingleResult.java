package fr.insee.rmes.magmaFusion.results;

import fr.insee.rmes.magmaFusion.utils.EndpointsUtils;
import org.springframework.http.ResponseEntity;

public record SingleResult<E>(E result) {
    public ResponseEntity<E> toResponseEntity() {
        return EndpointsUtils.toResponseEntity(result);
    }
}
