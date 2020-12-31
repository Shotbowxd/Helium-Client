package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import org.apache.commons.lang3.RandomUtils;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerMoveEvent;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.event.minecraft.ProcessPacketEvent;
import rip.helium.event.minecraft.SendPacketEvent;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.property.impl.StringsProperty;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Disabler extends Cheat {
    private static final int VERUS_DISABLE_AUTOBAN_CHANNEL = 65536;
    private static final short VERUS_DISABLE_AUTOBAN_UID = 32767;
    public static ConcurrentLinkedQueue<Packet> list;

    static {
        Disabler.list = new ConcurrentLinkedQueue<Packet>();
    }

    private final Queue<Packet> packetQueue = new ConcurrentLinkedQueue();
    private final Stopwatch timer1 = new Stopwatch();
    public ArrayList<Packet> delayedtransactions;
    public ArrayList<Packet> delayedtransactions2;
    public int confirmtransactioncounter;
    public ConcurrentLinkedQueue<Packet> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
    int i = 0;
    private final Stopwatch timer = new Stopwatch();
    private int iNigga;
    private final StringsProperty mode = new StringsProperty("Mode", "How the priority target will be selected.", null, false, true, new String[]{"Viper", "Muncher", "Kohi", "RinaOrc", "Mineplex", "Faithful", "Verus", "PingSpoof", "OmegaCraft", "Ghostly", "Watchdog", "Poopful"}, new Boolean[]{true, false, false, false, false, false, false, false, false, false, false, false});

    public Disabler() {
        super("Disabler", "Fuck that little faggot!", CheatCategory.MOVEMENT);
        registerProperties(mode);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.iNigga = 0;
        timer.reset();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        packetQueue.clear();

    }

    @Collect
    public void playerMoveEvent(PlayerMoveEvent event) {
        if (mode.getValue().get("Ghostly")) {
            mc.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive(0));
            if (mc.thePlayer.ticksExisted % 3 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput());
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY, mc.thePlayer.posZ, true));
            }
        }
    }

    @Collect
    public void playerUpdateEvent(PlayerUpdateEvent event) {
        if (mode.getValue().get("Ghostly")) {
           /*/mc.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive(0));
           if (mc.thePlayer.ticksExisted % 3 == 0) {
               mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput());
               mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                       mc.thePlayer.posY, mc.thePlayer.posZ, true));
           }/*/
        } else if (mode.getValue().get("Mineplex")) {
            if (timer.hasPassed(1500) && !concurrentLinkedQueue.isEmpty()) {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(concurrentLinkedQueue.poll());
                timer.reset();
            }
            if (mc.thePlayer.ticksExisted % 100 == 0) {
                concurrentLinkedQueue.clear();
            }

        } else if (mode.getValue().get("Verus")) {

            if (timer.hasPassed(220L)) {
                timer.reset();
            }

            if (timer1.hasPassed(80L) && event.isPre()) {
                //PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(0.134546, -999, -0.113534232, false));
                timer1.reset();
            }

        } else if (mode.getValue().get("OmegaCraft")) {
            if (mc.thePlayer.ticksExisted % 3 == 0) {
                PlayerCapabilities pc = new PlayerCapabilities();
                pc.isFlying = true;
                pc.allowFlying = true;
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C13PacketPlayerAbilities(pc));
            }

        } else if (mode.getValue().get("Watchdog")) {
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0FPacketConfirmTransaction(VERUS_DISABLE_AUTOBAN_CHANNEL, VERUS_DISABLE_AUTOBAN_UID, true));

        } else if (mode.getValue().get("Kohi")) {
            event.setOnGround(true);
        }
    }

    @Collect
    public void packetEvent(SendPacketEvent event) {
        if (mode.getValue().get("RinaOrc")) {
            if (event.getPacket() instanceof C0BPacketEntityAction || event.getPacket() instanceof C0FPacketConfirmTransaction) {
                event.setCancelled(true);
            }
        } else if (mode.getValue().get("Mineplex")) {
            if (event.getPacket() instanceof C00PacketKeepAlive) {
                this.concurrentLinkedQueue.add(new C00PacketKeepAlive(RandomUtils.nextInt(400, 999)));
                this.concurrentLinkedQueue.add(new C0FPacketConfirmTransaction(-1, (short) RandomUtils.nextInt(400, 999), true));
                event.setCancelled(true);
            }
        } else if (mode.getValue().get("Muncher")) {
            if (event.getPacket() instanceof S00PacketKeepAlive || event.getPacket() instanceof C0FPacketConfirmTransaction) {
                event.setCancelled(true);
            } else if (mode.getValue().get("Faithful")) {

                if (mc.thePlayer.isEating() && !Helium.instance.cheatManager.isCheatEnabled("Speed") && !Helium.instance.cheatManager.isCheatEnabled("Flight") && mc.thePlayer.isSneaking()) {
                    return;

                }
                if (getMc().thePlayer != null && event.getPacket() instanceof C03PacketPlayer) {
                    event.setCancelled(true);
                    if (i > 2) {
                        getMc().thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(getMc().thePlayer.prevPosX + ((getMc().thePlayer.posX - getMc().thePlayer.prevPosX) / 3), getMc().thePlayer.prevPosY + ((getMc().thePlayer.posY - getMc().thePlayer.prevPosY) / 3), getMc().thePlayer.prevPosZ + ((getMc().thePlayer.posZ - getMc().thePlayer.prevPosZ) / 3),
                                getMc().thePlayer.rotationYaw, getMc().thePlayer.rotationPitch, true));
                        i = 0;
                    } else {
                        getMc().thePlayer.sendQueue.addToSendQueueNoEvent(new C00PacketKeepAlive(-Integer.MAX_VALUE));
                    }
                    i++;
                }

            } else if (mode.getValue().get("Verus")) {
                if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                    C0FPacketConfirmTransaction c0fPacketConfirmTransaction = (C0FPacketConfirmTransaction) event.getPacket();
                    c0fPacketConfirmTransaction.uid -= 1;
                }

                if (event.getPacket() instanceof C03PacketPlayer) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer());
                }

                if (mc.thePlayer != null && mc.thePlayer.ticksExisted <= 7) {
                    timer.reset();
                    packetQueue.clear();
                }
            } else if (mode.getValue().get("OmegaCraft")) {

                if (event.getPacket() instanceof S32PacketConfirmTransaction || event.getPacket() instanceof C0FPacketConfirmTransaction)
                    event.setCancelled(true);

            } else if (mode.getValue().get("Viper")) {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer packetPlayer = (C03PacketPlayer) event.getPacket();
                    if (mc.thePlayer.ticksExisted < 50) {

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition());
                    } else {
                        packetPlayer.y += 0.42F;
                    }
                }

            } else if (mode.getValue().get("Ghostly")) {
                if (event.getPacket() instanceof S00PacketKeepAlive) {
                    event.setCancelled(true);
                }
                if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                    event.setCancelled(true);
                }
                if (event.getPacket() instanceof C03PacketPlayer) {
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0CPacketInput());
                }
                if (event.getPacket() instanceof C0CPacketInput) {
                    C0CPacketInput packet = (C0CPacketInput) event.getPacket();
                    packet.forwardSpeed = Float.MAX_VALUE;
                    packet.strafeSpeed = Float.MAX_VALUE;
                    packet.jumping = (mc.thePlayer.ticksExisted % 2 == 0);
                    packet.sneaking = (mc.thePlayer.ticksExisted % 2 != 0);
                }
            } else if (mode.getValue().get("Watchdog")) {
                if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.contains("hypixel.net")) {
                    if (mc.thePlayer.ticksExisted % 29 == 0) {
                        PlayerCapabilities pc = new PlayerCapabilities();
                        pc.allowFlying = true;
                        pc.isFlying = true;
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C13PacketPlayerAbilities(pc));
                    }

                    if (event.getPacket() instanceof C0FPacketConfirmTransaction || event.getPacket() instanceof S32PacketConfirmTransaction) {
                        event.setCancelled(true);
                    }
                } else return;
            }


        } else if (mode.getValue().get("Poopful") && mc.thePlayer != null && mc.thePlayer.getDistance(mc.thePlayer.prevPosX, mc.thePlayer.prevPosY, mc.thePlayer.prevPosZ) > 0.6D && event.getPacket() instanceof C03PacketPlayer) {
            if (this.iNigga > 2) {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) / 2.0D, mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) / 2.0D, mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) / 2.0D, true));
                this.iNigga = 0;
            } else {
                event.setCancelled(true);
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C00PacketKeepAlive(-2147483648));
            }
            this.iNigga++;
        }
    }

    @Collect
    public void onPacketIn(ProcessPacketEvent event) {

        if (mode.getValue().get("Viper")) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                S08PacketPlayerPosLook playerPosLook = (S08PacketPlayerPosLook) event.getPacket();

                playerPosLook.y += 1.0E-4F;
            }
        }

    }


    @Collect
    public void onPacketSend(final PlayerMoveEvent event) {
        setMode(this.mode.getSelectedStrings().get(0));
        /*/if (mc.thePlayer.ticksExisted % 100 == 0 && !this.delayedtransactions2.isEmpty() && delayedtransactions2 != null) {
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(this.delayedtransactions2.get(this.confirmtransactioncounter));
            ++this.confirmtransactioncounter;
        }/*/
    }

}
