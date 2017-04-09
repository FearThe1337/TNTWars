package me.abandoncaptian.TNTWars;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class LoadFunctions {
	Main pl;
	CountDowns cd;
	List<String> kitsListLowRate = new ArrayList<String>();
	List<String> kitsListHighRate = new ArrayList<String>();
	List<String> kitsListAll = new ArrayList<String>();
	List<Material> armorHelmet = new ArrayList<Material>();
	List<Material> armorChestplate = new ArrayList<Material>();
	List<Material> armorLegs = new ArrayList<Material>();
	List<Material> armorBoots = new ArrayList<Material>();
	
	public LoadFunctions(Main plugin){
		pl = plugin;
		kitsListLowRate.add("Sniper");
		kitsListLowRate.add("Heavy Loader");
		kitsListLowRate.add("Potion Worker");
		kitsListLowRate.add("Tank");
		kitsListLowRate.add("Doctor Who");
		kitsListLowRate.add("Hail Mary");
		kitsListLowRate.add("Storm");
		kitsListHighRate.add("Short Fuse");
		kitsListHighRate.add("Miner");
		kitsListHighRate.add("Suicide Bomber");
		kitsListHighRate.add("Glue Factory Worker");
		kitsListHighRate.add("Ender");
		kitsListHighRate.add("Boomerang");
		kitsListHighRate.add("Bribed");
		kitsListHighRate.add("Space Man");
		kitsListHighRate.add("Vampire");
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
		kitsListAll.add("Hail Mary");
		kitsListAll.add("Space Man");
		kitsListAll.add("Storm");
		kitsListAll.add("Vampire");
	}
}
