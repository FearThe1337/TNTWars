package me.abandoncaptian.TNTWars.Events;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.abandoncaptian.TNTWars.CountDowns;
import me.abandoncaptian.TNTWars.InvAndExp;
import me.abandoncaptian.TNTWars.Main;

public class PlayerLeaveAndJoin implements Listener{
	Main pl;
	InvAndExp IAE;
	CountDowns cd;
	public PlayerLeaveAndJoin(Main plugin) {
		pl = plugin;
		IAE = new InvAndExp(plugin);
	}

	@EventHandler
	public void playerLeaveGame(PlayerQuitEvent e){
		Player p = e.getPlayer();

		if(pl.Perks.containsKey(p.getName())){
			if(pl.Perks.get(p.getName()).containsKey("Fireworks")){
				pl.perksConfig.set(p.getName()+".Fireworks", pl.Perks.get(p.getName()).get("Fireworks"));
			}else{
				pl.perksConfig.set(p.getName()+".Fireworks", false);
			}
			if(pl.Perks.get(p.getName()).containsKey("Outline")){
				pl.perksConfig.set(p.getName()+".Outline", pl.Perks.get(p.getName()).get("Outline"));
			}else{
				pl.perksConfig.set(p.getName()+".Outline", false);
			}
			pl.Perks.remove(p.getName());
			try {
				pl.perksConfig.save(pl.perksFile);
			} catch (IOException e1) {
			}
		}
		
		if(!pl.cd.active){
			if(pl.gameQueue.contains(p.getName())){
				int preQue = pl.gameQueue.size();
				pl.gameQueue.remove(p.getName());
				pl.UpdateBoard(true, p.getName());
				int queued = pl.gameQueue.size();
				if(preQue >= pl.gameMin){
					if(queued < pl.gameMin){
						Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l]  §6Not enough players to play §7(§bMinimum: " + pl.gameMin + "§7)");
						if(cd.starting1){
							cd.countQueue.cancel();
							cd.starting1 = false;
							cd.starting2 = false;
						}
						if(cd.starting2){
							cd.count30.cancel();
							cd.count10.cancel();
							cd.count5.cancel();
							cd.count4.cancel();
							cd.count3.cancel();
							cd.count2.cancel();
							cd.count1.cancel();
							cd.countStart.cancel();
						}
					}
				}
				Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has left queue §7- §bQueued: " + queued);
				if(pl.selectedKit.containsKey(p.getName())){
					pl.selectedKit.remove(p.getName());
				}
			}
		}else if(pl.inGame.contains(p.getName())){
			pl.inGame.remove(p.getName());
			pl.UpdateBoard(true, p.getName());
			int game = pl.inGame.size();
			if(game == 1){
				String winner = pl.inGame.get(0);
				pl.UpdateBoard(true, pl.inGame.get(0));
				Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
					@Override
					public void run() {
						Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has left TNT Wars!");
						Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b§l" + winner + " §6has won!");
					}
				}, 2);
				p.closeInventory();
				p.getInventory().clear();
				IAE.InventorySwitch(p);
				IAE.ExpSwitch(p.getName());
				p.setHealthScale(20);
				p.teleport(pl.spawnpoint);
				p.setCanPickupItems(true);
				p.setHealth(20);
				p.setFoodLevel(20);
				Bukkit.getPlayer(winner).setHealth(20);
				Bukkit.getPlayer(winner).setFoodLevel(20);
				Bukkit.getPlayer(winner).getInventory().clear();
				Bukkit.getPlayer(winner).closeInventory();
				IAE.InventorySwitch(Bukkit.getPlayer(winner));
				IAE.ExpSwitch(winner);
				Bukkit.getPlayer(winner).setHealthScale(20);
				pl.inGame.clear();
				cd.active = false;
				cd.canKit = true;
				cd.starting1 = false;
				cd.starting2 = false;
				pl.selectedKit.clear();
				pl.kh.initInv();
			}else{
				Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has left TNT Wars §7- §b" + game + " remain!");
				if(pl.selectedKit.containsKey(p.getName())){
					pl.selectedKit.remove(p.getName());
				}
			}
		}
	}

	@EventHandler
	public void gameConnect(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if(pl.perksConfig.contains(p.getName())){
			pl.Perks.put(p.getName(),  new HashMap<String, Boolean>());
			pl.Perks.get(p.getName()).put("Fireworks", pl.perksConfig.getBoolean(p.getName()+".Fireworks"));
			pl.Perks.get(p.getName()).put("Outline", pl.perksConfig.getBoolean(p.getName()+".Outline"));
		}
		if(pl.tpBack.containsKey(p.getName())){
			p.teleport(pl.tpBack.get(p.getName()));
			pl.tpBack.remove(p.getName());
		}
		
		if(IAE.hasSwitchedInv(e.getPlayer()))IAE.InventorySwitch(e.getPlayer());
		if(IAE.hasSwitchedExp(e.getPlayer()))IAE.ExpSwitch(e.getPlayer().getName());
	}

}
