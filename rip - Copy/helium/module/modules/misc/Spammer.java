package rip.helium.module.modules.misc;

import java.util.Random;

import net.minecraft.network.play.client.C01PacketChatMessage;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.utils.client.Timer;
import rip.helium.utils.misc.MathUtils;
import rip.helium.utils.render.ColorUtils;

public class Spammer extends Module {

    public boolean nspam = false;
    private final Timer timer;
    private final String[] phraseList;
    private final String[] nphraseList;
    private int lastUsed;
	
	public Spammer(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		
		this.timer = new Timer();
        this.phraseList = new String[]{"Helium Client", "Helium good, sigma bad!", "sigma gey, get Helium", "yes i have cool gamin chair helium!111", "whatz the difference between a j3w and a pizza? the pizza wont scream in the oven"};
        this.nphraseList = new String[]{""};
	}
	
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		if (this.timer.hasPassed((float) this.randomDelay())) {
            if (this.nspam) {
                mc.getNetHandler().addToSendQueue(new C01PacketChatMessage(this.nrandomPhrase()));
                this.timer.updateLastTime();
            } else {
                mc.getNetHandler().addToSendQueue(new C01PacketChatMessage(this.randomPhrase()));
                this.timer.updateLastTime();
            }
        }
	}
	
	private String randomPhrase() {
        Random rand;
        int randInt;
        for (rand = new Random(), randInt = rand.nextInt(this.phraseList.length); this.lastUsed == randInt; randInt = rand.nextInt(this.phraseList.length)) {
        }
        this.lastUsed = randInt;
        return this.phraseList[randInt];
    }

    private String nrandomPhrase() {
        Random rand;
        int randInt;
        for (rand = new Random(), randInt = rand.nextInt(this.nphraseList.length); this.lastUsed == randInt; randInt = rand.nextInt(this.nphraseList.length)) {
        }
        this.lastUsed = randInt;
        return this.nphraseList[randInt];
    }

    private int randomDelay() {
        final int randyInt = MathUtils.getRandomInRange(1000, 1500);
        return randyInt;
    }
	
}
