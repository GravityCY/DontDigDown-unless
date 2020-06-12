package me.GravityIO.BlockMiner.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.GravityIO.BlockMiner.Miner;

public class Commands implements CommandExecutor {

	public static String mineCmd = "mine";

	private Miner miner = new Miner();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase(mineCmd)) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				miner.mine(player);
				return true;
			}
			sender.sendMessage("Only players can use this command!");
			return true;
		}
		return false;
	}
}
