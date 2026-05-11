package com.alchemy.game;

import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class GameController {
    private final String dbUrl = "jdbc:sqlite:alchemy.db";
    private PotionFormula currentIdeal;
    private String currentRiddle, currentRecipeName;
    private final Map<String, PotionFormula> masterBook = new LinkedHashMap<>();
    private final Map<String, String> riddles = new HashMap<>();
    
    private boolean usePattern = true;

    public GameController() {
        masterBook.put("Эликсир света",   new PotionFormula(PotionFormula.Base.WATER, 0,0, 0,0, 0,0, 1,3, 2,1, 20)); 
        masterBook.put("Яд мертвецов",    new PotionFormula(PotionFormula.Base.ACID,  0,0, 3,3, 2,3, 0,0, 0,0, 85)); 
        masterBook.put("Облачный туман",  new PotionFormula(PotionFormula.Base.FOG,   1,1, 0,0, 0,0, 3,2, 0,0, 50)); 
        masterBook.put("Дыхание феникса", new PotionFormula(PotionFormula.Base.SMOKE, 3,3, 0,0, 0,0, 0,0, 2,2, 90)); 
        masterBook.put("Ледяная ярость",  new PotionFormula(PotionFormula.Base.WATER, 0,0, 0,0, 2,2, 0,0, 3,3, 30)); 
        masterBook.put("Слезы сирены",    new PotionFormula(PotionFormula.Base.OIL,   2,1, 2,1, 2,1, 0,0, 0,0, 50)); 

        riddles.put("Эликсир света", "Тьма наступает. Нужен свет. Разотри перья в пыль, а кристаллы лишь слегка.");
        riddles.put("Яд мертвецов", "Армия мертвых у ворот. Изотри грибы и мох в мельчайший прах!");
        riddles.put("Облачный туман", "Мне нужно исчезнуть. Слегка помни траву и раскроши перья.");
        riddles.put("Дыхание феникса", "Жар пустыни в бутылке. Травы и кристаллы должны стать чистой пылью.");
        riddles.put("Ледяная ярость", "Сердце севера. Мох раскроши, а кристаллы преврати в муку.");
        riddles.put("Слезы сирены", "Песнь моря... Травы, грибы и мох лишь слегка надломи.");

        try { Class.forName("org.sqlite.JDBC"); } catch (Exception e) {}
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS tavern (id INTEGER PRIMARY KEY, money INTEGER, unlocked TEXT)");
            ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM tavern");
            if (rs.next() && rs.getInt(1) == 0) conn.createStatement().execute("INSERT INTO tavern VALUES (1, 300, 'Эликсир света')");
        } catch (SQLException e) { e.printStackTrace(); }
        generateNewOrder();
    }

    private void generateNewOrder() {
        List<String> keys = new ArrayList<>(masterBook.keySet());
        currentRecipeName = keys.get(new Random().nextInt(keys.size()));
        currentIdeal = masterBook.get(currentRecipeName);
        currentRiddle = riddles.getOrDefault(currentRecipeName, "Клиент ждет чего-то особенного...");
    }

    @PostMapping("/toggle-mode")
    public Map<String, Boolean> toggleMode() {
        this.usePattern = !this.usePattern;
        return Collections.singletonMap("usePattern", this.usePattern);
    }

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> res = new HashMap<>();
        res.put("riddle", currentRiddle);
        res.put("usePattern", usePattern);
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM tavern WHERE id = 1");
            if (rs.next()) {
                res.put("money", rs.getInt("money"));
                res.put("unlocked", rs.getString("unlocked") == null ? "" : rs.getString("unlocked"));
            }
        } catch (SQLException e) {}
        return res;
    }

    @PostMapping("/serve")
    public Map<String, Object> serve(@RequestBody Map<String, Object> data) {
        int acc = 0;

        if (usePattern) {
            PotionFormula player = new PotionFormula(
                PotionFormula.Base.valueOf((String)data.get("base")),
                (int)data.get("herbs"), (int)data.get("gHerb"),
                (int)data.get("mushrooms"), (int)data.get("gMush"),
                (int)data.get("moss"), (int)data.get("gMoss"),
                (int)data.get("feathers"), (int)data.get("gFeather"),
                (int)data.get("crystals"), (int)data.get("gCrystal"),
                (int)data.get("brewTime")
            );
            acc = player.calculateCompositionAccuracy(currentIdeal);

        } else {

            String base = (String)data.get("base");
            int h = (int)data.get("herbs"), gh = (int)data.get("gHerb");
            int m = (int)data.get("mushrooms"), gm = (int)data.get("gMush");
            int ms = (int)data.get("moss"), gms = (int)data.get("gMoss");
            int f = (int)data.get("feathers"), gf = (int)data.get("gFeather");
            int c = (int)data.get("crystals"), gc = (int)data.get("gCrystal");
            int bt = (int)data.get("brewTime");

            if (base.equals(currentIdeal.getBase().toString())) {
                int penalty = 0;
                penalty += Math.abs(h - currentIdeal.getHerbs()) * 20;
                if (h > 0 && gh != currentIdeal.getgHerb()) penalty += 25;

                penalty += Math.abs(m - currentIdeal.getMushrooms()) * 20;
                if (m > 0 && gm != currentIdeal.getgMush()) penalty += 25;

                penalty += Math.abs(ms - currentIdeal.getMoss()) * 20;
                if (ms > 0 && gms != currentIdeal.getgMoss()) penalty += 25;

                penalty += Math.abs(f - currentIdeal.getFeathers()) * 20;
                if (f > 0 && gf != currentIdeal.getgFeather()) penalty += 25;

                penalty += Math.abs(c - currentIdeal.getCrystals()) * 20;
                if (c > 0 && gc != currentIdeal.getgCrystal()) penalty += 25;

                penalty += Math.abs(bt - currentIdeal.getBrewTime());
                acc = Math.max(0, 100 - penalty);
            }
        }

        Map<String, Object> res = new HashMap<>();
        if (acc > 75) {
            try (Connection conn = DriverManager.getConnection(dbUrl)) {
                ResultSet rs = conn.createStatement().executeQuery("SELECT unlocked FROM tavern WHERE id = 1");
                String unlocked = rs.next() ? rs.getString("unlocked") : "";
                if (!unlocked.contains(currentRecipeName)) {
                    unlocked = (unlocked.isEmpty()) ? currentRecipeName : unlocked + "," + currentRecipeName;
                    PreparedStatement pstmt = conn.prepareStatement("UPDATE tavern SET unlocked = ? WHERE id = 1");
                    pstmt.setString(1, unlocked); pstmt.executeUpdate();
                }
                conn.createStatement().execute("UPDATE tavern SET money = money + 150 WHERE id = 1");
            } catch (SQLException e) {}
            res.put("feedback", "УСПЕХ! (" + (usePattern ? "Паттерн" : "Без паттерна") + ")");
        } else {
            res.put("feedback", "БРАК. Точность: " + acc + "%");
        }
        generateNewOrder();
        return res;
    }

    @PostMapping("/buy-item")
    public Map<String, Integer> buyItem() {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.createStatement().execute("UPDATE tavern SET money = money - 5 WHERE id = 1");
            ResultSet rs = conn.createStatement().executeQuery("SELECT money FROM tavern WHERE id = 1");
            if (rs.next()) return Collections.singletonMap("money", rs.getInt("money"));
        } catch (SQLException e) {}
        return Collections.singletonMap("money", 0);
    }
}