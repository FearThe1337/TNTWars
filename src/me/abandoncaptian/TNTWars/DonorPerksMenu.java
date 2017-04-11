package me.abandoncaptian.TNTWars;

import java.util.HashMap;
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

public class DonorPerksMenu implements Listener {
	public Inventory inv;
	Main pl;

	public DonorPerksMenu(Main plugin) {
		pl = plugin;
		this.inv = Bukkit.createInventory(null, 54, "§7§l[§c§lTNT Wars§7§l] §6§lDonor Perks Menu");
	}

	public void initInv() {
		addMenuItem(new ItemStack(Material.SLIME_BALL), "§bFireworks §7[§aON§7]",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§6When your TNT explodes it",
						"§6launches a firework into the air", " ", "§bClick to turn §7[§aON§7]"),
				10);
		addMenuItem(new ItemStack(Material.MAGMA_CREAM), "§bFireworks §7[§cOFF§7]",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§6When your TNT explodes it",
						"§6launches a firework into the air", " ", "§bClick to turn §7[§cOFF§7]"),
				19);
		addMenuItem(new ItemStack(Material.SLIME_BALL), "§bTNT Outline §7[§aON§7]",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§6Your TNT will be outlined in white", " ",
						"§bClick to turn §7[§aON§7]"),
				12);
		addMenuItem(new ItemStack(Material.MAGMA_CREAM), "§bTNT Outline §7[§cOFF§7]",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§6Your TNT will be outlined in white", " ",
						"§bClick to turn §7[§cOFF§7]"),
				21);
		addMenuItem(new ItemStack(Material.PAPER), "§bCurrent Perk Settings",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to see your current settings"), 49);
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
	public void invClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Inventory clickedInv = e.getClickedInventory();
		if (clickedInv != null) {
			if (clickedInv.getSize() == inv.getSize()) {
				if (clickedInv.getTitle().equals("§7§l[§c§lTNT Wars§7§l] §6§lDonor Perks Menu")) {
					e.setCancelled(true);
					if (p.hasPermission("tntwars.perks")) {
						ItemStack clicked = e.getCurrentItem();
						if (clicked == null)
							return;
						if (clicked.hasItemMeta()) {
							String itemName = clicked.getItemMeta().getDisplayName();
							switch (itemName) {
							case "§bFireworks §7[§aON§7]":
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bFireworks: §7[§aON§7]");
								if (!pl.Perks.containsKey(p.getName())) {
									pl.Perks.put(p.getName(), new HashMap<String, Boolean>());
									pl.Perks.get(p.getName()).put("Fireworks", true);
									pl.Perks.get(p.getName()).put("Outline", false);
								} else {
									pl.Perks.get(p.getName()).put("Fireworks", true);
									if (!pl.Perks.get(p.getName()).containsKey("Outline")) {
										pl.Perks.get(p.getName()).put("Outline", false);
									}
								}
								break;
							case "§bFireworks §7[§cOFF§7]":
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bFireworks: §7[§cOFF§7]");
								if (!pl.Perks.containsKey(p.getName())) {
									pl.Perks.put(p.getName(), new HashMap<String, Boolean>());
									pl.Perks.get(p.getName()).put("Fireworks", false);
									pl.Perks.get(p.getName()).put("Outline", false);
								} else {
									pl.Perks.get(p.getName()).put("Fireworks", false);
									if (!pl.Perks.get(p.getName()).containsKey("Outline")) {
										pl.Perks.get(p.getName()).put("Outline", false);
									}
								}
								break;
							case "§bTNT Outline §7[§aON§7]":
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bTNT Outline: §7[§aON§7]");
								if (!pl.Perks.containsKey(p.getName())) {
									pl.Perks.put(p.getName(), new HashMap<String, Boolean>());
									pl.Perks.get(p.getName()).put("Fireworks", false);
									pl.Perks.get(p.getName()).put("Outline", true);
								} else {
									pl.Perks.get(p.getName()).put("Outline", true);
									if (!pl.Perks.get(p.getName()).containsKey("Fireworks")) {
										pl.Perks.get(p.getName()).put("Fireworks", false);
									}
								}
								break;
							case "§bTNT Outline §7[§cOFF§7]":
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bTNT Outione: §7[§cOFF§7]");
								if (!pl.Perks.containsKey(p.getName())) {
									pl.Perks.put(p.getName(), new HashMap<String, Boolean>());
									pl.Perks.get(p.getName()).put("Fireworks", false);
									pl.Perks.get(p.getName()).put("Outline", false);
								} else {
									pl.Perks.get(p.getName()).put("Outline", false);
									if (!pl.Perks.get(p.getName()).containsKey("Fireworks")) {
										pl.Perks.get(p.getName()).put("Fireworks", false);
									}
								}
								break;
							case "§bCurrent Perk Settings":
								p.closeInventory();
								if (pl.Perks.containsKey(p.getName())) {
									if (pl.Perks.get(p.getName()).containsKey("Fireworks")) {
										if (pl.Perks.get(p.getName()).get("Fireworks")) {
											p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bFireworks: §7[§aON§7]");
										} else {
											p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bFireworks: §7[§cOFF§7]");
										}
									} else {
										pl.Perks.get(p.getName()).put("Fireworks", false);
									}

									if (pl.Perks.get(p.getName()).containsKey("Outline")) {
										if (pl.Perks.get(p.getName()).get("Outline")) {
											p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bTNT Outline: §7[§aON§7]");
										} else {
											p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bTNT Outline: §7[§cOFF§7]");
										}
									} else {
										pl.Perks.get(p.getName()).put("Outline", false);
									}
								}
								break;
							}
						}
					} else {
						p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou don't have donor perk permissions");
						p.closeInventory();
					}
				}
			}
		}
	}

	public void openInv(Player p) {
		p.openInventory(inv);
	}
}
