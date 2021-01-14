package rip.helium;

import com.thealtening.AltService;
import com.thealtening.utilities.SSLVerification;
import me.hippo.systems.lwjeb.EventBus;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import rip.helium.account.AccountLoginService;
import rip.helium.account.AccountManager;
import rip.helium.account.AccountsFile;
import rip.helium.cheat.CheatManager;
import rip.helium.cheat.CommandManager;
import rip.helium.cheat.FocusManager;
import rip.helium.configuration.ConfigurationManager;
import rip.helium.event.Stage;
import rip.helium.event.minecraft.ExitGameEvent;
import rip.helium.event.minecraft.StartGameEvent;
import rip.helium.gui.hud.Hud;
import rip.helium.notification.mgmt.NotificationManager;
import rip.helium.ui.main.Screen;
import rip.helium.utils.*;
import rip.helium.utils.font.Fonts;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
//import static rip.helium.cheat.impl.misc.FlagDetector.TabListCheck;

public class Helium {
    public static final Helium instance;
    public static final File clientDir;
    public static final Logger logger;
    public static final int client_build = 11121;
    public static EventBus eventBus;
    public static String getClient_name = "Helium";
    public static String clientUser;
    static Minecraft mc;

    static {
        instance = new Helium();
        //clientDir = new File("C:\\" + File.separator + "Helium");
        clientDir = new File(Minecraft.getMinecraft().mcDataDir, "Helium");
        logger = LogManager.getLogger();
    }

    public boolean is18Mode = false;

    public boolean developement = false;
    public CommandManager cmds;
    public CheatManager cheatManager;
    public ConfigurationManager configurationManager;
    public Screen userInterface;
    public FocusManager focusManager;
    public Hud hud;
    public NotificationManager notificationManager;
    public AccountManager accountManager;
    public AccountLoginService accountLoginService;
    public AltService altService;
    public String discordUsername = "";
    public boolean isDiscord = false;
    boolean auth;
    Stopwatch stopwatch = new Stopwatch();

    public Helium() {
        (Helium.eventBus = new EventBus().field().method().build()).register(this);
    }

