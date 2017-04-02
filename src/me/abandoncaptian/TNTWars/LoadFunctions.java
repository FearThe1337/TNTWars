package me.abandoncaptian.TNTWars;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

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
	public List<PotionEffectType> potions = new ArrayList<PotionEffectType>();
	public LoadFunctions(Main Plugin){
		pl = Plugin;
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
}
