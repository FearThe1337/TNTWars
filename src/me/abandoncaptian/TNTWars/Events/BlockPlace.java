package me.abandoncaptian.TNTWars.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.abandoncaptian.TNTWars.Main;

public class BlockPlace implements Listener {
	Main pl;

	public BlockPlace(Main plugin) {
		pl = plugin;
	}

	@EventHandler
	public void placeBlock(BlockPlaceEvent e) {
		if (pl.allInGame.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
			e.getPlayer().sendMessage("§7§l[§c§lTNT Wars§7§l] §cCan't place while in-game");
		}
		return;
	}
}