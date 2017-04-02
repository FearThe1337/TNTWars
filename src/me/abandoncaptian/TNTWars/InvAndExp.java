package me.abandoncaptian.TNTWars;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InvAndExp {
	Main pl;
	Map<String, ItemStack[]> extraInv;
	Map<String, Integer> savedXPL = new HashMap<String, Integer>();
	Map<String, Float> savedXP = new HashMap<String, Float>();
	public InvAndExp(Main plugin) {
		pl = plugin;
		this.extraInv = new HashMap<>();
	}
	
	public void ExpSwitch(String pName) {
		boolean found = false;
		if(savedXPL.size() > 0){
			for(String name : savedXPL.keySet()){
				if(name.equals(pName)){
					found = true;
					Player p = Bukkit.getPlayer(name);
					p.setLevel(savedXPL.get(name));
					savedXPL.remove(name);
					break;
				}
			}
		}
		if(savedXP.size() > 0){
			for(String name : savedXP.keySet()){
				if(name.equals(pName)){
					found = true;
					Player p = Bukkit.getPlayer(name);
					p.setExp(savedXP.get(name));
					savedXP.remove(name);
					break;
				}
			}
		}
		if(!found){
			Player p = Bukkit.getPlayer(pName);
			int expL = p.getLevel();
			Float exp = p.getExp();
			if(expL > 0){
				savedXPL.put(pName, expL);
			}
			if(exp > 0){
				savedXP.put(pName, exp);
			}
			p.setLevel(0);
			p.setExp(0);
		}
	}

	public void InventorySwitch(Player player) {
		boolean found = false;
		if(extraInv.size() > 0){
			for (String uuid : extraInv.keySet()) {
				if (uuid.equals(player.getUniqueId().toString())) {
					found = true;
					ItemStack[] items = extraInv.get(uuid);
					extraInv.remove(player.getUniqueId().toString());
					player.getInventory().clear();
					player.getInventory().setArmorContents((ItemStack[]) ArrayUtils.subarray(items, 0, 4));
					player.getInventory().setContents((ItemStack[]) ArrayUtils.subarray(items, 4, items.length));
					break;
				}
			}
		}
		if (!found) {
			ItemStack[] curr = (ItemStack[]) ArrayUtils.addAll(
					player.getInventory().getArmorContents(),
					player.getInventory().getContents()
					);
			extraInv.put(player.getUniqueId().toString(), curr);
			player.getInventory().clear();
		}
	}

	public boolean hasSwitchedInv(Player p) {
		return extraInv.keySet().contains(p.getUniqueId().toString());
	}

	public boolean hasSwitchedExp(Player p) {
		if(savedXP.keySet().contains(p.getName()) || savedXPL.keySet().contains(p.getName())){
			return true;
		}else return false;
	}
	
}
