package me.abandoncaptian.TNTWars;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Score;

import com.google.common.collect.Lists;

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

	public void gameJoin(String name, String map, int team){
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
				if (queuedPre < pl.gameMax.get(map)) {
					if((pl.teams.get(map).get(team).size() < pl.perTeam.get(map)) || pl.teams.get(map).get(team).isEmpty()){
						pl.teams.get(map).get(team).add(p.getName());
						pl.gameQueue.get(map).add(name);
						pl.playerInArena.put(p.getName(), map);
						pl.allQueue.add(p.getName());
						UpdateBoard(false, p.getName(), map);
						if (pl.cd.starting1.get(map)) {
							pl.mh.openKitMenu(p);
						}
						int queued = pl.gameQueue.get(map).size();
						for(String player : pl.gameQueue.get(map)){
							Bukkit.getPlayer(player).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §b" + name + " §6has joined queue §7- §bQueued: " + queued);
						}
						if (p.getGameMode() != GameMode.SURVIVAL)
							p.setGameMode(GameMode.SURVIVAL);
						p.setInvulnerable(true);
						resetPlayer(p);
						setPlayerInv(p);
						pl.playerInArena.put(p.getName(), map);
						p.setCanPickupItems(false);
						p.setFlying(false);
						p.setAllowFlight(false);
						if(pl.cd.tenSec.get(map)){
							p.teleport(pl.spawnpoint.get(map).get(team));
						}else{
							p.teleport(pl.lobby.get(map));
						}
						if (queued == pl.gameMin) {
							pl.cd.countDownPre(map);
						} else if (queued == pl.gameStart30Sec) {
							if (pl.cd.starting1.get(map)) {
								pl.cd.countQueue.get(map).cancel();
								pl.cd.countDown30(map);
							}
						}
					}else{
						p.sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §cThat Team is full");
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
				for(String player : pl.gameQueue.get(map)){
					Bukkit.getPlayer(player).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §b" + p.getName()
					+ " §6has left TNT Wars Queue §7- §bQueued: " + queued);
				}
				UpdateBoard(true, p.getName(), map);
				resetPlayer(p);
				setPlayerInv(p);
				p.teleport(pl.hub);
				p.setCanPickupItems(true);
				p.setInvulnerable(false);
				for(int i: pl.teams.get(map).keySet()){
					List<String> players = pl.teams.get(map).get(i);
					if(players.contains(p.getName())){
						pl.teams.get(map).get(i).remove(p.getName());
					}
				}
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
				resetPlayer(p);
				resetPlayerfromGame(p, map);
				if (game == 1) {
					String winner = pl.inGame.get(map).get(0);
					UpdateBoard(true, pl.inGame.get(map).get(0), map);
					for(String player : pl.inGame.get(map)){
						Bukkit.getPlayer(player).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §b" + p.getName() + " §6has left TNT Wars!");
					}
					Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §b§l" + winner + " §6has won!");
					resetPlayer(Bukkit.getPlayer(winner));
					resetPlayerfromGame(Bukkit.getPlayer(winner), map);
				} else {
					for(String player : pl.inGame.get(map)){
						Bukkit.getPlayer(player).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §b" + p.getName() + " §6has left TNT Wars!");
					}
				}
			} else {
				p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou are not in a TNT Wars game");
			}
		}
	}

	public void resetPlayer(Player p){
		p.closeInventory();
		p.getInventory().clear();
		p.setHealthScale(20);
		p.setHealth(20);
		p.setFoodLevel(20);
		if(p.hasPotionEffect(PotionEffectType.SPEED))p.removePotionEffect(PotionEffectType.SPEED);
		p.setDisplayName(p.getName());
	}
	public void resetPlayerfromGame(Player p, String map){
		if(pl.allInGame.contains(p.getName()))pl.allInGame.remove(p.getName());
		if(pl.allQueue.contains(p.getName()))pl.allQueue.remove(p.getName());
		if(pl.gameQueue.get(map).contains(p.getName()))pl.gameQueue.get(map).remove(p.getName());
		if(pl.inGame.get(map).contains(p.getName()))pl.inGame.get(map).remove(p.getName());
		if(pl.selectedKit.containsKey(p.getName()))pl.selectedKit.remove(p.getName());
		if(pl.playerInArena.containsKey(p.getName()))pl.playerInArena.remove(p.getName());
		p.teleport(pl.hub);
	}
	
	public void resetMap(String map){
		for(int i : pl.teams.get(map).keySet()){pl.teams.get(map).get(i).clear();}
		pl.gameQueue.get(map).clear();
		pl.inGame.get(map).clear();
		pl.dead.get(map).clear();
		pl.cd.active.put(map, false);
		pl.cd.canKit.put(map, true);
		pl.cd.starting1.put(map, false);
		pl.cd.starting2.put(map, false);
		pl.cd.tenSec.put(map, false);
	}
	
	public void setPlayerInv(Player p){
		p.getInventory().clear();
		if(pl.allQueue.contains(p.getName()))p.getInventory().setItem(8, addItem(new ItemStack(Material.WOOL, 1, (byte)14), "§cLeave TNT Wars", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----")));
		if(!pl.allInGame.contains(p.getName()))p.getInventory().setItem(6, addItem(new ItemStack(Material.EMERALD), "§aKit Shop", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----")));
		if(pl.allQueue.contains(p.getName()))p.getInventory().setItem(4, addItem(new ItemStack(Material.CHEST), "§aTNT Wars Kits", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----")));
		if(pl.allInGame.contains(p.getName()))p.getInventory().setItem(1, addItem(new ItemStack(Material.COOKED_BEEF, 5), "FOOD", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----")));
		if(!pl.allInGame.contains(p.getName()))p.getInventory().setItem(0, addItem(new ItemStack(Material.TNT), "§cTNT Wars Menu", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----")));
	}

	public ItemStack addItem(ItemStack item, String name, List<String> lore) {
		ItemMeta meta;
		meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public void gameForceStart(String map, Player p){
		if (!pl.cd.starting2.get(map)) {
			if (pl.gameQueue.get(map).size() >= pl.gameMin) {
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