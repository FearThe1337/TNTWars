package me.abandoncaptian.TNTWars.Events;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitTask;

import me.abandoncaptian.TNTWars.CountDowns;
import me.abandoncaptian.TNTWars.InvAndExp;
import me.abandoncaptian.TNTWars.KitHandler;
import me.abandoncaptian.TNTWars.Main;
import net.minecraft.server.v1_11_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_11_R1.PacketPlayInClientCommand.EnumClientCommand;

public class PlayerDeath implements Listener {
	Main pl;
	CountDowns cd;
	InvAndExp IAE;
	KitHandler kh;
	BukkitTask deathTest;
	boolean isDead;

	public PlayerDeath(Main plugin) {
		pl = plugin;
		kh = new KitHandler(plugin);
		IAE = new InvAndExp(plugin);
	}

	@EventHandler
	public void gameDeath(PlayerDeathEvent e) {
		if (pl.cd.active) {
			if (pl.inGame.contains(e.getEntity().getName())) {
				Player p = (Player) e.getEntity();
				PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
				CraftPlayer craftPlayer = (CraftPlayer) p;
				craftPlayer.getHandle().playerConnection.a(packet);
				Bukkit.getScheduler().runTaskLater(pl, new Runnable() {

					@Override
					public void run() {
						p.sendTitle("§7§l[§c§lTNT Wars§7§l]", "§bYou were eliminated", 0, 60, 0);
						p.getInventory().clear();
						pl.IAE.InventorySwitch(p);
						pl.IAE.ExpSwitch(p.getName());
					}
				}, 5);
				e.getDrops().clear();
				pl.dead.add(p.getName());
				pl.spec.add(p.getName());
				e.setDeathMessage(null);
				p.setHealthScale(20);
				p.setCanPickupItems(true);
				pl.inGame.remove(p.getName());
				pl.UpdateBoard(true, p.getName());
				int game = pl.inGame.size();
				e.getEntity().teleport(pl.spawnpoint);
				if (game == 1) {
					String winner = pl.inGame.get(0);
					Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
						@Override
						public void run() {
							Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] §b§l" + winner + " §6has won!");
						}
					}, 2);
					for (String name : pl.spec) {
						Bukkit.getPlayer(name).sendTitle("§7§l[§c§lTNT Wars§7§l]",
								"§b" + pl.inGame.get(0) + " has won!", 0, 120, 0);
						Bukkit.getPlayer(name).teleport(pl.tpBack.get(name));
						pl.tpBack.remove(name);
					}
					Bukkit.getPlayer(winner).setHealth(20);
					Bukkit.getPlayer(winner).setFoodLevel(20);
					Bukkit.getPlayer(winner).getInventory().clear();
					Bukkit.getPlayer(winner).closeInventory();
					pl.UpdateBoard(true, winner);
					pl.IAE.InventorySwitch(Bukkit.getPlayer(winner));
					pl.IAE.ExpSwitch(winner);
					Bukkit.getPlayer(winner).teleport(pl.tpBack.get(Bukkit.getPlayer(winner).getName()));
					pl.tpBack.remove(Bukkit.getPlayer(winner).getName());
					Bukkit.getPlayer(winner).setHealthScale(20);
					Bukkit.getPlayer(winner).setCanPickupItems(true);
					Bukkit.getPlayer(winner).sendTitle("§7§l[§c§lTNT Wars§7§l]", "§bYou Won", 0, 120, 0);
					Bukkit.getPlayer(winner).getActivePotionEffects().clear();
					pl.kh.initInv();
					pl.inGame.clear();
					pl.cd.active = false;
					pl.cd.canKit = true;
					pl.cd.starting1 = false;
					pl.cd.starting2 = false;
					pl.spec.clear();
					pl.selectedKit.clear();
					kh.initInv();
				}
			}
		}
	}
}
