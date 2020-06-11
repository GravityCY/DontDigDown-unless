package me.GravityIO.BlockMiner.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.GravityIO.BlockMiner.Miner;

public class Commands implements CommandExecutor {

	public String cmd1 = "mine";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase(cmd1)) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				Miner.mine(player);
				return true;
			}
			sender.sendMessage("Only players can use this command!");
			return true;
		}
		return false;
	}
}
