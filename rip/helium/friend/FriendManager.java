package rip.helium.friend;

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

import net.minecraft.util.StringUtils;
import rip.helium.ClientSupport;

public class FriendManager implements ClientSupport {
	
	private ArrayList<Friend> friends = new ArrayList<Friend>();
	
	public ArrayList<Friend> getFriendsList() {
		return friends;
	}
	
	public void loadConfig(Gson gson) {
		File dir = new File(mc.mcDataDir + File.separator + mc.hackedClient.getName() + File.separator + "friends");
		if(dir.exists()) {
			File[] directoryListing = dir.listFiles();
			for(File f: directoryListing) {
				try (FileReader reader = new FileReader(f)) {
					Map<String, Object> map = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());
					Friend friend = new Friend((String)map.get("name"), (String)map.get("alias"));
					this.friends.add(friend);
				} catch (JsonSyntaxException e) {
					
				} catch (JsonIOException e) {
					
				} catch (FileNotFoundException e) {
					
				} catch (IOException e1) {
					
				} catch (NullPointerException e) {
					
				}
			}
		}
	}
	
	public void saveConfig(Gson gson) {
		for(Friend f: this.friends) {
			File file = new File(mc.mcDataDir + File.separator + mc.hackedClient.getName() + File.separator + "friends" + File.separator + f.getName() + ".json");
			if(!file.exists()) {
				new File(mc.mcDataDir + File.separator + mc.hackedClient.getName() + File.separator + "friends").mkdirs();
				try {
					file.createNewFile();
				} catch (IOException e) {
				}
			}
			try (FileWriter writer = new FileWriter(file)) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("name", f.getName());
	            map.put("alias", f.getAlias());
	            gson.toJson(map, writer);
	        } catch (IOException e) {
	        }
		}
	}
	
	public void addFriend(String name, String alias) {
		getFriendsList().add(new Friend(name, alias));
		saveConfig(mc.hackedClient.getGson());
	}
	
	public void removeFriend(String name) {
		for(Friend friend: getFriendsList()) {
			if(friend.name.equalsIgnoreCase(name)) {
				getFriendsList().remove(friend);
				File file = new File(mc.mcDataDir + File.separator + "BDSM" + File.separator + "friends" + File.separator + friend.getName() + ".json");
				if(file.exists()) {
					file.delete();
				}
				break;
			}
		}
		saveConfig(mc.hackedClient.getGson());
	}
	
	public String getAliasName(String name) {
		String alias = "";
		for(Friend friend: getFriendsList()) {
			if(friend.name.equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
				alias = friend.alias;
				break;
			}
		}
		return alias;
	}
	
	public boolean isFriend(String name) {
		boolean isFriend = false;
		for (Friend friend : friends) {
			if (friend.name.equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
				isFriend = true;
				break;
			}
		}
		return isFriend;
	}
	
}
