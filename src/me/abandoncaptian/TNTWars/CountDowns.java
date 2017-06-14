package me.abandoncaptian.TNTWars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class CountDowns {
	Main pl;
	InvAndExp IAE;
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
		IAE = new InvAndExp(plugin);
		LF = new LoadFunctions(plugin);
		GF = new GameFunc(plugin);
		for(String map : pl.arenas.values()){
			starting1.put(map, false);
			starting2.put(map, false);
			active.put(map, false);
			canKit.put(map, true);
		}
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
					GF.ChangeBoard(map);
					for (String name : pl.inGame.get(map)) {
						pl.allInGame.add(name);
						pl.allQueue.remove(name);
						if(!pl.points.containsKey(name)){
							pl.points.put(name, 0);
						}
						if(!pl.wins.containsKey(name)){
							pl.wins.put(name, 0);
						}
						if(!pl.loses.containsKey(name)){
							pl.loses.put(name, 0);
						}
						Bukkit.getPlayer(name).getInventory().setItem(1, new ItemStack(Material.COOKED_BEEF, 5));
						Bukkit.getPlayer(name).setHealth(20);
						Bukkit.getPlayer(name).setFoodLevel(20);
						Bukkit.getPlayer(name).setInvulnerable(false);
						if (!pl.selectedKit.containsKey(name)) {
							int rand = (int) (Math.random() * (LF.kitsListAll.size() - 1));
							pl.selectedKit.put(name, LF.kitsListAll.get(rand));
							Bukkit.getPlayer(name)
							.sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §cYou didn't choose a kit! §6We selected §b"
									+ LF.kitsListAll.get(rand) + " §6for you");
						} else if (pl.selectedKit.get(name) == "Random") {
							int rand = (int) (Math.random() * (LF.kitsListAll.size() - 1));
							pl.selectedKit.put(name, pl.LF.kitsListAll.get(rand));
							Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §6We selected §b"
									+ LF.kitsListAll.get(rand) + " §6for you");
						}
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l]",
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
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l]", "§bStarts in 1", 0, 20, 0);
					}
				}
			}, 20 * 29));
			count2.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@Override
				public void run() {
					for (String name : pl.gameQueue.get(map)) {
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bStarts in 2");
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l]", "§bStarts in 2", 0, 20, 0);
					}
				}
			}, 20 * 28));
			count3.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@Override
				public void run() {
					for (String name : pl.gameQueue.get(map)) {
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bStarts in 3");
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l]", "§bStarts in 3", 0, 20, 0);
					}
				}
			}, 20 * 27));
			count4.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@Override
				public void run() {
					for (String name : pl.gameQueue.get(map)) {
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bStarts in 4");
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l]", "§bStarts in 4", 0, 20, 0);
					}
				}
			}, 20 * 26));
			count5.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@Override
				public void run() {
					for (String name : pl.gameQueue.get(map)) {
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bStarts in 5");
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l]", "§bStarts in 5", 0, 20, 0);
					}
				}
			}, 20 * 25));
			count10.put(map, Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
				@Override
				public void run() {
					for (String name : pl.gameQueue.get(map)) {
						if (!pl.selectedKit.containsKey(name)) {
							pl.mh.openKitMenu(Bukkit.getPlayer(name));
						}
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §bStarts in 10");
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l]", "§bStarts in 10", 3, 15, 2);
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
						Bukkit.getPlayer(name).sendMessage("§6Developed By: §b§labandoncaptian");
						Bukkit.getPlayer(name).sendMessage("§6Idea By: §b§lMrs_Ender88");
						Bukkit.getPlayer(name).sendMessage(" ");
						Bukkit.getPlayer(name).sendMessage("§6§l--------------------------------------");
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l]", "§bStarts in 30", 0, 60, 0);
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
