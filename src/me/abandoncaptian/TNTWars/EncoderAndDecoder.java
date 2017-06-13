package me.abandoncaptian.TNTWars;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;

public class EncoderAndDecoder {
	Main pl;
	public EncoderAndDecoder(Main plugin) {
		pl = plugin;
	}

	public Boolean decodeFWSettingsFlicker(String name){
		String id = pl.fwSettings.get(name);
		String var = Character.toString(id.charAt(0));
		if(var.contains("0")){
			return false;
		}else{
			return true;
		}
	}
	public Boolean decodeFWSettingsTrail(String name){
		String id = pl.fwSettings.get(name);
		String var = Character.toString(id.charAt(1));
		if(var.contains("0")){
			return false;
		}else{
			return true;
		}
	}
	public Type decodeFWSettingsType(String name){
		String id = pl.fwSettings.get(name);
		String var = Character.toString(id.charAt(2));
		if(var.contains("0")){
			return Type.BALL;
		}else if(var.contains("1")){
			return Type.BALL_LARGE;
		}else if(var.contains("2")){
			return Type.BURST;
		}else if(var.contains("3")){
			return Type.CREEPER;
		}else if(var.contains("4")){
			return Type.STAR;
		}else{
			return Type.BALL;
		}
	}
	public Color decodeFWSettingsColor(String name){
		String id = pl.fwSettings.get(name);
		String var = Character.toString(id.charAt(3));
		if(var.contains("0")){
			return Color.RED;
		}else if(var.contains("1")){
			return Color.BLUE;
		}else if(var.contains("2")){
			return Color.GREEN;
		}else if(var.contains("3")){
			return Color.WHITE;
		}else if(var.contains("4")){
			return Color.PURPLE;
		}else{
			return Color.WHITE;
		}
	}
	public void encodeFWSettings(String name){
		if(!pl.fwSettings.containsKey(name)){
			pl.fwSettings.put(name, "0000");
		}
	}
}
