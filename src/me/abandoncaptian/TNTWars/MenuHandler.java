package me.abandoncaptian.TNTWars;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class MenuHandler implements Listener {
	Main pl;

	public MenuHandler(Main plugin) {
		pl = plugin;
	}

	public void openMainMenu(Player p, Inventory inv){
		inv.clear();
		HashMap<ItemStack, Integer> items = mainMenuInvContents(p);
		for(ItemStack item : items.keySet()){
			inv.setItem(items.get(item), item);
		}
		return;
	}

	public void openMainMenu(Player p){
		Inventory inv = Bukkit.createInventory(p, 54, "§7§l[ §c§lTNT Wars Menu §7§l]");
		p.openInventory(inv);
		openMainMenu(p, inv);
		return;
	}

	public void openDonorMenu(Player p, Inventory inv){
		inv.clear();
		HashMap<ItemStack, Integer> items = donorMenuInvContents(p);
		for(ItemStack item : items.keySet()){
			inv.setItem(items.get(item), item);
		}
		return;
	}

	public void openDonorMenu(Player p){
		Inventory inv = Bukkit.createInventory(p, 54, "§7§l[ §c§lTNT Wars Menu §7§l]");
		p.openInventory(inv);
		openDonorMenu(p, inv);
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

	public void openArenaMenu(Player p, Inventory inv){
		inv.clear();
		HashMap<String, ItemStack> items = ArenaSettingsMenuContents(p);
		for(String i : items.keySet()){
			inv.setItem(Integer.parseInt(i), items.get(i));
		}
		return;
	}

	public void openArenaSettings(Player p, Inventory inv, String arena){
		inv.clear();
		HashMap<ItemStack, Integer> items = ArenaSettingsContents(p, arena);
		for(ItemStack item : items.keySet()){
			inv.setItem(items.get(item), item);
		}
		return;
	}

	public void openFWEditorMenu(Player p, Inventory inv){
		inv.clear();
		HashMap<ItemStack, Integer> items = fwEditorMenuInvContents(p);
		for(ItemStack item : items.keySet()){
			inv.setItem(items.get(item), item);
		}
		return;
	}

	public void openFWEditorMenu(Player p){
		Inventory inv = Bukkit.createInventory(p, 54, "§7§l[ §c§lTNT Wars Menu §7§l]");
		p.openInventory(inv);
		openFWEditorMenu(p, inv);
	}

	public void openCNameMenu(Player p, Inventory inv){
		inv.clear();
		HashMap<ItemStack, Integer> items = cNameMenuInvContents(p);
		for(ItemStack item : items.keySet()){
			inv.setItem(items.get(item), item);
		}
		return;
	}

	public void openCNameMenu(Player p){
		Inventory inv = Bukkit.createInventory(p, 54, "§7§l[ §c§lTNT Wars Menu §7§l]");
		p.openInventory(inv);
		openCNameMenu(p, inv);
	}

	public void openCNameChanger(Player p){
		new AnvilGUI(pl, p, " ", (player, name) -> {
			Boolean usable = true;
			if(!pl.cNameColor.containsKey(p.getName()))pl.cNameColor.put(p.getName(), ChatColor.WHITE);
			if(pl.cName.size() >= 1){
				for(String current: pl.cName.values()){
					if(name.equals(current)){
						usable = false;
						p.sendMessage("§7§l[§c§lTNT Wars§7§l] " + pl.cNameColor.get(p.getName()) + current + " §cis in use already, §bplease choose another.");
						break;
					}
				}
			}else{
				usable = true;
			}
			if(usable){
				pl.cName.put(p.getName(), name);
				p.closeInventory();
				p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bCustom name set to: " + pl.cNameColor.get(p.getName()) + pl.cName.get(p.getName()));
				return name;
			}
			return name;
		});
	}

	public void arenaSelectorMenu(Player p, Inventory inv){
		inv.clear();
		HashMap<String, ItemStack> items = arenaSelectorInvContents(p);
		for(String i : items.keySet()){
			inv.setItem(Integer.parseInt(i), items.get(i));
		}
		return;
	}

	public void arenaSelectorMenu(Player p){
		Inventory inv = Bukkit.createInventory(p, 54, "§7§l[ §c§lTNT Wars Menu §7§l]");
		p.openInventory(inv);
		openCNameMenu(p, inv);
	}

	private HashMap<String, ItemStack> arenaSelectorInvContents(Player p){
		HashMap<String, ItemStack> items = new HashMap<String, ItemStack>();
		for(int i = 0 ; i < 54; i++){
			if(i <= 20){
				items.put(String.valueOf(i), addItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8), " ", Lists.newArrayList()));
			}else if(i >= 25 && i <= 28){
				items.put(String.valueOf(i), addItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8), " ", Lists.newArrayList()));
			}else if(i >= 34){
				items.put(String.valueOf(i), addItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8), " ", Lists.newArrayList()));
			}
		}
		int slot = 20;
		for(int arenaIndex : pl.arenas.keySet()){
			String mapName = pl.arenas.get(arenaIndex);
			int adder = 1;
			if(slot == 25){
				slot = 29;
			}
			if(slot == 34){
				Bukkit.broadcastMessage("§cToo many arenas ATM");
				break;
			}
			if(pl.cd.active.get(mapName)){
				if(!pl.inGame.get(mapName).isEmpty())adder=0;
				if(slot >= 20 && slot <= 24){
					items.put(String.valueOf(slot), addItem(new ItemStack(pl.arenaIcons.get(mapName), pl.inGame.get(mapName).size() + adder), "§aJoin §7: §b" + mapName, 
							Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
									"§bClick to join this arena",
									" ",
									"§bCurrent§7: §a" + pl.inGame.get(mapName).size() +"§7/§a" + pl.gameMax,
									"§bActive§7: §a" + pl.cd.active.get(mapName))));
					slot++;
				}else if(slot >= 29 && slot <= 33){
					items.put(String.valueOf(slot), addItem(new ItemStack(pl.arenaIcons.get(mapName), pl.inGame.get(mapName).size() + adder), "§aJoin §7: §b" + mapName, 
							Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
									"§bClick to join this arena",
									" ",
									"§bCurrent§7: §a" + pl.inGame.get(mapName).size() +"§7/§a" + pl.gameMax,
									"§bActive§7: §a" + pl.cd.active.get(mapName))));
					slot++;
				}
			}else{
				if(slot >= 20 && slot <= 24){
					items.put(String.valueOf(slot), addItem(new ItemStack(pl.arenaIcons.get(mapName), pl.inGame.get(mapName).size() + adder), "§aJoin §7: §b" + mapName, 
							Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
									"§bClick to join this arena",
									" ",
									"§bCurrent§7: §a" + pl.gameQueue.get(mapName).size() +"§7/§a" + pl.gameMax,
									"§bActive§7: §a" + pl.cd.active.get(mapName))));
					slot++;
				}else if(slot >= 29 && slot <= 33){
					items.put(String.valueOf(slot), addItem(new ItemStack(pl.arenaIcons.get(mapName), pl.inGame.get(mapName).size() + adder), "§aJoin §7: §b" + mapName, 
							Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
									"§bClick to join this arena",
									" ",
									"§bCurrent§7: §a" + pl.gameQueue.get(mapName).size() +"§7/§a" + pl.gameMax,
									"§bActive§7: §a" + pl.cd.active.get(mapName))));
					slot++;
				}
			}
		}
		items.put("49", addItem(new ItemStack(Material.BED), "§bReturn to Main Menu",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Return to Main Menu")));
		return items;
	}

	private HashMap<ItemStack, Integer> ArenaSettingsContents(Player p, String arena){
		HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();	
		items.put(addItem(new ItemStack(Material.CHEST), "§aEditing §7: §b" + arena,
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----")), 4);
		items.put(addItem(new ItemStack(Material.BEACON), "§aSet SpawnPoint",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Set the SpawnPoint for this arena")), 20);
		items.put(addItem(new ItemStack(Material.EYE_OF_ENDER), "§aSet SpecPoint",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Set the SpawnPoint for this arena")), 24);
		items.put(addItem(new ItemStack(Material.BED), "§aArena Selector",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Return to the settings arena selector")), 49);
		return items;
	}

	private HashMap<String, ItemStack> ArenaSettingsMenuContents(Player p){
		HashMap<String, ItemStack> items = new HashMap<String, ItemStack>();
		for(int i = 0 ; i < 54; i++){
			if(i <= 20){
				items.put(String.valueOf(i), addItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8), " ", Lists.newArrayList()));
			}else if(i >= 25 && i <= 28){
				items.put(String.valueOf(i), addItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8), " ", Lists.newArrayList()));
			}else if(i >= 34){
				items.put(String.valueOf(i), addItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8), " ", Lists.newArrayList()));
			}
		}
		int slot = 20;
		for(int arenaIndex : pl.arenas.keySet()){
			if(slot == 25){
				slot = 29;
			}
			if(slot == 34){
				Bukkit.broadcastMessage("§cToo many arenas ATM");
				break;
			}
			if(slot >= 20 && slot <= 24){
				items.put(String.valueOf(slot), addItem(new ItemStack(pl.arenaIcons.get(pl.arenas.get(arenaIndex))), "§aEdit §7: §b" + pl.arenas.get(arenaIndex), Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to edit this arena")));
				slot++;
			}else if(slot >= 29 && slot <= 33){
				items.put(String.valueOf(slot), addItem(new ItemStack(pl.arenaIcons.get(pl.arenas.get(arenaIndex))), "§aEdit §7: §b" + pl.arenas.get(arenaIndex), Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to edit this arena")));
				slot++;
			}
		}
		items.put("49", addItem(new ItemStack(Material.BED), "§bReturn to Main Menu",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Return to Main Menu")));
		return items;
	}

	private HashMap<ItemStack, Integer> cNameMenuInvContents(Player p){
		HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();	
		items.put(addItem(new ItemStack(Material.CHEST), "§bColor",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select a color below")), 1);
		items.put(addItem(new ItemStack(Material.WOOL, 1, (short) 14), "§bRed Name",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the color §c§lRed")), 10);
		items.put(addItem(new ItemStack(Material.WOOL, 1, (short) 11), "§bBlue Name",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the color §1§lBlue")), 19);
		items.put(addItem(new ItemStack(Material.WOOL, 1, (short) 13), "§bGreen Name",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the color §2§lGreen")), 28);
		items.put(addItem(new ItemStack(Material.WOOL), "§bWhite Name",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the color §f§lWhite")), 37);
		items.put(addItem(new ItemStack(Material.WOOL, 1, (short) 10), "§bPurple Name",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the color §5§lPurple")), 46);
		items.put(addItem(new ItemStack(Material.BOOK), "§b§lDonor Perks",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Return to donator settings")), 8);
		if(pl.cName.containsKey(p.getName())){
			items.put(addItem(new ItemStack(Material.ANVIL), "§bChange Name",
					Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
							"§6Click to change name",
							" ",
							"§6Current: " + pl.cNameColor.get(p.getName()) + pl.cName.get(p.getName()))), 53);
		}else{
			items.put(addItem(new ItemStack(Material.ANVIL), "§bChange Name",
					Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
							"§6Click to set name",
							" ",
							"§6Current: §7[§cNot Set Yet§7]")), 53);
		}
		return items;
	}

	private HashMap<ItemStack, Integer> fwEditorMenuInvContents(Player p){
		HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();	
		pl.ED.encodeFWSettings(p.getName());
		Boolean opt = pl.ED.decodeFWSettingsFlicker(p.getName());
		if(opt == true){
			items.put(addItem(new ItemStack(Material.SLIME_BALL), "§bFlicker",
					Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
							"§6Toggle the filcker")), 1);
		}else{
			items.put(addItem(new ItemStack(Material.MAGMA_CREAM), "§bFlicker",
					Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
							"§6Toggle the filcker")), 1);
		}
		opt = pl.ED.decodeFWSettingsTrail(p.getName());
		if(opt == true){
			items.put(addItem(new ItemStack(Material.SLIME_BALL), "§bTrail",
					Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
							"§6Toggle the trail")), 3);
		}else{
			items.put(addItem(new ItemStack(Material.MAGMA_CREAM), "§bTrail",
					Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
							"§6Toggle the trail")), 3);
		}
		items.put(addItem(new ItemStack(Material.CHEST), "§bType",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select a type below")), 5);
		items.put(addItem(new ItemStack(Material.SNOW_BALL), "§bBall",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the ball type")), 14);
		items.put(addItem(new ItemStack(Material.SLIME_BALL), "§bLarge Ball",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the large ball type")), 23);
		items.put(addItem(new ItemStack(Material.TNT), "§bBurst",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the burst type")), 32);
		items.put(addItem(new ItemStack(Material.SKULL_ITEM, 1, (short) 4), "§bCreeper",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the creeper type")), 41);
		items.put(addItem(new ItemStack(Material.NETHER_STAR), "§bStar",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the star type")), 50);

		items.put(addItem(new ItemStack(Material.CHEST), "§bColor",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select a color below")), 7);
		items.put(addItem(new ItemStack(Material.WOOL, 1, (short) 14), "§bRed",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the color §c§lRed")), 16);
		items.put(addItem(new ItemStack(Material.WOOL, 1, (short) 11), "§bBlue",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the color §1§lBlue")), 25);
		items.put(addItem(new ItemStack(Material.WOOL, 1, (short) 13), "§bGreen",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the color §2§lGreen")), 34);
		items.put(addItem(new ItemStack(Material.WOOL), "§bWhite",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the color §f§lWhite")), 43);
		items.put(addItem(new ItemStack(Material.WOOL, 1, (short) 10), "§bPurple",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§6Select the color §5§lPurple")), 52);

		items.put(addItem(new ItemStack(Material.BOOK), "§b§lDonor Perks",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Return to donator settings")), 45);
		return items;
	}

	private HashMap<ItemStack, Integer> donorMenuInvContents(Player p){
		HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();	
		items.put(addItem(new ItemStack(Material.BED), "§bReturn to Main Menu",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Return to Main Menu")), 49);
		if(p.hasPermission("tntwars.fireworks")){
			items.put(addItem(new ItemStack(Material.PAPER), "§bFireworks §7Settings",
					Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
							"§6Open Firework settings")), 19);
			boolean toggle = false;
			if(pl.Perks.containsKey(p.getName())){
				if(pl.Perks.get(p.getName()).containsKey("Fireworks")){
					if(pl.Perks.get(p.getName()).get("Fireworks")){
						toggle = true;
						items.put(addItem(new ItemStack(Material.SLIME_BALL), "§bTurn Fireworks §7[§cOFF§7]",
								Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
										"§6When your TNT explodes it",
										"§6launches a firework into the air",
										" ",
										"§bClick to turn §7[§cOFF§7]")), 10);	
					}
				}
			}
			if(!toggle){
				items.put(addItem(new ItemStack(Material.MAGMA_CREAM), "§bTurn Fireworks §7[§aON§7]",
						Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
								"§6When your TNT explodes it",
								"§6launches a firework into the air", 
								" ",
								"§bClick to turn §7[§aON§7]")), 10);
			}
		}
		if(p.hasPermission("tntwars.outline")){
			boolean toggle = false;
			if(pl.Perks.containsKey(p.getName())){
				if(pl.Perks.get(p.getName()).containsKey("Outline")){
					if(pl.Perks.get(p.getName()).get("Outline")){
						toggle = true;
						items.put(addItem(new ItemStack(Material.SLIME_BALL), "§bTurn TNT Outline §7[§cOFF§7]",
								Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
										"§6Your TNT will be outlined in white",
										" ",
										"§bClick to turn §7[§cOFF§7]")), 12);
					}
				}
			}
			if(!toggle){
				items.put(addItem(new ItemStack(Material.MAGMA_CREAM), "§bTurn TNT Outline §7[§aON§7]",
						Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
								"§6Your TNT will be outlined in white", 
								" ",
								"§bClick to turn §7[§aON§7]")), 12);
			}
		}
		if(p.hasPermission("tntwars.customname")){
			boolean toggle = false;
			items.put(addItem(new ItemStack(Material.PAPER), "§bCustom Name §7Settings",
					Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
							"§6Change your custom name settings")), 23);
			if(pl.Perks.containsKey(p.getName())){
				if(pl.Perks.get(p.getName()).containsKey("CName")){
					if(pl.Perks.get(p.getName()).get("CName")){
						toggle = true;
						items.put(addItem(new ItemStack(Material.SLIME_BALL), "§bCustom Name",
								Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
										"§6When your TNT explodes it",
										"§6launches a firework into the air",
										" ",
										"§bClick to toggle the custom name")), 14);	
					}
				}
			}
			if(!toggle){
				items.put(addItem(new ItemStack(Material.MAGMA_CREAM), "§bCustom Name",
						Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
								"§6When your TNT explodes it",
								"§6launches a firework into the air",
								" ",
								"§bClick to toggle the custom name")), 14);	
			}
		}
		if(items.size() == 0){
			items.put(addItem(new ItemStack(Material.BARRIER), "§cNo Avaible Perks",
					Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
							"§6Purchase the perks on the SFT website")), 13);
		}
		return items;
	}

	private HashMap<ItemStack, Integer> mainMenuInvContents(Player p){
		HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();
		items.put(addItem(new ItemStack(Material.BOOK), "§aArena List", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to see all the arenas to join")), 10);
		items.put(addItem(new ItemStack(Material.CHEST), "§aTNT Wars Kits", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to see TNT Wars kits")), 13);
		items.put(addItem(new ItemStack(Material.WOOL, 1, (short) 14), "§aLeave TNT Wars", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to leave TNT Wars")), 16);
		if(p.hasPermission("tntwars.host"))items.put(addItem(new ItemStack(Material.TNT), "§aForce Start TNT Wars", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to force start TNT Wars")), 20);
		if(p.hasPermission("tntwars.host"))items.put(addItem(new ItemStack(Material.PAPER), "§aArenas Settings", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to open arena list")), 24);
		if(p.hasPermission("tntwars.donor"))items.put(addItem(new ItemStack(Material.BOOK), "§b§lDonor Perks", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Choose your donator settings")), 45);
		items.put(addItem(new ItemStack(Material.ARROW), "§aClose TNT Wars Menu", Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§bClick to close TNT Wars Menu")), 40);
		if(!pl.points.containsKey(p.getName())){
			pl.points.put(p.getName(), 0);
		}
		if(!pl.wins.containsKey(p.getName())){
			pl.wins.put(p.getName(), 0);
		}
		if(!pl.loses.containsKey(p.getName())){
			pl.loses.put(p.getName(), 0);
		}
		items.put(addItem(new ItemStack(Material.BOOK), "§aTNT Wars Stats", 
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", 
						"§bPoints§7: " + pl.points.get(p.getName()),
						"§bWins§7: " + pl.wins.get(p.getName()),
						"§bLoses§7: " + pl.loses.get(p.getName()))), 53);
		return items;
	}

	private HashMap<ItemStack, Integer> kitMenuInvContents(Player p){
		HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();
		if(p.hasPermission("tntwars.sniper"))items.put(addItem(new ItemStack(Material.BOW), "§b§lSniper",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Get a more accurate toss from tnt",
						"§7 - §6Longer TNT respawn rate",
						"§7 - §6Holds 1 TNT at a time",
						" ",
						"§fRequested By: §7nixcluster")), 10);
		if(p.hasPermission("tntwars.doctorwho"))items.put(addItem(new ItemStack(Material.BLAZE_ROD), "§b§lDoctor Who",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6You have x2 health at the start",
						" ",
						"§fRequested By: §7Mrs_Ender88")), 11);
		if(p.hasPermission("tntwars.heavyloader"))items.put(addItem(new ItemStack(Material.RED_SHULKER_BOX), "§b§lHeavy Loader",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Hold up to 5 TNT at a time",
						"§7 - §6Longer TNT respawn rate")), 12);
		if(p.hasPermission("tntwars.miner"))items.put(addItem(new ItemStack(Material.DIAMOND_PICKAXE), "§b§lMiner",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Place your TNT instead of throwing it",
						"§7 - §6Recieve a 10 Minute speed boost",
						"§7 - §6Right Click : 2 Sec Fuse",
						"§7 - §6Left Click : 5 Sec Fuse")), 13);
		if(p.hasPermission("tntwars.suicidebomber"))items.put(addItem(new ItemStack(Material.TNT), "§b§lSuicide Bomber",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Activate your TNT for a instant explosion",
						"§7 - §6Receive 1/2 the damage from the TNT explosion",
						"§7 - §6Holds 1 TNT at a time", 
						" ",
						"§fRequested By: §7SashaLarie")), 14);
		if(p.hasPermission("tntwars.gluefactoryworker"))items.put(addItem(new ItemStack(Material.SLIME_BALL), "§b§lGlue Factory Worker",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Once your TNT hits the ground, it sticks",
						" ", 
						"§fRequested By: §7Mr_Ender86")), 15);
		if(p.hasPermission("tntwars.ender"))items.put(addItem(new ItemStack(Material.ENDER_PEARL), "§b§lEnder",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Teleport the TNT to look location(30 Block Range)",
						" ",
						"§fRequested By: §7Mrs_Ender88")), 16);



		if(p.hasPermission("tntwars.boomerang"))items.put(addItem(new ItemStack(Material.STICK), "§b§lBoomerang",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Throw tnt far but it comes back half the distance",
						" ",
						"§fRequested By: §7A past bug in the plugin")), 19);
		if(p.hasPermission("tntwars.potionworker"))items.put(addItem(new ItemStack(Material.POTION), "§b§lPotion Worker",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Cause a potion effect to players near the explosion",
						" ",
						"§fRequested By: §7Mr_Ender86")), 20);
		if(p.hasPermission("tntwars.shortfuse"))items.put(addItem(new ItemStack(Material.REDSTONE_BLOCK), "§b§lShort Fuse",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Your TNT has a shorter fuse",
						" ",
						"§fRequested By: §7Mr_Ender86")), 21);
		if(p.hasPermission("tntwars.tank"))items.put(addItem(new ItemStack(Material.DIAMOND_CHESTPLATE), "§b§lTank",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6take 1/2 damage from TNT")), 22);
		if(p.hasPermission("tntwars.bribed"))items.put(addSpecialItem(new ItemStack(Material.LEATHER_CHESTPLATE), "§b§lBribed",
				Color.fromRGB(170, 0, 0),
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Spawn with random armor",
						" ",
						"§7In Honor Of timelord_emma's manager promotion")), 23);
		if(p.hasPermission("tntwars.hailmary"))items.put(addItem(new ItemStack(Material.NETHER_STAR), "§b§lHail Mary",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Your TNT causes a larger explosion")), 24);
		if(p.hasPermission("tntwars.spaceman"))items.put(addItem(new ItemStack(Material.GLASS), "§b§lSpace Man",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Your TNT has no gravity")), 25);

		if(p.hasPermission("tntwars.storm"))items.put(addItem(new ItemStack(Material.BLAZE_POWDER), "§b§lStorm",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Your TNT rains from the sky",
						" ",
						"§fRequested By: §7TeraStorm")), 28);
		if(p.hasPermission("tntwars.vampirw"))items.put(addItem(new ItemStack(Material.REDSTONE), "§b§lVampire",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6If your TNT Strikes an enemy,",
						"§7 - §6gain 1 heart",
						" ",
						"§fRequested By: §7TeraStorm")), 29);
		items.put(addItem(new ItemStack(Material.COOKED_BEEF), "§b§lDefault",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Nothing Special")), 30);
		if(p.getName().equalsIgnoreCase("thevirginian") || p.getName().equalsIgnoreCase("abandoncaptian"))items.put(addItem(new ItemStack(Material.INK_SACK), "§b§lVirg Special",
				Lists.newArrayList( "§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Exclusive to: TheVirginian")), 53);

		items.put(addItem(new ItemStack(Material.ARROW), "§b§lRandom",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----", "§7 - §6Click for a random kit")), 49);
		items.put(addItem(new ItemStack(Material.BED), "§bReturn to Main Menu",
				Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
						"§7 - §6Return to Main Menu")), 45);
		return items;
	}

	public ItemStack addItem(ItemStack item, String name, List<String> lore) {
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
}