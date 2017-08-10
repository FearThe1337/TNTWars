package me.abandoncaptian.TNTWars.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.abandoncaptian.TNTWars.Main;

public class PlayerDropItem implements Listener {
	Main pl;

	public PlayerDropItem(Main plugin) {
		pl = plugin;
	}
	
	@EventHandler
	public void playerDropItem(PlayerDropItemEvent e){
		e.setCancelled(true);
	}
}
