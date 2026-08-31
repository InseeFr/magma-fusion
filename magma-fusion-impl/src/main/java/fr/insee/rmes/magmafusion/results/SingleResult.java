package fr.insee.rmes.magmafusion.results;

import fr.insee.rmes.magmafusion.utils.EndpointsUtils;
import org.springframework.http.ResponseEntity;

public record SingleResult<E>(E result) {
    public ResponseEntity<E> toResponseEntity() {
        return EndpointsUtils.toResponseEntity(result);
    }
}
