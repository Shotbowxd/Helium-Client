package rip.helium.setting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import rip.helium.ClientSupport;
import rip.helium.module.Module;

public class SettingManager implements ClientSupport {
	
	private ArrayList<Setting> settings;
	
	public SettingManager(){
		this.settings = new ArrayList<Setting>();
	}
	
	public void loadConfig(Gson gson) {
		for(Setting s: this.settings) {
			File file = new File(mc.mcDataDir + File.separator + mc.hackedClient.getName() + File.separator + "settings" + File.separator + s.getParentMod().getName() + File.separator + s.getName() + ".json");
			try (FileReader reader = new FileReader(file)) {
				Map<String, Object> map = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());
				if((boolean)map.get("isCheck")) {
					s.setValBoolean((boolean)map.get("value"));
				} else if((boolean)map.get("isSlider")) {
					s.setValDouble((double)map.get("value"));
				} else if((boolean)map.get("isCombo")) {
					s.setValString((String)map.get("value"));
				}
			} catch (JsonSyntaxException e) {
				
			} catch (JsonIOException e) {
				
			} catch (FileNotFoundException e) {
				
			} catch (IOException e1) {
				
			} catch (NullPointerException e) {
				
			}
		}
	}
	
	public void saveConfig (Gson gson) {
		for(Setting s: this.settings) {
			File file = new File(mc.mcDataDir + File.separator + mc.hackedClient.getName() + File.separator + "settings" + File.separator + s.getParentMod().getName() + File.separator + s.getName() + ".json");
			if(!file.exists()) {
				new File(mc.mcDataDir + File.separator + mc.hackedClient.getName() + File.separator + "settings" + File.separator + s.getParentMod().getName()).mkdirs();
				try {
					file.createNewFile();
				} catch (IOException e) {
				}
			}
			try (FileWriter writer = new FileWriter(file)) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("isCheck", s.isCheck());
	            map.put("isSlider", s.isSlider());
	            map.put("isCombo", s.isCombo());
	            if(s.isCombo()) {
	            	map.put("value", s.getValString());
	            } else if(s.isCheck()) {
	            	map.put("value", s.getValBoolean());
	            } else if(s.isSlider()) {
	            	map.put("value", s.getValDouble());
	            }
	            gson.toJson(map, writer);
	        } catch (IOException e) {
	        }
		}
	}
	
	public void addSetting(Setting in){
		this.settings.add(in);
	}
	
	public ArrayList<Setting> getSettings(){
		return this.settings;
	}
	
	public ArrayList<Setting> getSettingsByMod(Module mod){
		ArrayList<Setting> out = new ArrayList<Setting>();
		for(Setting s : getSettings()){
			if(s.getParentMod().equals(mod)){
				out.add(s);
			}
		}
		if(out.isEmpty()){
			return null;
		}
		return out;
	}
	
	public Setting getSettingByName(String name, Module mod){
		for(Setting set : getSettings()){
			if(set.getName().equalsIgnoreCase(name) && set.getParentMod() == mod){
				return set;
			}
		}
		return null;
	}

}