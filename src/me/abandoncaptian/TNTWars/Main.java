package me.abandoncaptian.TNTWars;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.abandoncaptian.TNTWars.Events.BlockPlace;
import me.abandoncaptian.TNTWars.Events.EntityDamageByEntity;
import me.abandoncaptian.TNTWars.Events.EntityExplode;
import me.abandoncaptian.TNTWars.Events.MenuClickHandler;
import me.abandoncaptian.TNTWars.Events.PlayerDeath;
import me.abandoncaptian.TNTWars.Events.PlayerInteract;
import me.abandoncaptian.TNTWars.Events.PlayerLeaveAndJoin;

public class Main extends JavaPlugin implements Listener {
	Logger Log = Bukkit.getLogger();
	public File configFile;
	public FileConfiguration config;
	public File perksFile;
	public FileConfiguration perksConfig;
	MenuHandler mh;
	public InvAndExp IAE;
	public CountDowns cd;
	PlayerInteract PI;
	PlayerLeaveAndJoin PLAJ;
	EntityExplode EE;
	LoadFunctions LF;
	PlayerDeath PD;
	EntityDamageByEntity EDBE;
	BlockPlace BP;
	CommandHandler CH;
	MenuClickHandler MCH;
	GameFunc GF;
	public EncoderAndDecoder ED;
	public Map<String, List<String>> gameQueue = new HashMap<String, List<String>>();
	public List<String> allQueue = new ArrayList<String>();
	public Map<String, List<String>> inGame = new HashMap<String, List<String>>();
	public List<String> allInGame = new ArrayList<String>();
	public Map<String, List<String>> dead = new HashMap<String, List<String>>();
	public Map<String, List<String>> spec = new HashMap<String, List<String>>();
	public Map<String, String> playerInArena = new HashMap<String, String>();
	public List<PotionEffectType> potions = new ArrayList<PotionEffectType>();
	public Map<Integer, String> arenas = new HashMap<Integer, String>();
	public Map<String, Material> arenaIcons = new HashMap<String, Material>();
	public Map<String, ItemStack[]> extraInv;
	public Map<String, HashMap<String, Boolean>> Perks = new HashMap<String, HashMap<String, Boolean>>();
	public Map<String, String> fwSettings = new HashMap<String, String>();
	public Map<String, Integer> savedXPL = new HashMap<String, Integer>();
	public Map<String, Float> savedXP = new HashMap<String, Float>();
	public Map<String, Location> tpBack = new HashMap<String, Location>();
	public Map<String, String> cName = new HashMap<String, String>();
	public Map<String, ChatColor> cNameColor = new HashMap<String, ChatColor>();
	public Map<String, Location> spawnpoint = new HashMap<String, Location>();
	public Map<String, Location> specpoint = new HashMap<String, Location>();
	ScoreboardManager manager;
	Map<String, Scoreboard> queueBoard = new HashMap<String, Scoreboard>();
	Map<String, Scoreboard> remainingBoard = new HashMap<String, Scoreboard>();
	Scoreboard clearBoard;
	Map<String, Objective> objectiveQueue = new HashMap<String, Objective>();
	Map<String, Objective> objectiveRemaining = new HashMap<String, Objective>();
	public int gameMin;
	int gameStart30Sec;
	int gameMax;
	int gameQueueTime;
	public Main instance;
	public HashMap<Entity, String> tntActive = new HashMap<Entity, String>();
	public HashMap<String, String> selectedKit = new HashMap<String, String>();
	BukkitTask deathCount;

