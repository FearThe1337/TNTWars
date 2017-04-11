package me.abandoncaptian.TNTWars;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.google.common.collect.Lists;

public class KitHandler implements Listener {
	public Inventory inv;
	Main pl;
	DonorPerksMenu DPM;

	public KitHandler(Main plugin) {
		pl = plugin;
		this.inv = Bukkit.createInventory(null, 54, "§7§l[§c§lTNT Wars§7§l] §6§lKit Menu");
	}

	public void initInv() {
		// Row 1

		addMenuItem(new ItemStack(Material.BOW), "§b§lSniper",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Get a more accurate toss from tnt",
						"§7 - §6Longer TNT respawn rate", "§7 - §6Holds 1 TNT at a time", " ",
						"§fRequested By: §7nixcluster"),
				10);
		addMenuItem(new ItemStack(Material.BLAZE_ROD), "§b§lDoctor Who",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6You have x2 health at the start", " ",
						"§fRequested By: §7Mrs_Ender88"),
				11);
		addMenuItem(new ItemStack(Material.RED_SHULKER_BOX), "§b§lHeavy Loader",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Hold up to 5 TNT at a time",
						"§7 - §6Longer TNT respawn rate"),
				12);
		addMenuItem(new ItemStack(Material.DIAMOND_PICKAXE), "§b§lMiner",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Place your TNT instead of throwing it",
						"§7 - §6Recieve a 10 Minute speed boost", "§7 - §6Right Click : 2 Sec Fuse",
						"§7 - §6Left Click : 5 Sec Fuse"),
				13);
		addMenuItem(new ItemStack(Material.TNT), "§b§lSuicide Bomber",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Activate your TNT for a instant explosion",
						"§7 - §6Receive 1/2 the damage from the TNT explosion", "§7 - §6Holds 1 TNT at a time", " ",
						"§fRequested By: §7SashaLarie"),
				14);
		addMenuItem(new ItemStack(Material.SLIME_BALL), "§b§lGlue Factory Worker",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Once your TNT hits the ground, it sticks", " ", "§fRequested By: §7Mr_Ender86"),
				15);
		addMenuItem(new ItemStack(Material.ENDER_PEARL), "§b§lEnder",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Teleport the TNT to look location(30 Block Range)", " ",
						"§fRequested By: §7Mrs_Ender88"),
				16);

		// Row 2

		addMenuItem(new ItemStack(Material.STICK), "§b§lBoomerang",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Throw tnt far but it comes back half the distance", " ",
						"§fRequested By: §7A past bug in the plugin"),
				19);
		addMenuItem(new ItemStack(Material.POTION), "§b§lPotion Worker",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Cause a potion effect to players near the explosion", " ",
						"§fRequested By: §7Mr_Ender86"),
				20);
		addMenuItem(new ItemStack(Material.REDSTONE_BLOCK), "§b§lShort Fuse",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Your TNT has a shorter fuse", " ",
						"§fRequested By: §7Mr_Ender86"),
				21);
		addMenuItem(new ItemStack(Material.DIAMOND_CHESTPLATE), "§b§lTank",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6take 1/2 damage from TNT"), 22);
		addSpecialMenuItem(new ItemStack(Material.LEATHER_CHESTPLATE), "§b§lBribed", Color.fromRGB(170, 0, 0),
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Spawn with random armor", " ",
						"§7In Honor Of timelord_emma's manager promotion"),
				23);
		addMenuItem(new ItemStack(Material.NETHER_STAR), "§b§lHail Mary",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Your TNT causes a larger explosion"),
				24);
		addMenuItem(new ItemStack(Material.GLASS), "§b§lSpace Man",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Your TNT has no gravity"), 25);

		// Row 3

		addMenuItem(new ItemStack(Material.BLAZE_POWDER), "§b§lStorm",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Your TNT rains from the sky", " ",
						"§fRequested By: §7TeraStorm"),
				28);
		addMenuItem(new ItemStack(Material.REDSTONE), "§b§lVampire",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6If your TNT Strikes an enemy,",
						"§7 - §6gain 1 heart", " ", "§fRequested By: §7TeraStorm"),
				29);
		addMenuItem(new ItemStack(Material.BARRIER), "§b§lTBA",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Coming Soon"), 30);
		addMenuItem(new ItemStack(Material.BARRIER), "§b§lTBA",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Coming Soon"), 31);
		addMenuItem(new ItemStack(Material.BARRIER), "§b§lTBA",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Coming Soon"), 32);
		addMenuItem(new ItemStack(Material.BARRIER), "§b§lTBA",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Coming Soon"), 33);
		addMenuItem(new ItemStack(Material.BARRIER), "§b§lTBA",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Coming Soon"), 34);

		// Extra

		addMenuItem(new ItemStack(Material.ARROW), "§b§lRandom",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Click for a random kit"), 49);
		/*
		 * addMenuItem(new ItemStack(Material.INK_SACK), "§b§lVirg Special",
		 * Lists.newArrayList( "§6§l---- §c[ TNT Wars ] §6§l----",
		 * "§7 - §6Exclusive to: TheVirginian"), 53);
		 */
		addMenuItem(new ItemStack(Material.BOOK), "§b§lDonor Perks", Lists.newArrayList(
				"§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Choose your donator settings", "§7 - §6Coming Soon!"), 45);
	}

	private void addMenuItem(ItemStack item, String name, List<String> lore, int invPos) {
		ItemMeta meta;
		meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(invPos, item);
	}

	private void addSpecialMenuItem(ItemStack item, String name, Color color, List<String> lore, int invPos) {
		LeatherArmorMeta meta;
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setDisplayName(name);
		meta.setColor(color);
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(invPos, item);
	}

	@EventHandler
	public void invClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (pl.inGame.contains(e.getWhoClicked().getName())) {
			Inventory clickedInv = e.getClickedInventory();
			if (clickedInv != null) {
				if (clickedInv.getSize() == inv.getSize()) {
					if (clickedInv.getTitle().equals("§7§l[§c§lTNT Wars§7§l] §6§lKit Menu")) {
						p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou can't change kits while in-game.");
						e.setCancelled(true);
						p.closeInventory();
					}
				}
			}
		}
		if (pl.gameQueue.contains(e.getWhoClicked().getName())) {
			Inventory clickedInv = e.getClickedInventory();
			if (clickedInv != null) {
				if (clickedInv.getSize() == inv.getSize()) {
					if (clickedInv.getTitle().equals("§7§l[§c§lTNT Wars§7§l] §6§lKit Menu")) {
						e.setCancelled(true);
						ItemStack clicked = e.getCurrentItem();
						if (clicked == null)
							return;
						if (clicked.hasItemMeta()) {
							String itemName = clicked.getItemMeta().getDisplayName();
							switch (itemName) {
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
							case "§b§lPotion Worker":
								p.sendMessage("§6You selected: " + itemName);
								pl.selectedKit.put(p.getName(), "Potion Worker");
								p.closeInventory();
								break;
							case "§b§lDoctor Who":
								p.sendMessage("§6You selected: " + itemName);
								pl.selectedKit.put(p.getName(), "Doctor Who");
								p.closeInventory();
								break;
							case "§b§lTank":
								p.sendMessage("§6You selected: " + itemName);
								pl.selectedKit.put(p.getName(), "Tank");
								p.closeInventory();
								break;
							case "§b§lRandom":
								p.sendMessage("§6You selected: " + itemName);
								pl.selectedKit.put(p.getName(), "Random");
								p.closeInventory();
								break;
							case "§b§lBribed":
								p.sendMessage("§6You selected: " + itemName);
								pl.selectedKit.put(p.getName(), "Bribed");
								p.closeInventory();
								break;
							case "§b§lHail Mary":
								p.sendMessage("§6You selected: " + itemName);
								pl.selectedKit.put(p.getName(), "Hail Mary");
								p.closeInventory();
								break;
							case "§b§lSpace Man":
								p.sendMessage("§6You selected: " + itemName);
								pl.selectedKit.put(p.getName(), "Space Man");
								p.closeInventory();
								break;
							case "§b§lStorm":
								p.sendMessage("§6You selected: " + itemName);
								pl.selectedKit.put(p.getName(), "Storm");
								p.closeInventory();
								break;
							case "§b§lVampire":
								p.sendMessage("§6You selected: " + itemName);
								pl.selectedKit.put(p.getName(), "Vampire");
								p.closeInventory();
								break;
							case "§b§lVirg Special":
								if (p.getName().equals("abandoncaptian") || p.getName().equals("TheVirginian")) {
									p.sendMessage("§6You selected: " + itemName);
									pl.selectedKit.put(p.getName(), "Virg Special");
									p.closeInventory();
								} else {
									p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cThis kit is exclusive to TheVirgian");
								}
								break;
							case "§b§lTBA":
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cThis is not a usable Kit");
								break;
							case "§b§lDonor Perks":
								p.closeInventory();
								Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
									@Override
									public void run() {
										pl.DPM.openInv(p);
									}
								}, 1);
								break;
							default:
								p.closeInventory();
								break;
							}
							return;
						}
					}
				}
			}
		}
	}

	public void kitsMenu(Player p) {
		p.openInventory(inv);
	}
}
