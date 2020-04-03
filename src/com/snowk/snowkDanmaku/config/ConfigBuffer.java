package com.snowk.snowkDanmaku.config;

import com.snowk.snowkDanmaku.SnowkDanmaku;

public class ConfigBuffer {
	public static boolean enableCooldown = SnowkDanmaku.snowkPlugin.getConfig().getBoolean("cooldown");
	public static long Delay = SnowkDanmaku.snowkPlugin.getConfig().getLong("cd_timeSec");
}
