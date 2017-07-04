package me.abandoncaptian.TNTWars.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

import me.abandoncaptian.TNTWars.CountDowns;
import me.abandoncaptian.TNTWars.EncoderAndDecoder;
import me.abandoncaptian.TNTWars.LoadFunctions;
import me.abandoncaptian.TNTWars.Main;

public class EntityExplode implements Listener {
	Main pl;
	CountDowns cd;
	LoadFunctions LF;
	EncoderAndDecoder ED;

	public EntityExplode(Main plugin) {
		pl = plugin;
		ED = new EncoderAndDecoder(pl);
	}

	@EventHandler
	public void tntExplode(EntityExplodeEvent e) {
		e.blockList().clear();
		for(String map : pl.arenas.values()){
			if (pl.cd.active.get(map)) {
				if (e.getEntity() instanceof TNTPrimed) {
					if (pl.selectedKit.get(e.getEntity().getCustomName()) == "Potion Worker") {
						List<Entity> ents = new ArrayList<Entity>();
						List<Player> players = new ArrayList<Player>();
						ents.clear();
						players.clear();
						ents = e.getEntity().getNearbyEntities(6, 6, 6);
						if (ents != null) {
							if (ents.size() > 1) {
								for (Entity ent : ents) {
									if (ent instanceof Player) {
										if (pl.allInGame.contains(ent.getName())) {
											int rand = (int) (Math.random() * 7);
											((LivingEntity) ent).addPotionEffect(new PotionEffect(pl.potions.get(rand), (20 * 2), 2));
											ents.remove(ent);
										}
									} else {
										ents.remove(ent);
									}
								}
							}else if (ents.size() == 1) {
								if (ents.get(0) instanceof Player) {
									if (pl.allInGame.contains(ents.get(0).getName())) {
										int rand = (int) (Math.random() * 7);
										((LivingEntity) ents.get(0)).addPotionEffect(new PotionEffect(pl.potions.get(rand), (20 * 2), 2));
										ents.remove(ents.get(0));
									}
								} else {
									ents.remove(ents.get(0));
								}
							}
						}
					}
					if (pl.selectedKit.get(e.getEntity().getName()) == "Hail Mary") {
						Location loc = e.getEntity().getLocation();
						loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 5, false, false);
						e.setCancelled(true);
					}
					if (pl.Perks.containsKey(e.getEntity().getCustomName())) {
						String p = "";
						if(pl.cName.values().contains(e.getEntity().getCustomName())){
							for(String player : pl.cName.keySet()){
								if(e.getEntity().getCustomName().contains(pl.cName.get(player)))p = player;
							}
						}else{
							p = e.getEntity().getCustomName();
						}
						if (pl.Perks.get(p).get("Fireworks")) {
							Location loc = e.getEntity().getLocation();
							Firework fw = (Firework) loc.getWorld().spawn(loc, Firework.class);
							FireworkMeta fm = fw.getFireworkMeta();
							Boolean flick = ED.decodeFWSettingsFlicker(p);
							Boolean trail = ED.decodeFWSettingsTrail(p);
							Type type = ED.decodeFWSettingsType(p);
							Color color = ED.decodeFWSettingsColor(p);
							fm.addEffect(FireworkEffect.builder()
									.flicker(flick)
									.trail(trail)
									.with(type)
									.withColor(color)
									.build());
							fw.setFireworkMeta(fm);
						}
					}
					pl.tntActive.remove(e.getEntity());
				}
			}
		}
	}
}