package com.windskull.GhostBan.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.windskull.GhostBan.PlayerGhostBan;

public class PlayerGhostUnbanEvent extends Event implements Cancellable
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
	
	private PlayerGhostBan playerGhostBan;
	private boolean isCanceled;
	
	
	public PlayerGhostUnbanEvent(PlayerGhostBan player)
	{
		super();
		this.playerGhostBan = player;
	}
	
	public PlayerGhostBan getPlayerGhostBan()
	{
		return playerGhostBan;
	}

	public void setPlayerGhostBan(PlayerGhostBan player)
	{
		this.playerGhostBan = player;
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
