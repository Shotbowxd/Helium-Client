package rip.helium.module.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.client.MouseClickEvent;
import rip.helium.event.events.impl.network.PacketReceiveEvent;
import rip.helium.event.events.impl.network.PacketSendEvent;
import rip.helium.event.events.impl.player.BlockStepEvent;
import rip.helium.event.events.impl.player.PlayerJumpEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.client.Timer;
import rip.helium.utils.entity.PlayerUtils;
import rip.helium.utils.entity.UPlayer;
import rip.helium.utils.misc.MathUtils;
import rip.helium.utils.render.ColorUtils;
import rip.helium.utils.world.BlockUtils;

public class TPAura extends Module {
	
	public TPAura(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		
		ArrayList<String> priorities = new ArrayList<String>();
		priorities.add("Lowest Health");
		priorities.add("Least Armor");
		priorities.add("Closest");
		
		this.targetPriority = new Setting("Target Priority", this, "Lowest Health", priorities);
		
		this.players = new Setting("Players", this, true);
		this.monsters = new Setting("Monsters", this, false);
		this.animals = new Setting("Animals", this, false);
		this.villagers = new Setting("Villagers", this, false);
		this.golems = new Setting("Golems", this, false);
		this.prioritisePlayers = new Setting("Prioritise Players", this, true);
		this.keepSprint = new Setting("Keep Sprint", this, true);
		this.teams = new Setting("Hypixel Teams", this, false);
		this.minSpeed = new Setting("Minimum APS", this, 6, 1, 20, false);
		this.maxSpeed = new Setting("Maximum APS", this, 8, 1, 20, false);
		this.distance = new Setting("Maximum Distance", this, 100.0, 0.1, 150.0, false);
		this.fakeab = new Setting("Auto Block", this, true);
		
		ArrayList<String> abmodes = new ArrayList<String>();
		abmodes.add("Real");
		abmodes.add("Fake");
		abmodes.add("Hypixel");
		abmodes.add("Ghostly");
		
		this.abMode = new Setting("Auto Block Mode", this, "Real", abmodes);
		
		mc.hackedClient.getSettingManager().addSetting(this.abMode);
		mc.hackedClient.getSettingManager().addSetting(this.targetPriority);
		mc.hackedClient.getSettingManager().addSetting(this.players);
		mc.hackedClient.getSettingManager().addSetting(this.monsters);
		mc.hackedClient.getSettingManager().addSetting(this.animals);
		mc.hackedClient.getSettingManager().addSetting(this.villagers);
		mc.hackedClient.getSettingManager().addSetting(this.golems);
		mc.hackedClient.getSettingManager().addSetting(this.prioritisePlayers);
		mc.hackedClient.getSettingManager().addSetting(this.keepSprint);
		mc.hackedClient.getSettingManager().addSetting(this.teams);
		mc.hackedClient.getSettingManager().addSetting(this.minSpeed);
		mc.hackedClient.getSettingManager().addSetting(this.maxSpeed);
		mc.hackedClient.getSettingManager().addSetting(this.distance);
		mc.hackedClient.getSettingManager().addSetting(this.fakeab);
		
		isBlocking = false;
        targetIndex = 0;
        mcf = new ArrayList<>();
        this.sr = new ScaledResolution(mc);
        targetList = new ArrayList<>();
        apsStopwatch = new Timer();
        ignoredEntities = new ArrayList<>();
        whitelistedEntity = new ArrayList<>();
        botClearStopwatch = new Timer();
	}
	private Setting abMode;
	private Setting targetPriority;
	private Setting players;
	private Setting monsters;
	private Setting animals;
	private Setting villagers;
	private Setting golems;
	private Setting prioritisePlayers;
	private Setting keepSprint;
	private Setting teams;
	private Setting minSpeed;
	private Setting maxSpeed;
	private Setting distance;
	private Setting fakeab;
	
	private boolean isBlocking, apsDecrease, yawDecrease, pitchDecrease;
    private Timer apsStopwatch, botClearStopwatch;
    private float animated = 20f;
    public static int blockDelay, attackSpeed;
    public int waitDelay, groundTicks, crits;
    private float aps;
    public long lastHit;
    private ScaledResolution sr;
    private static int auraDelay;
    private float yaw, pitch, yawIncrease, pitchIncrease, serverSideYaw, serverSidePitch;
    public static int targetIndex;

    double x;
    double y;
    double z;
    double xPreEn;
    double yPreEn;
    double zPreEn;
    double xPre;
    double yPre;
    double zPre;
    
    boolean attack = false;
    int stage = 0;
    ArrayList<Vec3> positions = new ArrayList();
    ArrayList<Vec3> positionsBack = new ArrayList();
    public static final double maxXZTP = 9.5;
    public static final int maxYTP = 9;
    
    private ArrayList<EntityLivingBase> mcf;
    private static ArrayList<EntityLivingBase> targetList;
    public static EntityLivingBase currentEntity;
    public ArrayList<EntityLivingBase> ignoredEntities;
    private ArrayList<EntityLivingBase> whitelistedEntity;
	
	public boolean isBlocking() {
		return this.isBlocking;
	}
	
