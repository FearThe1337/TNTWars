package me.abandoncaptian.TNTWars;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftTNTPrimed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.abandoncaptian.TNTWars.KitHandler;

public class Main extends JavaPlugin implements Listener{
	Logger Log = Bukkit.getLogger();
	File configFile;
	FileConfiguration config;
	KitHandler kh;
	MenuHandler mh;
	MenuHandlerHost mhh;
	InvAndExp IAE;
	List<String> gameQueue = new ArrayList<String>();
	List<String> inGame = new ArrayList<String>();
	List<String> dead = new ArrayList<String>();
	List<String> kitsListLowRate = new ArrayList<String>();
	List<String> kitsListHighRate = new ArrayList<String>();
	List<String> kitsListAll = new ArrayList<String>();
	List<Material> armorHelmet = new ArrayList<Material>();
	List<Material> armorChestplate = new ArrayList<Material>();
	List<Material> armorLegs = new ArrayList<Material>();
	List<Material> armorBoots = new ArrayList<Material>();
	List<PotionEffectType> potions = new ArrayList<PotionEffectType>();
	int gameMin;
	int gameStart30Sec;
	int gameMax;
	int gameQueueTime;
	boolean starting1 = false;
	boolean starting2 = false;
	BukkitTask count30;
	BukkitTask count10;
	BukkitTask count5;
	BukkitTask count4;
	BukkitTask count3;
	BukkitTask count2;
	BukkitTask count1;
	BukkitTask countStart;
	BukkitTask countQueue;
	Location spawnpoint;
	ItemStack tnt = new ItemStack(Material.TNT);
	ItemMeta meta = tnt.getItemMeta();
	List<String> lore = new ArrayList<String>();
	HashMap<Entity, String> tntActive = new HashMap<Entity, String>();
	HashMap<String, String> selectedKit = new HashMap<String, String>();

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
		if(!(configFile.exists())){
			config.options().copyDefaults(true);
			this.saveDefaultConfig();
			this.saveConfig();
			Log.info("File Didn't Exist ----");
		}
		gameMin = config.getInt("Game-Min");
		gameStart30Sec = config.getInt("Game-Start-30Sec");
		gameMax = config.getInt("Game-Max");
		gameQueueTime = (config.getInt("Game-Queue-Time")*1200);
		this.spawnpoint = new Location(Bukkit.getWorld((String) config.get("SpawnPoint.world")), config.getInt("SpawnPoint.x"), config.getInt("SpawnPoint.y"), config.getInt("SpawnPoint.z"));
		kh = new KitHandler(this);
		mh = new MenuHandler(this);
		mhh = new MenuHandlerHost(this);
		IAE = new InvAndExp(this);
		Bukkit.getPluginManager().registerEvents(kh, this);
		Bukkit.getPluginManager().registerEvents(mh, this);
		Bukkit.getPluginManager().registerEvents(mhh, this);
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable1(this), 0, 20*3);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable2(this), 0, 2);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable3(this), 0, 20*5);
		kitsListLowRate.add("Sniper");
		kitsListLowRate.add("Heavy Loader");
		kitsListLowRate.add("Potion Worker");
		kitsListLowRate.add("Tank");
		kitsListLowRate.add("Doctor Who");
		kitsListHighRate.add("Short Fuse");
		kitsListHighRate.add("Miner");
		kitsListHighRate.add("Suicide Bomber");
		kitsListHighRate.add("Glue Factory Worker");
		kitsListHighRate.add("Ender");
		kitsListHighRate.add("Boomerang");
		kitsListHighRate.add("Bribed");
		potions.add(PotionEffectType.BLINDNESS);
		potions.add(PotionEffectType.CONFUSION);
		potions.add(PotionEffectType.HUNGER);
		potions.add(PotionEffectType.LEVITATION);
		potions.add(PotionEffectType.POISON);
		potions.add(PotionEffectType.SLOW);
		potions.add(PotionEffectType.WEAKNESS);
		potions.add(PotionEffectType.WITHER);
		kitsListAll.add("Sniper");
		kitsListAll.add("Short Fuse");
		kitsListAll.add("Heavy Loader");
		kitsListAll.add("Miner");
		kitsListAll.add("Suicide Bomber");
		kitsListAll.add("Glue Factory Worker");
		kitsListAll.add("Ender");
		kitsListAll.add("Boomerang");
		kitsListAll.add("Potion Worker");
		kitsListAll.add("Tank");
		kitsListAll.add("Doctor Who");
		kitsListAll.add("Bribed");
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
		meta.setDisplayName("§6§lThrowable §c§lTNT");
		lore.add("§bLeft click to throw");
		lore.add("§bMade By: abandoncaptian");
		meta.setLore(lore);
		tnt.setItemMeta(meta);
		kh.initInv();
	}

	@Override
	public void onDisable()
	{
		Log.info("----------- [ TNT Wars ] -----------");
		Log.info(" ");
		Log.info("              Disabled!             ");
		Log.info(" ");
		Log.info("------------------------------------");
		inGame.clear();
		gameQueue.clear();
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
					if(p.hasPermission("tntwars.host")){
						p.openInventory(mhh.inv);
					}
					else{
						p.openInventory(mh.inv);
					}
				}

				else if(args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("j"))

				{
					if(!active){
						if(gameQueue.contains(p.getName())){
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cAlready in the queue!");
						}else{
							int queuedPre = gameQueue.size();
							if(queuedPre < gameMax){
								gameQueue.add(p.getName());
								p.teleport(this.spawnpoint);
								if(starting1){
									kh.kitsMenu(Bukkit.getPlayer(p.getName()));
								}
								int queued = gameQueue.size();
								if(queued == gameMin){
									countDownPre();
								}else if(queued == gameStart30Sec){
									if(starting1){
										countQueue.cancel();
										countDown30();
										starting1 = false;
									}
								}
								Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has joined queue §7- §bQueued: " + queued);
							}else{
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cTNT Wars Queue is full");
							}
						}
					}else{
						p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6Game is currently active!");
					}
				}

				else if(args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("quit") || args[0].equalsIgnoreCase("l"))

				{
					if(!active){
						if(gameQueue.contains(p.getName())){
							gameQueue.remove(p.getName());
							int queued = gameQueue.size();
							Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has left TNT Wars Queue §7- §bQueued: " + queued);
							if(queued < gameMin){
								Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §6Not enough players to play §7(§bMinimum: " + gameMin + "§7)");
								if(starting1){
									countQueue.cancel();
									starting1 = false;
								}
								if(starting2){
									starting2 = false;
									count30.cancel();
									count10.cancel();
									count5.cancel();
									count4.cancel();
									count3.cancel();
									count2.cancel();
									count1.cancel();
									countStart.cancel();
								}
							}
							if(selectedKit.containsKey(p.getName())){
								selectedKit.remove(p.getName());
							}
						}else{
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou are not in the TNT Wars Queue");
						}
					}else{
						if(inGame.contains(p.getName())){
							inGame.remove(p.getName());
							int game = inGame.size();
							Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has left TNT Wars §7- §b" + game + " remain!");
							if(selectedKit.containsKey(p.getName())){
								selectedKit.remove(p.getName());
							}
						}else{
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou are not in the TNT Wars game");
						}
					}
				}

				else if(args[0].equalsIgnoreCase("setspawn") && p.hasPermission("tntwars.host"))

				{
					this.spawnpoint = p.getLocation();
					config.set("SpawnPoint.world", this.spawnpoint.getWorld().getName());
					config.set("SpawnPoint.x", (int) this.spawnpoint.getX());
					config.set("SpawnPoint.y",(int)  this.spawnpoint.getY());
					config.set("SpawnPoint.z", (int) this.spawnpoint.getZ());
					p.sendMessage("§6TNT Wars SpawnPoint Set!");
					try {
						this.config.save(configFile);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}else if(args[0].equalsIgnoreCase("forcestart") && p.hasPermission("tntwars.host")){
					if(!starting2){
						if(gameQueue.size() >= gameMin){
							countQueue.cancel();
							countDown30();
						}else{
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cNot enough players to start the game.");
						}
					}else{
						p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6TNT Wars is already started!");
					}
				}

				else if(args[0].equalsIgnoreCase("kit") || args[0].equalsIgnoreCase("kits"))

				{
					if(gameQueue.contains(p.getName())){
						if(!canKit){
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §6You cannot change kits mid-game");
						}else{
							kh.kitsMenu(p);
						}
					}else{
						p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cNeed to be in the queue to select a kit");
					}
				}else{
					if(p.hasPermission("tntwars.host"))mhh.MenuHosts(p);
					else mh.Menu(p);
				}
			}
		}
		return true;
	}

	public void countDown30(){
		if(!active){
			starting2 = true;
			countStart = Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				@Override
				public void run() {
					active = true;
					inGame.addAll(gameQueue);
					starting2 = false;
					starting1 = false;
					for(String name : inGame){
						Bukkit.getPlayer(name).closeInventory();
						IAE.InventorySwitch(Bukkit.getPlayer(name));
						IAE.ExpSwitch(name);
						if(Bukkit.getPlayer(name).getGameMode() != GameMode.SURVIVAL) Bukkit.getPlayer(name).setGameMode(GameMode.SURVIVAL);
						Bukkit.getPlayer(name).setInvulnerable(false);
						Bukkit.getPlayer(name).setHealth(20);
						Bukkit.getPlayer(name).setFoodLevel(20);
						Bukkit.getPlayer(name).getInventory().clear();
						Bukkit.getPlayer(name).getInventory().setItem(1, new ItemStack(Material.COOKED_BEEF, 5));
						if(!selectedKit.containsKey(name)){
							int rand = (int) (Math.random()*(kitsListAll.size()-1));
							selectedKit.put(name, kitsListAll.get(rand));
							Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou didn't choose a kit! §6We selected §b" + kitsListAll.get(rand) + " §6for you");
						}else if(selectedKit.get(name) == "Random"){
							int rand = (int) (Math.random()*(kitsListAll.size()-1));
							selectedKit.put(name, kitsListAll.get(rand));
							Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §6We selected §b" + kitsListAll.get(rand) + " §6for you");
						}
						if(selectedKit.get(name) == "Doctor Who"){
							Bukkit.getPlayer(name).setHealthScale(40);
						}
						if(selectedKit.get(name) == "Bribed"){
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
					gameQueue.clear(); 
					canKit = false;
					Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §6TNT Wars has started!");
				}
			}, 20*30);
			count1 = Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				@Override
				public void run() {
					for(String name: gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 1");
					}
				}
			}, 20*29);
			count2 = Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				@Override
				public void run() {
					for(String name: gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 2");
					}
				}
			}, 20*28);
			count3 = Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				@Override
				public void run() {
					for(String name: gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 3");
					}
				}
			}, 20*27);
			count4 = Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				@Override
				public void run() {
					for(String name: gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 4");
					}
				}
			}, 20*26);
			count5 = Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				@Override
				public void run() {
					for(String name: gameQueue){
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 5");
					}
				}
			}, 20*25);
			count10 = Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				@Override
				public void run() {
					for(String name : gameQueue){
						if(!selectedKit.containsKey(name)){
							kh.kitsMenu(Bukkit.getPlayer(name));
						}
						Bukkit.getPlayer(name).sendMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 10");
					}
				}
			}, 20*20);
			count30 = Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				@Override
				public void run() {
					Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in 30 seconds");
					for(String name : gameQueue){
						if(!selectedKit.containsKey(name)){
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
			Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §bStarts in " + config.getInt("Game-Queue-Time") + " minutes.");
			countQueue = Bukkit.getScheduler().runTaskLater(this, new Runnable(){
				@Override
				public void run() {
					countDown30();
				}
			}, (gameQueueTime-600));
		}
	}


	@SuppressWarnings("deprecation")
	@EventHandler
	public void tntClick(PlayerInteractEvent e){
		if(active){
			if((e.getAction() == Action.LEFT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_AIR)){
				Player p = e.getPlayer();
				if(selectedKit.get(p.getName()) == "Miner"){
					ItemStack item = p.getItemInHand();
					if(item.getType() == Material.TNT){
						if(item.getItemMeta().getDisplayName().equals("§6§lThrowable §c§lTNT")){
							if(inGame.contains(p.getName())){
								this.primeTnt = (TNTPrimed) p.getWorld().spawn(p.getLocation(), TNTPrimed.class);
								tntActive.put(this.primeTnt, p.getName());
								this.primeTnt.setFuseTicks(20*5);
								this.primeTnt.setCustomName(p.getName());
								this.primeTnt.setCustomNameVisible(false);
								e.setCancelled(true);
							}
						}
						if((p.getItemInHand().getAmount()-1)>=1){
							p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
						}else{
							p.getItemInHand().setAmount(0);
						}
					}
				}
				return;
			}
			if((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)){
				Player p = e.getPlayer();
				ItemStack item = p.getItemInHand();
				if(item.getType() == Material.TNT){
					if(item.getItemMeta().getDisplayName().equals("§6§lThrowable §c§lTNT")){
						if(inGame.contains(p.getName())){
							double power = 0;
							int fuse;
							if(selectedKit.containsKey(p.getName())){
								if(selectedKit.get(p.getName()) == "Sniper") power = 4;
								else if((selectedKit.get(p.getName()) == "Suicide Bomber"))power = 0;
								else if((selectedKit.get(p.getName()) == "Boomerang"))power = 3;
								else if((selectedKit.get(p.getName()) == "Miner"))power = 0;
								else power = 1.5;
							}else power =  1.5;
							if(selectedKit.containsKey(p.getName())){
								if(selectedKit.get(p.getName()) == "Short Fuse")fuse = 10;
								else if((selectedKit.get(p.getName()) == "Ender"))fuse = 60;
								else if((selectedKit.get(p.getName()) == "Suicide Bomber"))fuse = 0;
								else if((selectedKit.get(p.getName()) == "Boomerang"))fuse = 30;
								else if((selectedKit.get(p.getName()) == "Miner"))fuse = 40;
								else fuse = 20;
							}else fuse = 20;
							Location eye = p.getEyeLocation();
							Vector vec = eye.getDirection().normalize().multiply(power);
							eye.setY(eye.getY() + 0.4);
							this.primeTnt = (TNTPrimed) p.getWorld().spawn(eye, TNTPrimed.class);
							this.primeTnt.setFuseTicks(fuse);
							this.primeTnt.setCustomName(p.getName());
							this.primeTnt.setCustomNameVisible(false);
							tntActive.put(this.primeTnt, p.getName());
							if(selectedKit.get(p.getName()) == "Ender"){
								Block block = p.getTargetBlock((HashSet<Byte>)null, 30);
								Location bLoc = block.getLocation();
								bLoc.setY(bLoc.getY() + 2);
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
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou are not in-game");
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
	}

	@EventHandler
	public void playerLeaveGame(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(!active){
			if(gameQueue.contains(p.getName())){
				int preQue = gameQueue.size();
				gameQueue.remove(p.getName());
				int queued = gameQueue.size();
				if(preQue >= gameMin){
					if(queued < gameMin){
						Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l]  §6Not enough players to play §7(§bMinimum: " + gameMin + "§7)");
						if(starting1){
							countQueue.cancel();
							starting1 = false;
							starting2 = false;
						}
						if(starting2){
							count30.cancel();
							count10.cancel();
							count5.cancel();
							count4.cancel();
							count3.cancel();
							count2.cancel();
							count1.cancel();
							countStart.cancel();
						}
					}
				}
				Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has left queue §7- §bQueued: " + queued);
				if(selectedKit.containsKey(p.getName())){
					selectedKit.remove(p.getName());
				}
			}
		}else if(inGame.contains(p.getName())){
			inGame.remove(p.getName());
			int game = inGame.size();
			Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has left §7- §b" + game + " remain!");
			if(selectedKit.containsKey(p.getName())){
				selectedKit.remove(p.getName());
			}
		}
	}

	@EventHandler
	public void tntExplode(EntityExplodeEvent e){
		if(active){
			if(e.getEntity() instanceof CraftTNTPrimed){
				if(selectedKit.get(e.getEntity().getCustomName()) == "Potion Worker"){
					List<Entity> ents = new ArrayList<Entity>();
					List<Player> players = new ArrayList<Player>();
					ents.clear();
					players.clear();
					ents = e.getEntity().getNearbyEntities(6, 6, 6);
					if(ents.size() >= 1){
						for(Entity ent : ents){
							if(ent instanceof Player){
								if(inGame.contains(ent.getName())){
									int rand = (int) (Math.random()*7);
									((LivingEntity) ent).addPotionEffect(new PotionEffect(potions.get(rand), (20*2), 2));
									ents.remove(ent);
								}
							}else{
								ents.remove(ent);
							}
						}
						if(ents.get(0) instanceof Player){
							if(inGame.contains(ents.get(0).getName())){
								int rand = (int) (Math.random()*7);
								((LivingEntity) ents.get(0)).addPotionEffect(new PotionEffect(potions.get(rand), (20*2), 2));
								ents.remove(ents.get(0));
							}
						}else{
							ents.remove(ents.get(0) );
						}
					}
				}
				tntActive.remove(e.getEntity());
			}
		}
	}

	@EventHandler
	public void gameConnect(PlayerJoinEvent e){
		if(IAE.hasSwitchedInv(e.getPlayer()))IAE.InventorySwitch(e.getPlayer());
		if(IAE.hasSwitchedExp(e.getPlayer()))IAE.ExpSwitch(e.getPlayer().getName());
	}
	@EventHandler
	public void gameDeath(PlayerDeathEvent e){
		if(active){
			if(inGame.contains(e.getEntity().getName())){
				Player p = (Player) e.getEntity();
				e.getDrops().clear();
				dead.add(p.getName());
				e.setDeathMessage(null);
				p.setHealthScale(20);
				int preGame = inGame.size();
				inGame.remove(e.getEntity().getName());
				int game = inGame.size();
				e.getEntity().teleport(this.spawnpoint);
				Bukkit.getScheduler().runTaskLater(this, new Runnable(){
					@Override
					public void run() {
						p.getInventory().clear();
						IAE.InventorySwitch(p);
						IAE.ExpSwitch(p.getName());
					}
				}, (20*3));
				Bukkit.getScheduler().runTaskLater(this, new Runnable(){
					@Override
					public void run() {
						if(dead.contains(p.getName())){
							Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + e.getEntity().getName() + " §6was killed §7- §b" + preGame + " remain!");
							dead.remove(p.getName());
						}
					}
				}, 5);
				if(game == 1){
					Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b§l" + inGame.get(0) + " §6has won!");
					Bukkit.getPlayer(inGame.get(0)).setHealth(20);
					Bukkit.getPlayer(inGame.get(0)).setFoodLevel(20);
					Bukkit.getPlayer(inGame.get(0)).getInventory().clear();
					IAE.InventorySwitch(Bukkit.getPlayer(inGame.get(0)));
					IAE.ExpSwitch(inGame.get(0));
					Bukkit.getPlayer(inGame.get(0)).setHealthScale(20);
					Bukkit.getPlayer(inGame.get(0)).teleport(this.spawnpoint);
					inGame.clear();
					kh.inv.clear();
					active = false;
					canKit = true;
					starting1 = false;
					starting2 = false;
					selectedKit.clear();
					kh.initInv();
				}
			}
		}
	}

	@EventHandler
	public void tntDamage(EntityDamageByEntityEvent e){
		if(active){
			if(e.getEntity() instanceof Player){
				Player p = (Player)e.getEntity();
				if(e.getDamager() instanceof CraftTNTPrimed){
					if(!inGame.contains(p.getName())){
						e.setCancelled(true);
						p.sendMessage("§7§l[§c§lTNT Wars§7§l] §7You got lucky this time, the next TNT won't be so kind!");
					}	
					if(!inGame.contains(p.getName()))return;
					if(p.getName() == e.getDamager().getCustomName()){
						if(selectedKit.get(p.getName()) == "Suicide Bomber"){
							double dam = p.getLastDamage();
							e.setDamage(dam/2);
						}
					}
					if(selectedKit.get(p.getName()) == "Tank"){
						double dam = p.getLastDamage();
						e.setDamage(dam/2);
					}
					Bukkit.getScheduler().runTaskLater(this, new Runnable(){
						@Override
						public void run() {
							if(dead.contains(p.getName())){
								int game = inGame.size();
								if(e.getEntity().getName() == e.getDamager().getCustomName())Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + e.getEntity().getName() + " §6pulled a SashaLarie and killed themself §7- §b" + game + " remain!");
								else Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + e.getEntity().getName() + " §6was killed by §c" + e.getDamager().getCustomName() + " §7- §b" + game + " remain!");
								dead.remove(p.getName());
							}
						}
					}, 2);
				}else{
					if(!inGame.contains(p.getName()))return;
					Bukkit.getScheduler().runTaskLater(this, new Runnable(){
						@Override
						public void run() {
							if(dead.contains(p.getName())){
								int game = inGame.size();
								Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + e.getEntity().getName() + " §6was killed §7- §b" + game + " remain!");
								dead.remove(p.getName());
							}
						}
					}, 2);
				}
			}
		}
	}
}