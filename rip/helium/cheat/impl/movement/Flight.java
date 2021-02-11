package rip.helium.cheat.impl.movement;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import de.gerrygames.viarewind.utils.PacketUtil;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import rip.helium.ChatUtil;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.BoundingBoxEvent;
import rip.helium.event.minecraft.PlayerMoveEvent;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.event.minecraft.SendPacketEvent;
import rip.helium.utils.*;
import rip.helium.utils.property.impl.BooleanProperty;
import rip.helium.utils.property.impl.DoubleProperty;
import rip.helium.utils.property.impl.StringsProperty;

import java.util.ArrayList;
import java.util.List;

public class Flight extends Cheat {
    public static long disabled;
    private final StringsProperty prop_mode;
    public double lastreporteddistance;
    public double movementSpeed, lastDist, y;
    int stage;
    double speed;
    boolean allowmcc;
    DoubleProperty sped = new DoubleProperty("Speed", "epic", null, 1.1, 0.1, 10.0, 0.1, null);
    int i = 0;
    private final BooleanProperty antikick;
    private final BooleanProperty prop_bobbing;
    private final BooleanProperty prop_memebobbing;
    private final Stopwatch timer = new Stopwatch();

    boolean canPacketBoost;
    private int level;
    private long lastboost;
    private boolean initialboost, secondaryboost, theogdamageboost;
    private int delay, counter;
    public double oldX, oldY, oldZ;

    ArrayList<Packet> blinkNigger = new ArrayList<>();


    public Flight() {
        super("Flight", "Fuck that little faggot!", CheatCategory.MOVEMENT);
        this.prop_mode = new StringsProperty("Flight", "How this cheat will function.", null, false, false, new String[]{"Vanilla", "LongjumpFly", "WatchdogVoid", "Collide", "Watchdog"}, new Boolean[]{true, false, false, false, false});
        this.prop_bobbing = new BooleanProperty("View Bobbing", "bippity boppity", null, false);
        this.prop_memebobbing = new BooleanProperty("Meme Bobbing", "FUCKING BOBBING XDDDDD", null, false);
        this.antikick = new BooleanProperty("AntiKick", "no vanilla kicke", null, true);
        this.registerProperties(this.prop_mode, this.prop_bobbing, this.prop_memebobbing, antikick, sped);
    }

