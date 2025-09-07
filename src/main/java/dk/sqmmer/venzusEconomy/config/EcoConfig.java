package dk.sqmmer.venzusEconomy.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Configuration(path = "config.yml")
public class EcoConfig extends OkaeriConfig {
    public double startingBalance = 0.0;
    public String currencySingular = "coin";
    public String currencyPlural = "coins";
    public int fractionalDigits = 2;
}
