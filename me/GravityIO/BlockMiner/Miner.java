package me.GravityIO.BlockMiner;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Miner {

	public void mine(Player player) {
		Location blockUnderPlayer = player.getLocation().clone().add(0, -1, 0);
		Block initBlock = blockUnderPlayer.getBlock();
		Material initMat = initBlock.getType();

		if (!Main.main.getConfig().getBoolean("allow-use-of-mine-in-air")) {
			if (initBlock.isEmpty()) {
				String inAirMsg = Main.main.getConfig().getString("not-allowed-use-cmd-in-air-message");
				inAirMsg = evaluateColorCodes(inAirMsg);
				player.sendMessage(inAirMsg);
				return;
			}
		}

		if (initMat != Material.BEDROCK) {
			if (initMat != Material.AIR) {
				if (Main.main.getConfig().getBoolean("safe-block-check-teleport"))
					if (!teleportPlayerToSafeLocation(player)) {
						String cannotFindLocMsg = evaluateColorCodes(
								Main.main.getConfig().getString("cannot-find-safe-location"));
						player.sendMessage(cannotFindLocMsg);
						return;
					}
				player.getInventory().addItem(new ItemStack(initMat));
				initBlock.setType(Material.AIR);
			}

			int itemsGiven = 0;
			int liqSoundPlayed = 0;
			int blockSoundPlayed = 0;

			Location blockPos = blockUnderPlayer;
			while (blockPos.add(0, -1, 0).getBlock().getType() != Material.BEDROCK && blockPos.getBlockY() >= 0) {

				Block block = blockPos.getBlock();
				if (block.getType() != Material.AIR) {
					if (block.isLiquid()) {
						if (isLiquidSource(block) && player.getInventory().contains(Material.BUCKET)) {
							int bucketIndex = player.getInventory().first(Material.BUCKET);
							ItemStack bucket = player.getInventory().getItem(bucketIndex);
							bucket.setAmount(bucket.getAmount() - 1);

							ItemStack liquidToAdd = new ItemStack(
									Material.matchMaterial(block.getType().toString() + "_BUCKET"));

							player.getInventory().addItem(liquidToAdd);
							block.setType(Material.AIR);

							if (liqSoundPlayed <= 5) {
								if (block.getType() == Material.LAVA) {
									player.playSound(player.getLocation(), Sound.ITEM_BUCKET_FILL_LAVA, 0.25f, 1);
								} else {
									player.playSound(player.getLocation(), Sound.ITEM_BUCKET_FILL, 0.25f, 1);
								}
								liqSoundPlayed++;
							}

							itemsGiven++;
						}
					} else {
						player.getInventory().addItem(new ItemStack(block.getType()));
						block.setType(Material.AIR);

						if (blockSoundPlayed <= 5) {
							player.playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 0.25f, 1);
							blockSoundPlayed++;
						}

						itemsGiven++;
					}
				}
			}
			if (itemsGiven != 0) {
				if (itemsGiven != 1) {
					String multiBlockMsg = evaluateColorCodes(
							Main.main.getConfig().getString("mined-multiple-blocks-message"));
					if (multiBlockMsg.contains("$BLOCKS")) {
						multiBlockMsg = multiBlockMsg.replace("$BLOCKS", itemsGiven + "");
					}
					player.sendMessage(multiBlockMsg);
				} else {
					String oneBlockMsg = evaluateColorCodes(Main.main.getConfig().getString("mined-one-block-message"));
					player.sendMessage(oneBlockMsg);
				}
			} else {
				String minedNothingMsg = evaluateColorCodes(Main.main.getConfig().getString("mined-nothing-message"));
				player.sendMessage(minedNothingMsg);
			}
		} else {
			String bedrockUnder = evaluateColorCodes(Main.main.getConfig().getString("bedrock-under-feet-message"));
			player.sendMessage(bedrockUnder);
		}
	}

	private boolean teleportPlayerToSafeLocation(Player player) {
		Block blockUnder = player.getLocation().clone().add(0, -1, 0).getBlock();
		Block blockCheck;

		blockCheck = blockUnder.getRelative(1, 0, 0);
		if (!blockCheck.isEmpty()) {
			if (blockCheck.getRelative(0, 1, 0).isEmpty() && blockCheck.getRelative(0, 2, 0).isEmpty()) {
				player.teleport(player.getLocation().add(1, 0.05f, 0));
				return true;
			}
		}
		blockCheck = blockUnder.getRelative(-1, 0, 0);
		if (!blockCheck.isEmpty()) {
			if (blockCheck.getRelative(0, 1, 0).isEmpty() && blockCheck.getRelative(0, 2, 0).isEmpty()) {
				player.teleport(player.getLocation().add(-1, 0.05f, 0));
				return true;
			}
		}
		blockCheck = blockUnder.getRelative(0, 0, 1);
		if (!blockCheck.isEmpty()) {
			if (blockCheck.getRelative(0, 1, 0).isEmpty() && blockCheck.getRelative(0, 2, 0).isEmpty()) {
				player.teleport(player.getLocation().add(0, 0.05f, 1));
				return true;
			}
		}
		blockCheck = blockUnder.getRelative(0, 0, -1);
		if (!blockCheck.isEmpty()) {
			if (blockCheck.getRelative(0, 1, 0).isEmpty() && blockCheck.getRelative(0, 2, 0).isEmpty()) {
				player.teleport(player.getLocation().add(0, 0.05f, -1));
				return true;
			}
		}
		return false;
	}

	private String evaluateColorCodes(String multiBlockMsg) {
		multiBlockMsg = multiBlockMsg.replaceAll("&0", ChatColor.BLACK + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&1", ChatColor.DARK_BLUE + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&2", ChatColor.DARK_GREEN + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&3", ChatColor.DARK_AQUA + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&4", ChatColor.DARK_RED + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&5", ChatColor.DARK_PURPLE + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&6", ChatColor.GOLD + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&7", ChatColor.GRAY + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&8", ChatColor.DARK_GRAY + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&9", ChatColor.BLUE + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&a", ChatColor.GREEN + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&b", ChatColor.AQUA + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&c", ChatColor.RED + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&e", ChatColor.YELLOW + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&f", ChatColor.WHITE + "");
		multiBlockMsg = multiBlockMsg.replaceAll("&g", ChatColor.MAGIC + "");
		return multiBlockMsg;
	}

	private boolean isLiquidSource(Block block) {
		Levelled liqLevel = (Levelled) block.getBlockData();
		return liqLevel.getLevel() == 0;
	}
}