	@Override
	public void onEnable() {
		Log.info("----------- [ TNT Wars ] -----------");
		Log.info(" ");
		Log.info("               Enabled!             ");
		Log.info(" ");
		Log.info("------------------------------------");
		this.configFile = new File("plugins/TNTWars/config.yml");
		this.config = YamlConfiguration.loadConfiguration(configFile);
		this.perksFile = new File("plugins/TNTWars/DonorPerksDatabase.yml");
		this.perksConfig = YamlConfiguration.loadConfiguration(perksFile);
		if (!(configFile.exists())) {
			config.options().copyDefaults(true);
			this.saveDefaultConfig();
			this.saveConfig();
			Log.info("Config File Didn't Exist ----");
		}
		if (!(perksFile.exists())) {
			perksConfig.options().copyDefaults(true);
			try {
				perksConfig.save(perksFile);
			} catch (IOException e) {
			}
			Log.info("Perks File Didn't Exist ----");
		}
		if (!config.contains("Arenas")) {
			config.set("Arenas.1.Spawnpoint.world", "world");
			config.set("Arenas.1.Spawnpoint.x", 0);
			config.set("Arenas.1.Spawnpoint.y", 100);
			config.set("Arenas.1.Spawnpoint.z", 0);
			config.set("Arenas.1.SpecPoint.world", "world");
			config.set("Arenas.1.SpecPoint.x", 0);
			config.set("Arenas.1.SpecPoint.y", 100);
			config.set("Arenas.1.SpecPoint.z", 0);
			this.saveConfig();
		}
		Boolean check = true;
		int checkNum = 1;
		while(check){
			if(config.contains("Arenas." + checkNum)){
				String mapName = config.getString("Arenas." + checkNum + ".Name");
				arenas.put(checkNum, mapName);
				arenaIcons.put(mapName, Material.valueOf(config.getString("Arenas." + checkNum + ".Icon")));
				checkNum++;
			}else{
				check = false;
			}
		}
		for(Integer aName : arenas.keySet()){
			spawnpoint.put(arenas.get(aName), new Location(Bukkit.getWorld((String)
					config.get("Arenas."+ aName +".SpawnPoint.world")),
					config.getInt("Arenas."+ aName +".SpawnPoint.x"),
					config.getInt("Arenas."+ aName +".SpawnPoint.y"),
					config.getInt("Arenas."+ aName +".SpawnPoint.z")));
			specpoint.put(arenas.get(aName), new Location(Bukkit.getWorld((String)
					config.get("Arenas."+ aName +".SpecPoint.world")),
					config.getInt("Arenas."+ aName +".SpecPoint.x"),
					config.getInt("Arenas."+ aName +".SpecPoint.y"),
					config.getInt("Arenas."+ aName +".SpecPoint.z")));
		}
		LF = new LoadFunctions(this);
		gameMin = config.getInt("Game-Min");
		gameStart30Sec = config.getInt("Game-Start-30Sec");
		gameMax = config.getInt("Game-Max");
		gameQueueTime = (config.getInt("Game-Queue-Time") * 1200);
		manager = Bukkit.getScoreboardManager();
		clearBoard = manager.getNewScoreboard();
		cd = new CountDowns(this);
		mh = new MenuHandler(this);
		IAE = new InvAndExp(this);
		PI = new PlayerInteract(this);
		PLAJ = new PlayerLeaveAndJoin(this);
		EDBE = new EntityDamageByEntity(this);
		EE = new EntityExplode(this);
		PD = new PlayerDeath(this);
		BP = new BlockPlace(this);
		CH = new CommandHandler(this);
		ED = new EncoderAndDecoder(this);
		MCH = new MenuClickHandler(this);
		GF = new GameFunc(this);
		extraInv = new HashMap<>();
		Bukkit.getPluginManager().registerEvents(mh, this);
		Bukkit.getPluginManager().registerEvents(PI, this);
		Bukkit.getPluginManager().registerEvents(PLAJ, this);
		Bukkit.getPluginManager().registerEvents(EE, this);
		Bukkit.getPluginManager().registerEvents(EDBE, this);
		Bukkit.getPluginManager().registerEvents(BP, this);
		Bukkit.getPluginManager().registerEvents(PD, this);
		Bukkit.getPluginManager().registerEvents(MCH, this);
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("tw").setExecutor(CH);
		for(String map : arenas.values()){
			queueBoard.put(map, manager.getNewScoreboard());
			remainingBoard.put(map, manager.getNewScoreboard());
			objectiveQueue.put(map, queueBoard.get(map).registerNewObjective("Queued", "dummy"));
			objectiveRemaining.put(map, remainingBoard.get(map).registerNewObjective("Remaining", "dummy"));
			objectiveQueue.get(map).setDisplayName("§7§l[§c§lQueued§7§l]");
			objectiveQueue.get(map).setDisplaySlot(DisplaySlot.SIDEBAR);
			objectiveRemaining.get(map).setDisplayName("§7§l[§c§lRemaining§7§l]");
			objectiveRemaining.get(map).setDisplaySlot(DisplaySlot.SIDEBAR);
			cd.active.put(map, false);
			gameQueue.put(map, new ArrayList<String>());
			gameQueue.get(map).clear();
			inGame.put(map, new ArrayList<String>());
			inGame.get(map).clear();
			dead.put(map, new ArrayList<String>());
			dead.get(map).clear();
			spec.put(map, new ArrayList<String>());
			spec.get(map).clear();
			cd.starting1.put(map, false);
			cd.starting2.put(map, false);
			cd.active.put(map, false);
			cd.canKit.put(map, true);
		}
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new HighGiveRate(this), 0, 20 * 3);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new StickTNTCheck(this), 0, 2);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new LowGiveRate(this), 0, 20 * 5);
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
	public void onDisable() {
		Log.info("----------- [ TNT Wars ] -----------");
		Log.info(" ");
		Log.info("              Disabled!             ");
		Log.info(" ");
		Log.info("------------------------------------");
		for(String map : arenas.values()){
			cd.active.put(map, false);
			cd.canKit.put(map, true);
		}
		inGame.clear();
		gameQueue.clear();
		allInGame.clear();
		allQueue.clear();
		arenas.clear();
		selectedKit.clear();
	}
}