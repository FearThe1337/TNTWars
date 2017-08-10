package me.abandoncaptian.TNTWars.Events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import me.abandoncaptian.TNTWars.GameFunc;
import me.abandoncaptian.TNTWars.Main;
import me.abandoncaptian.TNTWars.MenuHandler;

public class BlockPlace implements Listener {
	Main pl;
	GameFunc GF;
	MenuHandler MH;

	public BlockPlace(Main plugin) {
		pl = plugin;
		GF = new GameFunc(plugin);
		MH = new MenuHandler(plugin);
	}

	@EventHandler
	public void placeBlock(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(!p.hasPermission("tntwars.bypass")){
			e.setBuild(false);
			e.setCancelled(true);
		}
		if(pl.allInGame.contains(p.getName()))e.setCancelled(true);
		if(pl.allQueue.contains(p.getName()))e.setCancelled(true);
		ItemStack hand = e.getItemInHand();
		if(hand == null)return;
		if(!hand.hasItemMeta())return;
		if(hand.getItemMeta().getDisplayName().contains("§cLeave TNT Wars")){
			GF.gameLeave(p.getName());
			e.setCancelled(true);
			e.setBuild(false);
		}
		if(hand.getItemMeta().getDisplayName().contains("§aKit Shop")){
			MH.openKitShop(p);
			e.setCancelled(true);
			e.setBuild(false);
		}
		if(hand.getItemMeta().getDisplayName().contains("§aTNT Wars Kits")){
			MH.openKitMenu(p);
			if(pl.allQueue.contains(p.getName()))p.getInventory().setItem(4, GF.addItem(new ItemStack(Material.CHEST), "§aTNT Wars Kits", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----")));
			e.setCancelled(true);
			e.setBuild(false);
		}
		if(hand.getItemMeta().getDisplayName().contains("§cTNT Wars Menu")){
			MH.openMainMenu(p);
			p.getInventory().setItem(0, GF.addItem(new ItemStack(Material.TNT), "§cTNT Wars Menu", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----")));
			e.setCancelled(true);
			e.setBuild(false);
		}
		return;
	}
}