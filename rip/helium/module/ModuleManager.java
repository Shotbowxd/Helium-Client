package rip.helium.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import rip.helium.ClientSupport;
import rip.helium.event.EventManager;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.client.KeyPressEvent;
import rip.helium.module.Module.Category;
import rip.helium.module.modules.combat.AntiBot;
import rip.helium.module.modules.combat.AutoPot;
import rip.helium.module.modules.combat.Criticals;
import rip.helium.module.modules.combat.CrystalAura;
import rip.helium.module.modules.combat.FastBow;
import rip.helium.module.modules.combat.KillAura;
import rip.helium.module.modules.combat.TPAura;
import rip.helium.module.modules.combat.Targeting;
import rip.helium.module.modules.combat.Velocity;
import rip.helium.module.modules.exploits.Disabler;
import rip.helium.module.modules.exploits.ServerLagger;
import rip.helium.module.modules.misc.Announcer;
import rip.helium.module.modules.misc.AutoInventory;
import rip.helium.module.modules.misc.AutoPlay;
import rip.helium.module.modules.misc.ChestAura;
import rip.helium.module.modules.misc.ChestStealer;
import rip.helium.module.modules.misc.Spammer;
import rip.helium.module.modules.movement.Flight;
import rip.helium.module.modules.movement.Freecam;
import rip.helium.module.modules.movement.Jesus;
import rip.helium.module.modules.movement.LongJump;
import rip.helium.module.modules.movement.NoSlowdown;
import rip.helium.module.modules.movement.Phase;
import rip.helium.module.modules.movement.ScreenWalk;
import rip.helium.module.modules.movement.Speed;
import rip.helium.module.modules.movement.Sprint;
import rip.helium.module.modules.movement.Step;
import rip.helium.module.modules.movement.TargetStrafe;
import rip.helium.module.modules.movement.Teleport;
import rip.helium.module.modules.player.AutoEat;
import rip.helium.module.modules.player.FastUse;
import rip.helium.module.modules.player.Ghost;
import rip.helium.module.modules.player.GoodHack;
import rip.helium.module.modules.player.NoFall;
import rip.helium.module.modules.player.NoRotate;
import rip.helium.module.modules.player.Reach;
import rip.helium.module.modules.player.Regen;
import rip.helium.module.modules.render.Animations;
import rip.helium.module.modules.render.Brightness;
import rip.helium.module.modules.render.Capes;
import rip.helium.module.modules.render.Chams;
import rip.helium.module.modules.render.ClickGUI;
import rip.helium.module.modules.render.Colors;
import rip.helium.module.modules.render.CustomChat;
import rip.helium.module.modules.render.CustomViewmodel;
import rip.helium.module.modules.render.HUD;
import rip.helium.module.modules.render.LivingESP;
import rip.helium.module.modules.render.NameTags;
import rip.helium.module.modules.render.Night;
import rip.helium.module.modules.render.StorageESP;
import rip.helium.module.modules.render.Tracers;
import rip.helium.module.modules.render.XRay;
import rip.helium.module.modules.world.Smasher;
import rip.helium.module.modules.world.SpeedMine;

public class ModuleManager implements ClientSupport {
	
	private ArrayList<Module> modules = new ArrayList<Module>();
	
	public ArrayList<Module> getModules() {
		return this.modules;
	}
	
	public ArrayList<Module> getModulesForArrayList() {
		ArrayList<Module> renderList = this.modules;
	    renderList.sort(new Comparator<Module>() {
	    	public int compare(Module m1, Module m2) {
	            String s1 = String.format("%s" + ((m1.getSuffix().length() > 0) ? " [%s]" : ""), m1.getDisplayName(), m1.getSuffix());
	            String s2 = String.format("%s" + ((m2.getSuffix().length() > 0) ? " [%s]" : ""), m2.getDisplayName(), m2.getSuffix());
	            return mc.fontRendererObj.getStringWidth(s2) - mc.fontRendererObj.getStringWidth(s1);
	        }
	    });
	    return renderList;
	}
	
	public Module getModule(String s) {
		for(Module m: getModules()) {
			if(m.getName().equalsIgnoreCase(s)) {
				return m;
			}
		}
		return new Module(0, "Null", Category.Misc);
	}
	
	public List<Module> getModsInCategory(Module.Category category) {
	    ArrayList<Module> modList = new ArrayList<Module>();
	    for (Module mod : this.modules) {
	      if (mod.getCategory() == category)
	        modList.add(mod); 
	    } 
	    modList.sort(new Comparator<Module>() {
	          public int compare(Module m1, Module m2) {
	            String s1 = m1.getDisplayName();
	            String s2 = m2.getDisplayName();
	            return s1.compareTo(s2);
	          }
	        });
	    return modList;
	  }
	
	public void addModule(Module m) {
		this.modules.add(m);
	}
	
