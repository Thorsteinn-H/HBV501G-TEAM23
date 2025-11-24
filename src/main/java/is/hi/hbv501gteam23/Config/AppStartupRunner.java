package is.hi.hbv501gteam23.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppStartupRunner implements ApplicationRunner {

    private final CountrySyncService countrySyncService;

    @Override
    public void run(ApplicationArguments args) {
        countrySyncService.syncCountriesBulk();
        System.out.println("Countries table synchronized on startup.");
    }
}
