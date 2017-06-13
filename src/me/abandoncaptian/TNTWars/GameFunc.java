package me.abandoncaptian.TNTWars;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Score;

public class GameFunc {
	Main pl;
	CountDowns cd;
	public GameFunc(Main plugin){
		pl = plugin;
	}

	public void UpdateBoard(Boolean leave, String name, String map) {
		if (!leave) {
			Score score;
			score = pl.objectiveQueue.get(map).getScore("§a" + name);
			score.setScore(pl.gameQueue.get(map).indexOf(name));
			Bukkit.getPlayer(name).setScoreboard(pl.queueBoard.get(map));
			score = pl.objectiveRemaining.get(map).getScore("§c" + name);
			score.setScore(pl.gameQueue.get(map).indexOf(name));
		} else {
			pl.queueBoard.get(map).resetScores("§a" + name);
			pl.remainingBoard.get(map).resetScores("§c" + name);
			Bukkit.getPlayer(name).setScoreboard(pl.clearBoard);
		}
	}

	public void ChangeBoard(String map) {
		for (String name : pl.inGame.get(map)) {
			Bukkit.getPlayer(name).setScoreboard(pl.remainingBoard.get(map));
		}
	}
	
	public void gameJoin(String name, String map){
		Player p = Bukkit.getPlayer(name);
		if (!pl.cd.active.get(map)) {
			if (pl.allQueue.contains(name) || pl.allInGame.contains(name)) {
				p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cAlready in a queue/game!");
			} else {
				int queuedPre;
				if(pl.gameQueue.get(map).isEmpty()){
					queuedPre = 0;
				}else{
					queuedPre = pl.gameQueue.get(map).size();
				}
				if (queuedPre < pl.gameMax) {
					pl.gameQueue.get(map).add(name);
					pl.playerInArena.put(p.getName(), map);
					pl.allQueue.add(p.getName());
					UpdateBoard(false, p.getName(), map);
					if (pl.cd.starting1.get(map)) {
						pl.mh.openKitMenu(p);
					}
					int queued = pl.gameQueue.get(map).size();
					Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §b" + name + " §6has joined queue §7- §bQueued: " + queued);
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
					p.teleport(pl.spawnpoint.get(map));
					if (queued == pl.gameMin) {
						pl.cd.countDownPre(map);
					} else if (queued == pl.gameStart30Sec) {
						if (pl.cd.starting1.get(map)) {
							pl.cd.countQueue.get(map).cancel();
							pl.cd.countDown30(map);
						}
					}
				} else {
					p.sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §cTNT Wars Queue is full");
				}
			}
		} else {
			p.sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §6Game is currently active!");
		}
	}
	
	public void gameLeave(String name){
		Player p = Bukkit.getPlayer(name);
		String map = pl.playerInArena.get(name);
		if (!pl.cd.active.get(map)) {
			if (pl.gameQueue.get(map).contains(p.getName())) {
				pl.gameQueue.get(map).remove(p.getName());
				pl.allQueue.remove(p.getName());
				int queued = pl.gameQueue.get(map).size();
				Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §b" + p.getName()
						+ " §6has left TNT Wars Queue §7- §bQueued: " + queued);
				UpdateBoard(true, p.getName(), map);
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
					if (pl.cd.starting1.get(map)) {
						pl.cd.countQueue.get(map).cancel();
						pl.cd.starting1.put(map, false);
					}
					if (pl.cd.starting2.get(map)) {
						pl.cd.starting2.put(map, false);
						pl.cd.count30.get(map).cancel();
						pl.cd.count10.get(map).cancel();
						pl.cd.count5.get(map).cancel();
						pl.cd.count4.get(map).cancel();
						pl.cd.count3.get(map).cancel();
						pl.cd.count2.get(map).cancel();
						pl.cd.count1.get(map).cancel();
						pl.cd.countStart.get(map).cancel();
					}
				}
				if (pl.selectedKit.containsKey(p.getName())) {
					pl.selectedKit.remove(p.getName());
				}
			} else {
				p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou are not in the TNT Wars Queue");
			}
		} else {
			if (pl.inGame.get(map).contains(p.getName())) {
				pl.inGame.get(map).remove(p.getName());
				pl.allInGame.remove(p.getName());
				UpdateBoard(true, p.getName(), map);
				int game = pl.inGame.get(map).size();
				if (game == 1) {
					String winner = pl.inGame.get(map).get(0);
					UpdateBoard(true, pl.inGame.get(map).get(0), map);
					Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
						@Override
						public void run() {
							Bukkit.broadcastMessage(
									"§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §b" + p.getName() + " §6has left TNT Wars!");
							Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §b§l" + winner + " §6has won!");
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
					for (String name1 : pl.spec.get(map)) {
						Bukkit.getPlayer(name1).sendMessage(
								"§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bReturning all players to last location");
					}
					Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
						@Override
						public void run() {
							for (String name : pl.spec.get(map)) {
								Bukkit.getPlayer(name).teleport(pl.tpBack.get(name));
								pl.tpBack.remove(name);
							}
						}
					}, (20 * 3));
					pl.inGame.get(map).clear();
					pl.cd.active.put(map, false);
					pl.cd.canKit.put(map, true);
					pl.cd.starting1.put(map, false);
					pl.cd.starting2.put(map, false);
				} else {
					Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §b" + p.getName()
							+ " §6has left TNT Wars §7- §b" + game + " remain!");
					if (pl.selectedKit.containsKey(p.getName())) {
						pl.selectedKit.remove(p.getName());
					}
				}
			} else {
				p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou are not in a TNT Wars game");
			}
		}
	}
	
	public void gameForceStart(String map, Player p){
		if (!pl.cd.starting2.get(map)) {
			if (pl.gameQueue.size() >= pl.gameMin) {
				pl.cd.countQueue.get(map).cancel();
				pl.cd.countDown30(map);
			} else {
				p.sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §cNot enough players to start the game.");
			}
		} else {
			p.sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §6TNT Wars is already started!");
		}
	}
}