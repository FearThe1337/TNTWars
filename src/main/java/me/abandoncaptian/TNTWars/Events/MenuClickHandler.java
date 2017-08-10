package me.abandoncaptian.TNTWars.Events;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;

import com.google.common.collect.Lists;

import me.abandoncaptian.TNTWars.GameFunc;
import me.abandoncaptian.TNTWars.Main;
import me.abandoncaptian.TNTWars.MenuHandler;
public class MenuClickHandler implements Listener{
	Main pl;
	MenuHandler MH;
	GameFunc GF;
	ItemStack mapItem;
	Location loc;
	public MenuClickHandler(Main plugin){
		pl = plugin;
		MH = new MenuHandler(pl);
		GF = new GameFunc(plugin);
	}

	@EventHandler
	public void invClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Inventory clickedInv = e.getInventory();
		if (clickedInv != null) {
			if (clickedInv.getTitle().equals("§7§l[ §c§lTNT Wars Menu §7§l]")) {
				e.setCancelled(true);
				boolean com = false;
				if(!com){
					ItemStack clicked = e.getCurrentItem();
					if (clicked == null)return;
					if (clicked.hasItemMeta()) {
						String itemName = clicked.getItemMeta().getDisplayName();
						if(itemName.startsWith("§aJoin §7: §b")){
							String[] splits = itemName.split("§b");
							String map = splits[1];
							Boolean temp = false;
							if(pl.perTeam.get(map) == 1){
								p.closeInventory();
								for(int i = 1; i <= pl.teamAmount.get(map); i++){
									if(pl.teams.get(map).get(i).size()==0){
										GF.gameJoin(p.getName(), map, i);
										temp = true;
										break;
									}else continue;
								}
								if(!temp){
									p.sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §cTNT Wars Queue is full");
								}
							}else{
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §6Select a team");
								MH.openArenaTeams(p, clickedInv, map);
							}
							com = true;
						}
						if(itemName.startsWith("§aEdit §7: §b")){
							String[] splits = itemName.split("§b");
							String map = splits[1];
							MH.openArenaSettings(p, clickedInv, map);
							com = true;
						}
						if(itemName.startsWith("§aSpectate §7: §b")){
							String[] splits = itemName.split("§b");
							String map = splits[1];
							p.closeInventory();
							p.teleport(pl.spectate.get(map));
							com = true;
						}
						if(itemName.startsWith("§aSet Spectate §7: §b")){
							String[] splits = itemName.split("§b");
							String map = splits[1];
							Location loc = p.getLocation();
							pl.spectate.put(map, new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()));
							for(int index: pl.arenas.keySet()){
								String temp = pl.arenas.get(index);
								if(map.contains(temp)){
									p.sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §6Set Spectate Point for map: " + map + " §6to §7(§b" + loc.getX() + "§7, §b" + loc.getY() + "§7, §b" + loc.getZ() + "§7)");
									pl.config.set("Arenas."+ index + ".spectate.world", loc.getWorld().getName());
									pl.config.set("Arenas."+ index + ".spectate.x", loc.getX());
									pl.config.set("Arenas."+ index + ".spectate.y", loc.getY());
									pl.config.set("Arenas."+ index + ".spectate.z", loc.getZ());
									try {
										pl.config.save(pl.configFile);
									} catch (IOException e1) {
									}
								}
							}
							com = true;
						}
						if(itemName.startsWith("§aEditing §7: §b")){
							String[] splits = itemName.split("§b");
							String map = splits[1];
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Yes, you are editing the settings for " + map);
							com = true;
						}
						if(itemName.startsWith("§aJoin Team§7: §b")){
							String[] splits = itemName.split("§b");
							int team = Integer.valueOf(splits[1]);
							String map = clickedInv.getItem(4).getItemMeta().getDisplayName();
							map = map.split("§aJoining §7: §b")[1];
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §6Joining team §b" + team);
							GF.gameJoin(p.getName(), map, team);
							com = true;
						}
						if(itemName.startsWith("§aSet SpawnPoint§7:")){
							mapItem = clickedInv.getItem(4);
							String mapNameSpawn = mapItem.getItemMeta().getDisplayName();
							String[] splitsSpawn = mapNameSpawn.split("§b");
							String mapSpawn = splitsSpawn[1];
							int i = 0;
							for(String lore : clicked.getItemMeta().getLore()){
								if(lore.contains("team §b")){
									i = Integer.valueOf(lore.split("team §b")[1]);
								}else continue;
							}
							loc = new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
							pl.spawnpoint.get(mapSpawn).put(i, loc);
							for(int index: pl.arenas.keySet()){
								String map = pl.arenas.get(index);
								if(map.contains(mapSpawn)){
									p.sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §6Set SpawnPoint for team §b" + i + " §6to §7(§b" + loc.getX() + "§7, §b" + loc.getY() + "§7, §b" + loc.getZ() + "§7)");
									pl.config.set("Arenas."+index+"."+i+".world", loc.getWorld().getName());
									pl.config.set("Arenas."+index+"."+i+".x", loc.getX());
									pl.config.set("Arenas."+index+"."+i+".y", loc.getY());
									pl.config.set("Arenas."+index+"."+i+".z", loc.getZ());
									try {
										pl.config.save(pl.configFile);
									} catch (IOException e1) {
									}
								}
							}
							com = true;
						}
						if(itemName.startsWith("§a§lBUY§7§l:")){
							String[] splits = itemName.split("§b§l");
							String kit = splits[1];
							for(String lore : clicked.getItemMeta().getLore()){
								if(lore.contains("§bCost§7:")){
									String[] strings = lore.split("Cost");
									String price = strings[1];
									price = ChatColor.stripColor(price);
									price = price.replaceAll(":", "");
									price = price.replaceAll(" ", "");
									int cost = Integer.valueOf(price);
									if(pl.econ.hasBalance(p, cost)){
										pl.econ.withdrawBalance(p, cost);
										p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Purchased the kit§7: §b" + kit + " §6for §a" + cost + " points");
										p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Points Balance§7: §a" + pl.econ.getBalance(p));
										kit = kit.toLowerCase();
										kit = kit.replace(" ", "-");
										PermissionAttachment pPerms = pl.perms.get(p.getUniqueId());
										pPerms.setPermission("tntwars." + kit, true);
										pl.perms.put(p.getUniqueId(), pPerms);
										MH.openKitShop(p, clickedInv);
									}else{
										p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou don't have §a" + cost + " points §cto purchase kit§7: §b" + kit);
									}
								}else continue;
							}
							com = true;
						}
					}
				}
				if(!com){
					if (pl.allInGame.contains(e.getWhoClicked().getName())) {
						ItemStack clicked = e.getCurrentItem();
						if (clicked == null)return;
						if (clicked.hasItemMeta()) {
							String itemName = clicked.getItemMeta().getDisplayName();
							switch (itemName) {
							case "§b§lSniper":
							case "§b§lShort Fuse":
							case "§b§lHeavy Loader":
							case "§b§lMiner":
							case "§b§lSuicide Bomber":
							case "§b§lGlue Factory Worker":
							case "§b§lEnder":
							case "§b§lBoomerang":
							case "§b§lPotion Worker":
							case "§b§lDoctor Who":
							case "§b§lTank":
							case "§b§lRandom":
							case "§b§lBribed":
							case "§b§lHail Mary":
							case "§b§lSpace Man":
							case "§b§lStorm":
							case "§b§lVampire":
							case "§b§lVirg Special":
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cCan't change kits while in-game");
								p.closeInventory();
								return;
							}
						}
					}
					String id;
					char id1;
					char id2;
					char id3;
					char id4;
					ItemStack clicked = e.getCurrentItem();
					if (clicked == null)return;
					if (clicked.hasItemMeta()) {
						String itemName = clicked.getItemMeta().getDisplayName();
						switch (itemName) {
						case "§aSet Hub":
							pl.hub = new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), (int)p.getLocation().getYaw(), 0);
							pl.config.set("Hub.world", pl.hub.getWorld().getName());
							pl.config.set("Hub.x", pl.hub.getX());
							pl.config.set("Hub.y", pl.hub.getY());
							pl.config.set("Hub.z", pl.hub.getZ());
							pl.config.set("Hub.yaw", (int)pl.hub.getYaw());
							try {
								pl.config.save(pl.configFile);
							} catch (IOException e2) {
							}
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Set Hub to §7(§b" + pl.hub.getX() + "§7, §b" + pl.hub.getY() + "§7, §b" + pl.hub.getZ() + "§7)");
							break;
						case "§aArena List":
							MH.arenaSelectorMenu(p, clickedInv);
							break;
						case "§aSpectate":
							MH.arenaSpectatorSelectorMenu(p, clickedInv);
							break;
						case "§aEdit Spectating Points":
							MH.arenaSpectatorEditorSelectorMenu(p, clickedInv);
							break;
						case "§bBalance":
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Points Balance§7: §a" + pl.econ.getBalance(p));
							break;
						case "§aLeave TNT Wars":
							p.closeInventory();
							GF.gameLeave(p.getName());
							break;
						case "§aTNT Wars Kits":
							if(pl.allQueue.contains(p.getName()))MH.openKitMenu(p, clickedInv);
							else p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou need to be queued to select a kit");
							break;
						case "§aKit Shop":
							MH.openKitShop(p, clickedInv);
							break;
						case "§bYou Own ALL Kits!":
							break;
						case "§aForce Start TNT Wars":
							p.closeInventory();
							GF.gameForceStart(pl.playerInArena.get(p.getName()), p);
							break;
						case "§aArenas Settings":
							MH.openArenaMenu(p, clickedInv);
							break;
						case "§aSet Lobby Point":
							mapItem = clickedInv.getItem(4);
							String mapNameSpawn = mapItem.getItemMeta().getDisplayName();
							String[] splitsSpawn = mapNameSpawn.split("§b");
							String mapSpawn = splitsSpawn[1];
							loc = new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
							pl.lobby.put(mapSpawn, loc);
							for(int index: pl.arenas.keySet()){
								String map = pl.arenas.get(index);
								if(map.contains(mapSpawn)){
									p.sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §6Set Lobby Point to §7(§b" + loc.getX() + "§7, §b" + loc.getY() + "§7, §b" + loc.getZ() + "§7)");
									pl.config.set("Arenas."+index+".lobby.world", loc.getWorld().getName());
									pl.config.set("Arenas."+index+".lobby.x", loc.getX());
									pl.config.set("Arenas."+index+".lobby.y", loc.getY());
									pl.config.set("Arenas."+index+".lobby.z", loc.getZ());
									try {
										pl.config.save(pl.configFile);
									} catch (IOException e1) {
									}
								}
							}
							break;
						case "§aArena Selector":
							MH.openArenaMenu(p, clickedInv);
							break;
						case "§aClose TNT Wars Menu":
							p.closeInventory();
							break;
						case "§b§lDonor Perks":
							MH.openDonorMenu(p, clickedInv);
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
						case "§b§lDefault":
							p.sendMessage("§6You selected: " + itemName);
							pl.selectedKit.put(p.getName(), "Default");
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
						case "§bFireworks §7Settings":
							MH.openFWEditorMenu(p, clickedInv);
							break;
						case "§bReturn to Main Menu":
							MH.openMainMenu(p, clickedInv);
							break;
						case "§bChange Name":
							p.closeInventory();
							MH.openCNameChanger(p);
							break;
						case "§bCustom Name §7Settings":
							MH.openCNameMenu(p, clickedInv);
							break;
						case "§bRed Name":
							pl.cNameColor.put(p.getName(), ChatColor.RED);
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Set custom name color: " + pl.cNameColor.get(p.getName()) + "RED");
							break;
						case "§bBlue Name":
							pl.cNameColor.put(p.getName(), ChatColor.BLUE);
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Set custom name color: " + pl.cNameColor.get(p.getName()) + "BLUE");
							break;
						case "§bGreen Name":
							pl.cNameColor.put(p.getName(), ChatColor.GREEN);
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Set custom name color: " + pl.cNameColor.get(p.getName()) + "GREEN");
							break;
						case "§bWhite Name":
							pl.cNameColor.put(p.getName(), ChatColor.WHITE);
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Set custom name color: " + pl.cNameColor.get(p.getName()) + "WHITE");
							break;
						case "§bPurple Name":
							pl.cNameColor.put(p.getName(), ChatColor.DARK_PURPLE);
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Set custom name color: " + pl.cNameColor.get(p.getName()) + "PURPLE");
							break;
						case "§bCustom Name":
							boolean toggle = false;
							if(!pl.Perks.containsKey(p.getName())){
								pl.Perks.put(p.getName(), new HashMap<String, Boolean>());
								pl.Perks.get(p.getName()).put("Fireworks", false);
								pl.Perks.get(p.getName()).put("Outline", false);
								pl.Perks.get(p.getName()).put("CName", true);
								clickedInv.setItem(14, MH.addItem(new ItemStack(Material.SLIME_BALL), "§bCustom Name",
										Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
												"§6When your TNT explodes it",
												"§6launches a firework into the air",
												" ",
												"§bClick to toggle the custom name")));	
								pl.Perks.get(p.getName()).put("CName", true);
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bCustom Name: §7[§aON§7]");
							}else{
								if(pl.Perks.get(p.getName()).containsKey("CName")){
									if(pl.Perks.get(p.getName()).get("CName")){
										toggle = true;
										clickedInv.setItem(14, MH.addItem(new ItemStack(Material.MAGMA_CREAM), "§bCustom Name",
												Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
														"§6When your TNT explodes it",
														"§6launches a firework into the air",
														" ",
														"§bClick to toggle the custom name")));	
										pl.Perks.get(p.getName()).put("CName", false);
										p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bCustom Name: §7[§cOFF§7]");
									}
								}
								if(!toggle){
									clickedInv.setItem(14, MH.addItem(new ItemStack(Material.SLIME_BALL), "§bCustom Name",
											Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
													"§6When your TNT explodes it",
													"§6launches a firework into the air",
													" ",
													"§bClick to toggle the custom name")));	
									pl.Perks.get(p.getName()).put("CName", true);
									p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bCustom Name: §7[§aON§7]");
								}
							}
							break;
						case "§bFlicker":
							pl.ED.encodeFWSettings(p.getName());
							id = pl.fwSettings.get(p.getName());
							id2 = id.charAt(1);
							id3 = id.charAt(2);
							id4 = id.charAt(3);
							Boolean flicker = pl.ED.decodeFWSettingsFlicker(p.getName());
							if(flicker == true){
								pl.fwSettings.put(p.getName(), "0" + Character.toString(id2) + Character.toString(id3) + Character.toString(id4));
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework type: §cOFF");
								clickedInv.setItem(1, MH.addItem(new ItemStack(Material.MAGMA_CREAM), "§bFlicker",
										Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
												"§6Toggle the filcker")));
							}else{
								pl.fwSettings.put(p.getName(), "1" + Character.toString(id2) + Character.toString(id3) + Character.toString(id4));
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework type: §aON");
								clickedInv.setItem(1, MH.addItem(new ItemStack(Material.SLIME_BALL), "§bFlicker",
										Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
												"§6Toggle the filcker")));
							}
							break;
						case "§bTrail":
							pl.ED.encodeFWSettings(p.getName());
							id = pl.fwSettings.get(p.getName());
							id1 = id.charAt(0);
							id3 = id.charAt(2);
							id4 = id.charAt(3);
							Boolean trail = pl.ED.decodeFWSettingsTrail(p.getName());
							if(trail == true){
								pl.fwSettings.put(p.getName(), Character.toString(id1) + "0" + Character.toString(id3) + Character.toString(id4));
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework type: §cOFF");
								clickedInv.setItem(3, MH.addItem(new ItemStack(Material.MAGMA_CREAM), "§bTrail",
										Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
												"§6Toggle the trail")));
							}else{
								pl.fwSettings.put(p.getName(), Character.toString(id1) + "1" + Character.toString(id3) + Character.toString(id4));
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework type: §aON");
								clickedInv.setItem(3, MH.addItem(new ItemStack(Material.SLIME_BALL), "§bTrail",
										Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
												"§6Toggle the trail")));
							}
							break;
						case "§bType":
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Choose a type from below");
							break;
						case "§bBall":
							pl.ED.encodeFWSettings(p.getName());
							id = pl.fwSettings.get(p.getName());
							id1 = id.charAt(0);
							id2 = id.charAt(1);
							id4 = id.charAt(3);
							pl.fwSettings.put(p.getName(), Character.toString(id1) + Character.toString(id2) + "0" + Character.toString(id4));
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework type: §bBALL");
							break;
						case "§bLarge Ball":
							pl.ED.encodeFWSettings(p.getName());
							id = pl.fwSettings.get(p.getName());
							id1 = id.charAt(0);
							id2 = id.charAt(1);
							id4 = id.charAt(3);
							pl.fwSettings.put(p.getName(), Character.toString(id1) + Character.toString(id2) + "1" + Character.toString(id4));
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework type: §bLARGE BALL");
							break;
						case "§bBurst":
							pl.ED.encodeFWSettings(p.getName());
							id = pl.fwSettings.get(p.getName());
							id1 = id.charAt(0);
							id2 = id.charAt(1);
							id4 = id.charAt(3);
							pl.fwSettings.put(p.getName(), Character.toString(id1) + Character.toString(id2) + "2" + Character.toString(id4));
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework type: §bBURST");
							break;
						case "§bCreeper":
							pl.ED.encodeFWSettings(p.getName());
							id = pl.fwSettings.get(p.getName());
							id1 = id.charAt(0);
							id2 = id.charAt(1);
							id4 = id.charAt(3);
							pl.fwSettings.put(p.getName(), Character.toString(id1) + Character.toString(id2) + "3" + Character.toString(id4));
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework type: §bCREEPER");
							break;
						case "§bStar":
							pl.ED.encodeFWSettings(p.getName());
							id = pl.fwSettings.get(p.getName());
							id1 = id.charAt(0);
							id2 = id.charAt(1);
							id4 = id.charAt(3);
							pl.fwSettings.put(p.getName(), Character.toString(id1) + Character.toString(id2) + "4" + Character.toString(id4));
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework type: §bSTAR");
							break;
						case "§bColor":
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Choose a color from below");
							break;
						case "§bRed":
							pl.ED.encodeFWSettings(p.getName());
							id = pl.fwSettings.get(p.getName());
							id1 = id.charAt(0);
							id2 = id.charAt(1);
							id3 = id.charAt(2);
							pl.fwSettings.put(p.getName(), Character.toString(id1) + Character.toString(id2) + Character.toString(id3) + "0");
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework color: §cRED");
							break;
						case "§bBlue":
							pl.ED.encodeFWSettings(p.getName());
							id = pl.fwSettings.get(p.getName());
							id1 = id.charAt(0);
							id2 = id.charAt(1);
							id3 = id.charAt(2);
							pl.fwSettings.put(p.getName(), Character.toString(id1) + Character.toString(id2) + Character.toString(id3) + "1");
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework color: §1BLUE");
							break;
						case "§bGreen":
							pl.ED.encodeFWSettings(p.getName());
							id = pl.fwSettings.get(p.getName());
							id1 = id.charAt(0);
							id2 = id.charAt(1);
							id3 = id.charAt(2);
							pl.fwSettings.put(p.getName(), Character.toString(id1) + Character.toString(id2) + Character.toString(id3) + "2");
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework color: §2GREEN");
							break;
						case "§bWhite":
							pl.ED.encodeFWSettings(p.getName());
							id = pl.fwSettings.get(p.getName());
							id1 = id.charAt(0);
							id2 = id.charAt(1);
							id3 = id.charAt(2);
							pl.fwSettings.put(p.getName(), Character.toString(id1) + Character.toString(id2) + Character.toString(id3) + "3");
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework color: §fWHITE");
							break;
						case "§bSettings":
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework settings: " + pl.fwSettings.get(p.getName()));
							break;
						case "§bPurple":
							pl.ED.encodeFWSettings(p.getName());
							id = pl.fwSettings.get(p.getName());
							id1 = id.charAt(0);
							id2 = id.charAt(1);
							id3 = id.charAt(2);
							pl.fwSettings.put(p.getName(), Character.toString(id1) + Character.toString(id2) + Character.toString(id3) + "4");
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Firework color: §5PURPLE");
							break;
						case "§bTurn Fireworks §7[§aON§7]":
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bFireworks: §7[§aON§7]");
							clickedInv.setItem(10, MH.addItem(new ItemStack(Material.SLIME_BALL), "§bTurn Fireworks §7[§cOFF§7]",
									Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
											"§6When your TNT explodes it",
											"§6launches a firework into the air", 
											" ",
											"§bClick to turn §7[§cOFF§7]")));
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
						case "§bTurn Fireworks §7[§cOFF§7]":
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bFireworks: §7[§cOFF§7]");
							clickedInv.setItem(10, MH.addItem(new ItemStack(Material.MAGMA_CREAM), "§bTurn Fireworks §7[§aON§7]",
									Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
											"§6When your TNT explodes it",
											"§6launches a firework into the air", 
											" ",
											"§bClick to turn §7[§aON§7]")));
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
						case "§bTurn TNT Outline §7[§aON§7]":
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bTNT Outline: §7[§aON§7]");
							clickedInv.setItem(12, MH.addItem(new ItemStack(Material.SLIME_BALL), "§bTurn TNT Outline §7[§cOFF§7]",
									Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
											"§6Your TNT will be outlined in white", 
											" ",
											"§bClick to turn §7[§cOFF§7]")));
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
						case "§bTurn TNT Outline §7[§cOFF§7]":
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §bTNT Outline: §7[§cOFF§7]");
							clickedInv.setItem(12, MH.addItem(new ItemStack(Material.MAGMA_CREAM), "§bTurn TNT Outline §7[§aON§7]",
									Lists.newArrayList("§6§l---- §c[ TNT Wars ] §6§l----",
											"§6Your TNT will be outlined in white", 
											" ",
											"§bClick to turn §7[§aON§7]")));
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
						default:
							break;
						}
						return;
					}
				}
			}
		}
	}
}