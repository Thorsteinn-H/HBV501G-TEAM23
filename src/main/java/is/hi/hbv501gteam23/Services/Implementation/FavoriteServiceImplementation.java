package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Repositories.FavoriteRepository;
import is.hi.hbv501gteam23.Services.Interfaces.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Nafn : Þorsteinn H. Erlendsson
 * Tölvupóstur: the85@hi.is
 * Lýsing:
 **/

@Service
public class FavoriteServiceImplementation implements FavoriteService {
    private FavoriteRepository favoriteRepository;

    @Autowired
    public FavoriteServiceImplementation(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }
}
