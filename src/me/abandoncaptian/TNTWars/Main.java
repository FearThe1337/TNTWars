package me.abandoncaptian.TNTWars;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
//import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftTNTPrimed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.abandoncaptian.TNTWars.KitHandler;

public class Main extends JavaPlugin implements Listener{
	Logger Log = Bukkit.getLogger();
	File configFile;
	FileConfiguration config;
	KitHandler kh;
	List<String> modeON = new ArrayList<String>();
	List<String> gameQueue = new ArrayList<String>();
	List<String> inGame = new ArrayList<String>();
	List<String> dead = new ArrayList<String>();
	List<String> kitsList = new ArrayList<String>();
	ItemStack tnt = new ItemStack(Material.TNT);
	ItemMeta meta = tnt.getItemMeta();
	List<String> lore = new ArrayList<String>();
	HashMap<String, String> selectedKit = new HashMap<String, String>();
	HashMap<Entity, String> tntActive = new HashMap<Entity, String>();
	HashMap<UUID, PlayerInventory> invSaves = new HashMap<UUID, PlayerInventory>();

	TNTPrimed primeTnt;
	BukkitTask deathCount;
	boolean active = false;
	boolean canKit = true;

	@Override
	public void onEnable()
	{
		Log.info("----------- [ TNT Wars ] -----------");
		Log.info(" ");
		Log.info("               Enabled!             ");
		Log.info(" ");
		Log.info("------------------------------------");
		this.configFile = new File("plugins/TNTWars/config.yml");
		this.config = YamlConfiguration.loadConfiguration(configFile);
		kh = new KitHandler(this);
		Bukkit.getPluginManager().registerEvents(kh, this);
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable1(this), 0, 20*3);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable2(this), 0, 2);
		kitsList.add("Sniper");
		kitsList.add("Short Fuse");
		kitsList.add("Heavy Loader");
		kitsList.add("Miner");
		kitsList.add("Suicide Bomber");
		kitsList.add("Glue Factory Worker");
		kitsList.add("Ender");
		kitsList.add("Boomerang");
		meta.setDisplayName("§6§lThrowable §c§lTNT");
		lore.add("§bRight click to throw");
		lore.add("§bMade By: abandoncaptian");
		meta.setLore(lore);
		tnt.setItemMeta(meta);
		if(!(configFile.exists())){
			config.options().copyDefaults(true);
			this.saveDefaultConfig();
			this.saveConfig();
			Log.info("File Didn't Exist ----");
		}
	}

	@Override
	public void onDisable()
	{
		Log.info("----------- [ TNT Wars ] -----------");
		Log.info(" ");
		Log.info("              Disabled!             ");
		Log.info(" ");
		Log.info("------------------------------------");
		for (Player p : Bukkit.getOnlinePlayers()){
			if(modeON.contains(p.getName())){
				modeON.remove(p.getName());
				p.sendMessage("§6TNT Mode: turned off due to plugin reload.");
			}
		}
		inGame.clear();
		gameQueue.clear();
		modeON.clear();
		selectedKit.clear();
		active = false;
		canKit = true;
	}

	public boolean onCommand(CommandSender theSender, Command cmd, String commandLabel, String[] args)
	{
		if (commandLabel.equalsIgnoreCase("tnt") || commandLabel.equalsIgnoreCase("tw"))
		{
			if(theSender instanceof Player)
			{
				Player p = (Player)theSender;
				if(args.length == 0){
					p.sendMessage("§6§l---- §c[ TNT Wars ] §6§l----");
					p.sendMessage("§a/tnt [join/leave/kits]");
					p.sendMessage("§b - Example: /tnt join");
					if(p.hasPermission("tntwars.staff"))p.sendMessage("§a/tnt [start/end]");
					p.sendMessage("§6Author: §7abandoncaptian");
				}else if(args[0].equalsIgnoreCase("join")){
					if(gameQueue.contains(p.getName())){
						p.sendMessage("§cAlready in the TNT Game Queue!");
					}else{
						gameQueue.add(p.getName());
						int queued = gameQueue.size();
						Bukkit.broadcastMessage("§b" + p.getName() + " §6has joined TNT Wars Queue §7- §bQueued: " + queued);
					}
				}else if(args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("quit")){
					if(!active){
						if(gameQueue.contains(p.getName())){
							gameQueue.remove(p.getName());
							int queued = gameQueue.size();
							Bukkit.broadcastMessage("§b" + p.getName() + " §6has left TNT Wars Queue §7- §b" + queued + " remain!");
							if(selectedKit.containsKey(p.getName())){
								selectedKit.remove(p.getName());
							}
						}else{
							p.sendMessage("§cYou are not in the TNT Wars Queue");
						}
					}else{
						if(inGame.contains(p.getName())){
							p.sendMessage("§6Left TNT Wars!");
							Bukkit.broadcastMessage(p.getName() + " has left TNT Wars");
							inGame.remove(p.getName());
							if(selectedKit.containsKey(p.getName())){
								selectedKit.remove(p.getName());
							}
						}else{
							p.sendMessage("§cYou are not in the TNT Wars");
						}
					}
				}else if(args[0].equalsIgnoreCase("start") && p.hasPermission("tntwars.gamestart")){
					if(!active){
						if(gameQueue.size() >= 2){
							active = true;
							
							Bukkit.getScheduler().runTaskLater(this, new Runnable(){
								@Override
								public void run() {
									inGame.addAll(gameQueue);
									modeON.addAll(inGame);
									for(String name : inGame){
										Bukkit.getPlayer(name).setHealth(20);
										Bukkit.getPlayer(name).setFoodLevel(20);
										invSaves.put(Bukkit.getPlayer(name).getUniqueId(), Bukkit.getPlayer(name).getInventory());
										p.getInventory().clear();
										p.getInventory().setItem(1, new ItemStack(Material.COOKED_BEEF, 5));
										if(!selectedKit.containsKey(name)){
											int rand = (int) (Math.random()*8);
											selectedKit.put(name, kitsList.get(rand));
											Bukkit.getPlayer(name).sendMessage("§6We selected §b" + kitsList.get(rand) + " §6for you");
										}
									}
									gameQueue.clear(); 
									canKit = false;
									Bukkit.broadcastMessage("§6TNT Wars has started!");
								}
							}, 20*30);
							Bukkit.getScheduler().runTaskLater(this, new Runnable(){
								@Override
								public void run() {
									Bukkit.broadcastMessage("§6Remember to do  §7[§b/TNT Kits§7] §6to select a kit!");
									Bukkit.broadcastMessage("§c[Warning] §7- §cDo not bring items into game §7- §cYou will lose items!");
									for(String name : gameQueue){
										kh.kitsMenu(Bukkit.getPlayer(name));
									}
								}
							}, 0);
							for(int i = 30; i > 0; i--) {
								Bukkit.broadcastMessage("§bStarts in " + i);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}else{
							p.sendMessage("§cNot enough players to start the game.");
						}
					}else{
						p.sendMessage("§6TNT Wars is already started!");
					}
				}else if((args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("stop")) && p.hasPermission("tntwars.gameend")){
					if(active){
						inGame.clear();
						modeON.clear();
						active = false;
						Bukkit.broadcastMessage("§6TNT Wars Has ended!");
					}else{
						p.sendMessage("§6TNT Wars is not running!");
					}
				}else if(args[0].equalsIgnoreCase("kit") || args[0].equalsIgnoreCase("kits")){
					if(gameQueue.contains(p.getName())){
						if(!canKit){
							p.sendMessage("§6You cannot change kits mid-game");
						}else{
							kh.kitsMenu(p);
						}
					}else{
						p.sendMessage("§cNeed to be in the queue to select a kit");
					}
				}
			}
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void tntClick(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			Player p = e.getPlayer();
			if(selectedKit.get(p.getName()) == "Miner"){
				ItemStack item = p.getItemInHand();
				if(item.getType() == Material.TNT){
					if(item.getItemMeta().getDisplayName().equals("§6§lThrowable §c§lTNT")){
						if(modeON.contains(p.getName())){
							this.primeTnt = (TNTPrimed) p.getWorld().spawn(p.getLocation(), TNTPrimed.class);
							tntActive.put(this.primeTnt, p.getName());
							primeTnt.setFuseTicks(0);
							e.setCancelled(true);
						}
					}
				}
			}
			return;
		}
		if((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)){
			Player p = e.getPlayer();
			ItemStack item = p.getItemInHand();
			if(item.getType() == Material.TNT){
				if(item.getItemMeta().getDisplayName().equals("§6§lThrowable §c§lTNT")){
					if(modeON.contains(p.getName())){
						double power = 0;
						int fuse;
						if(selectedKit.containsKey(p.getName())){
							if(selectedKit.get(p.getName()) == "Sniper") power = 4;
							else if((selectedKit.get(p.getName()) == "Suicide Bomber"))power = 0;
							else if((selectedKit.get(p.getName()) == "Boomerang"))power = 3;
							else power = 1.5;
						}else power =  1.5;
						if(selectedKit.containsKey(p.getName())){
							if(selectedKit.get(p.getName()) == "Short Fuse")fuse = 10;
							else if((selectedKit.get(p.getName()) == "Ender"))fuse = 60;
							else if((selectedKit.get(p.getName()) == "Suicide Bomber"))fuse = 0;
							else if((selectedKit.get(p.getName()) == "Boomerang"))fuse = 30;
							else fuse = 20;
						}else fuse = 20;
						Location eye = p.getEyeLocation();
						Vector vec = eye.getDirection().normalize().multiply(power);
						eye.setY(eye.getY() + 0.4);
						this.primeTnt = (TNTPrimed) p.getWorld().spawn(eye, TNTPrimed.class);
						this.primeTnt.setFuseTicks(fuse);
						this.primeTnt.setCustomName(p.getName());
						this.primeTnt.setCustomNameVisible(true);
						tntActive.put(this.primeTnt, p.getName());
						if(selectedKit.get(p.getName()) == "Ender"){
							Block block = p.getTargetBlock((HashSet<Byte>)null, 30);
							Location bLoc = block.getLocation();
							bLoc.setY(bLoc.getWorld().getHighestBlockYAt(bLoc));
							this.primeTnt.teleport(bLoc);
						}else{
							this.primeTnt.setVelocity(vec);
						}
						if(selectedKit.containsKey(p.getName())){
							if((selectedKit.get(p.getName()) == "Boomerang")){
								Bukkit.getScheduler().runTaskLater(this, new Runnable(){
									@Override
									public void run() {
										Vector vec = eye.getDirection().normalize().multiply(-1);
										primeTnt.setVelocity(vec);
									}
								}, 10);
							}
						}

						if((p.getItemInHand().getAmount()-1)>=1){
							p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
						}else{
							p.getItemInHand().setAmount(0);
						}
					}else{
						p.sendMessage("§6TNT Mode: §7[§cOFF§7]");
						return;
					}
				}else{
					return;
				}
			}else{
				return;
			}
		}
	}

	@EventHandler
	public void playerLeaveGame(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(gameQueue.contains(p.getName())){
			gameQueue.remove(p.getName());
			int queued = gameQueue.size();
			Bukkit.broadcastMessage("§b" + p.getName() + " §6has left TNT Wars Queue §7- §b" + queued + " remain!");
			if(selectedKit.containsKey(p.getName())){
				selectedKit.remove(p.getName());
			}
		}
		if(inGame.contains(p.getName())){
			inGame.remove(p.getName());
			modeON.remove(p.getName());
			int game = inGame.size();
			Bukkit.broadcastMessage("§b" + p.getName() + " §6has left TNT Wars §7- §b" + game + " remain!");
			if(selectedKit.containsKey(p.getName())){
				selectedKit.remove(p.getName());
			}
		}
	}

	@EventHandler
	public void tntExplode(EntityExplodeEvent e){
		if(e.getEntity() instanceof CraftTNTPrimed){
			tntActive.remove(e.getEntity());
		}
	}

	public void PlayerInvCheck(String name){
		Player p = Bukkit.getPlayer(name);
		p.getInventory().clear();
		p.getInventory().setContents(invSaves.get(p.getUniqueId()).getContents());
	}

	@EventHandler
	public void gameDeath(PlayerDeathEvent e){
		if(inGame.contains(e.getEntity().getName())){
			Player p = (Player) e.getEntity();
			e.getDrops().clear();
			dead.add(p.getName());
			p.getInventory().setStorageContents(null);
			e.setDeathMessage(null);
			int game = inGame.size();
			if(game > 2){
				inGame.remove(e.getEntity().getName());
				modeON.remove(e.getEntity().getName());
				Bukkit.getScheduler().runTaskLater(this, new Runnable(){
					@Override
					public void run() {
						PlayerInvCheck(p.getName());
					}
				}, (20*10));

			}else{
				inGame.remove(e.getEntity().getName());
				modeON.remove(e.getEntity().getName());
				PlayerInvCheck(e.getEntity().getName());
				Bukkit.broadcastMessage("§cTNT Wars §6has ended!");
				Bukkit.broadcastMessage("§c§l" + inGame.get(0) + " §bhas won!");
				Bukkit.getPlayer(inGame.get(0)).setHealth(20);
				Bukkit.getPlayer(inGame.get(0)).setFoodLevel(20);
				PlayerInvCheck(inGame.get(0));
				inGame.clear();
				modeON.clear();
				active = false;
				canKit = true;
				selectedKit.clear();
			}
		}
	}

	@EventHandler
	public void tntDamage(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(e.getDamager() instanceof CraftTNTPrimed){				
				if(!modeON.contains(p.getName())){
					e.setCancelled(true);
					p.sendMessage("§7You got lucky this time, the next TNT won't be so kind!");
				}
				Bukkit.getScheduler().runTaskLater(this, new Runnable(){
					@Override
					public void run() {
						if(dead.contains(p.getName())){
							int game = inGame.size();
							Bukkit.broadcastMessage("§b" + e.getEntity().getName() + " §6was killed by §c" + e.getDamager().getCustomName() + " §7- §b" + game + " remain!");
							dead.remove(p.getName());
						}
					}
				}, 5);

			}
		}
	}
}
