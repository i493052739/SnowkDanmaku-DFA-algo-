package com.snowk.snowkDanmaku;

public class Message {
	public static String msg_Reject = SnowkDanmaku.snowkPlugin.getConfig().getString("Msg_Reject").replace("&", "��");
	public static String msg_Sent = SnowkDanmaku.snowkPlugin.getConfig().getString("Msg_Sent").replace("&", "��");
	public static String msg_Cancel = SnowkDanmaku.snowkPlugin.getConfig().getString("Msg_Cancel").replace("&", "��");
	public static String msg_CannotSend = SnowkDanmaku.snowkPlugin.getConfig().getString("Msg_CannotSend").replace("&", "��");
	public static String msg_StartToSend = SnowkDanmaku.snowkPlugin.getConfig().getString("Msg_StartToSend").replace("&", "��");
	public static String msg_Reload = SnowkDanmaku.snowkPlugin.getConfig().getString("Msg_Reload").replace("&", "��");
	public static String msg_NoPerm = SnowkDanmaku.snowkPlugin.getConfig().getString("Msg_NoPerm").replace("&", "��");
	public static String msg_Cd = SnowkDanmaku.snowkPlugin.getConfig().getString("Msg_Cd").replace("&", "��");
}
