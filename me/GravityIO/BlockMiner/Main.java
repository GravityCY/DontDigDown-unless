package me.GravityIO.BlockMiner;

import org.bukkit.plugin.java.JavaPlugin;

import me.GravityIO.BlockMiner.Commands.Commands;

public class Main extends JavaPlugin {

	public static Main main;

	public void onEnable() {
		this.getCommand(new Commands().cmd1).setExecutor(new Commands());
		main = this;
	}

	public void onDisable() {

	}
}
