package dk.sqmmer.venzusEconomy.vault;

import dk.sqmmer.venzusEconomy.config.EcoConfig;
import dk.sqmmer.venzusEconomy.core.EconomyService;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Collections;
import java.util.List;

public class VaultEconomyBridge implements Economy {

    private final EconomyService eco;

    public VaultEconomyBridge(EconomyService eco) { this.eco = eco; }

    private EcoConfig cfg() { return new EcoConfig(); }

    @Override public boolean isEnabled() { return true; }
    @Override public String getName() { return "VenzusEconomy"; }
    @Override public boolean hasBankSupport() { return false; }
    @Override public int fractionalDigits() { return cfg().fractionalDigits; }
    @Override public String format(double amount) { return String.format("%." + fractionalDigits() + "f", amount); }
    @Override public String currencyNamePlural() { return cfg().currencyPlural; }
    @Override public String currencyNameSingular() { return cfg().currencySingular; }

    // Accounts (global)
    @Override public boolean hasAccount(OfflinePlayer player) { return eco.hasAccount(player.getUniqueId()); }
    @Override public boolean hasAccount(String playerName) { return hasAccount(Bukkit.getOfflinePlayer(playerName)); }
    @Override public boolean createPlayerAccount(OfflinePlayer player) { eco.createAccount(player.getUniqueId()); return true; }
    @Override public boolean createPlayerAccount(String playerName) { return createPlayerAccount(Bukkit.getOfflinePlayer(playerName)); }

    @Override public boolean hasAccount(OfflinePlayer player, String worldName) { return hasAccount(player); }
    @Override public boolean hasAccount(String playerName, String worldName) { return hasAccount(playerName); }
    @Override public boolean createPlayerAccount(OfflinePlayer player, String worldName) { return createPlayerAccount(player); }
    @Override public boolean createPlayerAccount(String playerName, String worldName) { return createPlayerAccount(playerName); }

    @Override public double getBalance(OfflinePlayer player) { return eco.getBalance(player.getUniqueId()); }
    @Override public double getBalance(String playerName) { return getBalance(Bukkit.getOfflinePlayer(playerName)); }
    @Override public double getBalance(OfflinePlayer player, String worldName) { return getBalance(player); }
    @Override public double getBalance(String playerName, String worldName) { return getBalance(playerName); }

    @Override public boolean has(OfflinePlayer player, double amount) { return getBalance(player) >= amount; }
    @Override public boolean has(String playerName, double amount) { return getBalance(playerName) >= amount; }
    @Override public boolean has(OfflinePlayer player, String worldName, double amount) { return has(player, amount); }
    @Override public boolean has(String playerName, String worldName, double amount) { return has(playerName, amount); }

    @Override public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        boolean ok = eco.deposit(player.getUniqueId(), amount);
        return new EconomyResponse(amount, getBalance(player),
                ok ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, "");
    }
    @Override public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }
    @Override public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }
    @Override public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        boolean ok = eco.withdraw(player.getUniqueId(), amount);
        return new EconomyResponse(amount, getBalance(player),
                ok ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, "");
    }
    @Override public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }
    @Override public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }
    @Override public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override public EconomyResponse createBank(String name, String player) { return notSupported(); }
    @Override public EconomyResponse createBank(String name, OfflinePlayer player) { return notSupported(); }
    @Override public EconomyResponse deleteBank(String name) { return notSupported(); }
    @Override public EconomyResponse bankBalance(String name) { return notSupported(); }
    @Override public EconomyResponse bankHas(String name, double amount) { return notSupported(); }
    @Override public EconomyResponse bankWithdraw(String name, double amount) { return notSupported(); }
    @Override public EconomyResponse bankDeposit(String name, double amount) { return notSupported(); }
    @Override public EconomyResponse isBankOwner(String name, String playerName) { return notSupported(); }
    @Override public EconomyResponse isBankOwner(String name, OfflinePlayer player) { return notSupported(); }
    @Override public EconomyResponse isBankMember(String name, String playerName) { return notSupported(); }
    @Override public EconomyResponse isBankMember(String name, OfflinePlayer player) { return notSupported(); }
    @Override public List<String> getBanks() { return Collections.emptyList(); }

    private EconomyResponse notSupported() {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks not supported");
    }
}
