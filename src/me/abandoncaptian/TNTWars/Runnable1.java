package me.abandoncaptian.TNTWars;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Runnable1 implements Runnable {
	Main pl;
	CountDowns cd;
	LoadFunctions LF;
	int max = 2;
	boolean had = false;
	boolean canGive = true;
	boolean correctKit = true;
	public Runnable1(Main plugin){
		this.pl = plugin;
		cd = new CountDowns(plugin);
		LF = new LoadFunctions(plugin);
	}

	@Override
	public void run() {
		if(pl.cd.active){
			for(String name : pl.inGame){
				this.had = false;
				if(pl.selectedKit.containsKey(name)){
					for(String kit : LF.kitsListLowRate){
						if(pl.selectedKit.get(name) == kit){
							correctKit = false;
							break;
						}else correctKit = true;
					}
					if(correctKit){
						if(pl.selectedKit.get(name) == "Suicide Bomber")this.max = 1;
						else this.max = 2;
						Player p = Bukkit.getPlayer(name);
						ItemStack tnt = new ItemStack(Material.TNT);
						ItemMeta meta = tnt.getItemMeta();
						List<String> lore = new ArrayList<String>();
						meta.setDisplayName("§6§lThrowable §c§lTNT");
						lore.add("§bRight click to throw");
						lore.add("§bMade By: abandoncaptian");
						meta.setLore(lore);
						tnt.setItemMeta(meta);
						for(int index = 0; index < p.getInventory().getSize(); index++){
							ItemStack item = p.getInventory().getItem(index);
							if(item != null){	
								if(item.getItemMeta().getDisplayName() == tnt.getItemMeta().getDisplayName()){
									this.had = true;
									if(item.getAmount() < this.max){
										tnt.setAmount(1);
										p.getInventory().addItem(tnt);
										break;
									}
								}
							}
						}if(!had){
							tnt.setAmount(1);
							p.getInventory().addItem(tnt);
						}
					}else{
						continue;
					}
				}
			}
		}
	}
}
