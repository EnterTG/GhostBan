package com.windskull.GhostBan.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.windskull.GhostBan.GhostBanCore;
import com.windskull.GhostBan.PlayerGhostBan;

public class DefaultCommand implements CommandExecutor
{

	public DefaultCommand(GhostBanCore core)
	{
		super();
		this.core = core;
	}

	private GhostBanCore core;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(args.length > 1)
		{
			Player p = Bukkit.getPlayer(args[1]);
			if(p == null) return false;
			switch (args[0])
			{
				case "ban":
					if(!core.banedPlayer.containsKey(p))
					{
						PlayerGhostBan pgb = new PlayerGhostBan(p);
						core.banedPlayer.put(p, pgb);
					}
					return true;
				case "unban":
					if(core.banedPlayer.containsKey(p))
					{
						PlayerGhostBan pgb = core.banedPlayer.get(p);
						pgb.unbanPlayer();
						core.banedPlayer.remove(p);
					}
					return true;
				default:
					return false;
			}
		}
		return false;
	}

}
