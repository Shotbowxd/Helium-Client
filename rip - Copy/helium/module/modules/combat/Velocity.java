package rip.helium.module.modules.combat;

import java.util.ArrayList;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.network.PacketReceiveEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.module.modules.movement.Speed;
import rip.helium.module.modules.player.Regen;
import rip.helium.setting.Setting;
import rip.helium.utils.render.ColorUtils;

public class Velocity extends Module{

	private Setting mode;
	private Setting horizontal;
	private Setting vertical;
	
    private final double[] positions = new double[]{0.0, 0.0, 0.0};
    private final double[] motions = new double[]{0.0, 0.0, 0.0};
    private double cosmicVel;
    private KillAura cheat_killAura;
    private Speed cheat_Speed;
    private Regen cheat_Regen;
	
	public Velocity(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Packet");
		modes.add("Motion");
		modes.add("Ghostly");
		modes.add("OldAGC");
		
		this.mode = new Setting("Mode", this, "Packet", modes);
		this.horizontal = new Setting("Horizontal", this, 0.10, 0, 1.0, false);
		this.vertical = new Setting("Vertical", this, 0.10, 0., 1.0, false);
		
		mc.hackedClient.getSettingManager().addSetting(this.mode);
		mc.hackedClient.getSettingManager().addSetting(this.horizontal);
		mc.hackedClient.getSettingManager().addSetting(this.vertical);
	}
	
	@EventTarget
	public void onPacketReceive(PacketReceiveEvent event) {
		if(this.mode.getValString().equalsIgnoreCase("Packet")) {
			if ((event.getPacket() instanceof S12PacketEntityVelocity) && (((S12PacketEntityVelocity) event.getPacket()).getEntityID() == mc.thePlayer.getEntityId())) {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
                packet.motionX = 0;
                packet.motionY = 0;
                packet.motionZ = 0;
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof S27PacketExplosion) {
                S27PacketExplosion packetExplosion = (S27PacketExplosion) event.getPacket();
                packetExplosion.field_149152_f = 0;
                packetExplosion.field_149153_g = 0;
                packetExplosion.field_149159_h = 0;
                event.setCancelled(true);
            }
		}
	}
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		this.setSuffix(this.mode.getValString());
		if (mc.getCurrentServerData() != null && (mc.getCurrentServerData().serverIP.toLowerCase().contains("ghostly"))) {
            if (mc.thePlayer.isPotionActive(Potion.moveSlowdown) || mc.thePlayer.isPotionActive(Potion.blindness)) {
                mc.thePlayer.removePotionEffectClient(Potion.moveSlowdown.id);
                mc.thePlayer.removePotionEffectClient(Potion.blindness.id);
                mc.thePlayer.removePotionEffect(Potion.moveSlowdown.id);
                mc.thePlayer.removePotionEffect(Potion.blindness.id);
                mc.thePlayer.addChatComponentMessage(new ChatComponentText("§c§lRemoved shitty effects :)."));

            }
        }

        if (mode.getValString().equalsIgnoreCase("Packet")) {
            if (mc.thePlayer.hurtTime > 0) {
                if (mc.getCurrentServerData() != null) {
                    if (mc.getCurrentServerData().serverIP.toLowerCase().contains("cosmicpvp")
                            || mc.getCurrentServerData().serverIP.toLowerCase().contains("viper")) {
                        if (cosmicVel > 1.0E-8D) {
                            cosmicVel = 1.0E-8D;
                        }
                        event.setPosY(event.getPosY() + cosmicVel);
                        cosmicVel += 2.15E-12D;
                    }
                }
            } else {
                cosmicVel = 0;
            }
        }

        if (mode.getValString().equalsIgnoreCase("OldAGC")) {
            if (mc.thePlayer.hurtTime != 0) {
                mc.thePlayer.motionY -= 10000;
                mc.thePlayer.motionX *= .65;
                mc.thePlayer.motionZ *= .65;
            }
        }
        if (mode.getValString().equalsIgnoreCase("Motion")) {
            if (cheat_killAura == null) {
                cheat_killAura = (KillAura)mc.hackedClient.getModuleManager().getModule("KillAura");
            }
            if (cheat_Speed == null) {
                cheat_Speed = (Speed)mc.hackedClient.getModuleManager().getModule("Speed");
            }
            if (mc.thePlayer.hurtTime == 0) {
                positions[0] = mc.thePlayer.posX;
                positions[1] = mc.thePlayer.posY + vertical.getValDouble();
                positions[2] = mc.thePlayer.posZ;

                motions[0] = mc.thePlayer.motionX * horizontal.getValDouble();
                motions[1] = mc.thePlayer.motionY + mc.thePlayer.fallDistance < 0.7 ? vertical.getValDouble() : vertical.getValDouble() / 2;
                motions[2] = mc.thePlayer.motionZ * horizontal.getValDouble();
            } else if (mc.thePlayer.hurtTime == 9 && (event.getPosY() - mc.thePlayer.getEntityBoundingBox().minY <= 0.0001)) {
                mc.thePlayer.posX = mc.thePlayer.lastTickPosX = positions[0];
                mc.thePlayer.posY = mc.thePlayer.lastTickPosY = positions[1];
                mc.thePlayer.posZ = mc.thePlayer.lastTickPosZ = positions[2];
                mc.thePlayer.motionX = motions[0];
                mc.thePlayer.motionY = motions[1];
                mc.thePlayer.motionZ = motions[2];
                mc.thePlayer.hurtTime = 0;
            }
            if (!mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.hurtTime < 9 && mc.thePlayer.hurtTime >= 1 && !(mc.hackedClient.getModuleManager().getModule("Flight").getState() || mc.hackedClient.getModuleManager().getModule("Speed").getState())) {

            }
        }
	}
	
}
