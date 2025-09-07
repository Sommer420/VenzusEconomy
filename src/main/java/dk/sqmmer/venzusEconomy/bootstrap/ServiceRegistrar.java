package dk.sqmmer.venzusEconomy.bootstrap;

import dk.sqmmer.venzusEconomy.core.EconomyService;
import dk.sqmmer.venzusEconomy.vault.VaultEconomyBridge;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.bukkit.OkaeriBukkitPlugin;
import eu.okaeri.platform.core.annotation.Component;
import eu.okaeri.platform.core.plan.ExecutionPhase;
import eu.okaeri.platform.core.plan.Planned;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

@Component
public class ServiceRegistrar {

    @Inject private OkaeriBukkitPlugin plugin;
    @Inject private EconomyService economy;
    @Inject private VaultEconomyBridge vault;

    @Planned(ExecutionPhase.STARTUP)
    public void register() {
        Bukkit.getServicesManager().register(EconomyService.class, economy, plugin, ServicePriority.Normal);

        if (vault != null) {
            Bukkit.getServicesManager().register(net.milkbowl.vault.economy.Economy.class, vault, plugin, ServicePriority.Normal);
        }
    }

    @Planned(ExecutionPhase.SHUTDOWN)
    public void unregister() {
        Bukkit.getServicesManager().unregister(EconomyService.class, economy);
        if (vault != null) {
            Bukkit.getServicesManager().unregister(net.milkbowl.vault.economy.Economy.class, vault);
        }
    }
}
