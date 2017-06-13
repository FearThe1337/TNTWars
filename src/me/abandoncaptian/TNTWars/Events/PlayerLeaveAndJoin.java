package me.abandoncaptian.TNTWars.Events;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.abandoncaptian.TNTWars.CountDowns;
import me.abandoncaptian.TNTWars.GameFunc;
import me.abandoncaptian.TNTWars.InvAndExp;
import me.abandoncaptian.TNTWars.Main;

public class PlayerLeaveAndJoin implements Listener {
	Main pl;
	InvAndExp IAE;
	CountDowns cd;
	GameFunc GF;

	public PlayerLeaveAndJoin(Main plugin) {
		pl = plugin;
		IAE = new InvAndExp(plugin);
		GF = new GameFunc(plugin);
	}

	@EventHandler
	public void playerLeaveGame(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		if (pl.Perks.containsKey(p.getName())) {
			if (pl.Perks.get(p.getName()).containsKey("Fireworks")) {
				pl.perksConfig.set(p.getName() + ".Fireworks", pl.Perks.get(p.getName()).get("Fireworks"));
			} else {
				pl.perksConfig.set(p.getName() + ".Fireworks", false);
			}
			if (pl.Perks.get(p.getName()).containsKey("Outline")) {
				pl.perksConfig.set(p.getName() + ".Outline", pl.Perks.get(p.getName()).get("Outline"));
			} else {
				pl.perksConfig.set(p.getName() + ".Outline", false);
			}
			if (pl.Perks.get(p.getName()).containsKey("CName")) {
				pl.perksConfig.set(p.getName() + ".CName", pl.Perks.get(p.getName()).get("CName"));
			} else {
				pl.perksConfig.set(p.getName() + ".Outline", false);
			}
			pl.Perks.remove(p.getName());
			try {
				pl.perksConfig.save(pl.perksFile);
			} catch (IOException e1) {
			}
		}
		if(pl.fwSettings.containsKey(p.getName())){
			pl.perksConfig.set(p.getName() + ".FWSettings", pl.fwSettings.get(p.getName()));
			try {
				pl.perksConfig.save(pl.perksFile);
			} catch (IOException e1) {
			}
		}
		if(pl.cName.containsKey(p.getName())){
			pl.perksConfig.set(p.getName() + ".Custom-Name", pl.cName.get(p.getName()));

			if(pl.cNameColor.containsKey(p.getName())){
				if(pl.cNameColor.get(p.getName()) == ChatColor.RED)pl.perksConfig.set(p.getName() + ".Custom-Name-Color", "RED");
				if(pl.cNameColor.get(p.getName()) == ChatColor.BLUE)pl.perksConfig.set(p.getName() + ".Custom-Name-Color", "BLUE");
				if(pl.cNameColor.get(p.getName()) == ChatColor.GREEN)pl.perksConfig.set(p.getName() + ".Custom-Name-Color", "GREEN");
				if(pl.cNameColor.get(p.getName()) == ChatColor.WHITE)pl.perksConfig.set(p.getName() + ".Custom-Name-Color", "WHITE");
				if(pl.cNameColor.get(p.getName()) == ChatColor.DARK_PURPLE)pl.perksConfig.set(p.getName() + ".Custom-Name-Color", "DARK_PURPLE");
			}else pl.perksConfig.set(p.getName() + ".Custom-Name-Color", "WHITE");
			try {
				pl.perksConfig.save(pl.perksFile);
			} catch (IOException e1) {
			}
		}
		GF.gameLeave(p.getName());
	}

	@EventHandler
	public void gameConnect(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (pl.perksConfig.contains(p.getName())) {
			pl.Perks.put(p.getName(), new HashMap<String, Boolean>());
			pl.Perks.get(p.getName()).put("Fireworks", pl.perksConfig.getBoolean(p.getName() + ".Fireworks"));
			pl.Perks.get(p.getName()).put("Outline", pl.perksConfig.getBoolean(p.getName() + ".Outline"));
			pl.Perks.get(p.getName()).put("CName", pl.perksConfig.getBoolean(p.getName() + ".CName"));
			if(pl.perksConfig.contains(p.getName() + ".FWSettings")){
				pl.fwSettings.put(p.getName(), pl.perksConfig.getString(p.getName() + ".FWSettings"));
			}
			if(pl.perksConfig.contains(p.getName() + ".Custom-Name")){
				pl.cName.put(p.getName(), pl.perksConfig.getString(p.getName() + ".Custom-Name"));
				pl.cNameColor.put(p.getName(), ChatColor.valueOf(pl.perksConfig.getString(p.getName() + ".Custom-Name-Color")));
			}
		}
		if (pl.tpBack.containsKey(p.getName())) {
			p.teleport(pl.tpBack.get(p.getName()));
			pl.tpBack.remove(p.getName());
		}

		if (IAE.hasSwitchedInv(e.getPlayer()))
			IAE.InventorySwitch(e.getPlayer());
		if (IAE.hasSwitchedExp(e.getPlayer()))
			IAE.ExpSwitch(e.getPlayer().getName());
	}
}