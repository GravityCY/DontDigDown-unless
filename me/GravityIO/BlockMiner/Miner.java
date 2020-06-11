package me.GravityIO.BlockMiner;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Miner {

	public static void mine(Player player) {
		Location initBlockPos = player.getLocation().add(0, -1, 0);
		Block initBlock = player.getLocation().getBlock().getRelative(0, -1, 0);
		Material initMat = initBlock.getType();

		if (!initBlock.isEmpty()) {
			if (initBlock.getType() != Material.BEDROCK) {

				initBlock.setType(Material.GLASS);
				revertBlockChange(initBlock, initMat);

				int blocksGiven = 0;
				int liqSoundPlayed = 0;
				int blockSoundPlayed = 0;

				Location blockPos = initBlockPos;
				while (blockPos.add(0, -1, 0).getBlock().getType() != Material.BEDROCK && blockPos.getBlockY() >= 0) {

					Block block = blockPos.getBlock();
					if (block.getType() != Material.AIR) {
						if (block.isLiquid()) {
							
							if (isLiquidSource(block)) {
								if (player.getInventory().contains(Material.BUCKET)) {
									int bucketIndex = player.getInventory().first(Material.BUCKET);
									ItemStack bucket = player.getInventory().getItem(bucketIndex);
									bucket.setAmount(bucket.getAmount() - 1);

									ItemStack liquidToAdd = new ItemStack(
											Material.matchMaterial(block.getType().toString() + "_BUCKET"));

									player.getInventory().addItem(liquidToAdd);
									blockPos.getBlock().setType(Material.AIR);

									if (liqSoundPlayed <= 5) {
										if (block.getType() == Material.LAVA) {
											player.playSound(player.getLocation(), Sound.ITEM_BUCKET_FILL_LAVA, 0.25f,1);
										} else {
											player.playSound(player.getLocation(), Sound.ITEM_BUCKET_FILL, 0.25f, 1);
										}
										liqSoundPlayed++;
									}

									blocksGiven++;
								}
							}
						} else {
							player.getInventory().addItem(new ItemStack(block.getType()));
							block.setType(Material.AIR);

							if (blockSoundPlayed <= 5) {
								player.playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 0.25f, 1);
								blockSoundPlayed++;
							}

							blocksGiven++;
						}
					}
				}
				if (blocksGiven != 0) {
					if (blocksGiven != 1) {
						player.sendMessage(ChatColor.GREEN + "Mined " + ChatColor.DARK_GREEN + blocksGiven
								+ ChatColor.GREEN + " blocks, they were added into your inventory!");
					} else {
						player.sendMessage(ChatColor.GREEN + "Mined " + ChatColor.DARK_GREEN + blocksGiven
								+ ChatColor.GREEN + " block, it was added into your inventory!" + ChatColor.AQUA
								+ " HAHAHAHAHA 1 BLOCK-- 1 BLOCK!");
					}
				} else {
					player.sendMessage(ChatColor.YELLOW + "*Loser sound effect goes here*" + ChatColor.RED
							+ " Found nothing to mine... what a shame...");
				}
			} else {
				player.sendMessage(ChatColor.RED + "There is bedrock right under you....");
			}
		} else {
			player.sendMessage(ChatColor.RED + "You are not allowed to use this command in the air!");
		}
	}

	private static void revertBlockChange(Block block, Material type) {
		new BukkitRunnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				block.setType(type);
			}
		}.runTaskLater(Main.main, 5 * 20);
	}

	private static boolean isLiquidSource(Block block) {
		Levelled liqLevel = (Levelled) block.getBlockData();
		return liqLevel.getLevel() == 0;
	}
}
