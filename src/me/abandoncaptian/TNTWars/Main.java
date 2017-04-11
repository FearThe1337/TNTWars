package me.abandoncaptian.TNTWars;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.abandoncaptian.TNTWars.Events.BlockPlace;
import me.abandoncaptian.TNTWars.Events.EntityDamageByEntity;
import me.abandoncaptian.TNTWars.Events.EntityExplode;
import me.abandoncaptian.TNTWars.Events.PlayerDeath;
import me.abandoncaptian.TNTWars.Events.PlayerInteract;
import me.abandoncaptian.TNTWars.Events.PlayerLeaveAndJoin;

public class Main extends JavaPlugin implements Listener {
	Logger Log = Bukkit.getLogger();
	File configFile;
	FileConfiguration config;
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
	DonorPerksMenu DPM;
	public List<String> gameQueue = new ArrayList<String>();
	public List<String> inGame = new ArrayList<String>();
	public List<String> dead = new ArrayList<String>();
	public List<String> spec = new ArrayList<String>();
	public List<PotionEffectType> potions = new ArrayList<PotionEffectType>();
	public Map<String, ItemStack[]> extraInv;
	public Map<String, HashMap<String, Boolean>> Perks = new HashMap<String, HashMap<String, Boolean>>();
	public Map<String, Integer> savedXPL = new HashMap<String, Integer>();
	public Map<String, Float> savedXP = new HashMap<String, Float>();
	public Map<String, Location> tpBack = new HashMap<String, Location>();
	ScoreboardManager manager;
	Scoreboard queueBoard;
	Scoreboard remainingBoard;
	Scoreboard clearBoard;
	Objective objectiveQueue;
	Objective objectiveRemaining;
	public int gameMin;
	int gameStart30Sec;
	int gameMax;
	int gameQueueTime;
	public Location spawnpoint;
	public Location specpoint;
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
			Log.info("File Didn't Exist ----");
		}
		if (!(perksFile.exists())) {
			perksConfig.options().copyDefaults(true);
			this.saveDefaultConfig();
			this.saveConfig();
			Log.info("File Didn't Exist ----");
		}
		if (!config.contains("SpawnPoint")) {
			config.set("Spawnpoint.world", "world");
			config.set("Spawnpoint.x", 0);
			config.set("Spawnpoint.y", 100);
			config.set("Spawnpoint.z", 0);
			this.saveConfig();
		}
		if (!config.contains("SpecPoint")) {
			config.set("SpecPoint.world", "world");
			config.set("SpecPoint.x", 0);
			config.set("SpecPoint.y", 100);
			config.set("SpecPoint.z", 0);
			this.saveConfig();
		}
		LF = new LoadFunctions(this);
		gameMin = config.getInt("Game-Min");
		gameStart30Sec = config.getInt("Game-Start-30Sec");
		gameMax = config.getInt("Game-Max");
		gameQueueTime = (config.getInt("Game-Queue-Time") * 1200);
		this.spawnpoint = new Location(Bukkit.getWorld((String) config.get("SpawnPoint.world")),
				config.getInt("SpawnPoint.x"), config.getInt("SpawnPoint.y"), config.getInt("SpawnPoint.z"));
		this.specpoint = new Location(Bukkit.getWorld((String) config.get("SpecPoint.world")),
				config.getInt("SpecPoint.x"), config.getInt("SpecPoint.y"), config.getInt("SpecPoint.z"));
		manager = Bukkit.getScoreboardManager();
		queueBoard = manager.getNewScoreboard();
		remainingBoard = manager.getNewScoreboard();
		clearBoard = manager.getNewScoreboard();
		objectiveQueue = queueBoard.registerNewObjective("Queued", "dummy");
		objectiveRemaining = remainingBoard.registerNewObjective("Remaining", "dummy");
		cd = new CountDowns(this);
		mh = new MenuHandler(this);
		IAE = new InvAndExp(this);
		PI = new PlayerInteract(this);
		PLAJ = new PlayerLeaveAndJoin(this);
		EDBE = new EntityDamageByEntity(this);
		EE = new EntityExplode(this);
		PD = new PlayerDeath(this);
		BP = new BlockPlace(this);
		DPM = new DonorPerksMenu(this);
		DPM.initInv();
		CH = new CommandHandler(this);
		objectiveQueue.setDisplayName("§7§l[§c§lQueued§7§l]");
		objectiveQueue.setDisplaySlot(DisplaySlot.SIDEBAR);
		objectiveRemaining.setDisplayName("§7§l[§c§lRemaining§7§l]");
		objectiveRemaining.setDisplaySlot(DisplaySlot.SIDEBAR);
		extraInv = new HashMap<>();
		Bukkit.getPluginManager().registerEvents(mh, this);
		Bukkit.getPluginManager().registerEvents(PI, this);
		Bukkit.getPluginManager().registerEvents(PLAJ, this);
		Bukkit.getPluginManager().registerEvents(EE, this);
		Bukkit.getPluginManager().registerEvents(EDBE, this);
		Bukkit.getPluginManager().registerEvents(BP, this);
		Bukkit.getPluginManager().registerEvents(PD, this);
		Bukkit.getPluginManager().registerEvents(DPM, this);
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("tw").setExecutor(CH);
		cd.active = false;
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
		inGame.clear();
		gameQueue.clear();
		selectedKit.clear();
		cd.active = false;
		cd.canKit = true;
	}

	public void UpdateBoard(Boolean leave, String name) {
		if (!leave) {
			Score score;
			score = objectiveQueue.getScore("§a" + name);
			score.setScore(gameQueue.indexOf(name));
			Bukkit.getPlayer(name).setScoreboard(queueBoard);
			score = objectiveRemaining.getScore("§c" + name);
			score.setScore(gameQueue.indexOf(name));
		} else {
			queueBoard.resetScores("§a" + name);
			remainingBoard.resetScores("§c" + name);
			Bukkit.getPlayer(name).setScoreboard(clearBoard);
		}
	}

	public void ChangeBoard() {
		for (String name : inGame) {
			Bukkit.getPlayer(name).setScoreboard(remainingBoard);
		}
	}

}