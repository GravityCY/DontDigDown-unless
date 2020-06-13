package me.GravityIO.BlockMiner.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.GravityIO.BlockMiner.Main;
import me.GravityIO.BlockMiner.Miner;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	public static String mineCmd = "mine";

	private Miner miner = new Miner();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase(mineCmd)) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can use this command!");
				return true;
			}
			Player player = (Player) sender;
			if (args.length == 0) {
				miner.mine(player);
				return true;
			} else {
				if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
					Main.main.reloadConfig();
					player.sendMessage(ChatColor.GREEN + "Succesfully reloaded config file!");
					return true;
				}
			}

		}
		return false;
	}
}
