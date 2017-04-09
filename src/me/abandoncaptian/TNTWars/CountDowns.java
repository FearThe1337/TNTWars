package me.abandoncaptian.TNTWars;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class CountDowns {
	Main pl;
	InvAndExp IAE;
	KitHandler kh;
	LoadFunctions LF;
	public BukkitTask count30;
	public BukkitTask count10;
	public BukkitTask count5;
	public BukkitTask count4;
	public BukkitTask count3;
	public BukkitTask count2;
	public BukkitTask count1;
	public BukkitTask countStart;
	public BukkitTask countQueue;
	public boolean starting1 = false;
	public boolean starting2 = false;
	public boolean active;
	public boolean canKit = true;
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
		active = false;
		IAE = new InvAndExp(plugin);
		kh = new KitHandler(plugin);
		LF = new LoadFunctions(plugin);
	}
	public void countDown30(){
		if(!active){
			starting2 = true;
			countStart = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					active = true;
					pl.inGame.addAll(pl.gameQueue);
					starting2 = false;
					starting1 = false;
					pl.ChangeBoard();
					for(String name : pl.inGame){
						Bukkit.getPlayer(name).getInventory().setItem(1, new ItemStack(Material.COOKED_BEEF, 5));
						Bukkit.getPlayer(name).setHealth(20);
						Bukkit.getPlayer(name).setFoodLevel(20);
						if(!pl.selectedKit.containsKey(name)){
							int rand = (int) (Math.random()*(LF.kitsListAll.size()-1));
							pl.selectedKit.put(name, LF.kitsListAll.get(rand));
							Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou didn't choose a kit! §6We selected §b" + LF.kitsListAll.get(rand) + " §6for you");
						}else if(pl.selectedKit.get(name) == "Random"){
							int rand = (int) (Math.random()*(LF.kitsListAll.size()-1));
							pl.selectedKit.put(name, pl.LF.kitsListAll.get(rand));
							Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §6We selected §b" + LF.kitsListAll.get(rand) + " §6for you");
						}
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l]", "§bKit : " + pl.selectedKit.get(name), 10, 60, 10);
						if(pl.selectedKit.get(name) == "Doctor Who"){
							Bukkit.getPlayer(name).setHealthScale(40);
						}
						if(pl.selectedKit.get(name) == "Miner"){
							Bukkit.getPlayer(name).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, ((20*60)*10), 1));
						}
						if(pl.selectedKit.get(name) == "Bribed"){
							int rand1 = (int) (Math.random()*4);
							int rand2 = (int) (Math.random()*4);
							int rand3 = (int) (Math.random()*4);
							int rand4 = (int) (Math.random()*4);
							Bukkit.getPlayer(name).getInventory().setHelmet(new ItemStack(armorHelmet.get(rand1)));
							Bukkit.getPlayer(name).getInventory().setChestplate(new ItemStack(armorChestplate.get(rand2)));
							Bukkit.getPlayer(name).getInventory().setLeggings(new ItemStack(armorLegs.get(rand3)));
							Bukkit.getPlayer(name).getInventory().setBoots(new ItemStack(armorBoots.get(rand4)));
						}
					}
					pl.gameQueue.clear(); 
					canKit = false;
					Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §6TNT Wars has started!");
				}
			}, 20*30);
			count1 = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					for(String name: pl.gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 1");
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l]", "§bStarts in 1", 0, 20, 0);
					}
				}
			}, 20*29);
			count2 = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					for(String name: pl.gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 2");
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l]", "§bStarts in 2", 0, 20, 0);
					}
				}
			}, 20*28);
			count3 = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					for(String name: pl.gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 3");
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l]", "§bStarts in 3", 0, 20, 0);
					}
				}
			}, 20*27);
			count4 = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					for(String name:pl. gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 4");
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l]", "§bStarts in 4", 0, 20, 0);
					}
				}
			}, 20*26);
			count5 = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					for(String name: pl.gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 5");
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l]", "§bStarts in 5", 0, 20, 0);
					}
				}
			}, 20*25);
			count10 = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					for(String name : pl.gameQueue){
						if(!pl.selectedKit.containsKey(name)){
							kh.kitsMenu(Bukkit.getPlayer(name));
						}
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 10");
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l]", "§bStarts in 10", 3, 15, 2);
					}
				}
			}, 20*20);
			count30 = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 30 seconds");
					kh.initInv();
					for(String name : pl.gameQueue){
						if(!pl.selectedKit.containsKey(name)){
							pl.kh.kitsMenu(Bukkit.getPlayer(name));
						}
						Bukkit.getPlayer(name).sendMessage("§6§l--------------------------------- §7§l[§c§lTNT Wars§7§l] §6§l----------------------------------");
						Bukkit.getPlayer(name).sendMessage(" ");
						Bukkit.getPlayer(name).sendMessage("                          §6Right click to throw your TNT");
						Bukkit.getPlayer(name).sendMessage("               §6Different kits do give an advantage in some way");
						Bukkit.getPlayer(name).sendMessage(" ");
						Bukkit.getPlayer(name).sendMessage("                          §6Developed By: §b§labandoncaptian");
						Bukkit.getPlayer(name).sendMessage("                               §6Idea By: §b§lMrs_Ender88");
						Bukkit.getPlayer(name).sendMessage(" ");
						Bukkit.getPlayer(name).sendMessage("§6§l--------------------------------------------------------------------------------------");
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l]", "§bStarts in 30", 0, 60, 0);
						pl.kh.initInv();
					}
				}
			}, 0);
			starting2 = true;
		}
	}


	public void countDownPre(){
		if(!active && !starting1){
			starting1 = true;
			pl.kh.initInv();
			Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in " + pl.config.getInt("Game-Queue-Time") + " minute(s).");
			countQueue = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					countDown30();
				}
			}, (pl.gameQueueTime-600));
		}
	}
	
}
