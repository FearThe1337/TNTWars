package me.abandoncaptian.TNTWars.Events;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitTask;

import me.abandoncaptian.TNTWars.CountDowns;
import me.abandoncaptian.TNTWars.GameFunc;
import me.abandoncaptian.TNTWars.Main;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand.EnumClientCommand;

public class PlayerDeath implements Listener {
	Main pl;
	CountDowns cd;
	GameFunc GF;
	BukkitTask deathTest;
	boolean isDead;
	HashMap<String, String> winnerNames = new HashMap<String, String>();

	public PlayerDeath(Main plugin) {
		pl = plugin;
		GF = new GameFunc(plugin);
	}

	@EventHandler
	public void gameDeath(PlayerDeathEvent e) {
		Player p = (Player) e.getEntity();
		e.getEntity().teleport(pl.hub);
		e.setDeathMessage("");
		e.getDrops().clear();
		Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
			@Override
			public void run() {
				GF.setPlayerInv(p);
			}
		}, 10);
		if(pl.playerInArena.containsKey(p.getName())){
			String map = pl.playerInArena.get(p.getName());
			if (pl.cd.active.get(map)) {
				String name = p.getName();
				PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
				CraftPlayer craftPlayer = (CraftPlayer) p;
				craftPlayer.getHandle().playerConnection.a(packet);
				e.getDrops().clear();
				if(pl.perTeam.get(map)==1){
					for(int i: pl.teams.get(map).keySet()){
						if(pl.teams.get(map).get(i).contains(name)){
							pl.teams.get(map).get(i).clear();
							break;
						}
					}
				}
				pl.dead.get(map).add(name);
				GF.resetPlayerfromGame(p, map);
				GF.UpdateBoard(true, name, map);
				GF.resetPlayer(p);
				GF.setPlayerInv(p);
				int game = pl.inGame.get(map).size();
				if(game > 1){
					String  tempP = pl.inGame.get(map).get(0);
					int team = 1;
					for(int i: pl.teams.get(map).keySet()){
						if(pl.teams.get(map).get(i).contains(tempP))team = i;
					}
					if(pl.teams.get(map).get(team).containsAll(pl.inGame.get(map))){
						List<String> winners = pl.teams.get(map).get(team);
						winnerNames.put(map, "");
						int size = winners.size();
						for(String tempPlayer: winners){
							winnerNames.put(map, winnerNames.get(map) + tempPlayer);
							if(winners.indexOf(tempPlayer) < (size-1))winnerNames.put(map, winnerNames.get(map) + ", ");
						}
						Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
							@Override
							public void run() {
								Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §b§l" + winnerNames + " §6has won as a team!");
								winnerNames.remove(map);
							}
						}, 2);
						for(String tempPlayer: winners){
							Player winnerP = Bukkit.getPlayer(tempPlayer);
							GF.UpdateBoard(true, tempPlayer, map);
							GF.resetPlayer(winnerP);
							Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
								@Override
								public void run() {
									pl.econ.depositBalance(winnerP, 10);
									winnerP.sendMessage("§7§l[§c§lTNT Wars§7§l] §a+§710 Points");
									winnerP.sendMessage("§7§l[§c§lTNT Wars§7§l] §6New Points Balance§7: §a" + pl.econ.getBalance(winnerP));
									GF.resetPlayerfromGame(winnerP, map);
									GF.setPlayerInv(winnerP);
								}
							}, 5);
							Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
								@Override
								public void run() {
									GF.setPlayerInv(winnerP);
								}
							}, 10);
						}
						Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
							@Override
							public void run() {
								GF.resetMap(map);
							}
						}, 5);
					}
				}
				if (game == 1) {
					String winner = pl.inGame.get(map).get(0);
					Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
						@Override
						public void run() {
							Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §b§l" + winner + " §6has won!");
						}
					}, 2);
					Player winnerP = Bukkit.getPlayer(winner);
					GF.resetPlayer(winnerP);
					GF.UpdateBoard(true, winner, map);
					Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
						@Override
						public void run() {
							pl.econ.depositBalance(winnerP, 10);
							winnerP.sendMessage("§7§l[§c§lTNT Wars§7§l] §a+§710 Points");
							winnerP.sendMessage("§7§l[§c§lTNT Wars§7§l] §6New Points Balance§7: §a" + pl.econ.getBalance(winnerP));
							GF.resetPlayerfromGame(winnerP, map);
							GF.resetMap(map);
							GF.setPlayerInv(winnerP);
						}
					}, 5);
					Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
						@Override
						public void run() {
							GF.setPlayerInv(winnerP);
						}
					}, 10);
				}
			}
		}
	}
}