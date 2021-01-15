package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
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
import rip.helium.utils.SpeedUtils;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.Vec3d;
import rip.helium.utils.property.impl.BooleanProperty;
import rip.helium.utils.property.impl.DoubleProperty;
import rip.helium.utils.property.impl.StringsProperty;

import java.util.ArrayList;
import java.util.List;

public class Flight extends Cheat {
    public static long disabled;
    private final StringsProperty prop_mode;
    public double lastreporteddistance;
    public double movementSpeed;
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
        if (prop_mode.getValue().get("Watchdog") || prop_mode.getValue().get("WatchdogVoid")) {
            for (Packet georgefloyd : blinkNigger) {
                mc.getNetHandler().addToSendQueue(georgefloyd);
                ChatUtil.chat("sent " + georgefloyd.toString());
            }
        }
        blinkNigger.clear();
        mc.gameSettings.keyBindJump.pressed = false;
        Timer.timerSpeed = 1.0f;
        allowmcc = false;
        getPlayer().stepHeight = 0.6f;
        getPlayer().setSpeed(0.0);
        Flight.disabled = System.currentTimeMillis();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (this.prop_bobbing.getValue()) {
            mc.thePlayer.cameraYaw = 0.11f;
        }
        if (this.prop_memebobbing.getValue()) {
            mc.thePlayer.cameraYaw = 0.41f;
        }
        speed = 0;
        timer.reset();
        stage = 0;
        if (prop_mode.getValue().get("Watchdog")) {
            oldY = getPlayer().posY;
            canPacketBoost = false;
            getPlayer().stepHeight = 0;
            if (mc.thePlayer.fallDistance > 3 || System.currentTimeMillis() - lastboost >= 500 && prop_mode.getValue().get("Watchdog") && getPlayer().onGround && getPlayer().isMoving()) {
                double fallDistance = 3.0125;
                while (fallDistance > 0) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY +  0.0624986421, mc.thePlayer.posZ, false));
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY +  0.0000013579, mc.thePlayer.posZ, false));
                    fallDistance -= 0.0624986421;
                }
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                mc.thePlayer.setMotion(0);
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                getPlayer().motionY = 0.416868886655f;
                theogdamageboost = true;
                secondaryboost = false;
                initialboost = false;
                lastboost = System.currentTimeMillis();
            } else {
                mc.thePlayer.setSpeed(0);
                theogdamageboost = false;
                secondaryboost = false;
                initialboost = false;
            }
            level = 1;

            mc.thePlayer.packets = 0;
            mc.thePlayer.packets2 = 0;

            mc.thePlayer.hurtTime = 0;

        }
    }



    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        setMode(prop_mode.getSelectedStrings().get(0));
        if (this.antikick.getValue() && this.timer.hasPassed(700.0f)) {
            this.fall();
            this.ascend();
            this.timer.reset();
        }
        if (this.prop_bobbing.getValue()) {
            mc.thePlayer.cameraYaw = 0.11f;
        }
        if (this.prop_memebobbing.getValue()) {
            mc.thePlayer.cameraYaw = 0.31f;
        }
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
                if (!mc.thePlayer.onGround) {
                    if (mc.thePlayer.isMoving()) {
                        SpeedUtils.setPlayerSpeed(1.5);
                    }
                    mc.thePlayer.motionY = 0;
                } else {
                    ChatUtil.chat("bro it void fly jump in void idiot");
                }
                break;
            }
            case "Watchdog": {
                if (event.isPre()) {
                    if (getPlayer().movementInput.jump) {
                        getPlayer().motionY += 0.26622;
                    } else if (getPlayer().movementInput.sneak) {
                        getPlayer().motionY -= 0.26622;
                    } else {
                        lastreporteddistance = Math.hypot(getPlayer().posX - getPlayer().prevPosX, getPlayer().posZ - getPlayer().prevPosZ) * .99;
                        if (initialboost || secondaryboost) {
                            delay -= 1;
                            setMode("HypixelFast " + delay);
                            if (delay <= 0) {

                                initialboost = false;
                                secondaryboost = true;

                            }
                            if (delay <= -10) {
                                mc.timer.timerSpeed = 1.0F;
                                secondaryboost = false;
                            }
                        } else {
                            setMode("HypixelFast");
                        }
                        getPlayer().motionY = 0;
                        getPlayer().onGround = true;
                        counter++;
                        if (prop_bobbing.getValue())
                            getPlayer().cameraYaw = .105f;

                        if (counter == 1) {
                            getPlayer().setPosition(getPlayer().posX, getPlayer().posY + 1.0E-13D, getPlayer().posZ);
                        } else if (counter == 2) {
                            getPlayer().setPosition(getPlayer().posX, getPlayer().posY + 1.0E-123D, getPlayer().posZ);
                            counter = 0;
                        }
                    }
                }
                break;
            }
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
                if (!canPacketBoost) {
                    if (prop_mode.getValue().get("Watchdog")) {
                        List collidingList = mc.theWorld.getCollidingBoundingBoxes(getPlayer(), getPlayer().getEntityBoundingBox().offset(0, 0, 0));
                        if (theogdamageboost) {
                            if (level != 1 || !getPlayer().isMoving()) {
                                if (level == 2) {
                                    level = 3;
                                    movementSpeed *= mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 2.14 : 2.22;
                                } else if (level == 3) {
                                    level = 4;
                                    double difference = (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? .0079 : .0002) * (lastreporteddistance - mc.thePlayer.getBaseMoveSpeed());
                                    movementSpeed = lastreporteddistance - difference;
                                } else {
                                    if (collidingList.size() > 0 || getPlayer().isCollidedVertically) {
                                        level = 1;
                                    }
                                    movementSpeed = lastreporteddistance - (lastreporteddistance / 95000);
                                }
                            } else {
                                level = 2;
                                double boost = getPlayer().isPotionActive(Potion.moveSpeed) ? 1.89 : 2.24;
                                movementSpeed = boost * mc.thePlayer.getBaseMoveSpeed();

                            }
                            mc.thePlayer.setMoveSpeedAris(event, movementSpeed = Math.max(mc.thePlayer.getBaseMoveSpeed(), movementSpeed));
                        } else if (initialboost) {
                            if (level != 1 || !getPlayer().isMoving()) {
                                mc.timer.timerSpeed = 1.0F;
                                if (level == 2) {
                                    level = 3;
                                    movementSpeed *= getPlayer().isPotionActive(Potion.moveSpeed) ? 2.14 : 2.2;
                                } else if (level == 3) {
                                    level = 4;
                                    double difference = (getPlayer().isPotionActive(Potion.moveSpeed) ? .7 : .6) * (lastreporteddistance - mc.thePlayer.getBaseMoveSpeed());
                                    movementSpeed = lastreporteddistance - difference;
                                } else {
                                    if (collidingList.size() > 0 || getPlayer().isCollidedVertically) {
                                        level = 1;
                                    }
                                    movementSpeed = lastreporteddistance - lastreporteddistance / (120 + delay);
                                }
                            } else {
                                level = 2;
                                mc.timer.timerSpeed = 0.42F;
                                double boost = getPlayer().isPotionActive(Potion.moveSpeed) ? 1.85 : 2.13;
                                movementSpeed = boost * mc.thePlayer.getBaseMoveSpeed();
                            }
                            mc.thePlayer.setMoveSpeedAris(event, movementSpeed = Math.max(mc.thePlayer.getBaseMoveSpeed(), movementSpeed));
                        }
                    }
                } else {
                    mc.thePlayer.setMoveSpeedAris(event,  4.4);
                }
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
                if (event.getPacket() instanceof C03PacketPlayer) {
                    blinkNigger.add(event.getPacket());
                    event.setCancelled(true);
                }
                break;
            }
            case "WatchdogVoid": {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    event.setCancelled(true);
                    blinkNigger.add(event.getPacket());
                }
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
