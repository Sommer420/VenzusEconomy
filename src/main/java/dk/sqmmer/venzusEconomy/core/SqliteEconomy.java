package dk.sqmmer.venzusEconomy.core;

import dk.sqmmer.venzusEconomy.config.EcoConfig;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class SqliteEconomy implements EconomyService {

    private final File dbFile;
    private final EcoConfig cfg;

    public SqliteEconomy(File dataFolder, EcoConfig cfg) {
        this.dbFile = new File(dataFolder, "economy.db");
        this.cfg = cfg;
        initSchema();
    }

    private Connection con() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
    }

    private void initSchema() {
        try (Connection c = con(); Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (" +
                    "uuid TEXT PRIMARY KEY," +
                    "balance REAL NOT NULL DEFAULT 0" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override public boolean hasAccount(UUID uuid) {
        try (Connection c = con();
             PreparedStatement ps = c.prepareStatement("SELECT 1 FROM accounts WHERE uuid=?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public void createAccount(UUID uuid) {
        if (hasAccount(uuid)) return;
        try (Connection c = con();
             PreparedStatement ps = c.prepareStatement("INSERT INTO accounts(uuid,balance) VALUES(?, ?)")) {
            ps.setString(1, uuid.toString());
            ps.setDouble(2, cfg.startingBalance);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override public double getBalance(UUID uuid) {
        try (Connection c = con();
             PreparedStatement ps = c.prepareStatement("SELECT balance FROM accounts WHERE uuid=?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : cfg.startingBalance;
            }
        } catch (SQLException e) { e.printStackTrace(); return cfg.startingBalance; }
    }

    @Override public boolean deposit(UUID uuid, double amount) {
        if (amount < 0) return false;
        createAccount(uuid);
        try (Connection c = con();
             PreparedStatement ps = c.prepareStatement("UPDATE accounts SET balance=balance+? WHERE uuid=?")) {
            ps.setDouble(1, amount);
            ps.setString(2, uuid.toString());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public boolean withdraw(UUID uuid, double amount) {
        if (amount < 0) return false;
        createAccount(uuid);
        double bal = getBalance(uuid);
        if (bal < amount) return false;
        try (Connection c = con();
             PreparedStatement ps = c.prepareStatement("UPDATE accounts SET balance=balance-? WHERE uuid=?")) {
            ps.setDouble(1, amount);
            ps.setString(2, uuid.toString());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
