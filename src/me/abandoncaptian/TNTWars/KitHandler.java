package me.abandoncaptian.TNTWars;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitHandler implements Listener{
	static Inventory inv;
	Main pl;
	public KitHandler(Main plugin) {
		pl = plugin;
		ItemMeta meta;
		List<String> lore = new ArrayList<String>();
		inv = Bukkit.createInventory(null, 54, "§c§lTNT Wars §6Kits Menu");
		ItemStack kit1 = new ItemStack(Material.BOW);
		meta = kit1.getItemMeta();
		meta.setDisplayName("§b§lSniper");
		lore.add("§6§l---- §c[ TNT Wars ] §6§l----");
		lore.add("§7 - §6Get a more accurate toss from tnt");
		lore.add("§fRequested By: §7nixcluster");
		lore.add("§7 - §6Holds 1 TNT at a time");
		lore.add("§7 - §6Longer TNT respawn rate");
		meta.setLore(lore);
		lore.clear();
		kit1.setItemMeta(meta);
		ItemStack kit2 = new ItemStack(Material.REDSTONE);
		meta = kit2.getItemMeta();
		meta.setDisplayName("§b§lShort Fuse");
		lore.add("§6§l---- §c[ TNT Wars ] §6§l----");
		lore.add("§7 - §6Your TNT has a shorter fuse");
		lore.add("§fRequested By: §7Mr_Ender86");
		meta.setLore(lore);
		lore.clear();
		kit2.setItemMeta(meta);
		ItemStack kit3 = new ItemStack(Material.RED_SHULKER_BOX);
		meta = kit3.getItemMeta();
		meta.setDisplayName("§b§lHeavy Loader");
		lore.add("§6§l---- §c[ TNT Wars ] §6§l----");
		lore.add("§7 - §6Hold up to 5 TNT at a time");
		lore.add("§7 - §6Longer TNT respawn rate");
		meta.setLore(lore);
		lore.clear();
		kit3.setItemMeta(meta);
		ItemStack kit4 = new ItemStack(Material.DIAMOND_PICKAXE);
		meta = kit4.getItemMeta();
		meta.setDisplayName("§b§lMiner");
		lore.add("§6§l---- §c[ TNT Wars ] §6§l----");
		lore.add("§7 - §6Place your TNT instead of throwing it");
		lore.add("§7 - §6Fuse lasts Longer");
		meta.setLore(lore);
		lore.clear();
		kit4.setItemMeta(meta);
		ItemStack kit5 = new ItemStack(Material.TNT);
		meta = kit5.getItemMeta();
		meta.setDisplayName("§b§lSuicide Bomber");
		lore.add("§6§l---- §c[ TNT Wars ] §6§l----");
		lore.add("§7 - §6Activate your TNT for a instant explosion");
		lore.add("§7 - §6Receive 1/2 the damage from the TNT explosion");
		lore.add("§7 - §6Holds 1 TNT at a time");
		lore.add("§fRequested By: §7SashaLarie");
		meta.setLore(lore);
		lore.clear();
		kit5.setItemMeta(meta);
		ItemStack kit6 = new ItemStack(Material.SLIME_BALL);
		meta = kit6.getItemMeta();
		meta.setDisplayName("§b§lGlue Factory Worker");
		lore.add("§6§l---- §c[ TNT Wars ] §6§l----");
		lore.add("§7 - §6Once your TNT hits the ground, it sticks");
		meta.setLore(lore);
		lore.clear();
		kit6.setItemMeta(meta);
		ItemStack kit7 = new ItemStack(Material.ENDER_PEARL);
		meta = kit7.getItemMeta();
		meta.setDisplayName("§b§lEnder");
		lore.add("§6§l---- §c[ TNT Wars ] §6§l----");
		lore.add("§7 - §6Teleport the TNT to look location(20 Block Range)");
		lore.add("§fRequested By: §7Mrs_Ender88");
		meta.setLore(lore);
		lore.clear();
		kit7.setItemMeta(meta);
		ItemStack kit8 = new ItemStack(Material.STICK);
		meta = kit8.getItemMeta();
		meta.setDisplayName("§b§lBoomerang");
		lore.add("§6§l---- §c[ TNT Wars ] §6§l----");
		lore.add("§7 - §6Throw tnt far but it comes back half the distance");
		lore.add("§fRequested By: §7A past bug in the plugin");
		meta.setLore(lore);
		lore.clear();
		kit8.setItemMeta(meta);
		ItemStack kit9 = new ItemStack(Material.ARROW);
		meta = kit9.getItemMeta();
		meta.setDisplayName("§b§lRandom");
		lore.add("§6§l---- §c[ TNT Wars ] §6§l----");
		lore.add("§7 - §6Click for a random kit");
		meta.setLore(lore);
		lore.clear();
		kit9.setItemMeta(meta);
		ItemStack kit10 = new ItemStack(Material.BOOK);
		meta = kit10.getItemMeta();
		meta.setDisplayName("§b§lDonor Perks");
		lore.add("§6§l---- §c[ TNT Wars ] §6§l----");
		lore.add("§7 - §6Choose your donator settings");
		lore.add("§7 - §6Comming Soon!");
		meta.setLore(lore);
		lore.clear();
		kit10.setItemMeta(meta);
		inv.setItem(10, kit1);
		inv.setItem(11, kit2);
		inv.setItem(12, kit3);
		inv.setItem(13, kit4);
		inv.setItem(14, kit5);
		inv.setItem(15, kit6);
		inv.setItem(16, kit7);
		inv.setItem(22, kit8);
		inv.setItem(40, kit9);
		inv.setItem(45, kit10);
	}

	@EventHandler
	public void invClick(InventoryClickEvent e){
		if(e.getClickedInventory().getTitle() == null){
			return;
		}else if(e.getClickedInventory().getTitle() == "§c§lTNT Wars §6Kits Menu"){
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			ItemStack clicked = e.getCurrentItem();
			String itemName = clicked.getItemMeta().getDisplayName();
			switch(itemName){
			case "§b§lSniper": 
				p.sendMessage("§6You selected: " + itemName);
				pl.selectedKit.put(p.getName(), "Sniper");
				p.closeInventory();
				break;
			case "§b§lShort Fuse": 
				p.sendMessage("§6You selected: " + itemName);
				pl.selectedKit.put(p.getName(), "Short Fuse");
				p.closeInventory();
				break;
			case "§b§lHeavy Loader": 
				p.sendMessage("§6You selected: " + itemName);
				pl.selectedKit.put(p.getName(), "Heavy Loader");
				p.closeInventory();
				break;
			case "§b§lMiner": 
				p.sendMessage("§6You selected: " + itemName);
				pl.selectedKit.put(p.getName(), "Miner");
				p.closeInventory();
				break;
			case "§b§lSuicide Bomber": 
				p.sendMessage("§6You selected: " + itemName);
				pl.selectedKit.put(p.getName(), "Suicide Bomber");
				p.closeInventory();
				break;
			case "§b§lGlue Factory Worker": 
				p.sendMessage("§6You selected: " + itemName);
				pl.selectedKit.put(p.getName(), "Glue Factory Worker");
				p.closeInventory();
				break;
			case "§b§lEnder": 
				p.sendMessage("§6You selected: " + itemName);
				pl.selectedKit.put(p.getName(), "Ender");
				p.closeInventory();
				break;
			case "§b§lBoomerang": 
				p.sendMessage("§6You selected: " + itemName);
				pl.selectedKit.put(p.getName(), "Boomerang");
				p.closeInventory();
				break;
			case "§b§lRandom": 
				p.sendMessage("§6You selected: " + itemName);
				p.closeInventory();
				break;
			case "§b§lDonor Perks":
				p.sendMessage("§cThis feature is not implamented yet");
				p.closeInventory();
				break;
			default: 
				p.sendMessage("§Invalid Kit!");
				p.closeInventory();
				break;
			}
		}
	}

	public void kitsMenu(Player p){
		p.openInventory(inv);
	}
}
