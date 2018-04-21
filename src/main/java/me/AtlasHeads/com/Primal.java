package me.AtlasHeads.com;

import java.io.File;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class Primal extends JavaPlugin {

	public static Plugin instance;

	public static FileConfiguration configsConfig;
	public static File configs;
	public static Economy economy = null;

	public void onEnable() {
		instance = this;

		configs = new File(getDataFolder(), "settings.yml");
		configsConfig = YamlConfiguration.loadConfiguration(configs);
		saveConfigs();
		if (!setupEconomy()) {
			getLogger().severe(String.format("[%s] - Disabled. Error Reason: No Economy plugin found!", new Object[] { getDescription().getName() }));
			getServer().getPluginManager().disablePlugin(this);
		}
		getCommand("head").setExecutor(new Methods());
		getServer().getPluginManager().registerEvents(new Methods(), this);
	}

	private boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}

	public static void saveConfigs() {
		try{
			configsConfig.save(configs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static void loadConfigs() {
		try{
			configsConfig.load(configs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}