package me.abandoncaptian.TNTWars;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandHandler implements CommandExecutor {
	Main pl;

	public CommandHandler(Main plugin) {
		pl = plugin;
	}

	@Override
	public boolean onCommand(CommandSender theSender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("tw")) {
			if (theSender instanceof Player) {
				Player p = (Player) theSender;
				if (args.length == 0) {
					pl.mh.openMainMenu(p);
				}

				else if (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("j"))

				{
					if (!pl.cd.active) {
						if (pl.gameQueue.contains(p.getName())) {
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cAlready in the queue!");
						} else {
							int queuedPre = pl.gameQueue.size();
							if (queuedPre < pl.gameMax) {
								pl.gameQueue.add(p.getName());
								pl.UpdateBoard(false, p.getName());
								if (pl.cd.starting1) {
									pl.mh.openKitMenu(p);
								}
								int queued = pl.gameQueue.size();
								Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName()
										+ " §6has joined queue §7- §bQueued: " + queued);

								if (p.getGameMode() != GameMode.SURVIVAL)
									p.setGameMode(GameMode.SURVIVAL);
								pl.IAE.InventorySwitch(p);
								pl.IAE.ExpSwitch(p.getName());
								p.setInvulnerable(true);
								p.setHealth(20);
								p.setFoodLevel(20);
								p.getInventory().clear();
								p.getInventory().setItem(1, new ItemStack(Material.COOKED_BEEF, 5));
								p.getActivePotionEffects().clear();
								p.setCanPickupItems(false);
								p.setFlying(false);
								p.setAllowFlight(false);
								pl.tpBack.put(p.getName(), p.getLocation());
								p.teleport(pl.spawnpoint);
								if (queued == pl.gameMin) {
									pl.cd.countDownPre();
								} else if (queued == pl.gameStart30Sec) {
									if (pl.cd.starting2) {
										pl.cd.starting1 = false;
									} else if (pl.cd.starting1) {
										pl.cd.countQueue.cancel();
										pl.cd.countDown30();
										pl.cd.starting1 = false;
									}
								}
							} else {
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cTNT Wars Queue is full");
							}
						}
					} else {
						p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Game is currently active!");
					}
				}

				else if (args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("quit")
						|| args[0].equalsIgnoreCase("l"))

				{
					if (!pl.cd.active) {
						if (pl.gameQueue.contains(p.getName())) {
							pl.gameQueue.remove(p.getName());
							int queued = pl.gameQueue.size();
							Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName()
									+ " §6has left TNT Wars Queue §7- §bQueued: " + queued);
							pl.UpdateBoard(true, p.getName());
							p.teleport(pl.tpBack.get(p.getName()));
							pl.tpBack.remove(p.getName());
							pl.IAE.InventorySwitch(p);
							pl.IAE.ExpSwitch(p.getName());
							p.setHealthScale(20);
							p.setHealth(20);
							p.setFoodLevel(20);
							p.setCanPickupItems(true);
							p.setInvulnerable(false);
							if (queued < pl.gameMin) {
								Bukkit.broadcastMessage(
										"§7§l[§c§lTNT Wars§7§l] §6Not enough players to play §7(§bMinimum: "
												+ pl.gameMin + "§7)");
								if (pl.cd.starting1) {
									pl.cd.countQueue.cancel();
									pl.cd.starting1 = false;
								}
								if (pl.cd.starting2) {
									pl.cd.starting2 = false;
									pl.cd.count30.cancel();
									pl.cd.count10.cancel();
									pl.cd.count5.cancel();
									pl.cd.count4.cancel();
									pl.cd.count3.cancel();
									pl.cd.count2.cancel();
									pl.cd.count1.cancel();
									pl.cd.countStart.cancel();
								}
							}
							if (pl.selectedKit.containsKey(p.getName())) {
								pl.selectedKit.remove(p.getName());
							}
						} else {
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou are not in the TNT Wars Queue");
						}
					} else {
						if (pl.inGame.contains(p.getName())) {
							pl.inGame.remove(p.getName());
							pl.UpdateBoard(true, p.getName());
							int game = pl.inGame.size();
							if (game == 1) {
								String winner = pl.inGame.get(0);
								pl.UpdateBoard(true, pl.inGame.get(0));
								Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
									@Override
									public void run() {
										Bukkit.broadcastMessage(
												"§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has left TNT Wars!");
										Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b§l" + winner + " §6has won!");
									}
								}, 2);
								p.closeInventory();
								p.getInventory().clear();
								pl.IAE.InventorySwitch(p);
								pl.IAE.ExpSwitch(p.getName());
								p.setHealthScale(20);
								p.setHealth(20);
								p.setFoodLevel(20);
								Bukkit.getPlayer(winner).setHealth(20);
								Bukkit.getPlayer(winner).setFoodLevel(20);
								Bukkit.getPlayer(winner).getInventory().clear();
								Bukkit.getPlayer(winner).closeInventory();
								pl.IAE.InventorySwitch(Bukkit.getPlayer(winner));
								pl.IAE.ExpSwitch(winner);
								Bukkit.getPlayer(winner).setHealthScale(20);
								for (String name : pl.spec) {
									Bukkit.getPlayer(name).sendMessage(
											"§7§l[§c§lTNT Wars§7§l] §bReturning all players to last location");
								}
								Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
									@Override
									public void run() {
										for (String name : pl.spec) {
											Bukkit.getPlayer(name).teleport(pl.tpBack.get(name));
											pl.tpBack.remove(name);
										}
									}
								}, (20 * 3));
								pl.inGame.clear();
								pl.cd.active = false;
								pl.cd.canKit = true;
								pl.cd.starting1 = false;
								pl.cd.starting2 = false;
								pl.selectedKit.clear();
							} else {
								Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName()
										+ " §6has left TNT Wars §7- §b" + game + " remain!");
								if (pl.selectedKit.containsKey(p.getName())) {
									pl.selectedKit.remove(p.getName());
								}
							}
						} else {
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou are not in the TNT Wars game");
						}
					}
				}

				else if (args[0].equalsIgnoreCase("setspawn") && p.hasPermission("tntwars.host"))

				{
					pl.spawnpoint = p.getLocation();
					pl.config.set("SpawnPoint.world", pl.spawnpoint.getWorld().getName());
					pl.config.set("SpawnPoint.x", (int) pl.spawnpoint.getX());
					pl.config.set("SpawnPoint.y", (int) pl.spawnpoint.getY());
					pl.config.set("SpawnPoint.z", (int) pl.spawnpoint.getZ());
					p.sendMessage("§6TNT Wars SpawnPoint Set!");
					try {
						pl.config.save(pl.configFile);
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else if (args[0].equalsIgnoreCase("setspecpoint") && p.hasPermission("tntwars.host"))

				{
					pl.specpoint = p.getLocation();
					pl.config.set("SpecPoint.world", pl.specpoint.getWorld().getName());
					pl.config.set("SpecPoint.x", (int) pl.specpoint.getX());
					pl.config.set("SpecPoint.y", (int) pl.specpoint.getY());
					pl.config.set("SpecPoint.z", (int) pl.specpoint.getZ());
					p.sendMessage("§6TNT Wars Spec Point Set!");
					try {
						pl.config.save(pl.configFile);
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else if (args[0].equalsIgnoreCase("forcestart") && p.hasPermission("tntwars.host")) {
					if (!pl.cd.starting2) {
						if (pl.gameQueue.size() >= pl.gameMin) {
							pl.cd.countQueue.cancel();
							pl.cd.countDown30();
						} else {
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cNot enough players to start the game.");
						}
					} else {
						p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6TNT Wars is already started!");
					}
				}
			}
		}
		return true;
	}
}
