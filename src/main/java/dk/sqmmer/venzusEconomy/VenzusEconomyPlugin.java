package dk.sqmmer.venzusEconomy;

import dk.sqmmer.venzusEconomy.config.EcoConfig;
import dk.sqmmer.venzusEconomy.core.EconomyService;
import dk.sqmmer.venzusEconomy.core.SqliteEconomy;
import dk.sqmmer.venzusEconomy.vault.VaultEconomyBridge;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import eu.okaeri.platform.bukkit.OkaeriBukkitPlugin;
import eu.okaeri.platform.core.annotation.Bean;
import eu.okaeri.platform.core.annotation.Scan;
import eu.okaeri.platform.core.config.EmptyConfig;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;

@Scan(deep = true)
public final class VenzusEconomyPlugin extends OkaeriBukkitPlugin {

    @Bean
    public BukkitScheduler scheduler() {
        return Bukkit.getScheduler();
    }

    @Bean
    public EcoConfig ecoConfig() {
        return ConfigManager.create(EcoConfig.class, it -> {
            it.withConfigurer(new YamlSnakeYamlConfigurer());
            it.withBindFile(new File(getDataFolder(), "config.yml"));
            it.saveDefaults();
            it.load(true);
        });
    }

    @Bean
    public EconomyService economyService(EcoConfig cfg) {
        return new SqliteEconomy(getDataFolder(), cfg);
    }

    @Bean
    public VaultEconomyBridge vaultBridge(EconomyService service) {
        return new VaultEconomyBridge(service);
    }

    @Bean
    public EmptyConfig placeholder() {
        return ConfigManager.create(EmptyConfig.class, it -> {
            it.withConfigurer(new YamlSnakeYamlConfigurer());
            it.withBindFile(new File(getDataFolder(), "placeholder.yml"));
            it.saveDefaults();
            it.load(true);
        });
    }
}
