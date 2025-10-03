package is.hi.hbv501gteam23;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import is.hi.hbv501gteam23.Persistence.Entities.User;

@SpringBootApplication
// Explicitly specify which components to scan
@ComponentScan({
    "is.hi.hbv501gteam23.Services.Implementation",
    "is.hi.hbv501gteam23.Services.Interfaces",
    "is.hi.hbv501gteam23.Controllers"
})
@EnableJpaRepositories("is.hi.hbv501gteam23.Persistence.Repositories")
@EntityScan(
    basePackages = "is.hi.hbv501gteam23.Persistence.Entities",
    basePackageClasses = {User.class}
)
public class Hbv501GTeam23Application {

    public static void main(String[] args) {
        // Disable database initialization for now
        System.setProperty("spring.jpa.hibernate.ddl-auto", "none");
        System.setProperty("spring.datasource.initialization-mode", "never");
        
        SpringApplication.run(Hbv501GTeam23Application.class, args);
    }

}
