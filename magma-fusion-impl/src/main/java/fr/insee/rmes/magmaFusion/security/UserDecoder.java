package fr.insee.rmes.magmaFusion.security;


import fr.insee.rmes.magmaFusion.utils.RmesException;

import java.util.Optional;

public interface UserDecoder {

    Optional<User> fromPrincipal(Object principal) throws RmesException;
}
