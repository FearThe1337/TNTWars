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
	public PlayerDeath(Main plugin){
		pl = plugin;
		kh = new KitHandler(plugin);
		IAE = new InvAndExp(plugin);
	}

	@EventHandler
	public void gameDeath(PlayerDeathEvent e){
		if(pl.cd.active){
			if(pl.inGame.contains(e.getEntity().getName())){
				Player p = (Player) e.getEntity();
				e.getDrops().clear();
				pl.dead.add(p.getName());
				e.setDeathMessage(null);
				p.setHealthScale(20);
				pl.inGame.remove(p.getName());
				int game = pl.inGame.size();
				e.getEntity().teleport(pl.spawnpoint);
				Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
					@Override
					public void run() {
						p.closeInventory();
						p.getInventory().clear();
						pl.IAE.InventorySwitch(p);
						pl.IAE.ExpSwitch(p.getName());
					}
				}, (20*3));
				if(game == 1){
					String winner = pl.inGame.get(0);
					Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
						@Override
						public void run() {
							Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b§l" + winner + " §6has won!");
						}
					}, 2);
					Bukkit.getPlayer(pl.inGame.get(0)).setHealth(20);
					Bukkit.getPlayer(pl.inGame.get(0)).setFoodLevel(20);
					Bukkit.getPlayer(pl.inGame.get(0)).getInventory().clear();
					Bukkit.getPlayer(pl.inGame.get(0)).closeInventory();
					pl.IAE.InventorySwitch(Bukkit.getPlayer(pl.inGame.get(0)));
					pl.IAE.ExpSwitch(pl.inGame.get(0));
					Bukkit.getPlayer(pl.inGame.get(0)).setHealthScale(20);
					Bukkit.getPlayer(pl.inGame.get(0)).teleport(pl.spawnpoint);
					pl.inGame.clear();
					pl.cd.active = false;
					pl.cd.canKit = true;
					pl.cd.starting1 = false;
					pl.cd.starting2 = false;
					pl.selectedKit.clear();
					kh.initInv();
				}
			}
		}
	}
}
