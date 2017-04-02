package me.abandoncaptian.TNTWars.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.abandoncaptian.TNTWars.CountDowns;
import me.abandoncaptian.TNTWars.InvAndExp;
import me.abandoncaptian.TNTWars.KitHandler;
import me.abandoncaptian.TNTWars.Main;

public class PlayerDeath implements Listener{
	Main pl;
	CountDowns cd;
	InvAndExp IAE;
	KitHandler kh;
	public PlayerDeath(Main Plugin){
		pl = Plugin;
	}
	
	@EventHandler
	public void gameDeath(PlayerDeathEvent e){
		if(cd.active){
			if(pl.inGame.contains(e.getEntity().getName())){
				Player p = (Player) e.getEntity();
				e.getDrops().clear();
				pl.dead.add(p.getName());
				e.setDeathMessage(null);
				p.setHealthScale(20);
				int preGame = pl.inGame.size();
				pl.inGame.remove(e.getEntity().getName());
				int game = pl.inGame.size();
				e.getEntity().teleport(pl.spawnpoint);
				Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
					@Override
					public void run() {
						p.getInventory().clear();
						IAE.InventorySwitch(p);
						IAE.ExpSwitch(p.getName());
					}
				}, (20*3));
				Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
					@Override
					public void run() {
						if(pl.dead.contains(p.getName())){
							Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + e.getEntity().getName() + " §6was killed §7- §b" + preGame + " remain!");
							pl.dead.remove(p.getName());
						}
					}
				}, 5);
				if(game == 1){
					Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b§l" + pl.inGame.get(0) + " §6has won!");
					Bukkit.getPlayer(pl.inGame.get(0)).setHealth(20);
					Bukkit.getPlayer(pl.inGame.get(0)).setFoodLevel(20);
					Bukkit.getPlayer(pl.inGame.get(0)).getInventory().clear();
					IAE.InventorySwitch(Bukkit.getPlayer(pl.inGame.get(0)));
					IAE.ExpSwitch(pl.inGame.get(0));
					Bukkit.getPlayer(pl.inGame.get(0)).setHealthScale(20);
					Bukkit.getPlayer(pl.inGame.get(0)).teleport(pl.spawnpoint);
					pl.inGame.clear();
					kh.inv.clear();
					cd.active = false;
					cd.canKit = true;
					cd.starting1 = false;
					cd.starting2 = false;
					pl.selectedKit.clear();
					kh.initInv();
				}
			}
		}
	}
}
