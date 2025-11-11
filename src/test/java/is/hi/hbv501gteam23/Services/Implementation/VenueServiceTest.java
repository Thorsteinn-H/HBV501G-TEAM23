package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.Venue;
import is.hi.hbv501gteam23.Services.Interfaces.VenueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class VenueServiceTest {

    @Autowired
    private VenueService venueService;

    @Test
    void testFindByFilters() {
        List<Venue> all = venueService.findByFilters(null, null, null);
        assertFalse(all.isEmpty(), "There should be some venues in the DB");

        List<Venue> byId = venueService.findByFilters(1L, null, null);
        assertEquals(1, byId.size(), "Should return exactly one venue for id=1");

        List<Venue> byName = venueService.findByFilters(null, "Laugardalsvöllur", null);
        assertEquals(1, byName.size(), "Should return exactly one venue for this name");
    }
}
