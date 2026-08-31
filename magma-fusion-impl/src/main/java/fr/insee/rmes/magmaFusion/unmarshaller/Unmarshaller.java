package fr.insee.rmes.magmaFusion.unmarshaller;

import fr.insee.rmes.magmaFusion.queryexecutor.Csv;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface Unmarshaller {
        <G> Optional<G> unmarshal(@NonNull Csv csv, @NonNull Class<G> targetClass);

        <G> G unmarshalOrNull(@NonNull Csv csv, @NonNull Class<G> targetClass);

        <G> List<G> unmarshalList(@NonNull Csv csv, @NonNull Class<G> targetClass);
}
