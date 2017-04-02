package me.abandoncaptian.TNTWars.Events;

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
	}
	
	@EventHandler
	public void playerLeaveGame(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(!cd.active){
			if(pl.gameQueue.contains(p.getName())){
				int preQue = pl.gameQueue.size();
				pl.gameQueue.remove(p.getName());
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
			int game = pl.inGame.size();
			Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has left §7- §b" + game + " remain!");
			if(pl.selectedKit.containsKey(p.getName())){
				pl.selectedKit.remove(p.getName());
			}
		}
	}
	
	@EventHandler
	public void gameConnect(PlayerJoinEvent e){
		if(IAE.hasSwitchedInv(e.getPlayer()))IAE.InventorySwitch(e.getPlayer());
		if(IAE.hasSwitchedExp(e.getPlayer()))IAE.ExpSwitch(e.getPlayer().getName());
	}

}
