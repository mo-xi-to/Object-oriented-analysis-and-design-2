package com.alchemy.game;

import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class GameController {
    private final String dbUrl = "jdbc:sqlite:good_potion_web.db";
    private PotionFormula currentIdeal;
    private String currentText;

    public GameController() {
        try { Class.forName("org.sqlite.JDBC"); } catch (Exception e) {}
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS tavern (id INTEGER PRIMARY KEY, money INTEGER)");
            ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM tavern");
            if (rs.next() && rs.getInt(1) == 0) conn.createStatement().execute("INSERT INTO tavern (id, money) VALUES (1, 0)");
        } catch (SQLException e) { e.printStackTrace(); }
        generateNewOrder();
    }

    private void generateNewOrder() {
        int type = new Random().nextInt(3);
        if (type == 0) {
            currentText = "Мне нужен легкий целебный отвар. Основа - вода, кинь 2 травы и вари ровно 5 секунд.";
            currentIdeal = new PotionFormula(PotionFormula.Base.WATER, 2, 0, 5);
        } else if (type == 1) {
            currentText = "Сделай крепкую настойку. Масло, 3 травы, 1 гриб. Варить долго, 10 секунд!";
            currentIdeal = new PotionFormula(PotionFormula.Base.OIL, 3, 1, 10);
        } else {
            currentText = "Я отравился! Срочно: вода, 3 гриба (без травы) и вари 7 секунд!";
            currentIdeal = new PotionFormula(PotionFormula.Base.WATER, 0, 3, 7);
        }
    }

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("orderText", currentText);
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT money FROM tavern WHERE id = 1");
            response.put("money", rs.getInt("money"));
        } catch (SQLException e) { response.put("money", 0); }
        return response;
    }

    @PostMapping("/serve")
    public Map<String, Object> servePotion(@RequestBody Map<String, Object> payload) {
        PotionFormula.Base base = PotionFormula.Base.valueOf((String) payload.get("base"));
        int herbs = (int) payload.get("herbs");
        int mushrooms = (int) payload.get("mushrooms");
        int boilTime = (int) payload.get("boilTime");

        PotionFormula playerFormula = new PotionFormula(base, herbs, mushrooms, boilTime);

        int accuracy = playerFormula.calculateAccuracy(currentIdeal);
        String feedback = playerFormula.generateFeedback(currentIdeal);

        int payment = accuracy > 80 ? 20 : (accuracy > 50 ? 10 : 0);
        if (payment > 0) {
            try (Connection conn = DriverManager.getConnection(dbUrl)) {
                PreparedStatement stmt = conn.prepareStatement("UPDATE tavern SET money = money + ? WHERE id = 1");
                stmt.setInt(1, payment); stmt.executeUpdate();
            } catch (SQLException e) { e.printStackTrace(); }
        }

        generateNewOrder();

        Map<String, Object> response = new HashMap<>();
        response.put("accuracy", accuracy);
        response.put("feedback", feedback);
        response.put("payment", payment);
        return response;
    }
}