package me.abandoncaptian.TNTWars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class CountDowns {
	Main pl;
	LoadFunctions LF;
	GameFunc GF;
	public Map<String, BukkitTask> count30 = new HashMap<String, BukkitTask>();
	public Map<String, BukkitTask> count10 = new HashMap<String, BukkitTask>();
	public Map<String, BukkitTask> count5 = new HashMap<String, BukkitTask>();
	public Map<String, BukkitTask> count4 = new HashMap<String, BukkitTask>();
	public Map<String, BukkitTask> count3 = new HashMap<String, BukkitTask>();
	public Map<String, BukkitTask> count2 = new HashMap<String, BukkitTask>();
	public Map<String, BukkitTask> count1 = new HashMap<String, BukkitTask>();
	public Map<String, BukkitTask> countStart = new HashMap<String, BukkitTask>();
	public Map<String, BukkitTask> countQueue = new HashMap<String, BukkitTask>();
	public Map<String, Boolean> starting1 = new HashMap<String, Boolean>();
	public Map<String, Boolean> starting2 = new HashMap<String, Boolean>();
	public Map<String, Boolean> active = new HashMap<String, Boolean>();
	public Map<String, Boolean> canKit = new HashMap<String, Boolean>();
	List<Material> armorHelmet = new ArrayList<Material>();
	List<Material> armorChestplate = new ArrayList<Material>();
	List<Material> armorLegs = new ArrayList<Material>();
	List<Material> armorBoots = new ArrayList<Material>();
	Map<String, Boolean> tenSec = new HashMap<String, Boolean>();

	public CountDowns(Main plugin) {
		pl = plugin;
		armorHelmet.add(Material.LEATHER_HELMET);
		armorHelmet.add(Material.IRON_HELMET);
		armorHelmet.add(Material.GOLD_HELMET);
		armorHelmet.add(Material.CHAINMAIL_HELMET);
		armorHelmet.add(Material.DIAMOND_HELMET);
		armorChestplate.add(Material.LEATHER_CHESTPLATE);
		armorChestplate.add(Material.IRON_CHESTPLATE);
		armorChestplate.add(Material.GOLD_CHESTPLATE);
		armorChestplate.add(Material.CHAINMAIL_CHESTPLATE);
		armorChestplate.add(Material.DIAMOND_CHESTPLATE);
		armorLegs.add(Material.LEATHER_LEGGINGS);
		armorLegs.add(Material.IRON_LEGGINGS);
		armorLegs.add(Material.GOLD_LEGGINGS);
		armorLegs.add(Material.CHAINMAIL_LEGGINGS);
		armorLegs.add(Material.DIAMOND_LEGGINGS);
		armorBoots.add(Material.LEATHER_BOOTS);
		armorBoots.add(Material.IRON_BOOTS);
		armorBoots.add(Material.GOLD_BOOTS);
		armorBoots.add(Material.CHAINMAIL_BOOTS);
		armorBoots.add(Material.DIAMOND_BOOTS);
		LF = new LoadFunctions(plugin);
		GF = new GameFunc(plugin);
	}

	public void countDown30(String map) {
		if (!active.get(map)){
			starting2.put(map, true);
			countStart.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@Override
				public void run() {
					active.put(map, true);
					pl.inGame.get(map).addAll(pl.gameQueue.get(map));
					starting2.put(map, false);
					tenSec.put(map, false);
					GF.ChangeBoard(map);
					for(int i : pl.teams.get(map).keySet()){
						List<String> players = pl.teams.get(map).get(i);
						if(players.isEmpty())continue;
						Location temp = pl.spawnpoint.get(map).get(i);
						Location loc = new Location(temp.getWorld(), temp.getX(), temp.getY(), temp.getZ());
						loc.setY(loc.getY()-1);
						Block block = loc.getBlock();
						block.setType(Material.AIR);
					}
					for (String name : pl.inGame.get(map)) {
						pl.allInGame.add(name);
						pl.allQueue.remove(name);
						GF.setPlayerInv(Bukkit.getPlayer(name));
						Bukkit.getPlayer(name).setHealth(20);
						Bukkit.getPlayer(name).setFoodLevel(20);
						Bukkit.getPlayer(name).setInvulnerable(false);
						UUID uuid = Bukkit.getPlayer(name).getUniqueId();
						if (!pl.selectedKit.containsKey(name)) {
							PermissionAttachment pPerms = pl.perms.get(uuid);
							ArrayList<String> avKits = new ArrayList<String>();
							for(String kit: pl.LF.kitsListAll){
								String tempKit = kit;
								kit = kit.toLowerCase();
								kit = kit.replace(" ", "-");
								if(pPerms.getPermissions().containsKey("tntwars." + kit)){
									avKits.add(tempKit);
								}else continue;
							}
							if(avKits.isEmpty()){
								pl.selectedKit.put(name, "Default");
								Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §cYou didn't choose a kit! §6We selected §bDefault §6for you");
							}else{
								int rand = (int) (Math.random() * (avKits.size() - 1));
								pl.selectedKit.put(name, avKits.get(rand));
								Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §cYou didn't choose a kit! §6We selected §b" + avKits.get(rand) + " §6for you");
							}
						} else if (pl.selectedKit.get(name) == "Random") {
							PermissionAttachment pPerms = pl.perms.get(uuid);
							ArrayList<String> avKits = new ArrayList<String>();
							for(String kit: pl.LF.kitsListAll){
								String tempKit = kit;
								kit = kit.toLowerCase();
								kit = kit.replace(" ", "-");
								if(pPerms.getPermissions().containsKey("tntwars." + kit)){
									avKits.add(tempKit);
								}else continue;
							}
							if(avKits.isEmpty()){
								pl.selectedKit.put(name, "Default");
								Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §6We selected §bDefault §6for you");
							}else{
								int rand = (int) (Math.random() * (avKits.size() - 1));
								pl.selectedKit.put(name, avKits.get(rand));
								Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §6We selected §b" + avKits.get(rand) + " §6for you");
							}
						}


						Bukkit.getPlayer(name).sendTitle("",
								"§bKit : " + pl.selectedKit.get(name), 10, 60, 10);
						if (pl.selectedKit.get(name) == "Doctor Who") {
							Bukkit.getPlayer(name).setHealthScale(40);
						}
						if (pl.selectedKit.get(name) == "Miner") {
							Bukkit.getPlayer(name)
							.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, ((20 * 60) * 10), 1));
						}
						if (pl.selectedKit.get(name) == "Bribed") {
							int rand1 = (int) (Math.random() * 4);
							int rand2 = (int) (Math.random() * 4);
							int rand3 = (int) (Math.random() * 4);
							int rand4 = (int) (Math.random() * 4);
							Bukkit.getPlayer(name).getInventory().setHelmet(new ItemStack(armorHelmet.get(rand1)));
							Bukkit.getPlayer(name).getInventory()
							.setChestplate(new ItemStack(armorChestplate.get(rand2)));
							Bukkit.getPlayer(name).getInventory().setLeggings(new ItemStack(armorLegs.get(rand3)));
							Bukkit.getPlayer(name).getInventory().setBoots(new ItemStack(armorBoots.get(rand4)));
						}
					}
					pl.gameQueue.get(map).clear();
					canKit.put(map, false);
					Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §6TNT Wars has started!");
				}
			}, 20 * 30));
			count1.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@Override
				public void run() {
					for (String name : pl.gameQueue.get(map)) {
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bStarts in 1");
					}
				}
			}, 20 * 29));
			count2.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@Override
				public void run() {
					for (String name : pl.gameQueue.get(map)) {
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bStarts in 2");
					}
				}
			}, 20 * 28));
			count3.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@Override
				public void run() {
					for (String name : pl.gameQueue.get(map)) {
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bStarts in 3");
					}
				}
			}, 20 * 27));
			count4.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@Override
				public void run() {
					for (String name : pl.gameQueue.get(map)) {
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bStarts in 4");
					}
				}
			}, 20 * 26));
			count5.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@Override
				public void run() {
					for (String name : pl.gameQueue.get(map)) {
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bStarts in 5");
					}
				}
			}, 20 * 25));
			count10.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					for(int i : pl.teams.get(map).keySet()){
						List<String> players = pl.teams.get(map).get(i);
						if(players.isEmpty())continue;
						Location temp = pl.spawnpoint.get(map).get(i);
						Location loc = new Location(temp.getWorld(), temp.getX(), temp.getY(), temp.getZ());
						loc.setY(loc.getY()-1);
						Block block = loc.getBlock();
						block.setType(Material.STAINED_GLASS);
						block.setData((byte)14);
						for(String player : players){
							Bukkit.getPlayer(player).teleport(pl.spawnpoint.get(map).get(i));
							Bukkit.getPlayer(player).getLocation().getBlock().setType(Material.WATER);
							Bukkit.getPlayer(player).setDisplayName("§f ");
							Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
								@Override
								public void run() {
									Bukkit.getPlayer(player).getLocation().getBlock().setType(Material.AIR);
								}
							}, 20);
						}
					}
					for (String name : pl.gameQueue.get(map)) {
						if (!pl.selectedKit.containsKey(name)) {
							pl.mh.openKitMenu(Bukkit.getPlayer(name));
						}
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bStarts in 10");
						tenSec.put(map, true);
					}
				}
			}, 20 * 20));
			count30.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@Override
				public void run() {
					Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bStarts in 30 seconds");
					for (String name : pl.gameQueue.get(map)) {
						if (!pl.selectedKit.containsKey(name)) {
							pl.mh.openKitMenu(Bukkit.getPlayer(name));
						}
						Bukkit.getPlayer(name).sendMessage("§6§l---------- §7§l[§c§lTNT Wars§7§l] §6§l----------");
						Bukkit.getPlayer(name).sendMessage(" ");
						Bukkit.getPlayer(name).sendMessage("§6Right click to throw your TNT");
						Bukkit.getPlayer(name).sendMessage("§6Different kits do give an advantage in some way");
						Bukkit.getPlayer(name).sendMessage(" ");
						Bukkit.getPlayer(name).sendMessage("§6Created By: §b§labandoncaptian §6and §b§lMrs_Ender88");
						Bukkit.getPlayer(name).sendMessage(" ");
						Bukkit.getPlayer(name).sendMessage("§6§l--------------------------------------");
					}
				}
			}, 0));
			starting2.put(map, true);
			starting1.put(map, false);
		}
	}

	public void countDownPre(String map) {
		if (!active.get(map) && !starting1.get(map)) {
			starting1.put(map, true);
			Bukkit.broadcastMessage(
					"§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bStarts in " + pl.config.getInt("Game-Queue-Time") + " minute(s).");
			countQueue.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@Override
				public void run() {
					countDown30(map);
				}
			}, (pl.gameQueueTime - 600)));
		}
	}

}
