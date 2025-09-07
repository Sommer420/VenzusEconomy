package dk.sqmmer.venzusEconomy.cmd;

import dk.sqmmer.venzusEconomy.core.EconomyService;
import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.annotation.Context;
import eu.okaeri.commands.annotation.Executor;
import eu.okaeri.commands.bukkit.annotation.Permission;
import eu.okaeri.commands.service.CommandService;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Command(label = "eco")
public class EcoCmd implements CommandService {

    @Inject private EconomyService eco;

    @Executor
    public void help(@Context Player sender) {
        sender.sendMessage("§7/eco balance [spiller]");
        sender.sendMessage("§7/eco give <spiller> <beløb> §8(kræver venzuseco.admin)");
        sender.sendMessage("§7/eco take <spiller> <beløb> §8(kræver venzuseco.admin)");
    }

    @Executor(pattern = "balance")
    public void balanceSelf(@Context Player sender) {
        double bal = eco.getBalance(sender.getUniqueId());
        sender.sendMessage("§7Din saldo: §a" + String.format("%.2f", bal));
    }

    @Executor(pattern = "balance *")
    public void balanceOther(@Context Player sender, String name) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
        double bal = eco.getBalance(target.getUniqueId());
        sender.sendMessage("§7Saldo for §f" + target.getName() + "§7: §a" + String.format("%.2f", bal));
    }

    @Executor(pattern = "give * *")
    @Permission("venzuseco.admin")
    public void give(@Context Player sender, String name, double amount) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
        eco.deposit(target.getUniqueId(), amount);
        sender.sendMessage("§a+ " + amount + " til " + target.getName());
    }

    @Executor(pattern = "take * *")
    @Permission("venzuseco.admin")
    public void take(@Context Player sender, String name, double amount) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(name);
        boolean ok = eco.withdraw(target.getUniqueId(), amount);
        sender.sendMessage(ok ? "§c- " + amount + " fra " + target.getName() : "§cIkke nok saldo.");
    }
}
