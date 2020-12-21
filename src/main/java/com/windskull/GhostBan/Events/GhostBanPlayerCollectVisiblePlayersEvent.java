package com.windskull.GhostBan.Events;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GhostBanPlayerCollectVisiblePlayersEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
	
	private Player player;
	private ArrayList<Player> onlineVisiblePlayers = new ArrayList<>();
	private ArrayList<OfflinePlayer> offlineVisiblePlayers = new ArrayList<>();
	
	
	public GhostBanPlayerCollectVisiblePlayersEvent(Player player)
	{
		super();
		this.player = player;
	}
	
	
	
	public boolean addOnlineVisiblePlayer(Player p)
	{
		return onlineVisiblePlayers.add(p);
	}
	
	public boolean removeOnlineVisiblePlayer(Player p)
	{
		return onlineVisiblePlayers.remove(p);
	}

	public boolean addOfflineVisiblePlayer(Player p)
	{
		return offlineVisiblePlayers.add(p);
	}
	
	public boolean removeOfflineVisiblePlayer(Player p)
	{
		return offlineVisiblePlayers.remove(p);
	}
	
	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public ArrayList<OfflinePlayer> getOfflineVisiblePlayers()
	{
		return offlineVisiblePlayers;
	}

	public void setOfflineVisiblePlayers(ArrayList<OfflinePlayer> offlineVisiblePlayers)
	{
		this.offlineVisiblePlayers = offlineVisiblePlayers;
	}

	public ArrayList<Player> getOnlineVisiblePlayers()
	{
		return onlineVisiblePlayers;//Arrays.asList(onlineVisiblePlayers.toArray(new Player[onlineVisiblePlayers.size()]));
	}

	public void setOnlineVisiblePlayers(ArrayList<Player> onlineVisiblePlayers)
	{
		this.onlineVisiblePlayers = onlineVisiblePlayers;
	}
}
