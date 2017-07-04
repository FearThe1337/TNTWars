package me.abandoncaptian.TNTWars;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class Economy {
	Main pl;
	public Map<String, Integer> balance = new HashMap<String, Integer>();
	public Economy(Main plugin) {
		pl = plugin;
	}

	public Integer getBalance(Player p){
		if(!balance.containsKey(p.getName()))setupBalance(p);
		return balance.get(p.getName());
	}

	public void depositBalance(Player p, int i){
		balance.put(p.getName(), (balance.get(p.getName()) + i));
	}

	public void withdrawBalance(Player p, int i){
		balance.put(p.getName(), (balance.get(p.getName()) - i));
	}

	public void setBalance(Player p, int i){
		balance.put(p.getName(), i);
	}

	public boolean hasBalance(Player p, int i){
		if(balance.get(p.getName()) >= i)return true;
		else return false;
	}

	public void resetBalance(Player p){
		balance.put(p.getName(), 0);
	}

	public boolean hasBank(Player p){
		return balance.containsKey(p.getName());
	}

	public void setupBalance(Player p){
		balance.put(p.getName(), 0);
	}

	public void saveBalance(Player p){
		if(!hasBank(p))setupBalance(p);
		
		pl.econConfig.set(p.getUniqueId() + ".Balance", getBalance(p));
		
		try {
			pl.econConfig.save(pl.econFile);
		} catch (IOException e) {}
	}

	public void pullBalance(Player p){
		if(pl.econConfig.contains(p.getUniqueId() + ".Balance")){
			setBalance(p, pl.econConfig.getInt(p.getUniqueId() + ".Balance"));
		}else{
			saveBalance(p);
		}
	}
}
