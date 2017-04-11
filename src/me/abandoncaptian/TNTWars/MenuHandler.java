package me.abandoncaptian.TNTWars;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
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

public class MenuHandler implements Listener {
	Main pl;
	DonorPerksMenu DPM;

	public MenuHandler(Main plugin) {
		pl = plugin;
	}

	public void openMainMenu(Player p){
		Inventory inv = Bukkit.createInventory(p, 54, "§7§l[ §c§lTNT Wars Menu §7§l]");
		HashMap<ItemStack, Integer> items = mainMenuInvContents(p);
		p.openInventory(inv);
		for(ItemStack item : items.keySet()){
			inv.setItem(items.get(item), item);
		}
		return;
	}

	public void openKitMenu(Player p, Inventory inv){
		inv.clear();
		HashMap<ItemStack, Integer> items = kitMenuInvContents(p);
		for(ItemStack item : items.keySet()){
			inv.setItem(items.get(item), item);
		}
		return;
	}

	public void openKitMenu(Player p){
		Inventory inv = Bukkit.createInventory(p, 54, "§7§l[ §c§lTNT Wars Menu §7§l]");
		p.openInventory(inv);
		openKitMenu(p, inv);
	}

	public HashMap<ItemStack, Integer> mainMenuInvContents(Player p){
		HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();
		items.put(addItem(new ItemStack(Material.WOOL, 1, (short) 13), "§aJoin TNT Wars", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to join TNT Wars")), 10);
		items.put(addItem(new ItemStack(Material.CHEST), "§aTNT Wars Kits", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to see TNT Wars kits")), 13);
		items.put(addItem(new ItemStack(Material.WOOL, 1, (short) 14), "§aLeave TNT Wars", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to leave TNT Wars")), 16);
		if(p.hasPermission("tntwars.host"))items.put(addItem(new ItemStack(Material.TNT), "§aForce Start TNT Wars", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to force start TNT Wars")), 20);
		if(p.hasPermission("tntwars.host"))items.put(addItem(new ItemStack(Material.BEACON), "§aSet TNT Wars Spawn", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to set TNT Wars spawn")), 22);
		if(p.hasPermission("tntwars.host"))items.put(addItem(new ItemStack(Material.GLASS), "§aSet TNT Wars Spec Point", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to set TNT Wars spec point")), 24);
		if(p.hasPermission("tntwars.donor"))items.put(addItem(new ItemStack(Material.BOOK), "§b§lDonor Perks", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Choose your donator settings")), 45);
		items.put(addItem(new ItemStack(Material.ARROW), "§aClose TNT Wars Menu", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to close TNT Wars Menu")), 40);
		return items;
	}

	public HashMap<ItemStack, Integer> kitMenuInvContents(Player p){
		HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();
		items.put(addItem(new ItemStack(Material.BOW), "§b§lSniper",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Get a more accurate toss from tnt",
						"§7 - §6Longer TNT respawn rate",
						"§7 - §6Holds 1 TNT at a time",
						" ",
						"§fRequested By: §7nixcluster")), 10);
		items.put(addItem(new ItemStack(Material.BLAZE_ROD), "§b§lDoctor Who",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6You have x2 health at the start",
						" ",
						"§fRequested By: §7Mrs_Ender88")), 11);
		items.put(addItem(new ItemStack(Material.RED_SHULKER_BOX), "§b§lHeavy Loader",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Hold up to 5 TNT at a time",
						"§7 - §6Longer TNT respawn rate")), 12);
		items.put(addItem(new ItemStack(Material.DIAMOND_PICKAXE), "§b§lMiner",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Place your TNT instead of throwing it",
						"§7 - §6Recieve a 10 Minute speed boost",
						"§7 - §6Right Click : 2 Sec Fuse",
						"§7 - §6Left Click : 5 Sec Fuse")), 13);
		items.put(addItem(new ItemStack(Material.TNT), "§b§lSuicide Bomber",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Activate your TNT for a instant explosion",
						"§7 - §6Receive 1/2 the damage from the TNT explosion",
						"§7 - §6Holds 1 TNT at a time", 
						" ",
						"§fRequested By: §7SashaLarie")), 14);
		items.put(addItem(new ItemStack(Material.SLIME_BALL), "§b§lGlue Factory Worker",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Once your TNT hits the ground, it sticks",
						" ", 
						"§fRequested By: §7Mr_Ender86")), 15);
		items.put(addItem(new ItemStack(Material.ENDER_PEARL), "§b§lEnder",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Teleport the TNT to look location(30 Block Range)",
						" ",
						"§fRequested By: §7Mrs_Ender88")), 16);



		items.put(addItem(new ItemStack(Material.STICK), "§b§lBoomerang",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Throw tnt far but it comes back half the distance",
						" ",
						"§fRequested By: §7A past bug in the plugin")), 19);
		items.put(addItem(new ItemStack(Material.POTION), "§b§lPotion Worker",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Cause a potion effect to players near the explosion",
						" ",
						"§fRequested By: §7Mr_Ender86")), 20);
		items.put(addItem(new ItemStack(Material.REDSTONE_BLOCK), "§b§lShort Fuse",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Your TNT has a shorter fuse",
						" ",
						"§fRequested By: §7Mr_Ender86")), 21);
		items.put(addItem(new ItemStack(Material.DIAMOND_CHESTPLATE), "§b§lTank",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6take 1/2 damage from TNT")), 22);
		items.put(addSpecialItem(new ItemStack(Material.LEATHER_CHESTPLATE), "§b§lBribed",
				Color.fromRGB(170, 0, 0),
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Spawn with random armor",
						" ",
						"§7In Honor Of timelord_emma's manager promotion")), 23);
		items.put(addItem(new ItemStack(Material.NETHER_STAR), "§b§lHail Mary",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Your TNT causes a larger explosion")), 24);
		items.put(addItem(new ItemStack(Material.GLASS), "§b§lSpace Man",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Your TNT has no gravity")), 25);



		items.put(addItem(new ItemStack(Material.BLAZE_POWDER), "§b§lStorm",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Your TNT rains from the sky",
						" ",
						"§fRequested By: §7TeraStorm")), 28);
		items.put(addItem(new ItemStack(Material.REDSTONE), "§b§lVampire",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6If your TNT Strikes an enemy,",
						"§7 - §6gain 1 heart",
						" ",
						"§fRequested By: §7TeraStorm")), 29);
		items.put(addItem(new ItemStack(Material.BARRIER), "§b§lTBA 1",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Coming Soon")), 30);
		items.put(addItem(new ItemStack(Material.BARRIER), "§b§lTBA 2",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Coming Soon")), 31);
		items.put(addItem(new ItemStack(Material.BARRIER), "§b§lTBA 3",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Coming Soon")), 32);
		items.put(addItem(new ItemStack(Material.BARRIER), "§b§lTBA 4",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Coming Soon")), 33);
		items.put(addItem(new ItemStack(Material.BARRIER), "§b§lTBA 5",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Coming Soon")), 34);
		if(p.getName().equalsIgnoreCase("thevirginian") || p.getName().equalsIgnoreCase("abandoncaptian"))items.put(addItem(new ItemStack(Material.INK_SACK), "§b§lVirg Special",
				Lists.newArrayList( "§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Exclusive to: TheVirginian")), 53);



		items.put(addItem(new ItemStack(Material.ARROW), "§b§lRandom",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Click for a random kit")), 49);
		return items;
	}

	private ItemStack addItem(ItemStack item, String name, List<String> lore) {
		ItemMeta meta;
		meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	private ItemStack addSpecialItem(ItemStack item, String name, Color color, List<String> lore) {
		LeatherArmorMeta meta;
		meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setDisplayName(name);
		meta.setColor(color);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	@EventHandler
	public void invClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Inventory clickedInv = e.getClickedInventory();
		if (clickedInv != null) {
			if (clickedInv.getTitle().equals("§7§l[ §c§lTNT Wars Menu §7§l]")) {
				e.setCancelled(true);
				if (pl.inGame.contains(e.getWhoClicked().getName())) {
					p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou can't change kits while in-game.");
					e.setCancelled(true);
					p.closeInventory();
					return;
				}
				ItemStack clicked = e.getCurrentItem();
				if (clicked == null)return;
				if (clicked.hasItemMeta()) {
					String itemName = clicked.getItemMeta().getDisplayName();
					switch (itemName) {
					case "§aJoin TNT Wars":
						p.performCommand("tw j");
						p.closeInventory();
						break;
					case "§aLeave TNT Wars":
						p.performCommand("tw l");
						p.closeInventory();
						break;
					case "§aTNT Wars Kits":
						if(pl.gameQueue.contains(p.getName()))openKitMenu(p, clickedInv);
						else p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou need to be queued to select a kit");
						break;
					case "§aForce Start TNT Wars":
						p.performCommand("tw forcestart");
						p.closeInventory();
						break;
					case "§aSet TNT Wars Spawn":
						p.performCommand("tw setspawn");
						p.closeInventory();
						break;
					case "§aSet TNT Wars Spec Point":
						p.performCommand("tw setspecpoint");
						p.closeInventory();
						break;
					case "§aClose TNT Wars Menu":
						p.closeInventory();
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
					//KITS
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
					case "§b§lTBA 1":
					case "§b§lTBA 2":
					case "§b§lTBA 3":
					case "§b§lTBA 4":
					case "§b§lTBA 5":
						p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cThis is not a usable Kit");
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