	public ModuleManager() {
		EventManager.register(this);
		
		addModule(new KillAura(Keyboard.KEY_R, "KillAura", "Kill Aura", Category.Combat));
		addModule(new TPAura(0, "TPAura", "TP Aura", Category.Combat));
		addModule(new CrystalAura(0, "CrystalAura", "Crystal Aura", Category.Combat));
		addModule(new FastBow(0, "FastBow", "Fast Bow", Category.Combat));
		addModule(new AntiBot(0, "AntiBot", "Anti Bot", Category.Combat));
		addModule(new AutoPot(0, "AutoPot", "Auto Pot", Category.Combat));
		addModule(new Criticals(0, "Criticals", Category.Combat));
		addModule(new Velocity(0, "Velocity", Category.Combat));
		addModule(new Targeting(0, "Targeting", Category.Combat));
		addModule(new ChestStealer(0, "ChestStealer", "Chest Stealer", Category.Misc));
		addModule(new AutoInventory(0, "AutoInventory", "Auto Inventory", Category.Misc));
		addModule(new AutoPlay(0, "AutoPlay", "Auto Play", Category.Misc));
		addModule(new ChestAura(0, "ChestAura", "Chest Aura", Category.Misc));
		addModule(new Spammer(0, "Spammer", Category.Misc));
		addModule(new Announcer(0, "Announcer", Category.Misc));
		addModule(new Speed(Keyboard.KEY_F, "Speed", Category.Movement));
		addModule(new Flight(Keyboard.KEY_M, "Flight", Category.Movement));
		addModule(new Phase(Keyboard.KEY_Y, "Phase", Category.Movement));
		addModule(new NoSlowdown(0, "NoSlowdown", "No Slowdown", Category.Movement));
		addModule(new ScreenWalk(0, "ScreenWalk", "Screen Walk", Category.Movement));
		addModule(new Freecam(Keyboard.KEY_V, "Freecam", Category.Movement));
		addModule(new Sprint(0, "Sprint", Category.Movement));
		addModule(new Jesus(0, "Jesus", Category.Movement));
		addModule(new LongJump(Keyboard.KEY_G, "LongJump", "Long Jump", Category.Movement));
		addModule(new Teleport(0, "Teleport", Category.Movement));
		addModule(new Step(0, "Step", Category.Movement));
		addModule(new TargetStrafe(0, "TargetStrafe", "Target Strafe", Category.Movement));
		addModule(new NoFall(0, "NoFall", "No Fall", Category.Player));
		addModule(new Regen(0, "Regen", Category.Player));
		addModule(new Reach(0, "Reach", Category.Player));
		addModule(new GoodHack(0, "GoodHack", "Good Hack", Category.Player));
		addModule(new Ghost(0, "Ghost", Category.Player));
		addModule(new NoRotate(0, "NoRotate", "No Rotate", Category.Player));
		addModule(new AutoEat(0, "AutoEat", "Auto Eat", Category.Player));
		addModule(new FastUse(0, "FastUse", "Fast Use", Category.Player));
		addModule(new Smasher(0, "Smasher", Category.World));
		addModule(new SpeedMine(0, "SpeedMine", "Speed Mine", Category.World));
		addModule(new Disabler(0, "Disabler", Category.Exploits));
		addModule(new ServerLagger(0, "ServerLagger", "Server Lagger", Category.Exploits));
		addModule(new Brightness(0, "Brightness", Category.Render));
		addModule(new Tracers(0, "Tracers", Category.Render));
		addModule(new LivingESP(0, "LivingESP", "Living ESP", Category.Render));
		addModule(new StorageESP(0, "StorageESP", "Storage ESP", Category.Render));
		addModule(new NameTags(0, "NameTags", "Name Tags", Category.Render));
		addModule(new Capes(0, "Capes", Category.Render));
		addModule(new Animations(0, "Animations", Category.Render));
		addModule(new Chams(0, "Chams", Category.Render));
		addModule(new Colors(0, "Colors", Category.Render));
		addModule(new CustomChat(0, "CustomChat", "Custom Chat", Category.Render));
		addModule(new CustomViewmodel(0, "CustomViewmodel", "Custom Viewmodel", Category.Render));
		addModule(new XRay(Keyboard.KEY_X, "XRay", "X Ray", Category.Render));
		addModule(new Night(0, "Night", Category.Render));
		addModule(new ClickGUI(Keyboard.KEY_RSHIFT, "ClickGUI", "Click GUI", Category.Render));
		addModule(new HUD(0, "HUD", Category.Render));
	}
	
	public void loadConfig(Gson gson) {
		for(Module m: this.modules) {
			File file = new File(mc.mcDataDir + File.separator + mc.hackedClient.getName() + File.separator + "modules" + File.separator + m.getName() + ".json");
			try (FileReader reader = new FileReader(file)) {
				Map<String, Object> map = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());
				m.setBind((int)Math.round((double)map.get("bind")));
				m.setState((boolean)map.get("toggled"));
			} catch (JsonSyntaxException e) {
				
			} catch (JsonIOException e) {
				
			} catch (FileNotFoundException e) {
				
			} catch (IOException e) {
				
			} catch (NullPointerException e) {
				
			}
		}
	}
	
	public void saveConfig(Gson gson) {
		for(Module m: this.modules) {
			File file = new File(mc.mcDataDir + File.separator + mc.hackedClient.getName() + File.separator + "modules" + File.separator + m.getName() + ".json");
			if(!file.exists()) {
				new File(mc.mcDataDir + File.separator + mc.hackedClient.getName() + File.separator + "modules").mkdirs();
				try {
					file.createNewFile();
				} catch (IOException e) {
				}
			}
			try (FileWriter writer = new FileWriter(file)) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("name", m.getName());
	            map.put("bind", m.getBind());
	            map.put("toggled", m.getState());
	            gson.toJson(map, writer);
	        } catch (IOException e) {
	        }
		}
	}
	
	@EventTarget
	public void onKeyPress(KeyPressEvent event) {
		for(Module m: this.modules) {
			if(event.getKeyCode() == m.getBind()) {
				m.toggle();
			}
		}
	}
	
}
