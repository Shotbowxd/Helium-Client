package rip.helium.command.commands;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockSign;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import rip.helium.command.Command;
import rip.helium.event.EventManager;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.client.RunTickEvent;
import rip.helium.event.events.impl.network.PacketSendEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.utils.client.ClientUtils;
import rip.helium.utils.client.Timer;

public class TeleportCommand extends Command {
	
	private int x, y, z;
	private boolean gotonigga, niggay;
	private final Minecraft mc = Minecraft.getMinecraft();
	private boolean packet = true;
	private int moveUnder;
	private Timer timerUtility = new Timer();
	
	public TeleportCommand() {
		this.setNames(new String[]{"teleport", "tp"});
	}
	
	public void runCommand(String[] args) {
		switch (args.length) {
        case 2:
            if (args[1].toLowerCase().equals("stop")) {
                if (gotonigga) {
                    stopTP();
                    ClientUtils.addConsoleMessage("Stopped.");
                } else {
                    ClientUtils.addConsoleMessage("Not running.");
                }
                break;
            }
            if (args[1].toLowerCase().equals("help")) {
                ClientUtils.addConsoleMessage(".teleport stop/packet/xz/xyz/waypointname/playername/factionname");
                break;
            }
            if (args[1].toLowerCase().equals("packet")) {
                packet ^= true;
                ClientUtils.addConsoleMessage("Packet set to " + packet);
                break;
            }
            if (gotonigga) {
                ClientUtils.addConsoleMessage("Already going.");
                break;
            }

            for (EntityPlayer e : mc.theWorld.playerEntities) {
                if (e.getName().toLowerCase().equals(args[1].toLowerCase())) {
                    startTP(MathHelper.floor_double(e.posX), MathHelper.floor_double(e.posY), MathHelper.floor_double(e.posZ), true);
                    return;
                }
            }
            EventManager.register(this);
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C01PacketChatMessage("/f who " + args[1]));
            break;
        case 3:
            if (gotonigga) {
                ClientUtils.addConsoleMessage("Already going.");
                break;
            }
            if (NumberUtils.isNumber(args[1]) && NumberUtils.isNumber(args[2])) {
                if (!isUnderBlock() || packet) {
                    startTP(Integer.parseInt(args[1]), 255, Integer.parseInt(args[2]), true);
                } else {
                    ClientUtils.addConsoleMessage("You are under a block!");
                }
            } else {
                ClientUtils.addConsoleMessage("Invalid arguments.");
            }
            break;
        case 4:
            if (gotonigga) {
                ClientUtils.addConsoleMessage("Already going.");
                break;
            }
            if (NumberUtils.isNumber(args[1]) && NumberUtils.isNumber(args[2]) && NumberUtils.isNumber(args[3])) {
                if (!isUnderBlock() || packet) {
                    startTP(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), true);
                } else {
                    ClientUtils.addConsoleMessage("You are under a block!");
                }
            } else {
                ClientUtils.addConsoleMessage("Invalid arguments.");
            }
            break;
        default:
            ClientUtils.addConsoleMessage("Invalid arguments.");
            break;
		}
	}
	
	@EventTarget
    public void onUpdate(UpdateEvent event) {
        if (gotonigga && !packet) {
            final float storedangles = getRotationFromPosition(x, z);
            final double distancex = -4 * Math.sin(storedangles);
            final double distancez = 4 * Math.cos(storedangles);
            if (mc.thePlayer.ticksExisted % 3 == 0) {
                if (mc.thePlayer.posY < 250) {
                    mc.thePlayer.motionY = 5;
                } else {
                    mc.thePlayer.motionY = 0;
                    niggay = true;
                }
                if (mc.thePlayer.getDistanceSq(x, mc.thePlayer.posY, z) >= 32) {
                    if (niggay) {
                        mc.thePlayer.motionX = distancex;
                        mc.thePlayer.motionZ = distancez;
                    }
                } else {
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                    ClientUtils.addConsoleMessage("Finished you have arrived at x:" + (int) mc.thePlayer.posX + " z:" + (int) mc.thePlayer.posZ);
                    gotonigga = false;
                    niggay = false;
                    mc.renderGlobal.loadRenderers();
                    EventManager.unregister(this);
                }
            }
        }
    }

    @EventTarget
    public void onPacket(PacketSendEvent event) {
        if (packet) {
            if (gotonigga) {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
                    if (!niggay) {
                        packet.setY(y);
                        packet.setX(x);
                        packet.setZ(z);
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
                        mc.thePlayer.setPosition(x,y,z);
                        niggay = true;
                        moveUnder = 2;
                    }
                }
                if (timerUtility.hasPassed(500)) {
                    ClientUtils.addConsoleMessage("Finished you have arrived at x:" + x + " z:" + z);
                    gotonigga = false;
                    niggay = false;
                    mc.renderGlobal.loadRenderers();
                    EventManager.unregister(this);
                    timerUtility.updateLastTime();
                }
            }
        } else {
            if (event.getPacket() instanceof S08PacketPlayerPosLook && moveUnder == 2) {
                moveUnder = 1;
            }
            if (event.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) event.getPacket();
                String text = packet.getChatComponent().getUnformattedText();
                if (text.contains("You cannot go past the border.")) {
                    event.setCancelled(true);
                }
                if (text.contains("Home: ")) {
                    if (text.contains("Not set")) {
                        stopTP();
                        ClientUtils.addConsoleMessage("Player or faction found but f home was not set.");
                        return;
                    }
                    try {
                        int x = Integer.parseInt(StringUtils.substringBetween(text, "Home: ", ", "));
                        int z = Integer.parseInt(text.split(", ")[1]);
                        startTP(x, 255, z, false);
                    } catch (Exception e) {
                        stopTP();
                    }
                } else {
                    if (text.contains(" not found.")) {
                        stopTP();
                        ClientUtils.addConsoleMessage("Player or faction not found.");
                    }
                }
            }
        }
    }

    @EventTarget
    public void onTick(RunTickEvent event) {
        if (mc.thePlayer != null && moveUnder == 1 && packet) {
            if (mc.thePlayer.getDistanceSq(x, mc.thePlayer.posY, z) > 1) {
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                moveUnder = 0;
            }
        }
    }

    private void startTP(final int x, final int y, final int z, boolean register) {
        if (gotonigga) {
            ClientUtils.addConsoleMessage("Already active!");
            return;
        }
        this.x = x;
        this.y = y;
        this.z = z;
        gotonigga = true;
        ClientUtils.addConsoleMessage("Teleporting to x:" + x + " y:" + y + " z:" + z + ".");
        if (register) {
        	EventManager.register(this);
        }
        timerUtility.updateLastTime();
    }

    private void stopTP() {
        x = y = z = 0;
        gotonigga = false;
        niggay = false;
        EventManager.unregister(this);
    }

    private boolean isUnderBlock() {
        for (int i = (int) (Minecraft.getMinecraft().thePlayer.posY + 2); i < 255; ++i) {
            BlockPos pos = new BlockPos(Minecraft.getMinecraft().thePlayer.posX, i, Minecraft.getMinecraft().thePlayer.posZ);
            if (Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockAir || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockFenceGate || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockSign || Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockButton)
                continue;
            return true;
        }
        return false;
    }

    private float getRotationFromPosition(final double x, final double z) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final float yaw = (float) Math.atan2(zDiff, xDiff) - 1.57079632679f;
        return yaw;
    }
	
}
