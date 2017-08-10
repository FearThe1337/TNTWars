package me.abandoncaptian.TNTWars;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.abandoncaptian.TNTWars.Events.BlockExplode;
import me.abandoncaptian.TNTWars.Events.BlockPlace;
import me.abandoncaptian.TNTWars.Events.EntityDamage;
import me.abandoncaptian.TNTWars.Events.EntityDamageByEntity;
import me.abandoncaptian.TNTWars.Events.EntityExplode;
import me.abandoncaptian.TNTWars.Events.MenuClickHandler;
import me.abandoncaptian.TNTWars.Events.PlayerChat;
import me.abandoncaptian.TNTWars.Events.PlayerDeath;
import me.abandoncaptian.TNTWars.Events.PlayerDropItem;
import me.abandoncaptian.TNTWars.Events.PlayerInteract;
import me.abandoncaptian.TNTWars.Events.PlayerLeaveAndJoin;
import me.abandoncaptian.TNTWars.Events.SignChange;
import net.milkbowl.vault.chat.Chat;

public class Main extends JavaPlugin implements Listener {
	Logger Log = Bukkit.getLogger();
	public File configFile;
	public FileConfiguration config;
	public File perksFile;
	public FileConfiguration perksConfig;
	public File signsFile;
	public FileConfiguration signsConfig;
	public File playerFile;
	public FileConfiguration playerConfig;
	public File econFile;
	public FileConfiguration econConfig;
	MenuHandler mh;
	public CountDowns cd;
	PlayerInteract PI;
	PlayerLeaveAndJoin PLAJ;
	EntityExplode EE;
	public LoadFunctions LF;
	PlayerDeath PD;
	EntityDamageByEntity EDBE;
	BlockPlace BP;
	CommandHandler CH;
	MenuClickHandler MCH;
	GameFunc GF;
	SignChange SC;
	BlockExplode BE;
	EntityDamage EDE;
	PlayerDropItem PDI;
	PlayerChat PC;
	public SignUpdater SU;
	public Economy econ;
	public EncoderAndDecoder ED;
	public Chat chat;
	public Map<String, List<String>> gameQueue = new HashMap<String, List<String>>();
	public HashMap<UUID, PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();
	public List<String> allQueue = new ArrayList<String>();
	public List<String> signs = new ArrayList<String>();
	public Map<String, List<String>> inGame = new HashMap<String, List<String>>();
	public List<String> allInGame = new ArrayList<String>();
	public Map<String, List<String>> dead = new HashMap<String, List<String>>();
	public Map<String, String> playerInArena = new HashMap<String, String>();
	public List<PotionEffectType> potions = new ArrayList<PotionEffectType>();
	public Map<Integer, String> arenas = new HashMap<Integer, String>();
	public Map<String, Material> arenaIcons = new HashMap<String, Material>();
	public Map<String, HashMap<String, Boolean>> Perks = new HashMap<String, HashMap<String, Boolean>>();
	public Map<String, String> fwSettings = new HashMap<String, String>();
	public Map<String, String> cName = new HashMap<String, String>();
	public Map<String, ChatColor> cNameColor = new HashMap<String, ChatColor>();
	public Map<String, Map<Integer, Location>> spawnpoint = new HashMap<String, Map<Integer, Location>>();
	public Map<String, Location> lobby = new HashMap<String, Location>();
	public Map<String, Location> spectate = new HashMap<String, Location>();
	public Location hub;
	public Map<String, Integer> gameMax = new HashMap<String, Integer>();
	public Map<String, Integer> perTeam = new HashMap<String, Integer>();
	public Map<String, Integer> teamAmount = new HashMap<String, Integer>();
	public Map<String, Map<Integer, List<String>>> teams = new HashMap<String, Map<Integer, List<String>>>();
	public Map<String, Map<Integer, Material>> teamsIcons = new HashMap<String, Map<Integer, Material>>();
	ScoreboardManager manager;
	Map<String, Scoreboard> queueBoard = new HashMap<String, Scoreboard>();
	Map<String, Scoreboard> remainingBoard = new HashMap<String, Scoreboard>();
	Scoreboard clearBoard;
	Map<String, Objective> objectiveQueue = new HashMap<String, Objective>();
	Map<String, Objective> objectiveRemaining = new HashMap<String, Objective>();
	public int gameMin;
	int gameStart30Sec;
	int gameQueueTime;
	public HashMap<Entity, String> tntActive = new HashMap<Entity, String>();
	public HashMap<String, String> selectedKit = new HashMap<String, String>();