	public float[] aim(Entity ent, UpdateEvent event, boolean BBB) {
        double x = ent.posX - Minecraft.getMinecraft().thePlayer.posX, y = ent.posY + (BBB ? .1 : ent.getEyeHeight() / 2) - event.getPosY() - (BBB ? 0 : 1.2), z = ent.posZ - Minecraft.getMinecraft().thePlayer.posZ;


        return new float[]{MathHelper.wrapAngleTo180_float((float) (Math.atan2(z, x) * 180 / Math.PI) - 90), (float) -(Math.atan2(y, MathHelper.sqrt_double(x * x + z * z)) * 180 / Math.PI)};
    }
	
	public float[] getRotations(Entity e, UpdateEvent event) {
		float randomr = (float) MathUtils.getRandomInRange(0.01, 0.999999);
		float randomr2 = (float) MathUtils.getRandomInRange(0.01, 0.999999);
		return new float[] {randomr + aim(e, event, false)[0], randomr2 + aim(e, event, false)[1]};
	}
	
	public void setDelay(int delay) {//set the delay to actually use aura
        auraDelay = delay;
    }
	
	public void sendPacket(boolean goingBack, ArrayList<Vec3> positionsBack, ArrayList<Vec3> positions) {
        C03PacketPlayer.C04PacketPlayerPosition playerPacket = new C03PacketPlayer.C04PacketPlayerPosition(this.x, this.y, this.z, true);
        this.mc.getNetHandler().getNetworkManager().sendPacket((Packet)playerPacket);
        if (goingBack) {
            positionsBack.add(new Vec3(this.x, this.y, this.z));
            return;
        }
        positions.add(new Vec3(this.x, this.y, this.z));
    }
	
	public static double normalizeAngle(double angle) {
        return (angle + 360.0) % 360.0;
    }

    public static float normalizeAngle(float angle) {
        return (angle + 360.0f) % 360.0f;
    }
    
    public static float[] getFacePosRemote(Vec3 src, Vec3 dest) {
        double diffX = dest.xCoord - src.xCoord;
        double diffY = dest.yCoord - src.yCoord;
        double diffZ = dest.zCoord - src.zCoord;
        double dist = MathHelper.sqrt_double((double)(diffX * diffX + diffZ * diffZ));
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        return new float[]{MathHelper.wrapAngleTo180_float((float)yaw), MathHelper.wrapAngleTo180_float((float)pitch)};
    }
	
