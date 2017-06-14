package me.abandoncaptian.TNTWars.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.abandoncaptian.TNTWars.Main;

public class EntityDamage implements Listener {
	Main pl;

	public EntityDamage(Main plugin) {
		pl = plugin;
	}

	@EventHandler
	public void playerDamaged(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player){
			if(e.getCause().equals(DamageCause.FALL))e.setCancelled(true);
		}
	}

}
