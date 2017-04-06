package me.abandoncaptian.TNTWars.Events;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitTask;

import me.abandoncaptian.TNTWars.CountDowns;
import me.abandoncaptian.TNTWars.InvAndExp;
import me.abandoncaptian.TNTWars.KitHandler;
import me.abandoncaptian.TNTWars.Main;
import net.minecraft.server.v1_11_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_11_R1.PacketPlayInClientCommand.EnumClientCommand;

public class PlayerDeath implements Listener{
	Main pl;
	CountDowns cd;
	InvAndExp IAE;
	KitHandler kh;
	BukkitTask deathTest;
	boolean isDead;
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
			    PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
			    CraftPlayer craftPlayer = (CraftPlayer)p;
			    craftPlayer.getHandle().playerConnection.a(packet);
				Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
					
					@Override
					public void run() {
						p.sendTitle("§7§l[§c§lTNT Wars§7§l]", "§bYou were eliminated", 0, 60, 0);
						p.closeInventory();
						p.getInventory().clear();
						pl.IAE.InventorySwitch(p);
						pl.IAE.ExpSwitch(p.getName());
					}
				}, 5);
				e.getDrops().clear();
				pl.dead.add(p.getName());
				pl.spec.add(p.getName());
				e.setDeathMessage(null);
				p.setHealthScale(20);
				pl.inGame.remove(p.getName());
				pl.UpdateBoard(true, p.getName());
				int game = pl.inGame.size();
				e.getEntity().teleport(pl.spawnpoint);
				if(game == 1){
					String winner = pl.inGame.get(0);
					Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
						@Override
						public void run() {
							Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b§l" + winner + " §6has won!");
						}
					}, 2);
					for(String name : pl.spec){
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l]", "§b" + pl.inGame.get(0) + " has won!", 0, 120, 0);
					}
					Bukkit.getPlayer(pl.inGame.get(0)).setHealth(20);
					Bukkit.getPlayer(pl.inGame.get(0)).setFoodLevel(20);
					Bukkit.getPlayer(pl.inGame.get(0)).getInventory().clear();
					Bukkit.getPlayer(pl.inGame.get(0)).closeInventory();
					pl.UpdateBoard(true, pl.inGame.get(0));
					pl.IAE.InventorySwitch(Bukkit.getPlayer(pl.inGame.get(0)));
					pl.IAE.ExpSwitch(pl.inGame.get(0));
					Bukkit.getPlayer(pl.inGame.get(0)).setHealthScale(20);
					Bukkit.getPlayer(pl.inGame.get(0)).teleport(pl.spawnpoint);
					Bukkit.getPlayer(pl.inGame.get(0)).sendTitle("§7§l[§c§lTNT Wars§7§l]", "§bYou Won", 0, 120, 0);
					pl.inGame.clear();
					pl.cd.active = false;
					pl.cd.canKit = true;
					pl.cd.starting1 = false;
					pl.cd.starting2 = false;
					pl.spec.clear();
					pl.selectedKit.clear();
					kh.initInv();
				}
			}
		}
	}
}
