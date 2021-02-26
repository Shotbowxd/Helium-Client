package rip.helium;

import java.net.MalformedURLException;
import java.time.OffsetDateTime;

import com.github.creeper123123321.viafabric.ViaFabric;
import com.google.gson.Gson;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import com.thealtening.AltService.EnumAltService;

import rip.helium.account.Account;
import rip.helium.account.AccountLoginService;
import rip.helium.command.CommandManager;
import rip.helium.event.EventManager;
import rip.helium.friend.FriendManager;
import rip.helium.gui.click.ClickGUIScreen;
import rip.helium.module.ModuleManager;
import rip.helium.setting.SettingManager;

public class HeliumClient implements ClientSupport {
	
	private String name = "Helium";
	private String version = "22621"; //2.26.2021
	private String author = "Kansio & MichaelMaymays";
	
	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getAuthor() {
		return author;
	}
	
	private ModuleManager moduleManager;
	private SettingManager settingManager;
	private FriendManager friendManager;
	private CommandManager commandManager;
	private AccountLoginService accountLoginService;
	
	public ModuleManager getModuleManager() {
		return this.moduleManager;
	}
	
	public SettingManager getSettingManager() {
		return this.settingManager;
	}
	
	public FriendManager getFriendManager() {
		return this.friendManager;
	}
	
	public CommandManager getCommandManager() {
		return this.commandManager;
	}
	
	public AccountLoginService getAccountLoginService() {
		return accountLoginService;
	}

	private Gson gson;
	
	public Gson getGson() {
		return this.gson;
	}
	
	private ClickGUIScreen gui;
	
	public ClickGUIScreen getGUI() {
		return this.gui;
	}
	
	public HeliumClient() {
		mc.hackedClient = this;
		
		EventManager.register(this);
		
		try {
			new ViaFabric().onInitialize();
		} catch (IllegalAccessException | NoSuchFieldException | MalformedURLException e) {
			e.printStackTrace();
		}
		
		this.gson = new Gson();
		
		this.friendManager = new FriendManager();
		this.settingManager = new SettingManager();
		this.moduleManager = new ModuleManager();
		this.commandManager = new CommandManager();
		this.accountLoginService = new AccountLoginService();
	
		this.friendManager.loadConfig(gson);
		this.friendManager.saveConfig(gson);
		this.moduleManager.loadConfig(gson);
		this.moduleManager.saveConfig(gson);
		this.settingManager.loadConfig(gson);
		this.settingManager.saveConfig(gson);
		
		this.gui = new ClickGUIScreen();
		
		/*/IPCClient client = new IPCClient(808312902766428190L);
		client.setListener(new IPCListener() {
			@Override
			public void onReady(IPCClient client) {
				RichPresence.Builder builder = new RichPresence.Builder();
				builder.setState("Developer version: v" + version).setDetails("Destroying newfags!").setStartTimestamp(OffsetDateTime.now()).setLargeImage("rpc_logo");
				client.sendRichPresence(builder.build());
			}
		});
		try {
			client.connect();
		} catch (NoDiscordClientException e) {
			e.printStackTrace();
		}/*/
		
		Runtime.getRuntime().addShutdownHook(new Thread("Client shutdown thread") {
			public void run() {
				mc.hackedClient.friendManager.saveConfig(mc.hackedClient.getGson());
				mc.hackedClient.moduleManager.saveConfig(mc.hackedClient.getGson());
				mc.hackedClient.settingManager.saveConfig(mc.hackedClient.getGson());
	        }
		});
	}
	
}
