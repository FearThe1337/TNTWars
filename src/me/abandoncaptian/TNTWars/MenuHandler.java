package me.abandoncaptian.TNTWars;

import java.util.List;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuHandler implements Listener{
	Inventory inv;
	Main pl;
	DonorPerksMenu DPM;
	public MenuHandler(Main plugin) {
		pl = plugin;
		inv = Bukkit.createInventory(null, 45, "§7§l[§c§lTNT Wars§7§l] §6§lMenu");
		addMenuItem(new ItemStack(Material.WOOL, 1, (short)13),
				"§aJoin TNT Wars",
				Lists.newArrayList(
						"§6§l---- §c[ TNT Wars ] §6§l----",
						"§bClick to join TNT Wars"),
				10
				);
		addMenuItem(new ItemStack(Material.CHEST),
				"§aTNT Wars Kits",
				Lists.newArrayList(
						"§6§l---- §c[ TNT Wars ] §6§l----",
						"§bClick to see TNT Wars kits"),
				13
				);
		addMenuItem(new ItemStack(Material.WOOL, 1, (short)14),
				"§aLeave TNT Wars",
				Lists.newArrayList(
						"§6§l---- §c[ TNT Wars ] §6§l----",
						"§bClick to leave TNT Wars"),
				16
				);
		addMenuItem(new ItemStack(Material.ARROW),
				"§aClose TNT Wars Menu",
				Lists.newArrayList(
						"§6§l---- §c[ TNT Wars ] §6§l----",
						"§bClick to close TNT Wars Menu"),
				31
				);
		addMenuItem(new ItemStack(Material.BOOK), "§b§lDonor Perks",
				Lists.newArrayList(
						"§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Choose your donator settings",
						"§7 - §6Coming Soon!"), 36);
	}

	private void addMenuItem(ItemStack item, String name, List<String> lore, int invPos) {
		ItemMeta meta;
		meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(invPos, item);
	}

	@EventHandler
	public void invClick(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		Inventory clickedInv = e.getClickedInventory();
		if(clickedInv != null){
			if(clickedInv.getSize() == inv.getSize()){
				if(clickedInv.getTitle().equals("§7§l[§c§lTNT Wars§7§l] §6§lMenu")){
					e.setCancelled(true);
					ItemStack clicked = e.getCurrentItem();
					if(clicked == null)return;
					if(clicked.hasItemMeta()){
						String itemName = clicked.getItemMeta().getDisplayName();
						switch(itemName){
						case "§aJoin TNT Wars": 
							p.performCommand("tw j");
							p.closeInventory();
							break;
						case "§aLeave TNT Wars": 
							p.performCommand("tw l");
							p.closeInventory();
							break;
						case "§aTNT Wars Kits": 
							p.closeInventory();
							Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
								@Override
								public void run() {
									p.performCommand("tw kits");
								}
							}, 1);
							break;
						case "§aClose TNT Wars Menu": 
							p.closeInventory();
							break;
						case "§b§lDonor Perks":
							p.closeInventory();
							Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
								@Override
								public void run() {
									pl.DPM.openInv(p);
								}
							}, 1);
							break;
						default: 
							p.sendMessage("§cERROR");
							p.closeInventory();
							break;
						}
						return;
					}
				}
			}
		}
	}

	public void Menu(Player p){
		p.openInventory(inv);
	}
}
