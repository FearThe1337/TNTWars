package me.abandoncaptian.TNTWars.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.abandoncaptian.TNTWars.CountDowns;
import me.abandoncaptian.TNTWars.InvAndExp;
import me.abandoncaptian.TNTWars.Main;

public class EntityDamageByEntity implements Listener {
	Main pl;
	CountDowns cd;
	InvAndExp IAE;

	public EntityDamageByEntity(Main plugin) {
		pl = plugin;
		cd = new CountDowns(plugin);
	}

	@EventHandler
	public void tntDamage(EntityDamageByEntityEvent e) {
		for(String map : pl.arenas.values()){
			if (pl.cd.active.get(map)) {
				if (e.getEntity() instanceof Player) {
					Player p = (Player) e.getEntity();
					if (e.getDamager() instanceof TNTPrimed) {
						if (pl.allInGame.contains(p.getName())) {
							if (pl.selectedKit.get(e.getDamager().getCustomName()) == "Vampire") {
								double dam = p.getLastDamage();
								if (dam > 4) {
									Player damager = Bukkit.getPlayer(e.getDamager().getCustomName());
									if (damager.getHealth() <= 18) {
										damager.setHealth(damager.getHealth() + 2);
									}
								}
							}
							if (p.getName() == e.getDamager().getCustomName()) {
								if (pl.selectedKit.get(p.getName()) == "Suicide Bomber") {
									double dam = p.getLastDamage();
									e.setDamage(dam / 2);
								}
							}
							if (pl.selectedKit.get(p.getName()) == "Tank") {
								double dam = p.getLastDamage();
								e.setDamage(dam / 1.5);
							}
							Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
								@Override
								public void run() {
									if (pl.dead.get(map).contains(p.getName())) {
										pl.dead.get(map).remove(p.getName());
										int game = pl.inGame.get(map).size();
										String cName = e.getDamager().getCustomName();
										cName = ChatColor.stripColor(cName);
										String killer = null;
										if(pl.cName.size()>=1){
											for(String play : pl.cName.keySet()){
												if(pl.cName.get(play).contains(cName)){
													killer = play;
													break;
												}
											}
											if(killer == null)killer = e.getDamager().getCustomName();
										}else{
											killer = e.getDamager().getCustomName();
										}
										if (e.getEntity().getName() == killer){
											Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + e.getEntity().getName() + " §6pulled a SashaLarie and killed themself §7- §b" + game + " remain!");	
										}else{
											if(killer == e.getDamager().getCustomName()){
												Bukkit.broadcastMessage("§6§l------ §7§l[§c§lTNT Wars§7§l] §6§l------");
												Bukkit.broadcastMessage("§b" + e.getEntity().getName() + " §6was killed by §c" + killer + " §7- §b" + game + " remain!");
											}else{
												Bukkit.broadcastMessage("§6§l------ §7§l[§c§lTNT Wars§7§l] §6§l------");
												Bukkit.broadcastMessage("§b" + e.getEntity().getName() + " §6was killed by §c" + killer);
												Bukkit.broadcastMessage("§bUsing §7[" + e.getDamager().getCustomName() + "§7] §cTNT §7- §b" + game + " remain!");
											}
										}
									}
								}
							}, 1);
						} else {
							e.setCancelled(true);
							p.sendMessage(
									"§7§l[§c§lTNT Wars§7§l] §7You got lucky this time, the next TNT won't be so kind!");
						}
					}else {
						if (!pl.allInGame.contains(p.getName()))return;
						else e.setCancelled(true);
					}
				}
			}
		}
	}
}