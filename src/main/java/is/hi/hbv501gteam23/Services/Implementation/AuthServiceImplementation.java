package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Repositories.AuthRepository;
import is.hi.hbv501gteam23.Services.Interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Nafn : Þorsteinn H. Erlendsson
 * Tölvupóstur: the85@hi.is
 * Lýsing:
 **/

@Service
public class AuthServiceImplementation implements AuthService {
    private AuthRepository authRepository;

    @Autowired
    public AuthServiceImplementation(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
}