    public void createConfig(String server) {
        switch (server) {
            case "viper": {
                try {
                    File statText = new File(Minecraft.getMinecraft().mcDataDir + System.getProperty("file.separator") + "Helium" + System.getProperty("file.separator") + "configurations" + System.getProperty("file.separator") + "Viper(Default).kkk");
                    FileOutputStream is = new FileOutputStream(statText);
                    OutputStreamWriter osw = new OutputStreamWriter(is);
                    Writer w = new BufferedWriter(osw);
                    w.write("{\"default\":false,\"Inventory Cleaner\":{\"state\":false,\"bind\":0,\"values\":{}},\"Animations\":{\"state\":true,\"bind\":0,\"values\":{\"Mode\":\"Helium:true,Matt:false,Remix:false,1.7:false,Sigma:false,Slide:false,Kansio:false,Poke:false,oHare:false,Swang:false\"}},\"Brightness\":{\"state\":true,\"bind\":0,\"values\":{\"Gamma Multiplier\":\"250.0\",\"Mode\":\"Gamma:true,Night Vision:false\"}},\"Flight\":{\"state\":false,\"bind\":33,\"values\":{\"Speed\":\"5.099999999999998\",\"Flight\":\"Vanilla:true,LongjumpFly:false\",\"View Bobbing\":\"false\",\"MEME BOBBING\":\"false\",\"AntiKick\":\"true\"}},\"BookExploit\":{\"state\":false,\"bind\":0,\"values\":{\"Fire\":\"true\",\"Sharp\":\"true\",\"Knockback\":\"true\",\"Thorns\":\"true\",\"Level\":\"10.0\",\"Looting\":\"true\"}},\"Teleport\":{\"state\":false,\"bind\":0,\"values\":{}},\"Skeletal\":{\"state\":true,\"bind\":0,\"values\":{\"Color\":\"255.0:0.98999995:0.96999997:255\"}},\"Criticals\":{\"state\":false,\"bind\":0,\"values\":{}},\"Fucker\":{\"state\":false,\"bind\":0,\"values\":{}},\"No Rotate\":{\"state\":false,\"bind\":0,\"values\":{}},\"Items\":{\"state\":true,\"bind\":0,\"values\":{}},\"Spammer\":{\"state\":false,\"bind\":0,\"values\":{}},\"WaterSpeed\":{\"state\":true,\"bind\":0,\"values\":{}},\"Flag Detector\":{\"state\":false,\"bind\":0,\"values\":{}},\"FastBow\":{\"state\":false,\"bind\":0,\"values\":{\"Mode\":\"Multi:true,Viper:false\"}},\"Disabler\":{\"state\":true,\"bind\":0,\"values\":{\"Mode\":\"Viper:true,Muncher:false,Kohi:false,RinaOrc:false,Mineplex:false,Faithful:false,Verus:false,PingSpoof:false,OmegaCraft:false,Ghostly:false,Watchdog:false,Poopful:false\"}},\"NoFall\":{\"state\":true,\"bind\":0,\"values\":{\"Fast Fall\":\"false\",\"Mode\":\"Spoof:true,Hypixel:false\"}},\"Regen\":{\"state\":false,\"bind\":0,\"values\":{\"Health\":\"15.0\"}},\"Sneak\":{\"state\":false,\"bind\":0,\"values\":{}},\"TargetStrafe\":{\"state\":false,\"bind\":0,\"values\":{\"Rainbow\":\"true\",\"Hold Space\":\"true\",\"Distance\":\"2.5\"}},\"AutoPlay\":{\"state\":false,\"bind\":0,\"values\":{\"Mode\":\"Doubles:false,Solo:true\"}},\"Sprint\":{\"state\":true,\"bind\":0,\"values\":{}},\"No Clip\":{\"state\":false,\"bind\":0,\"values\":{\"Amount\":\"10.0\"}},\"InvMove\":{\"state\":true,\"bind\":0,\"values\":{\"Desyncronize\":\"false\"}},\"Tracers\":{\"state\":true,\"bind\":0,\"values\":{}},\"Chams\":{\"state\":true,\"bind\":0,\"values\":{\"Fill\":\"false\",\"Fill Color\":\"1.0:0.0:1.0:255\",\"Fill Rainbow\":\"false\"}},\"KillAura\":{\"state\":false,\"bind\":19,\"values\":{\"Autoblock\":\"true\",\"Prioritize Players\":\"false\",\"Middle Click to Ignore\":\"true\",\"APS\":\"13.699999999999969\",\"Autoblock Mode\":\"Real:true,Fake:false\",\"Priority\":\"Lowest Health:true,Least Armor:false,Closest:false\",\"Mode\":\"Priority:true,Multi:false\",\"KeepSprint\":\"true\",\"Targets\":\"Players:true,Monsters:true,Animals:false,Villagers:false,Golems:false\",\"APS Flucutation\":\"0.6\",\"Max Distance\":\"5.999999999999995\",\"Middle Click Reset\":\"true\",\"Left Click\":\"false\",\"Teams\":\"false\",\"Spinbot\":\"false\",\"HvH\":\"false\",\"Miss Hits\":\"true\",\"Show Circle\":\"true\"}},\"IceSpeed\":{\"state\":false,\"bind\":0,\"values\":{}},\"Phase\":{\"state\":false,\"bind\":0,\"values\":{\"Mode\":\"Aris:true,Vanilla:false,Faithful:false\"}},\"Inventory Manager\":{\"state\":false,\"bind\":0,\"values\":{\"Cleaning Delay\":\"150.0\",\"Automated Tasks\":\"Clean:false,Equip Armor:false,Sword Slot:false\",\"Sword Slot\":\"1.0\",\"Equip Delay\":\"150.0\",\"Only In Inventory\":\"false\",\"Spoof Open\":\"true\"}},\"Crosshair\":{\"state\":true,\"bind\":0,\"values\":{\"Length\":\"5.399999999999989\",\"Color\":\"1.0:0.0:1.0:255\",\"Gap\":\"1.1000000000000003\",\"Width\":\"0.25\",\"Fixed\":\"true\"}},\"AntiVoid\":{\"state\":false,\"bind\":0,\"values\":{}},\"Scaffold\":{\"state\":false,\"bind\":0,\"values\":{\"Downwards\":\"true\",\"Sneak\":\"true\",\"Hypixel\":\"true\",\"Keep Y\":\"true\",\"Switch\":\"true\",\"Mode\":\"Cubecraft:true,Hypixel:false,DEV:false\",\"Tower\":\"true\"}},\"Step\":{\"state\":false,\"bind\":0,\"values\":{}},\"ChatMods\":{\"state\":false,\"bind\":0,\"values\":{\"KillSult Mode\":\"AutoL:true,Insult:false\",\"Anti-Spam Bypass\":\"false\",\"Filter Bypass\":\"false\",\"AutoGG\":\"true\",\"Kill Sults\":\"false\"}},\"Hud\":{\"state\":true,\"bind\":0,\"values\":{\"Background Opacity\":\"0.3\",\"Client Name\":\"§4§lH§felium\",\"Mode\":\"Helium:true,Virtue:false,Memestick:false,Exhi:false,Helium2:false\",\"Shadow\":\"true\",\"Scoreboard Background\":\"true\",\"Target Hud\":\"true\",\"Lowercase\":\"true\",\"Tab GUI\":\"false\",\"Scoreboard Down\":\"10.0\",\"Arraylist\":\"true\",\"ArrayList Color Mode\":\"Wave:false,Custom:false,Rainbow:false,Rainbow2:true,Categories:false,Pulsing:false\",\"Arraylist Color\":\"1.0:0.22999999:0.88:255\",\"Tnt Alert\":\"true\"}},\"AntiBot\":{\"state\":false,\"bind\":0,\"values\":{}},\"LongJump\":{\"state\":false,\"bind\":0,\"values\":{\"Mode\":\"Vanilla:true,Mineplex:false,Fierce:false,Faithful:false\"}},\"NoStrike\":{\"state\":true,\"bind\":0,\"values\":{}},\"AutoPotion\":{\"state\":true,\"bind\":0,\"values\":{}},\"NoSlow\":{\"state\":true,\"bind\":0,\"values\":{\"Mode\":\"NCP:true,Hypixel:false\"}},\"ChestStealer\":{\"state\":false,\"bind\":0,\"values\":{\"BL Properties\":\"Harmful Potion:false,Worse Sword:false,Duplicate Tool:false,Worse Armor:false\",\"Ignore Custom Chest\":\"true\",\"BL Items\":\"\",\"Steal delay\":\"150.0\"}},\"Speed\":{\"state\":false,\"bind\":20,\"values\":{\"Speed\":\"1.2\",\"Mode\":\"Vanilla:true,Viper:false\"}},\"ESP\":{\"state\":false,\"bind\":0,\"values\":{\"Chest\":\"0.1:1.0:0.64:90\",\"Box (default)\":\"1.0:0.0:1.0:255\",\"Health Number\":\"1.0:0.0:1.0:255\",\"Health Style\":\"Number:false,Bar:true\",\"Box (target)\":\"0.0:1.0:0.64:255\",\"Box outline\":\"0.0:0.0:0.0:255\",\"Elements\":\"Box:false,Health:false,Name:false,Chests:false\",\"Avoid\":\"Invisible:false,Teammate:false,Bots:false\",\"Entities\":\"Players:false,Monsters:false,Animals:false,Villagers:false,Golems:false\",\"Name\":\"1.0:0.0:1.0:255\"}},\"NameTags\":{\"state\":true,\"bind\":0,\"values\":{\"Darkness\":\"0.3\"}},\"Velocity\":{\"state\":true,\"bind\":0,\"values\":{\"Vertical Adder\":\"0.21\",\"Mode\":\"Packet:true,Motion:false,OldAGC:false\",\"Horizontal Multiplier\":\"1.1\"}},\"FastUse\":{\"state\":false,\"bind\":0,\"values\":{}},\"Item Physics\":{\"state\":false,\"bind\":0,\"values\":{}},\"Freecam\":{\"state\":false,\"bind\":0,\"values\":{}},\"Xray\":{\"state\":false,\"bind\":0,\"values\":{}},\"Interface\":{\"state\":false,\"bind\":54,\"values\":{\"Cheat Descriptions\":\"true\"}},\"Protector\":{\"state\":false,\"bind\":0,\"values\":{\"Name Protect\":\"true\",\"Friend Protect\":\"true\"}},\"Waterwalk\":{\"state\":false,\"bind\":0,\"values\":{\"Mode\":\"Mineplex:true,Solid:false,NCP:false\"}},\"Chest Aura\":{\"state\":false,\"bind\":0,\"values\":{}},\"NoHurtCam\":{\"state\":false,\"bind\":0,\"values\":{}}}");
                    w.close();
                } catch (IOException e) {
                    System.err.println("Problem writing to the file statsTest.txt");
                }
            }
        }
    }

