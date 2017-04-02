package me.abandoncaptian.TNTWars;

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

import com.google.common.collect.Lists;

public class MenuHandlerHost implements Listener{
	Inventory inv;
	Main pl;
	public MenuHandlerHost(Main plugin) {
		pl = plugin;
		inv = Bukkit.createInventory(null, 54, "�c�lTNT Wars Menu Host");
		addMenuItem(new ItemStack(Material.WOOL, 1, (short)13),
				"�aJoin TNT Wars",
				Lists.newArrayList(
						"�6�l---- �c[ TNT Wars ] �6�l----",
						"�bClick to join TNT Wars"),
				10
				);
		addMenuItem(new ItemStack(Material.WOOL, 1, (short)14),
				"�aLeave TNT Wars",
				Lists.newArrayList(
						"�6�l---- �c[ TNT Wars ] �6�l----",
						"�bClick to leave TNT Wars"),
				13
		);
		addMenuItem(new ItemStack(Material.CHEST),
				"�aTNT Wars Kits",
				Lists.newArrayList(
						"�6�l---- �c[ TNT Wars ] �6�l----",
						"�bClick to see TNT Wars kits"),
				16
		);
		addMenuItem(new ItemStack(Material.TNT),
				"�aForce Start TNT Wars",
				Lists.newArrayList(
						"�6�l---- �c[ TNT Wars ] �6�l----",
						"�bClick to force start TNT Wars"),
				21
		);
		addMenuItem(new ItemStack(Material.BEACON),
				"�aSet TNT Wars Spawn",
				Lists.newArrayList(
						"�6�l---- �c[ TNT Wars ] �6�l----",
						"�bClick to set TNT Wars spawn"),
				23
		);
		addMenuItem(new ItemStack(Material.ARROW),
				"�aClose TNT Wars Menu",
				Lists.newArrayList(
						"�6�l---- �c[ TNT Wars ] �6�l----",
						"�bClick to close TNT Wars menu"),
				40
		);
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
				if(clickedInv.getTitle().equals("�c�lTNT Wars Menu Host")){
					e.setCancelled(true);
					ItemStack clicked = e.getCurrentItem();
					if(clicked == null)return;
					if(clicked.hasItemMeta()){
						String itemName = clicked.getItemMeta().getDisplayName();
						switch(itemName){
						case "�aJoin TNT Wars": 
							p.performCommand("tw j");
							p.closeInventory();
							break;
						case "�aLeave TNT Wars": 
							p.performCommand("tw l");
							p.closeInventory();
							break;
						case "�aTNT Wars Kits": 
							p.closeInventory();
							Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
								@Override
								public void run() {
									p.performCommand("tw kits");
								}
							}, 1);
							break;
						case "�aSet TNT Wars Spawn": 
							p.performCommand("tw setspawn");
							p.closeInventory();
							break;
						case "�aForce Start TNT Wars": 
							p.performCommand("tw forcestart");
							p.closeInventory();
							break;
						case "�aClose TNT Wars Menu": 
							p.closeInventory();
							break;
						default: 
							p.sendMessage("�cERROR HOST");
							p.closeInventory();
							break;
						}
						return;
					}
				}
			}
		}
	}

	public void MenuHosts(Player p){
		p.openInventory(inv);
	}
}