package me.abandoncaptian.TNTWars.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftTNTPrimed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.potion.PotionEffect;

import me.abandoncaptian.TNTWars.CountDowns;
import me.abandoncaptian.TNTWars.LoadFunctions;
import me.abandoncaptian.TNTWars.Main;

public class EntityExplode implements Listener{
	Main pl;
	CountDowns cd;
	LoadFunctions LF;
	public EntityExplode(Main plugin){
		pl = plugin;
	}

	@EventHandler
	public void tntExplode(EntityExplodeEvent e){
		if(pl.cd.active){
			if(e.getEntity() instanceof CraftTNTPrimed){
				if(pl.selectedKit.get(e.getEntity().getCustomName()) == "Potion Worker"){
					List<Entity> ents = new ArrayList<Entity>();
					List<Player> players = new ArrayList<Player>();
					ents.clear();
					players.clear();
					ents = e.getEntity().getNearbyEntities(6, 6, 6);
					if(ents != null){
						if(ents.size() > 1){
							for(Entity ent : ents){
								if(ent instanceof Player){
									if(pl.inGame.contains(ent.getName())){
										int rand = (int) (Math.random()*7);
										((LivingEntity) ent).addPotionEffect(new PotionEffect(pl.potions.get(rand), (20*2), 2));
										ents.remove(ent);
									}
								}else{
									ents.remove(ent);
								}
							}
						}else if(ents.get(0) instanceof Player){
							if(pl.inGame.contains(ents.get(0).getName())){
								int rand = (int) (Math.random()*7);
								((LivingEntity) ents.get(0)).addPotionEffect(new PotionEffect(pl.potions.get(rand), (20*2), 2));
								ents.remove(ents.get(0));
							}
						}else{
							ents.remove(ents.get(0) );
						}
					}
				}
				if(pl.selectedKit.get(e.getEntity().getCustomName()) == "Hail Mary"){
					Location loc = e.getEntity().getLocation();
					loc.getWorld().createExplosion(loc, 5);
					e.setCancelled(true);
				}
				pl.tntActive.remove(e.getEntity());
			}
		}
	}
}