	@Override
	public void onEnable() {
		Log.info("----------- [ TNT Wars ] -----------");
		Log.info(" ");
		Log.info("               Enabled!             ");
		Log.info(" ");
		Log.info("------------------------------------");
		setupChat();
		LF = new LoadFunctions(this);
		reload();
		manager = Bukkit.getScoreboardManager();
		clearBoard = manager.getNewScoreboard();
		cd = new CountDowns(this);
		mh = new MenuHandler(this);
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
		SC = new SignChange(this);
		BE = new BlockExplode(this);
		EDE = new EntityDamage(this);
		PDI = new PlayerDropItem(this);
		econ = new Economy(this);
		PC = new PlayerChat(this);
		Bukkit.getPluginManager().registerEvents(mh, this);
		Bukkit.getPluginManager().registerEvents(PI, this);
		Bukkit.getPluginManager().registerEvents(PLAJ, this);
		Bukkit.getPluginManager().registerEvents(EE, this);
		Bukkit.getPluginManager().registerEvents(EDBE, this);
		Bukkit.getPluginManager().registerEvents(BP, this);
		Bukkit.getPluginManager().registerEvents(PD, this);
		Bukkit.getPluginManager().registerEvents(MCH, this);
		Bukkit.getPluginManager().registerEvents(SC, this);
		Bukkit.getPluginManager().registerEvents(BE, this);
		Bukkit.getPluginManager().registerEvents(EDE, this);
		Bukkit.getPluginManager().registerEvents(PDI, this);
		Bukkit.getPluginManager().registerEvents(PC, this);
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("tw").setExecutor(CH);
		getCommand("mod").setExecutor(CH);
		getCommand("mods").setExecutor(CH);
		getCommand("admin").setExecutor(CH);
		getCommand("admins").setExecutor(CH);
		getCommand("owner").setExecutor(CH);
		getCommand("owners").setExecutor(CH);
		reloadTwo();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new HighGiveRate(this), 0, 20 * 3);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new StickTNTCheck(this), 0, 2);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new LowGiveRate(this), 0, 20 * 5);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SignUpdater(this), 0, 20 * 2);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SignUpdater2(this), 0, 20 * 2);
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
		int checkNum = 1;
		boolean check = true;
		while(check){
			if(signsConfig.contains(String.valueOf(checkNum))){
				signsConfig.set(String.valueOf(checkNum), null);
				checkNum++;
			}else{
				check = false;
			}
		}
		int i = 0;
		for(String loc: signs){
			i+=1;
			signsConfig.set(String.valueOf(i), loc);
			try {
				signsConfig.save(signsFile);
			} catch (IOException e) {
			}
		}
		inGame.clear();
		gameQueue.clear();
		allInGame.clear();
		allQueue.clear();
		arenas.clear();
		selectedKit.clear();
	}

	public void reload(){
		this.configFile = new File("plugins/TNTWars/config.yml");
		this.config = YamlConfiguration.loadConfiguration(configFile);
		this.perksFile = new File("plugins/TNTWars/DonorPerksDatabase.yml");
		this.perksConfig = YamlConfiguration.loadConfiguration(perksFile);
		this.signsFile = new File("plugins/TNTWars/Signs.yml");
		this.signsConfig = YamlConfiguration.loadConfiguration(signsFile);
		this.playerFile = new File("plugins/TNTWars/PlayerData.yml");
		this.playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		this.econFile = new File("plugins/TNTWars/EconData.yml");
		this.econConfig = YamlConfiguration.loadConfiguration(econFile);
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
		if (!(signsFile.exists())) {
			signsConfig.options().copyDefaults(true);
			try {
				signsConfig.save(signsFile);
			} catch (IOException e) {
			}
			Log.info("Signs File Didn't Exist ----");
		}
		if (!(playerFile.exists())) {
			playerConfig.options().copyDefaults(true);
			try {
				playerConfig.save(playerFile);
			} catch (IOException e) {
			}
			Log.info("Player File Didn't Exist ----");
		}
		if (!(econFile.exists())) {
			econConfig.options().copyDefaults(true);
			try {
				econConfig.save(econFile);
			} catch (IOException e) {
			}
			Log.info("Player File Didn't Exist ----");
		}
		gameMin = config.getInt("Game-Min");
		gameStart30Sec = config.getInt("Game-Start-30Sec");
		gameQueueTime = (config.getInt("Game-Queue-Time") * 1200);
		hub = new Location(Bukkit.getWorld(config.getString("Hub.world")), 
				config.getDouble("Hub.x"), 
				config.getDouble("Hub.y"), 
				config.getDouble("Hub.z"),
				config.getInt("Hub.yaw"), 0);
		Boolean check = true;
		int checkNum = 1;
		while(check){
			if(config.contains("Arenas." + checkNum)){
				String mapName = config.getString("Arenas." + checkNum + ".Name");
				arenas.put(checkNum, config.getString("Arenas." + checkNum + ".Name"));
				gameMax.put(mapName, config.getInt("Arenas." + checkNum + ".Game-Max"));
				perTeam.put(mapName, config.getInt("Arenas." + checkNum + ".Players-Per-Team"));
				teamAmount.put(mapName, (gameMax.get(mapName) / perTeam.get(mapName)));
				arenaIcons.put(mapName, Material.valueOf(config.getString("Arenas." + checkNum + ".Icon")));
				if(!config.contains("Arenas."+ checkNum + ".spectate.world")){
					config.set("Arenas."+ checkNum + ".spectate.world", "world");
					config.set("Arenas."+ checkNum + ".spectate.x", 0);
					config.set("Arenas."+ checkNum + ".spectate.y", 100);
					config.set("Arenas."+ checkNum + ".spectate.z", 0);
				}
				if(!config.contains("Arenas."+ checkNum + ".lobby.world")){
					config.set("Arenas."+ checkNum + ".lobby.world", "world");
					config.set("Arenas."+ checkNum + ".lobby.x", 0);
					config.set("Arenas."+ checkNum + ".lobby.y", 100);
					config.set("Arenas."+ checkNum + ".lobby.z", 0);
				}
				try {
					config.save(configFile);
				} catch (IOException e) {
				}
				lobby.put(mapName, new Location(Bukkit.getWorld(config.getString("Arenas."+ checkNum + ".lobby.world")), 
						config.getDouble("Arenas."+ checkNum + ".lobby.x"), 
						config.getDouble("Arenas."+ checkNum + ".lobby.y"), 
						config.getDouble("Arenas."+ checkNum + ".lobby.z")));
				spectate.put(mapName, new Location(Bukkit.getWorld(config.getString("Arenas."+ checkNum + ".spectate.world")), 
						config.getDouble("Arenas."+ checkNum + ".spectate.x"), 
						config.getDouble("Arenas."+ checkNum + ".spectate.y"), 
						config.getDouble("Arenas."+ checkNum + ".spectate.z")));
				spawnpoint.put(mapName, new HashMap<Integer, Location>());
				teams.put(mapName, new HashMap<Integer, List<String>>());
				teamsIcons.put(mapName, new HashMap<Integer, Material>());
				checkNum++;
			}else{
				check = false;
			}
		}
		for(int index: arenas.keySet()){
			String mapName = arenas.get(index);
			for(int i = 1; i <= teamAmount.get(mapName); i++){
				teams.get(mapName).put(i, new ArrayList<String>());
				teamsIcons.get(mapName).put(i, Material.getMaterial(config.getString("Arenas."+ index + "." + i + ".Icon")));
				teams.get(mapName).get(i).clear();
				if(!config.contains("Arenas."+ index + "." + i + ".world")){
					config.set("Arenas."+ index + "." + i + ".Icon", "PAPER");
					config.set("Arenas."+ index + "." + i + ".Damage", 0);
					config.set("Arenas."+ index + "." + i + ".world", "world");
					config.set("Arenas."+ index + "." + i + ".x", 0);
					config.set("Arenas."+ index + "." + i + ".y", 100);
					config.set("Arenas."+ index + "." + i + ".z", 0);
					try {
						config.save(configFile);
					} catch (IOException e) {
					}
				}
				spawnpoint.get(mapName).put(i, new Location(Bukkit.getWorld(config.getString("Arenas."+ index + "." + i + ".world")),
						config.getDouble("Arenas."+ index + "." + i + ".x"),
						config.getDouble("Arenas."+ index + "." + i + ".y"),
						config.getDouble("Arenas."+ index + "." + i + ".z")));
			}
		}
		checkNum = 1;
		check = true;
		while(check){
			if(signsConfig.contains(String.valueOf(checkNum))){
				signs.add(signsConfig.getString(String.valueOf(checkNum)));
				Sign s = (Sign) ED.DecodeSignPos(signsConfig.getString(String.valueOf(checkNum))).getBlock().getState();
				String map = ChatColor.stripColor(s.getLine(1));
				SU.mapStatus.put(map, "Waiting");
				checkNum++;
			}else{
				check = false;
			}
		}
	}

	public void reloadTwo(){
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
			cd.starting1.put(map, false);
			cd.starting2.put(map, false);
			cd.active.put(map, false);
			cd.canKit.put(map, true);
			cd.tenSec.put(map, false);
		}
	}

	public void setupChat()
	{
		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
		if (chatProvider != null) {
			chat = (Chat)chatProvider.getProvider();
		}
	}
}