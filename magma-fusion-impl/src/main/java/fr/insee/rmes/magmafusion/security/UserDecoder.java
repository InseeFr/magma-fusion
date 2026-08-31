package fr.insee.rmes.magmafusion.security;


import fr.insee.rmes.magmafusion.utils.RmesException;

import java.util.Optional;

public interface UserDecoder {

    Optional<User> fromPrincipal(Object principal) throws RmesException;
}
