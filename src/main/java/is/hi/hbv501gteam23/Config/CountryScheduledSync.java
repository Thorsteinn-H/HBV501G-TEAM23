package is.hi.hbv501gteam23.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountryScheduledSync {

    private final CountrySyncService countrySyncService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void dailySync() {
        countrySyncService.syncCountriesBulk();
    }
}
