package me.GravityIO.BlockMiner;

import org.bukkit.plugin.java.JavaPlugin;

import me.GravityIO.BlockMiner.Commands.Commands;

public class Main extends JavaPlugin {

	public static Main main;

	public void onEnable() {
		this.getCommand(Commands.mineCmd).setExecutor(new Commands());
		this.saveDefaultConfig();
		main = this;
	}

	public void onDisable() {

	}
}
