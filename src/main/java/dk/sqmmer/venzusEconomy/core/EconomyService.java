package dk.sqmmer.venzusEconomy.core;

import java.util.UUID;

public interface EconomyService {
    boolean hasAccount(UUID uuid);
    void createAccount(UUID uuid);
    double getBalance(UUID uuid);
    boolean deposit(UUID uuid, double amount);
    boolean withdraw(UUID uuid, double amount);

    boolean set(UUID uuid, double amount);
}
