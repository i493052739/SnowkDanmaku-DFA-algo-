package com.snowk.snowkDanmaku;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.snowk.snowkDanmaku.listener.PlayerDanmaku;


public class SnowkDanmaku extends JavaPlugin{
	
	public static SnowkDanmaku snowkPlugin;

	
    @Override
    public void onEnable() {
    	snowkPlugin = this;
    	getLogger().info("SnowkDanmaku successfully enabled! - By:Bear");
    	getLogger().info("See more information at: https://github.com/i493052739");
    	registerListeners();
    	
        if (!new File("./plugins/SnowkDanmaku/config.yml").exists()) {
            this.saveDefaultConfig();
            this.getLogger().info("SnowkDanmaku successfully created config file");
        }
    }
    
    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerDanmaku(), this);
    }

    @Override
    public void onDisable() {
    	getLogger().info("SnowkDanmaku successfully disabled!");
    	
    }
    	
    public boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] args) {
    	if (!(commandSender instanceof Player)) {
        	snowkPlugin.getLogger().info(Message.msg_CannotSend);
        	return true;
        }
    	final Player p = (Player)commandSender;
    	if (label.equalsIgnoreCase("fadanmu") || label.equalsIgnoreCase("fdm") || label.equalsIgnoreCase("fadm")) {
    		if (args.length == 0 && p.hasPermission("snowk.danmaku.use")) {
	        	p.sendMessage(Message.msg_StartToSend);
	        	PlayerDanmaku.playerMatcher.add(p.getName());
	        	return true;
	        } else if (args.length == 0 && !(p.hasPermission("snowk.danmaku.use"))) {
	        	p.sendMessage(Message.msg_NoPerm);
	        	return true;
	        }
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (p.hasPermission("snowk.danmaku.admin") || p.isOp()) {
                	this.reloadConfig();
                	p.sendMessage(Message.msg_Reload);
                } else {
                	p.sendMessage(Message.msg_NoPerm);
                }
                return true;
            }
    	}
    	return false;
    }
}
