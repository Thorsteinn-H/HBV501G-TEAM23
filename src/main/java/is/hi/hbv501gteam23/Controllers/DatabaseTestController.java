package is.hi.hbv501gteam23.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class DatabaseTestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test-db")
    public String testDatabaseConnection(Model model) {
        try {
            // Try to query a simple table that should exist in your database
            // If you have a 'users' table, you can use that, otherwise, you might need to adjust the query
            List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT * FROM users LIMIT 10");
            model.addAttribute("results", results);
            model.addAttribute("connectionStatus", "success");
            model.addAttribute("message", "Successfully connected to the database!");
        } catch (Exception e) {
            model.addAttribute("connectionStatus", "error");
            model.addAttribute("message", "Error connecting to database: " + e.getMessage());
        }
        return "database-test";
    }
}
