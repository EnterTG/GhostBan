package com.windskull.GhostBan;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.windskull.GhostBan.Commands.DefaultCommand;
import com.windskull.GhostBan.Listeners.InventoryActionListener;
import com.windskull.GhostBan.Listeners.PlayerInteractionsListener;

public class GhostBanCore extends JavaPlugin implements Listener
{

	public static boolean USE_SPECTATOR_MODE = true;
	
	
	private static GhostBanCore core;
	
	public static GhostBanCore getInstance()
	{
		return core;
	}
	
	
	
	
	
	@Override
	public void onEnable()
	{
		core = this;
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new InventoryActionListener(), this);
		this.getCommand("ghostban").setExecutor(new DefaultCommand(this));
		if(!USE_SPECTATOR_MODE)
			Bukkit.getPluginManager().registerEvents(new PlayerInteractionsListener(this), this);
	}
	
	public ConcurrentHashMap<Player,PlayerGhostBan> banedPlayer = new ConcurrentHashMap<Player, PlayerGhostBan>();
	
	public void banPlayer(Player p)
	{
		PlayerGhostBan ban = new PlayerGhostBan(p);
		ban.runTaskTimer(this, 0, 20);
		banedPlayer.put(p, ban);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		banPlayer(e.getPlayer());
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player player = e.getPlayer();
		if(banedPlayer.containsKey(player))
			banedPlayer.keys().asIterator().forEachRemaining(p -> {player.hidePlayer(this, p); p.hidePlayer(this,player);});
	}
}
