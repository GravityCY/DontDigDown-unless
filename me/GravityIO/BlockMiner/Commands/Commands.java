package me.GravityIO.BlockMiner.Commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

	Map<UUID, Long> whenMined = new HashMap<UUID, Long>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase(mineCmd)) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can use this command!");
				return true;
			}
			Player player = (Player) sender;
			UUID playerId = player.getUniqueId();
			if (args.length == 0) {
				if (player.hasPermission("blockminer.commands.cmdcooldown") || !whenMined.containsKey(playerId)
						|| System.currentTimeMillis() >= whenMined.get(playerId)
								+ (Main.main.getConfig().getInt("command-cooldown") * 1000)) {
					miner.mine(player);
					whenMined.put(playerId, System.currentTimeMillis());
					return true;
				} else {
					player.sendMessage(ChatColor.RED + "You've recently used this command, wait "
							+ (((whenMined.get(playerId) + Main.main.getConfig().getInt("command-cooldown") * 1000)
									- System.currentTimeMillis()) / 1000)
							+ " seconds");
					return true;
				}
			} else {
				if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
					if (player.hasPermission("blockminer.commands.reload")) {
						Main.main.reloadConfig();
						player.sendMessage(ChatColor.GREEN + "Succesfully reloaded config file!");
						return true;
					} else {
						player.sendMessage(
								ChatColor.RED + "You do not have the sufficient permissions to use this command");
						return true;
					}
				}
			}
			player.sendMessage(ChatColor.RED + "Unknown command...");
			return true;
		}
		return false;
	}
}