	public MovingObjectPosition rayTracePos(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        float[] rots = TPAura.getFacePosRemote(vec32, vec31);
        float yaw = rots[0];
        double angleA = Math.toRadians(TPAura.normalizeAngle(yaw));
        double angleB = Math.toRadians(TPAura.normalizeAngle(yaw) + 180.0f);
        double size = 2.1;
        double size2 = 2.1;
        Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord, vec31.zCoord + Math.sin(angleA) * size);
        Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord, vec31.zCoord + Math.sin(angleB) * size);
        Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord, vec32.zCoord + Math.sin(angleA) * size);
        Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord, vec32.zCoord + Math.sin(angleB) * size);
        Vec3 leftA = new Vec3(vec31.xCoord + Math.cos(angleA) * size2, vec31.yCoord, vec31.zCoord + Math.sin(angleA) * size2);
        Vec3 rightA = new Vec3(vec31.xCoord + Math.cos(angleB) * size2, vec31.yCoord, vec31.zCoord + Math.sin(angleB) * size2);
        Vec3 left2A = new Vec3(vec32.xCoord + Math.cos(angleA) * size2, vec32.yCoord, vec32.zCoord + Math.sin(angleA) * size2);
        Vec3 right2A = new Vec3(vec32.xCoord + Math.cos(angleB) * size2, vec32.yCoord, vec32.zCoord + Math.sin(angleB) * size2);
        MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        MovingObjectPosition trace5 = mc.theWorld.rayTraceBlocks(rightA, right2A, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        this.positionsBack.add(right);
        this.positionsBack.add(right2);
        this.positionsBack.add(left);
        this.positionsBack.add(left2);
        MovingObjectPosition trace4 = null;
        MovingObjectPosition trace51 = null;
        if (trace2 != null || trace1 != null || trace3 != null || trace4 != null || trace51 != null) {
            if (returnLastUncollidableBlock) {
                if (trace51 != null && (this.getBlock(trace51.getBlockPos()).getMaterial() != Material.air || trace51.entityHit != null)) {
                    return trace51;
                }
                if (trace4 != null && (this.getBlock(trace4.getBlockPos()).getMaterial() != Material.air || trace4.entityHit != null)) {
                    return trace4;
                }
                if (trace3 != null && (this.getBlock(trace3.getBlockPos()).getMaterial() != Material.air || trace3.entityHit != null)) {
                    return trace3;
                }
                if (trace1 != null && (this.getBlock(trace1.getBlockPos()).getMaterial() != Material.air || trace1.entityHit != null)) {
                    return trace1;
                }
                if (trace2 != null && (this.getBlock(trace2.getBlockPos()).getMaterial() != Material.air || trace2.entityHit != null)) {
                    return trace2;
                }
            } else {
                if (trace51 != null) {
                    return trace51;
                }
                if (trace4 != null) {
                    return trace4;
                }
                if (trace3 != null) {
                    return trace3;
                }
                if (trace1 != null) {
                    return trace1;
                }
                if (trace2 != null) {
                    return trace2;
                }
            }
        }
        if (trace2 == null) {
            if (trace3 == null) {
                if (trace1 == null) {
                    if (trace51 == null) {
                        if (trace4 == null) {
                            return null;
                        }
                        return trace4;
                    }
                    return trace51;
                }
                return trace1;
            }
            return trace3;
        }
        return trace2;
    }
	
	public Block getBlock(BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }
	
	public boolean rayTraceWide(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        float yaw = TPAura.getFacePosRemote(vec32, vec31)[0];
        yaw = TPAura.normalizeAngle(yaw);
        yaw += 180.0f;
        yaw = MathHelper.wrapAngleTo180_float((float)yaw);
        double angleA = Math.toRadians(yaw);
        double angleB = Math.toRadians(yaw + 180.0f);
        double size = 2.1;
        double size2 = 2.1;
        Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord, vec31.zCoord + Math.sin(angleA) * size);
        Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord, vec31.zCoord + Math.sin(angleB) * size);
        Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord, vec32.zCoord + Math.sin(angleA) * size);
        Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord, vec32.zCoord + Math.sin(angleB) * size);
        Vec3 leftA = new Vec3(vec31.xCoord + Math.cos(angleA) * size2, vec31.yCoord, vec31.zCoord + Math.sin(angleA) * size2);
        Vec3 rightA = new Vec3(vec31.xCoord + Math.cos(angleB) * size2, vec31.yCoord, vec31.zCoord + Math.sin(angleB) * size2);
        Vec3 left2A = new Vec3(vec32.xCoord + Math.cos(angleA) * size2, vec32.yCoord, vec32.zCoord + Math.sin(angleA) * size2);
        Vec3 right2A = new Vec3(vec32.xCoord + Math.cos(angleB) * size2, vec32.yCoord, vec32.zCoord + Math.sin(angleB) * size2);
        MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        MovingObjectPosition trace4 = null;
        MovingObjectPosition trace5 = null;
        if (returnLastUncollidableBlock) {
            return trace1 != null && this.getBlock(trace1.getBlockPos()).getMaterial() != Material.air || trace2 != null && this.getBlock(trace2.getBlockPos()).getMaterial() != Material.air || trace3 != null && this.getBlock(trace3.getBlockPos()).getMaterial() != Material.air || trace4 != null && this.getBlock(trace4.getBlockPos()).getMaterial() != Material.air || trace5 != null && this.getBlock(trace5.getBlockPos()).getMaterial() != Material.air;
        }
        return trace1 != null || trace2 != null || trace3 != null || trace5 != null || trace4 != null;
    }
	
	public boolean infiniteReach(double range, double maxXZTP, double maxYTP, ArrayList<Vec3> positionsBack, ArrayList<Vec3> positions, EntityLivingBase en) {
        int ind = 0;
        this.xPreEn = en.posX;
        this.yPreEn = en.posY;
        this.zPreEn = en.posZ;
        this.xPre = mc.thePlayer.posX;
        this.yPre = mc.thePlayer.posY;
        this.zPre = mc.thePlayer.posZ;
        boolean attack = true;
        boolean up = false;
        boolean tpUpOneBlock = false;
        boolean hit = false;
        boolean tpStraight = false;
        boolean sneaking = mc.thePlayer.isSneaking();
        positions.clear();
        positionsBack.clear();
        double step = maxXZTP / range;
        int steps = 0;
        int i = 0;
        while ((double)i < range) {
            if (maxXZTP * (double)(++steps) > range) break;
            ++i;
        }
        MovingObjectPosition rayTrace = null;
        MovingObjectPosition rayTrace1 = null;
        Object rayTraceCarpet = null;
        if (this.rayTraceWide(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vec3(en.posX, en.posY, en.posZ), false, false, true) || (rayTrace1 = this.rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ), new Vec3(en.posX, en.posY + (double)mc.thePlayer.getEyeHeight(), en.posZ), false, false, true)) != null) {
            rayTrace = this.rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vec3(en.posX, mc.thePlayer.posY, en.posZ), false, false, true);
            if (rayTrace != null || (rayTrace1 = this.rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ), new Vec3(en.posX, mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight(), en.posZ), false, false, true)) != null) {
                MovingObjectPosition trace = null;
                if (rayTrace == null) {
                    trace = rayTrace1;
                }
                if (rayTrace1 == null) {
                    trace = rayTrace;
                }
                if (trace == null) {
                    this.y = mc.thePlayer.posY;
                    this.yPreEn = mc.thePlayer.posY;
                } else {
                    if (trace.getBlockPos() == null) return false;
                    boolean fence = false;
                    BlockPos target = trace.getBlockPos();
                    up = true;
                    this.y = target.up().getY();
                    this.yPreEn = target.up().getY();
                    Block lastBlock = null;
                    Boolean found = false;
                    int i2 = 0;
                    while ((double)i2 < maxYTP) {
                        MovingObjectPosition tr = this.rayTracePos(new Vec3(mc.thePlayer.posX, (double)(target.getY() + i2), mc.thePlayer.posZ), new Vec3(en.posX, (double)(target.getY() + i2), en.posZ), false, false, true);
                        if (tr != null && tr.getBlockPos() != null) {
                            BlockPos blockPos = tr.getBlockPos();
                            Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                            if (block.getMaterial() != Material.air) {
                                lastBlock = block;
                            } else {
                                fence = lastBlock instanceof BlockFence;
                                this.y = target.getY() + i2;
                                this.yPreEn = target.getY() + i2;
                                if (fence) {
                                    this.y += 1.0;
                                    this.yPreEn += 1.0;
                                    if ((double)(i2 + 1) > maxYTP) {
                                        found = false;
                                        break;
                                    }
                                }
                                found = true;
                                break;
                            }
                        }
                        ++i2;
                    }
                    double difX = mc.thePlayer.posX - this.xPreEn;
                    double difZ = mc.thePlayer.posZ - this.zPreEn;
                    double divider = step * 0.0;
                    if (!found.booleanValue()) {
                        return false;
                    }
                }
            } else {
                MovingObjectPosition ent = this.rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vec3(en.posX, en.posY, en.posZ), false, false, false);
                if (ent != null && ent.entityHit == null) {
                    this.y = mc.thePlayer.posY;
                    this.yPreEn = mc.thePlayer.posY;
                } else {
                    this.y = mc.thePlayer.posY;
                    this.yPreEn = en.posY;
                }
            }
        }
        if (!attack) {
            return false;
        }
        if (sneaking) {
            this.mc.getNetHandler().getNetworkManager().sendPacket((Packet)new C0BPacketEntityAction((Entity)mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        int i3 = 0;
        while (i3 < steps) {
            double divider;
            double difZ;
            double difY;
            double difX;
            ++ind;
            if (i3 == 1 && up) {
                this.x = mc.thePlayer.posX;
                this.y = this.yPreEn;
                this.z = mc.thePlayer.posZ;
                this.sendPacket(true, positionsBack, positions);
            }
            if (i3 != steps - 1) {
                difX = mc.thePlayer.posX - this.xPreEn;
                difY = mc.thePlayer.posY - this.yPreEn;
                difZ = mc.thePlayer.posZ - this.zPreEn;
                divider = step * (double)i3;
                this.x = mc.thePlayer.posX - difX * divider;
                this.y = mc.thePlayer.posY - difY * (up ? 1.0 : divider);
                this.z = mc.thePlayer.posZ - difZ * divider;
                this.sendPacket(false, positionsBack, positions);
            } else {
                difX = mc.thePlayer.posX - this.xPreEn;
                difY = mc.thePlayer.posY - this.yPreEn;
                difZ = mc.thePlayer.posZ - this.zPreEn;
                divider = step * (double)i3;
                this.x = mc.thePlayer.posX - difX * divider;
                this.y = mc.thePlayer.posY - difY * (up ? 1.0 : divider);
                this.z = mc.thePlayer.posZ - difZ * divider;
                this.sendPacket(false, positionsBack, positions);
                double xDist = this.x - this.xPreEn;
                double zDist = this.z - this.zPreEn;
                double yDist = this.y - en.posY;
                double dist = Math.sqrt(xDist * xDist + zDist * zDist);
                if (dist > 4.0) {
                    this.x = this.xPreEn;
                    this.y = this.yPreEn;
                    this.z = this.zPreEn;
                    this.sendPacket(false, positionsBack, positions);
                } else if (dist > 0.05 && up) {
                    this.x = this.xPreEn;
                    this.y = this.yPreEn;
                    this.z = this.zPreEn;
                    this.sendPacket(false, positionsBack, positions);
                }
                if (Math.abs(yDist) < maxYTP && mc.thePlayer.getDistanceToEntity((Entity)en) >= 4.0f) {
                    this.x = this.xPreEn;
                    this.y = en.posY;
                    this.z = this.zPreEn;
                    this.sendPacket(false, positionsBack, positions);
                    this.attackInf(en);
                } else {
                    attack = false;
                }
            }
            ++i3;
        }
        i3 = positions.size() - 2;
        while (i3 > -1) {
            this.x = positions.get((int)i3).xCoord;
            this.y = positions.get((int)i3).yCoord;
            this.z = positions.get((int)i3).zCoord;
            this.sendPacket(false, positionsBack, positions);
            --i3;
        }
        this.x = mc.thePlayer.posX;
        this.y = mc.thePlayer.posY;
        this.z = mc.thePlayer.posZ;
        this.sendPacket(false, positionsBack, positions);
        if (!attack) {
            if (sneaking) {
                this.mc.getNetHandler().getNetworkManager().sendPacket((Packet)new C0BPacketEntityAction((Entity)mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            positions.clear();
            positionsBack.clear();
            return false;
        }
        if (!sneaking) return true;
        this.mc.getNetHandler().getNetworkManager().sendPacket((Packet)new C0BPacketEntityAction((Entity)mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        return true;
    }
	
	private void attackInf(EntityLivingBase en) {
        if (mc.thePlayer.isBlocking()) {
            mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
        }
        this.mc.getNetHandler().getNetworkManager().sendPacket((Packet)new C02PacketUseEntity((Entity)en, C02PacketUseEntity.Action.ATTACK));
        if (mc.thePlayer.isBlocking()) {
            this.mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
            mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), mc.thePlayer.getHeldItem().getMaxItemUseDuration());
            if (mc.thePlayer.isBlocking()) {
                mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
            }
        }
    }
	
	public void propertyupdate() {

        //Start fresh with a new target list

        targetIndex = -1;
        targetList.clear();
        ignoredEntities.clear();

        /* Reset block start*/

        isBlocking = false;

        /*Reset block state with packet AutoBlock*/
        attemptStopAutoblockNoSet_Watchdog();

        /*Make last reported aps to 6 aps*/
        aps = 1000 / 6;
        yawIncrease = 0;
        pitchIncrease = 0;

        /*Make sure player isn't null when setting yaw - ClickGui can be called from main menu*/
        if (mc.thePlayer != null) {
            serverSideYaw = mc.thePlayer.rotationYaw;
            serverSidePitch = mc.thePlayer.rotationPitch;
        }

        lastHit = System.currentTimeMillis() + 50;
        setDelay(0);
    }
	
	@Override
	public void onEnable() {
		super.onEnable();
		propertyupdate();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		propertyupdate();
	}
	
	@EventTarget
	public void onStep(BlockStepEvent event) {
		if (mc.thePlayer == null)
            return;
        if (mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY < .626 && mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY > .4) {
            waitDelay = 4;
        }
	}
	
	@EventTarget
	public void onPacketReceive(PacketReceiveEvent e) {
		if (keepSprint.getValBoolean()) {
            if (e.getPacket() instanceof C0BPacketEntityAction) {
                if (((C0BPacketEntityAction) e.getPacket()).getAction() == C0BPacketEntityAction.Action.STOP_SPRINTING || ((C0BPacketEntityAction) e.getPacket()).getAction() == C0BPacketEntityAction.Action.START_SPRINTING) {
                    e.setCancelled(true);
                }
            }
        }
	}
	
	public void attack(final EntityLivingBase entity) {
        this.attack(entity, true);
    }

    public void swingItem() {
        mc.thePlayer.swingItem();
    }

    public void attack(final EntityLivingBase entity, final boolean crit) {
        this.swingItem();
        final float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), entity.getCreatureAttribute());
        final boolean vanillaCrit = mc.thePlayer.fallDistance > 0.0f && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.ridingEntity == null;
        mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (crit || vanillaCrit) {
            mc.thePlayer.onCriticalHit(entity);

        }
        if (sharpLevel > 0.0f) {
            mc.thePlayer.onEnchantmentCritical(entity);
        }
    }
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		
		if(getCurrentTarget() == null) {
			aps = (1000 / (int)maxSpeed.getValDouble());
		}
		
		if (getCurrentTarget() != null) {
            if (abMode.getValString().equalsIgnoreCase("Ghostly")) {
                mc.gameSettings.keyBindUseItem.pressed = false;
            }
        }

		if (mc.currentScreen != null)
            return;
        //mc.theWorld.spawnParticle(EnumParticleTypes.CRIT_MAGIC, getCurrentTarget().posX, getCurrentTarget().posY, getCurrentTarget().posX, 2, 3, 4, 1);
        boolean preblocking = !PlayerUtils.isMoving() && mc.thePlayer.ticksExisted % 2 == 0;
        auraDelay = 0;

        if (botClearStopwatch.hasPassed(30000)) {
            ignoredEntities.clear();
            botClearStopwatch.updateLastTime();
        }


        mc.theWorld.getLoadedEntityList().forEach(entity -> {
            if (entity != mc.thePlayer && entity instanceof EntityLivingBase) {
                if (mc.hackedClient.getModuleManager().getModule("Anti Bot").getState() && !mc.getCurrentServerData().serverIP.contains("mineplex")) {
                    if (entity != mc.thePlayer) {
                        if (entity != mc.thePlayer && entity instanceof EntityPlayer) {
                            EntityPlayer entityPlayer = (EntityPlayer) entity;
                            if ((((AntiBot)mc.hackedClient.getModuleManager().getModule("Anti Bot")).bots.contains(entityPlayer))) ignoredEntities.add(entityPlayer);
                            if (!isInTablist(entityPlayer)) {
                                if (!ignoredEntities.contains(entityPlayer)) {
                                    ignoredEntities.add(entityPlayer);
//                                    entityPlayer.setInvisible(true);
                                }
                            }
                        }
                    }
                }
            }
        });

        updateTargetList();

        if (targetList.isEmpty() || targetList.size() - 1 < targetIndex) {
            targetIndex = -1;
            attemptStopAutoblock_Watchdog();
            if (groundTicks != 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, event.getYaw(), event.getPitch(), event.isOnground()));
                groundTicks = 0;
            }
            return;
        }

        if (targetIndex == -1) {
            targetIndex = 0;
            attemptStopAutoblock_Watchdog();
            if (groundTicks != 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, event.getYaw(), event.getPitch(), event.isOnground()));
                groundTicks = 0;
            }
            return;
        }

        if (!isValidTarget(targetList.get(targetIndex))) {
            targetIndex = -1;
            attemptStopAutoblock_Watchdog();
            if (groundTicks != 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, event.getYaw(), event.getPitch(), event.isOnground()));
                groundTicks = 0;
            }
            return;
        }

        if (targetIndex == -1) {
            targetIndex = 0;
            return;
        }

        if (!isValidTarget(targetList.get(targetIndex))) {
            targetIndex = -1;
            attemptStopAutoblock_Watchdog();
            return;
        }

        pitchIncrease += pitchDecrease ? -MathUtils.getRandomInRange(.1, .12) : MathUtils.getRandomInRange(.1, .12);
        if (pitchIncrease >= (Aim(targetList.get(targetIndex), event, false)[1] - 30)) {
            pitchDecrease = true;
        }
        if (event.getPitch() <= (Aim(targetList.get(targetIndex), event, false)[1] + 5)) {
            pitchDecrease = false;
        }
        if (event.isPre()) {
            if (mc.thePlayer.fallDistance != 0) {
                waitDelay = 2;
            }
            boolean cancritical = !mc.hackedClient.getModuleManager().getModule("LongJump").getState() && !mc.hackedClient.getModuleManager().getModule("Flight").getState() && !mc.hackedClient.getModuleManager().getModule("Speed").getState() && targetIndex != -1 && mc.thePlayer.fallDistance == 0.0 && !mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround && !mc.thePlayer.isInWater();
            if (mc.hackedClient.getModuleManager().getModule("Criticals").getState()) {
                if (BlockUtils.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).isFullBlock() && !BlockUtils.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ)).isFullBlock()) {
                    if (waitDelay <= 0) {
                        waitDelay = 0;

                        if (cancritical) {
                            event.setOnground(false);
                            groundTicks += 1;
                            if (groundTicks == 1) {
                                event.setOnground(false);
                                event.setPosY(event.getPosY() + 0.0627878);
                            } else if (groundTicks == 2) {
                                event.setOnground(false);
                                event.setPosY(event.getPosY() + 0.062663);
                            } else if (groundTicks == 3) {

                                event.setOnground(false);
                                event.setPosY(event.getPosY() + 0.0001);
                            } else if (groundTicks >= 4) {
                                event.setOnground(false);
                                event.setPosY(event.getPosY() + 0.0001);
                                groundTicks = 0;
                            }
                        } else {
                            waitDelay = 2;
                        }
                    } else {
                        waitDelay -= 1;
                    }
                } else if (groundTicks != 0) {
                    waitDelay = 4;
                    groundTicks = 0;
                }
            }


            if (!holdingSword() && isBlocking) {
                isBlocking = false;
            }

            if (aps >= (1000 / (maxSpeed.getValDouble() - minSpeed.getValDouble() + 2))) {
                apsDecrease = true;
            }
            if (apsDecrease) {
                if (aps <= ((1000 / (maxSpeed.getValDouble())))) {
                    apsDecrease = false;
                }
            }
            if (abMode.getValString().equalsIgnoreCase("Hypixel")) {
                attemptStopAutoblock_Watchdog();
            }

            //todo: gamer
            blockDelay++;
        } else
            attemptStartAutoblock_Watchdog();


        double minValue = this.minSpeed.getValDouble(), maxValue=this.maxSpeed.getValDouble();
        Random theRandom = new Random();
        double randAps = 0.0;
        blockDelay++;
     // Checking for a valid range-
        if( Double.valueOf(maxValue - minValue).isInfinite() == false) 
        	randAps = minValue + (maxValue - minValue) * theRandom.nextDouble(); randAps = randAps;
        if (apsStopwatch.hasPassed((randAps))) {
        	doReach(this.distance.getValDouble(), true); 
        	
        }
        
        yaw = event.getYaw();
        pitch = event.getPitch();
        if(!(auraDelay <= 0)) {
        	if (!event.isPre()) {
                if (mc.hackedClient.getModuleManager().getModule("Criticals").getState()) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.001, mc.thePlayer.posZ, true));
                    event.setOnground(false);
                }
                for (final Object entity : mc.theWorld.loadedEntityList) {
                    if (entity instanceof EntityLivingBase) {
                        if (entity != mc.thePlayer) {
                            if (mc.thePlayer.getDistanceToEntity((Entity) entity) < distance.getValDouble()) {
                                if (isValidTarget((EntityLivingBase) entity)) {
                                    //attack((EntityLivingBase) entity);
                                    currentEntity = (EntityLivingBase) entity;
                                    if (fakeab.getValBoolean() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
	}
	
	public void doReach(double range, boolean up) {
        if (mc.thePlayer.getDistanceToEntity((Entity)this.targetList.get(targetIndex)) <= 4.0f) {
            this.attack(this.targetList.get(targetIndex));
            return;
        }
        this.attack = this.infiniteReach(range, 9.5, 9.0, this.positionsBack, this.positions, this.targetList.get(targetIndex));
    }
	
	public void attemptStopAutoblock_Watchdog() {
        if (holdingSword() || fakeab.getValBoolean()) {
            if (isBlocking) {
                if (abMode.getValString().equalsIgnoreCase("Hypixel")) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-.8, -.8, -.8), EnumFacing.DOWN));
                } else if (abMode.getValString().equalsIgnoreCase("Fake")) {

                }
                isBlocking = false;
            }
        }
    }

    public void attemptStopAutoblockNoSet_Watchdog() {
        if (isBlocking || fakeab.getValBoolean()) {
            if (abMode.getValString().equalsIgnoreCase("Hypixel")) {
                //mc.thePlayer.addChatMessage(new ChatComponentText("UnBlocc"));
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-.8, -.8, -.8), EnumFacing.DOWN));
            } else if (abMode.getValString().equalsIgnoreCase("Fake")) {

            }
            isBlocking = false;

        }
    }

    public boolean shouldDoAutoblockAnim() {
        return isBlocking;
    }

    public EntityLivingBase getCurrentTarget() {
    	if (currentEntity != null) { //This code is bad I know it. I'm just too lazy to fix it.
            if (!currentEntity.isDead) {
                if (mc.thePlayer.getDistanceToEntity(currentEntity) < distance.getValDouble()) {
                    return currentEntity;
                } else {
                    currentEntity = null;
                    return null;
                }
            } else {
                currentEntity = null;
                return null;
            }
        } else {
            return null;
        }

    }
    
    @EventTarget
    public void onPacketSend(PacketSendEvent event) {
    	if(event.getPacket() instanceof C04PacketPlayerPosition || event.getPacket() instanceof C06PacketPlayerPosLook) {
    		//event.setCancelled(true);
    	}
    }
    public void setBlocking(boolean blocking) {
        isBlocking = blocking;
    }

    boolean didcrit;
	
	public void attackexecute(UpdateEvent event) {
        if (targetList.isEmpty())
            return;

        if (targetIndex == -1)
            return;

        if (targetIndex > targetList.size() - 1)
            return;

        if (abMode.getValString().equalsIgnoreCase("Ghostly")) {
            mc.gameSettings.keyBindUseItem.pressed = true;
        }

        if (mc.thePlayer.getDistanceToEntity(targetList.get(targetIndex)) <= (distance.getValDouble())) {
        	
        	if(event.isPre()) {
        		//mc.thePlayer.setPosition(targetList.get(targetIndex).posX, targetList.get(targetIndex).posY + 5, targetList.get(targetIndex).posZ);
        		mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        		mc.thePlayer.swingItem();
                attack(event);
                targetList.get(targetIndex).attacks += 1;
        	} else {
        		//mc.thePlayer.setPosition(targetList.get(targetIndex).posX + (1.5 * -Math.cos(Math.toRadians(targetList.get(targetIndex).rotationYaw + 90.0F))), targetList.get(targetIndex).posY + 5, targetList.get(targetIndex).posZ + (1.5 * -Math.sin(Math.toRadians(targetList.get(targetIndex).rotationYaw + 90.0F))));
        		mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(targetList.get(targetIndex).posX, targetList.get(targetIndex).posY + 5, targetList.get(targetIndex).posZ, true));
        	}
        } else {
            if (targetList.get(targetIndex).attacks <= 0) {
            	targetList.get(targetIndex).attacks = 0;

            } else {
            	targetList.get(targetIndex).attacks -= 1;
            }
            attackSpeed = 0;
        }
        if (abMode.getValString().equalsIgnoreCase("Real") && this.fakeab.getValBoolean()) {
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
        }
        apsStopwatch.updateLastTime();
    }

    public boolean isInputBetween(double input, double min, double max) {
        return input >= min && input <= max;//Check to see if yaw is between a specified number(input*)
    }

    public Entity raycast(EntityLivingBase target) {
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;//Credits to verble for this
            if (entity.isInvisible() && object != null) {
                if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)
                        && !(entity instanceof EntityArrow)) {
                    if (entity.getEntityBoundingBox().intersectsWith(mc.thePlayer.getEntityBoundingBox())) {
                        return entity;
                    }
                }
            }
        }
        return target;
    }

    private boolean isInTablist(EntityLivingBase player) {
        if (mc.isSingleplayer()) {
            return true;
        }
        for (Object o : mc.getNetHandler().getPlayerInfoMap()) {
            NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) o;
            if (playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean raytraceCheck(EntityLivingBase entity) {
        EntitySnowball entitySnowball = new EntitySnowball(mc.theWorld);
        entitySnowball.posX = entity.posX;
        entitySnowball.posY = entity.posY + entity.getEyeHeight() / 2;
        entitySnowball.posZ = entity.posZ;
        return mc.thePlayer.canEntityBeSeen(entitySnowball);
    }

    public float wrapAngleToSpecified_float(float value, float maxangle) {
        value = value % 360.0F;
        if (value >= maxangle) {
            value -= 360.0F;
        }
        if (value < -maxangle) {
            value += 360.0F;
        }
        return value;
    }

    private boolean isPosSolid(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isSolidFullCube() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }

    public float[] Aim(Entity ent, UpdateEvent event, boolean BBB) {
        double x = ent.posX - Minecraft.getMinecraft().thePlayer.posX, y = ent.posY + (BBB ? .1 : ent.getEyeHeight() / 2) - event.getPosY() - (BBB ? 0 : 1.2), z = ent.posZ - Minecraft.getMinecraft().thePlayer.posZ;


        return new float[]{MathHelper.wrapAngleTo180_float((float) (Math.atan2(z, x) * 180 / Math.PI) - 90), (float) -(Math.atan2(y, MathHelper.sqrt_double(x * x + z * z)) * 180 / Math.PI)};
    }
    
    public float[] Aim(Entity ent, boolean BBB) {
        double x = ent.posX - Minecraft.getMinecraft().thePlayer.posX, y = ent.posY + (BBB ? .1 : ent.getEyeHeight() / 2) - mc.thePlayer.posY - (BBB ? 0 : 1.2), z = ent.posZ - Minecraft.getMinecraft().thePlayer.posZ;


        return new float[]{MathHelper.wrapAngleTo180_float((float) (Math.atan2(z, x) * 180 / Math.PI) - 90), (float) -(Math.atan2(y, MathHelper.sqrt_double(x * x + z * z)) * 180 / Math.PI)};
    }

    public void attack(UpdateEvent event) {
        EntityLivingBase target = targetList.get(targetIndex);
        attack(target);

        aps += apsDecrease ? -MathUtils.getRandomInRange(15, 75) : MathUtils.getRandomInRange(15, 75);
        attackSpeed += 1;
        if (attackSpeed >= 4) {
            attackSpeed = 0;
        }
        blockDelay = 5;
    }

    //int sel = 1;
    int rand;

    public void attackexecute(EntityLivingBase target) {
        if (mc.thePlayer.getDistanceToEntity(targetList.get(targetIndex)) <= (distance.getValDouble())) {
            mc.thePlayer.swingItem();

            rand = MathUtils.getRandomInRange(1, 20);
            //ChatUtil.chat(rand + " is random; " + "has to be 1 to miss!");
          //UPlayer.sendPackets(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
            attack((EntityLivingBase) target);
            target.attacks += 1;
        } else {
            if (target.attacks <= 0) {
                target.attacks = 0;

            } else {
                target.attacks -= 1;
            }
            attackSpeed = 0;
        }
    }

    public boolean isValidTarget(EntityLivingBase entity) {
        if (teams.getValBoolean() && entity.getDisplayName().getUnformattedText().contains("\247a") && !mc.hackedClient.getFriendManager().isFriend(entity.getName())) {
            return false;
        }
        if (ignoredEntities.contains(entity) && !whitelistedEntity.contains(entity)) {
            return false;
        }
        if (mc.hackedClient.getFriendManager().isFriend(entity.getName())) {
            return false;
        }
        try {
            if (mc.getCurrentServerData().serverIP.contains("mineplex")) {
                if (!Double.isNaN(entity.getHealth())) {
                    return false;
                }
            }
        } catch (Exception e ) {
        }

        if (entity != mc.thePlayer && (entity instanceof EntityPlayer && players.getValBoolean())
                || (entity instanceof EntityMob && monsters.getValBoolean())
                || (entity instanceof EntitySlime && monsters.getValBoolean())
                || (entity instanceof EntityAnimal && animals.getValBoolean())
                || (entity instanceof EntityPig && animals.getValBoolean())
                || (entity instanceof EntityVillager && villagers.getValBoolean())
                || (entity instanceof EntityGolem && golems.getValBoolean())) {
            if (((mc.thePlayer.getDistanceToEntity(entity) <= (distance.getValDouble())))) {
            	return true;
            }

        }
        return false;
    }


    private static boolean canPassThrow(BlockPos pos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }


    public void updateTargetList() {
        targetList.clear();

        mc.theWorld.getLoadedEntityList().forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                if (isValidTarget((EntityLivingBase) entity)) {
                    targetList.add((EntityLivingBase) entity);
                } else {
                    targetList.remove((EntityLivingBase) entity);
                }

            }
        });

        if (targetList.size() > 1) {
            if (targetPriority.getValString().equalsIgnoreCase("Lowest Health"))
                targetList.sort(Comparator.comparingDouble(EntityLivingBase::getHealth).reversed());
            else if (targetPriority.getValString().equalsIgnoreCase("Least Armor"))
                targetList.sort(Comparator.comparingInt(EntityLivingBase::getTotalArmorValue));
            else if (targetPriority.getValString().equalsIgnoreCase("Closest"))
                targetList.sort(Comparator.comparingDouble(UPlayer::getDistanceToEntity));
            if (prioritisePlayers.getValBoolean()) {
                targetList.sort((e1, e2) -> Boolean.compare(e2 instanceof EntityPlayer, e1 instanceof EntityPlayer));
            }
        }
    }

    /*Things for autoblock*/
    public boolean holdingSword() {
        if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            return true;
        }
        if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemAxe) {
            return true;
        }
        return false;
    }

    public void attemptStartAutoblock_Watchdog() {
        if (fakeab.getValBoolean()) {
            if (!targetList.isEmpty() && targetIndex <= targetList.size() - 1 && UPlayer.getDistanceToEntity(targetList.get(targetIndex)) <= distance.getValDouble() && holdingSword() && !isBlocking) {
                if (abMode.getValString().equalsIgnoreCase("Hypixel")) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-.8, -.8, -.8), -1, mc.thePlayer.getHeldItem(), 0, 0, 0));
                } else if (abMode.getValString().equalsIgnoreCase("Fake")) {

                }
                isBlocking = true;
            }
        }
    }
    
    @EventTarget
    public void onJump(PlayerJumpEvent event) {
    	if (mc.hackedClient.getModuleManager().getModule("Criticals").getState() && groundTicks != 0 && PlayerUtils.isMoving()) {
            event.setCancelled(true);
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, yaw, pitch, true));
            mc.thePlayer.motionY = .42f;
            groundTicks = 0;
        } else {
            event.setCancelled(false);
        }
    }
    
    @EventTarget
    public void onMouseClick(MouseClickEvent mouseClickEvent) {
        if (mouseClickEvent.getMouseButton() == 2) {
            if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) mc.objectMouseOver.entityHit;
                if (!mcf.contains(entity)) {
                    mcf.add(entity);
                } else {
                    mcf.remove(entity);
                }
            }
        }
    }
    
    @EventTarget
    public void onProcessPacket(PacketReceiveEvent processPacketEvent) {
    	if (processPacketEvent.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) processPacketEvent.getPacket();
            if (packet.getYaw() == 0.0 && packet.getPitch() == 0.0 && packet.getX() == 0.0 && packet.getZ() == 0.0 && packet.getY() == 0.0) {
                packet.setYaw(mc.thePlayer.rotationYaw);
                packet.setPitch(mc.thePlayer.rotationPitch);
            }
        }
    }
    
}