    public static void placeHeldItemUnderPlayer() {
        final BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1,
                mc.thePlayer.posZ);
        final Vec3d vec = new Vec3d(blockPos).addVector(0.4F, 0.4F, 0.4F);
        mc.playerController.onPlayerRightClic(mc.thePlayer, mc.theWorld, null, blockPos, EnumFacing.UP,
                vec.scale(0.4));
    }

    @Override
    public void onDisable() {
        super.onDisable();
//        for(Packet packet : blinkNigger){
//            mc.getNetHandler().addToSendQueueNoEvent(packet);
//        }\
        Timer.timerSpeed = 1.0f;
        blinkNigger.clear();
        allowmcc = false;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        blinkNigger.clear();
        y = 0;
        i = 0;
        lastDist = 0;
        speed = 0;
        stage = 0;
        timer.reset();
    }



    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent event) {
//        if (this.antikick.getValue() && this.timer.hasPassed(700.0f)) {
//            this.fall();
//            this.ascend();
//            this.timer.reset();
//        }
//        if (this.prop_bobbing.getValue()) {
//            mc.thePlayer.cameraYaw = 0.11f;
//        }
//        if (this.prop_memebobbing.getValue()) {
//            mc.thePlayer.cameraYaw = 0.31f;
//        }
        switch (prop_mode.getSelectedStrings().get(0)) {
            case "LongjumpFly": {
                if (mc.gameSettings.keyBindJump.pressed) {
                    mc.thePlayer.motionY = 2;
                    SpeedUtils.setPlayerSpeed(0);
                }
                if (!mc.thePlayer.onGround && mc.thePlayer.isMoving() && !mc.gameSettings.keyBindJump.pressed) {
                    SpeedUtils.setPlayerSpeed(2.9);
                    if (mc.thePlayer.ticksExisted % 10 == 0) {
                        mc.thePlayer.onGround = true;
                        mc.thePlayer.jump();
                        mc.thePlayer.onGround = false;
                    }
                }
                break;
            }
            case "Vanilla": {
                if (event.isPre()) {
                    if (mc.thePlayer.movementInput.jump) {
                        mc.thePlayer.motionY = 2;
                    } else if (mc.thePlayer.movementInput.sneak) {
                        mc.thePlayer.motionY = -2;
                    } else {
                        mc.thePlayer.motionY = 0.0D;
                    }
                }
                break;
            }
            case "WatchdogVoid": {
                break;
            }
            case "Watchdog": {
                break;
            }
        }
        if (event.isPre()) {
            double xDif = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDif = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastDist = Math.sqrt(xDif * xDif + zDif * zDif);
        }
    }

    @Collect
    public void onPlayerMove(PlayerMoveEvent event) {
        switch (prop_mode.getSelectedStrings().get(0)) {
            case "Vanilla": {
                SpeedUtils.setPlayerSpeed(event, sped.getValue());
                break;
            }
            case "Watchdog": {
                    switch (stage) {
                        case 0:
                            if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                                PlayerUtils.damageHypixel();
                                event.setY(mc.thePlayer.motionY = MovementUtils.getJumpBoostModifier( 0.42F));
                            }
                            speed = MovementUtils.getBaseMoveSpeed() * 2.149D;
                            break;
                        case 1:
                            Timer.timerSpeed = 1.565f;
                            speed = 1.55;
                            break;
                        default:
                            event.setY(mc.thePlayer.motionY = mc.thePlayer.ticksExisted % 4 == 0 ? -0.001875 : 0.001875);
                            speed -= speed / 159;
                            break;
                    }
                    speed = Math.max(speed, MovementUtils.getBaseMoveSpeed());
                    stage++;
                MovementUtils.setSpeed(event, speed);
                if (!TargetStrafe.doStrafeAtSpeed(event, speed)) {
                    MovementUtils.setSpeed(event, speed);
                }
                break;
            }
            case "WatchdogVoid": {
                mc.timer.timerSpeed = 0.2488F;
                SpeedUtils.setPlayerSpeed(event, sped.getValue());
                event.setY(mc.thePlayer.motionY = mc.thePlayer.movementInput.jump ? sped.getValue() : mc.thePlayer.movementInput.sneak ? -sped.getValue() : 0.0D);
                mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + event.getX(), mc.thePlayer.posY + event.getY(), mc.thePlayer.posZ + event.getZ(), true));
                mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 20.0D, mc.thePlayer.posY, mc.thePlayer.posZ + 20.0D, true));
                break;
            }
        }
    }

    @Collect
    public void onPacketSend(SendPacketEvent event) {
        switch (prop_mode.getSelectedStrings().get(0)) {
            case "LongjumpFly": {
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
                    break;
                }
            }
            case "Watchdog": {
//                if(event.getPacket() instanceof C03PacketPlayer && mc.thePlayer.ticksExisted % 2 == 0){
//                    C03PacketPlayer packetPlayer = (C03PacketPlayer) event.getPacket();
//                    event.setCancelled(true);
//                    if(packetPlayer.isMoving()) {
//                        i++;
//                        if(i == 18){
//                            mc.getNetHandler().addToSendQueueNoEvent(packetPlayer);
//                            i = 0;
//                        }
//
//                    }
//
//                }
                break;
            }
            case "WatchdogVoid": {
                if(event.getPacket() instanceof C03PacketPlayer) event.setCancelled(true);
                break;
            }
        }
    }

    @Collect
    public void onCollide(BoundingBoxEvent event) {
        if (prop_mode.getValue().get("Collide")) {
            event.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ()));
        }
    }

    private boolean isColliding(final AxisAlignedBB box) {
        return mc.theWorld.checkBlockCollision(box);
    }

    public double getGroundLevel() {
        for (int i = (int) Math.round(mc.thePlayer.posY); i > 0; --i) {
            final AxisAlignedBB box = mc.thePlayer.boundingBox.addCoord(0.0, 0.0, 0.0);
            box.minY = i - 1;
            box.maxY = i;
            if (this.isColliding(box) && box.minY <= mc.thePlayer.posY) {
                return i;
            }
        }
        return 0.0;
    }

    public double fall() {
        double i;
        for (i = mc.thePlayer.posY; i > this.getGroundLevel(); i -= 8.0) {
            if (i < this.getGroundLevel()) {
                i = this.getGroundLevel();
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, i, mc.thePlayer.posZ, true));
        }
        return i;
    }

    private void ascend() {
        for (double i = this.getGroundLevel(); i < mc.thePlayer.posY; i += 8.0) {
            if (i > mc.thePlayer.posY) {
                i = mc.thePlayer.posY;
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, i, mc.thePlayer.posZ, true));
        }
    }
}
