package com.windskull.GhostBan.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerGhostBanEvent extends Event implements Cancellable
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
	private boolean isCanceled;
	
	
	public PlayerGhostBanEvent(Player player)
	{
		super();
		this.player = player;
	}
	
	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	@Override
	public boolean isCancelled() 
	{
		return isCanceled;
	}


	@Override
	public void setCancelled(boolean cancel) 
	{
		isCanceled = cancel;
	}
}
