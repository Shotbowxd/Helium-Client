package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerMoveEvent;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.event.minecraft.SendPacketEvent;
import rip.helium.utils.SpeedUtils;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.Vec3d;
import rip.helium.utils.property.impl.BooleanProperty;
import rip.helium.utils.property.impl.DoubleProperty;
import rip.helium.utils.property.impl.StringsProperty;

public class Flight extends Cheat {
    public static long disabled;
    private final StringsProperty prop_mode;
    public double lastreporteddistance;
    public double movementSpeed;
    int stage;
    double speed;
    double mot;
    boolean allowmcc;
    DoubleProperty sped = new DoubleProperty("Speed", "epic", null, 1.1, 0.1, 10.0, 0.1, null);
    int i = 0;
    private boolean back;
    private int counter;
    private double moveSpeed, lastDist;
    private int ticks;
    private double yNigga;
    private boolean down;
    private double mineplexSpeed;
    private final BooleanProperty antikick;
    private final BooleanProperty prop_bobbing;
    private final BooleanProperty prop_memebobbing;
    private final Stopwatch timer = new Stopwatch();


    public Flight() {
        super("Flight", "Fuck that little faggot!", CheatCategory.MOVEMENT);
        this.prop_mode = new StringsProperty("Flight", "How this cheat will function.", null, false, false, new String[]{"Vanilla", "LongjumpFly"}, new Boolean[]{true, false});
        this.prop_bobbing = new BooleanProperty("View Bobbing", "bippity boppity", null, false);
        this.prop_memebobbing = new BooleanProperty("MEME BOBBING", "FUCKING BOBBING XDDDDD", null, false);
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
        mineplexSpeed = 0;
        speed = 0;
        timer.reset();
        this.yNigga = 0.0D;
        ticks = 0;
        back = false;
        down = false;
        counter = 0;
        stage = 0;
        this.mineplexSpeed = SpeedUtils.getBaseMoveSpeed() + 0.115D;
        mineplexSpeed = 0;
        back = false;
        down = false;
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
        }
    }

    @Collect
    public void onPlayerMove(PlayerMoveEvent event) {
        switch (prop_mode.getSelectedStrings().get(0)) {
            case "Vanilla": {
                SpeedUtils.setPlayerSpeed(event, sped.getValue());
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
