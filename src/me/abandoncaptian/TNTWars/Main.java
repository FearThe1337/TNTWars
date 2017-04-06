package me.abandoncaptian.TNTWars;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.abandoncaptian.TNTWars.Events.BlockPlace;
import me.abandoncaptian.TNTWars.Events.EntityDamageByEntity;
import me.abandoncaptian.TNTWars.Events.EntityExplode;
import me.abandoncaptian.TNTWars.Events.PlayerDeath;
import me.abandoncaptian.TNTWars.Events.PlayerInteract;
import me.abandoncaptian.TNTWars.Events.PlayerLeaveAndJoin;

public class Main extends JavaPlugin implements Listener{
	Logger Log = Bukkit.getLogger();
	File configFile;
	FileConfiguration config;
	public KitHandler kh;
	MenuHandler mh;
	MenuHandlerHost mhh;
	public InvAndExp IAE;
	public CountDowns cd;
	PlayerInteract PI;
	PlayerLeaveAndJoin PLAJ;
	EntityExplode EE;
	LoadFunctions LF;
	PlayerDeath PD;
	EntityDamageByEntity EDBE;
	BlockPlace BP;
	public List<String> gameQueue = new ArrayList<String>();
	public List<String> inGame = new ArrayList<String>();
	public List<String> dead = new ArrayList<String>();
	public List<String> spec = new ArrayList<String>();
	public List<PotionEffectType> potions = new ArrayList<PotionEffectType>();
	public Map<String, ItemStack[]> extraInv;
	public Map<String, Integer> savedXPL = new HashMap<String, Integer>();
	public Map<String, Float> savedXP = new HashMap<String, Float>();
	ScoreboardManager manager = Bukkit.getScoreboardManager();
	Scoreboard queueBoard = manager.getNewScoreboard();
	Scoreboard remainingBoard = manager.getNewScoreboard();
	Scoreboard clearBoard = manager.getNewScoreboard();
	Objective objectiveQueue = queueBoard.registerNewObjective("Queued", "dummy");
	Objective objectiveRemaining = remainingBoard.registerNewObjective("Remaining", "dummy");
	public int gameMin;
	int gameStart30Sec;
	int gameMax;
	int gameQueueTime;
	public Location spawnpoint;
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
		LF = new LoadFunctions(this);
		gameMin = config.getInt("Game-Min");
		gameStart30Sec = config.getInt("Game-Start-30Sec");
		gameMax = config.getInt("Game-Max");
		gameQueueTime = (config.getInt("Game-Queue-Time")*1200);
		this.spawnpoint = new Location(Bukkit.getWorld((String) config.get("SpawnPoint.world")), config.getInt("SpawnPoint.x"), config.getInt("SpawnPoint.y"), config.getInt("SpawnPoint.z"));
		cd = new CountDowns(this);
		kh = new KitHandler(this);
		kh.initInv();
		mh = new MenuHandler(this);
		mhh = new MenuHandlerHost(this);
		IAE = new InvAndExp(this);
		PI = new PlayerInteract(this);
		PLAJ = new PlayerLeaveAndJoin(this);
		EDBE = new EntityDamageByEntity(this);
		EE = new EntityExplode(this);
		PD = new PlayerDeath(this);
		BP = new BlockPlace(this);
		objectiveQueue.setDisplayName("§7§l[§c§lQueued§7§l]");
		objectiveQueue.setDisplaySlot(DisplaySlot.SIDEBAR);
		objectiveRemaining.setDisplayName("§7§l[§c§lRemaining§7§l]");
		objectiveRemaining.setDisplaySlot(DisplaySlot.SIDEBAR);
		extraInv = new HashMap<>();
		Bukkit.getPluginManager().registerEvents(kh, this);
		Bukkit.getPluginManager().registerEvents(mh, this);
		Bukkit.getPluginManager().registerEvents(mhh, this);
		Bukkit.getPluginManager().registerEvents(PI, this);
		Bukkit.getPluginManager().registerEvents(PLAJ, this);
		Bukkit.getPluginManager().registerEvents(EE, this);
		Bukkit.getPluginManager().registerEvents(EDBE, this);
		Bukkit.getPluginManager().registerEvents(BP, this);
		Bukkit.getPluginManager().registerEvents(PD, this);
		Bukkit.getPluginManager().registerEvents(this, this);
		cd.active = false;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable1(this), 0, 20*3);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable2(this), 0, 2);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable3(this), 0, 20*5);
		potions.add(PotionEffectType.BLINDNESS);
		potions.add(PotionEffectType.CONFUSION);
		potions.add(PotionEffectType.HUNGER);
		potions.add(PotionEffectType.LEVITATION);
		potions.add(PotionEffectType.POISON);
		potions.add(PotionEffectType.SLOW);
		potions.add(PotionEffectType.WEAKNESS);
		potions.add(PotionEffectType.WITHER);
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

	public void UpdateBoard(Boolean leave, String name){
		if(!leave){
			Score score;
			score = objectiveQueue.getScore("§a" + name);
			score.setScore(gameQueue.indexOf(name));
			Bukkit.getPlayer(name).setScoreboard(queueBoard);
			score = objectiveRemaining.getScore("§c" + name);
			score.setScore(gameQueue.indexOf(name));
		}else{
			queueBoard.resetScores("§a" + name);
			remainingBoard.resetScores("§c" + name);
			Bukkit.getPlayer(name).setScoreboard(clearBoard);
		}
	}
	
	public void ChangeBoard(){
		for(String name : inGame){
			Bukkit.getPlayer(name).setScoreboard(remainingBoard);
		}
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
								UpdateBoard(false, p.getName());
								if(cd.starting1){
									kh.kitsMenu(Bukkit.getPlayer(p.getName()));
								}
								int queued = gameQueue.size();
								Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has joined queue §7- §bQueued: " + queued);
								if(queued == gameMin){
									cd.countDownPre();
								}else if(queued == gameStart30Sec){
									if(cd.starting2){
										cd.starting1 = false;
									}else if(cd.starting1){
										cd.countQueue.cancel();
										cd.countDown30();
										cd.starting1 = false;
									}
								}
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
							UpdateBoard(true, p.getName());
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
							UpdateBoard(true, p.getName());
							int game = inGame.size();
							if(game == 1){
								String winner = inGame.get(0);
								UpdateBoard(true, inGame.get(0));
								Bukkit.getScheduler().runTaskLater(this, new Runnable(){
									@Override
									public void run() {
										Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has left TNT Wars!");
										Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b§l" + winner + " §6has won!");
									}
								}, 2);
								p.closeInventory();
								p.getInventory().clear();
								IAE.InventorySwitch(p);
								IAE.ExpSwitch(p.getName());
								p.setHealthScale(20);
								p.teleport(spawnpoint);
								p.setHealth(20);
								p.setFoodLevel(20);
								Bukkit.getPlayer(inGame.get(0)).setHealth(20);
								Bukkit.getPlayer(inGame.get(0)).setFoodLevel(20);
								Bukkit.getPlayer(inGame.get(0)).getInventory().clear();
								Bukkit.getPlayer(inGame.get(0)).closeInventory();
								IAE.InventorySwitch(Bukkit.getPlayer(inGame.get(0)));
								IAE.ExpSwitch(inGame.get(0));
								Bukkit.getPlayer(inGame.get(0)).setHealthScale(20);
								Bukkit.getPlayer(inGame.get(0)).teleport(spawnpoint);
								inGame.clear();
								cd.active = false;
								cd.canKit = true;
								cd.starting1 = false;
								cd.starting2 = false;
								selectedKit.clear();
								kh.initInv();
							}else{
								Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b" + p.getName() + " §6has left TNT Wars §7- §b" + game + " remain!");
								if(selectedKit.containsKey(p.getName())){
									selectedKit.remove(p.getName());
								}
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
}