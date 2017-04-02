package me.abandoncaptian.TNTWars;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftTNTPrimed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import me.abandoncaptian.TNTWars.Events.PlayerInteract;
import me.abandoncaptian.TNTWars.Events.PlayerLeaveAndJoin;

public class Main extends JavaPlugin implements Listener{
	Logger Log = Bukkit.getLogger();
	File configFile;
	FileConfiguration config;
	KitHandler kh;
	MenuHandler mh;
	MenuHandlerHost mhh;
	InvAndExp IAE;
	CountDowns cd;
	PlayerInteract PI;
	PlayerLeaveAndJoin PLAJ;
	public List<String> gameQueue = new ArrayList<String>();
	public List<String> inGame = new ArrayList<String>();
	List<String> dead = new ArrayList<String>();
	List<String> kitsListLowRate = new ArrayList<String>();
	List<String> kitsListHighRate = new ArrayList<String>();
	List<String> kitsListAll = new ArrayList<String>();
	List<Material> armorHelmet = new ArrayList<Material>();
	List<Material> armorChestplate = new ArrayList<Material>();
	List<Material> armorLegs = new ArrayList<Material>();
	List<Material> armorBoots = new ArrayList<Material>();
	List<PotionEffectType> potions = new ArrayList<PotionEffectType>();
	public int gameMin;
	int gameStart30Sec;
	int gameMax;
	int gameQueueTime;
	Location spawnpoint;
	public HashMap<Entity, String> tntActive = new HashMap<Entity, String>();
	public HashMap<String, String> selectedKit = new HashMap<String, String>();
	BukkitTask deathCount;

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
		cd = new CountDowns(this);
		PI = new PlayerInteract(this);
		PLAJ = new PlayerLeaveAndJoin(this);
		Bukkit.getPluginManager().registerEvents(kh, this);
		Bukkit.getPluginManager().registerEvents(mh, this);
		Bukkit.getPluginManager().registerEvents(mhh, this);
		Bukkit.getPluginManager().registerEvents(PI, this);
		Bukkit.getPluginManager().registerEvents(PLAJ, this);
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable1(this), 0, 20*3);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable2(this), 0, 2);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable3(this), 0, 20*5);
		initKitsAll();
		initKitsRates();
		initPotions();
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
		cd.active = false;
		cd.canKit = true;
	}

	public void initKitsRates(){
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
	}
	
	public void initKitsAll(){
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
	}

	public void initPotions(){
		potions.add(PotionEffectType.BLINDNESS);
		potions.add(PotionEffectType.CONFUSION);
		potions.add(PotionEffectType.HUNGER);
		potions.add(PotionEffectType.LEVITATION);
		potions.add(PotionEffectType.POISON);
		potions.add(PotionEffectType.SLOW);
		potions.add(PotionEffectType.WEAKNESS);
		potions.add(PotionEffectType.WITHER);
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
					if(!cd.active){
						if(gameQueue.contains(p.getName())){
							p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cAlready in the queue!");
						}else{
							int queuedPre = gameQueue.size();
							if(queuedPre < gameMax){
								gameQueue.add(p.getName());
								p.teleport(this.spawnpoint);
								if(cd.starting1){
									kh.kitsMenu(Bukkit.getPlayer(p.getName()));
								}
								int queued = gameQueue.size();
								if(queued == gameMin){
									cd.countDownPre();
								}else if(queued == gameStart30Sec){
									if(cd.starting1){
										cd.countQueue.cancel();
										cd.countDown30();
										cd.starting1 = false;
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
					if(!cd.active){
						if(gameQueue.contains(p.getName())){
							gameQueue.remove(p.getName());
							int queued = gameQueue.size();
							Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has left TNT Wars Queue §7- §bQueued: " + queued);
							if(queued < gameMin){
								Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §6Not enough players to play §7(§bMinimum: " + gameMin + "§7)");
								if(cd.starting1){
									cd.countQueue.cancel();
									cd.starting1 = false;
								}
								if(cd.starting2){
									cd.starting2 = false;
									cd.count30.cancel();
									cd.count10.cancel();
									cd.count5.cancel();
									cd.count4.cancel();
									cd.count3.cancel();
									cd.count2.cancel();
									cd.count1.cancel();
									cd.countStart.cancel();
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
					if(!cd.starting2){
						if(gameQueue.size() >= gameMin){
							cd.countQueue.cancel();
							cd.countDown30();
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
						if(!cd.canKit){
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

	@EventHandler
	public void tntExplode(EntityExplodeEvent e){
		if(cd.active){
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
	public void gameDeath(PlayerDeathEvent e){
		if(cd.active){
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
					cd.active = false;
					cd.canKit = true;
					cd.starting1 = false;
					cd.starting2 = false;
					selectedKit.clear();
					kh.initInv();
				}
			}
		}
	}

	@EventHandler
	public void tntDamage(EntityDamageByEntityEvent e){
		if(cd.active){
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