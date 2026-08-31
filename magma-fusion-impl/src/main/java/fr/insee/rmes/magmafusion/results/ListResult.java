package fr.insee.rmes.magmafusion.results;

import fr.insee.rmes.magmafusion.utils.EndpointsUtils;
import org.springframework.http.ResponseEntity;

import java.util.List;

public record ListResult<E>(List<E> result) {
    public ResponseEntity<List<E>> toResponseEntity() {
        return EndpointsUtils.toResponseEntity(result);
    }
}
