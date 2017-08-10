package me.abandoncaptian.TNTWars.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

import me.abandoncaptian.TNTWars.Main;

public class BlockExplode implements Listener {
	Main pl;

	public BlockExplode(Main plugin) {
		pl = plugin;
	}

	@EventHandler
	public void tntExplodesBlocks(BlockExplodeEvent e) {
		e.blockList().clear();
		e.setCancelled(true);
		return;
	}
}
