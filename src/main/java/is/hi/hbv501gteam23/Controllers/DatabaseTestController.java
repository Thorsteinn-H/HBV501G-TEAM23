package is.hi.hbv501gteam23.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class DatabaseTestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test-db")
    public String showAllTables(Model model) {
        try {
            // Simple connection test first
            jdbcTemplate.execute("SELECT 1");
            
            // Get application tables (exclude Spring Session tables and other system tables)
            List<String> tableNames = jdbcTemplate.queryForList(
                "SELECT table_name " +
                "FROM information_schema.tables " +
                "WHERE table_schema = 'public' " +
                "AND table_type = 'BASE TABLE' " +
                "AND table_name NOT LIKE 'spring_%' " +
                "AND table_name NOT LIKE 'database%' " +
                "AND table_name NOT LIKE 'pg_%' " +
                "ORDER BY table_name", 
                String.class
            );
            
            // Get row count for each table
            Map<String, Object> tableInfo = new LinkedHashMap<>();
            for (String tableName : tableNames) {
                try {
                    int count = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM \"" + tableName + "\"", 
                        Integer.class
                    );
                    tableInfo.put(tableName, count + " rows");
                } catch (Exception e) {
                    tableInfo.put(tableName, "Error: " + e.getMessage());
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("tables", tableInfo);
            result.put("tableCount", tableNames.size());
            
            model.addAttribute("result", result);
            model.addAttribute("status", "success");
            model.addAttribute("message", "Successfully connected to the database.");
            
        } catch (Exception e) {
            model.addAttribute("status", "error");
            model.addAttribute("message", "Database error: " + e.getMessage());
            e.printStackTrace();
        }
        return "database-test";
    }
}
