package me.abandoncaptian.TNTWars;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class CountDowns {
	Main pl;
	InvAndExp IAE;
	KitHandler kh;
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
	public boolean active = false;
	boolean canKit = true;
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
					for(String name : pl.inGame){
						Bukkit.getPlayer(name).closeInventory();
						IAE.InventorySwitch(Bukkit.getPlayer(name));
						IAE.ExpSwitch(name);
						if(Bukkit.getPlayer(name).getGameMode() != GameMode.SURVIVAL) Bukkit.getPlayer(name).setGameMode(GameMode.SURVIVAL);
						Bukkit.getPlayer(name).setInvulnerable(false);
						Bukkit.getPlayer(name).setHealth(20);
						Bukkit.getPlayer(name).setFoodLevel(20);
						Bukkit.getPlayer(name).getInventory().clear();
						Bukkit.getPlayer(name).getInventory().setItem(1, new ItemStack(Material.COOKED_BEEF, 5));
						if(!pl.selectedKit.containsKey(name)){
							int rand = (int) (Math.random()*(pl.kitsListAll.size()-1));
							pl.selectedKit.put(name, pl.kitsListAll.get(rand));
							Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou didn't choose a kit! §6We selected §b" + pl.kitsListAll.get(rand) + " §6for you");
						}else if(pl.selectedKit.get(name) == "Random"){
							int rand = (int) (Math.random()*(pl.kitsListAll.size()-1));
							pl.selectedKit.put(name,pl. kitsListAll.get(rand));
							Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §6We selected §b" + pl.kitsListAll.get(rand) + " §6for you");
						}
						if(pl.selectedKit.get(name) == "Doctor Who"){
							Bukkit.getPlayer(name).setHealthScale(40);
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
					}
				}
			}, 20*29);
			count2 = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					for(String name: pl.gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 2");
					}
				}
			}, 20*28);
			count3 = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					for(String name: pl.gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 3");
					}
				}
			}, 20*27);
			count4 = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					for(String name:pl. gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 4");
					}
				}
			}, 20*26);
			count5 = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					for(String name: pl.gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 5");
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
					}
				}
			}, 20*20);
			count30 = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 30 seconds");
					for(String name : pl.gameQueue){
						if(!pl.selectedKit.containsKey(name)){
							kh.kitsMenu(Bukkit.getPlayer(name));
						}
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §6Right click to throw your TNT");
					}
				}
			}, 0);
			starting2 = true;
		}
	}


	public void countDownPre(){
		if(!active && !starting1){
			starting1 = true;
			Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in " + pl.config.getInt("Game-Queue-Time") + " minutes.");
			countQueue = Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
				@Override
				public void run() {
					countDown30();
				}
			}, (pl.gameQueueTime-600));
		}
	}
	
}