    public void readName() throws IOException {
        File file = new File(Minecraft.getMinecraft().mcDataDir + System.getProperty("file.separator") + "Helium" + System.getProperty("file.separator") + "helium.user");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            System.out.println(st);
            clientUser = st;
        }
    }


    @Collect
    public void onStartGame(final StartGameEvent event) throws URISyntaxException, IOException {
        int rand = Mafs.getRandom(1, 2);
        System.out.println(rand + " hahahaha yews");
        Desktop nigger = Desktop.getDesktop();
        if (rand == 1) {
            nigger.browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
        } else {
            nigger.browse(new URI("https://www.youtube.com/watch?v=Jqt9n9DwpgE&t=1s"));
        }
        File name = new File(Minecraft.getMinecraft().mcDataDir + System.getProperty("file.separator") + "Helium" + System.getProperty("file.separator") + "helium.user");
        if (!name.exists()) {
            clientUser = JOptionPane.showInputDialog(null, "What should we call you?");
            try {
                File file = new File(Minecraft.getMinecraft().mcDataDir + System.getProperty("file.separator") + "Helium" + System.getProperty("file.separator") + "helium.user");
                FileOutputStream is = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(is);
                Writer w = new BufferedWriter(osw);
                w.write(clientUser);
                w.close();
            } catch (Exception ex) {

            }
        }
        try {
            readName();
        } catch (Exception e) {

        }

        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog (null, "Do you want to see 18+ modules?",null, dialogButton);

        if(dialogResult == JOptionPane.YES_OPTION){
            is18Mode = true;
        } else {
            is18Mode = false;
        }
        //readName();

       /* try {
            IPCClient client = new IPCClient(699380061962371153L);
            client.setListener(new IPCListener() {
                @Override
                public void onReady(IPCClient client) {
                    RichPresence.Builder builder = new RichPresence.Builder();
                    if (developement) {
                        builder.setState("Developer Build: " + client_build)
                                .setDetails("Destroying Anticheats!")
                                .setStartTimestamp(OffsetDateTime.now())
                                .setLargeImage("shoot", "https://discord.gg/Rrb5PwP")
                                .setSmallImage("shoot", "best hacker client");
                    } else {
                        builder.setState("Release Build: " + client_build)
                                .setDetails("Destroying Anticheats!")
                                .setStartTimestamp(OffsetDateTime.now())
                                .setLargeImage("shoot", "https://discord.gg/Rrb5PwP")
                                .setSmallImage("shoot", "best hacker client");
                    }
                    client.sendRichPresence(builder.build());
                }
            });
            try {
                client.connect();
            } catch (Exception notreallyneededbutwhynot) {

            }
        } catch (Exception ev) {
            try {
                NotificationUtil.sendError("Client Error!", "Unable to set RPC. Is discord on????");
                ev.printStackTrace();
            } catch (Exception e) {

            }
        }*/

        AuthUtil.check();
        if (AuthUtil.check()) {
            auth = true;
            try {
                NotificationUtil.sendInfo("Client", "You were successfully authenticated!");
            } catch (Exception e) {

            }
        } else {
            auth = false;

            try {
                String hwid = HWID.getHWID();
                StringSelection stringSelection = new StringSelection(hwid);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            } catch (Exception e) {
                AuthUtil.close();
                try {
                    NotificationUtil.sendError("Client!", "Problem copying your hwid! Are you on windows?");
                } catch (Exception ex) {

                }
            }
            AuthUtil.close();

            try {
                NotificationUtil.sendError("Client!", "Your HWID is not whitelisted! Bought the client and not whitelisted? Send your HWID to Shotbowxd to be whitelisted.");
            } catch (Exception ex) {

            }
        }

        if (event.getStage().equals(Stage.PRE)) {
            if (!Helium.clientDir.exists()) {
                Helium.logger.info("Creating helium... " + (Helium.clientDir.mkdirs() ? "Done" : "Failed"));
            }
            System.out.println("Viper config is being created!");
            createConfig("viper");
            System.out.println("Done, created configs!");
            if (auth) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
//            	int heliumUser = JOptionPane.showConfirmDialog(null, "Do you want to enable helium server discovery");
//
//            	if(heliumUser == JOptionPane.YES_OPTION) {
//            	String name = JOptionPane.showInputDialog(null, "Put your Discord Username");
//            	this.discordUsername = name;
//            	this.isDiscord = true;
//            	}
//            	
                this.cheatManager = new CheatManager();
                this.configurationManager = new ConfigurationManager();
                this.notificationManager = new NotificationManager();
                //staffcheccers();
                this.focusManager = new FocusManager();
                this.accountManager = new AccountManager();
                this.cmds = new CommandManager();
                this.accountLoginService = new AccountLoginService();
                new SSLVerification().verify();
                this.altService = new AltService();
                Runtime.getRuntime().addShutdownHook(new Thread(this::onExitGame));

            }
        } else {
            if (auth) {
                Fonts.createFonts();
                this.cheatManager.registerCheats();
                this.configurationManager.loadConfigurationFiles(false);
                AccountsFile.load();
                this.userInterface = new Screen();
                this.hud = new Hud();
                Display.setTitle("Helium Client");
            }
        }
    }


    private void onExitGame() {
        this.configurationManager.saveConfigurationFiles();
        Helium.eventBus.publish(new ExitGameEvent());
    }

    public Object getMember() {
        return null;
    }
}
