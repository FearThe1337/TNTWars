package me.abandoncaptian.TNTWars.Events;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftTNTPrimed;
import org.bukkit.entity.Player;
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
		if (pl.cd.active) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (e.getDamager() instanceof CraftTNTPrimed) {
					if (pl.inGame.contains(p.getName())) {
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
								if (pl.dead.contains(p.getName())) {
									pl.dead.remove(p.getName());
									int game = pl.inGame.size();
									if (e.getEntity().getName() == e.getDamager().getCustomName())
										Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + e.getEntity().getName()
												+ " §6pulled a SashaLarie and killed themself §7- §b" + game
												+ " remain!");
									else
										Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + e.getEntity().getName()
												+ " §6was killed by §c" + e.getDamager().getCustomName() + " §7- §b"
												+ game + " remain!");
								}
							}
						}, 1);
					} else {
						e.setCancelled(true);
						p.sendMessage(
								"§7§l[§c§lTNT Wars§7§l] §7You got lucky this time, the next TNT won't be so kind!");
					}
				}else {
					if (!pl.inGame.contains(p.getName()))return;
					else e.setCancelled(true);
				}
			}
		}
	}
}
