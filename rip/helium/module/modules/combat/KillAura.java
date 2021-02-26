package rip.helium.module.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
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
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.client.MouseClickEvent;
import rip.helium.event.events.impl.network.PacketReceiveEvent;
import rip.helium.event.events.impl.player.BlockStepEvent;
import rip.helium.event.events.impl.player.PlayerJumpEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.client.ClientUtils;
import rip.helium.utils.client.Timer;
import rip.helium.utils.entity.PlayerUtils;
import rip.helium.utils.entity.UPlayer;
import rip.helium.utils.misc.MathUtils;
import rip.helium.utils.render.ColorUtils;
import rip.helium.utils.world.BlockUtils;

public class KillAura extends Module {
    
	private Setting abMode;
	private Setting mode;
	private Setting targetPriority;
	private Setting prioritisePlayers;
	private Setting hvh;
	private Setting click;
	private Setting miss;
	private Setting keepSprint;
	private Setting middleClickReset;
	private Setting teams;
	private Setting minSpeed;
	private Setting maxSpeed;
	private Setting distance;
	private Setting fakeab;
	private Setting spinbot;

	private Setting players;
	private Setting mobs;
	private Setting villagers;
	
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
    public int targetIndex;

    private ArrayList<EntityLivingBase> mcf;
    public ArrayList<EntityLivingBase> targetList;
    public static EntityLivingBase currentEntity;
    public ArrayList<EntityLivingBase> ignoredEntities;
    private ArrayList<EntityLivingBase> whitelistedEntity;
    
	
	public KillAura(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Priority");
		
		this.mode = new Setting("Mode", this, "Priority", modes);
		
		ArrayList<String> priorities = new ArrayList<String>();
		priorities.add("Lowest Health");
		priorities.add("Least Armor");
		priorities.add("Closest");
		
		this.targetPriority = new Setting("Target Priority", this, "Lowest Health", priorities);
		
		this.prioritisePlayers = new Setting("Prioritise Players", this, true);
		this.hvh = new Setting("Hacker vs Hacker", this, false);
		this.click = new Setting("Require Click", this, false);
		this.miss = new Setting("Miss Hits", this, false);
		this.keepSprint = new Setting("Keep Sprint", this, true);
		this.middleClickReset = new Setting("Auto Reset", this, false);
		this.teams = new Setting("Hypixel Teams", this, false);
		this.minSpeed = new Setting("Minimum Delay", this, 150, 0, 1000, false);
		this.maxSpeed = new Setting("Maximum Delay", this, 350, 0, 1000, false);
		this.distance = new Setting("Maximum Distance", this, 4.0, 0.1, 6.0, false);
		this.spinbot = new Setting("Spinbot", this, false);
		this.fakeab = new Setting("Auto Block", this, true);

		players = new Setting("Attack Players", this, true);
		mobs = new Setting("Attack Mobs", this, false);
		villagers = new Setting("Attack Villagers", this, true);
		
		ArrayList<String> abmodes = new ArrayList<String>();
		abmodes.add("Real");
		abmodes.add("Fake");
		abmodes.add("Hypixel");
		abmodes.add("Ghostly");
		
		this.abMode = new Setting("Auto Block Mode", this, "Real", abmodes);
		
		mc.hackedClient.getSettingManager().addSetting(this.abMode);
		mc.hackedClient.getSettingManager().addSetting(this.mode);
		mc.hackedClient.getSettingManager().addSetting(this.targetPriority);
		mc.hackedClient.getSettingManager().addSetting(this.prioritisePlayers);
		mc.hackedClient.getSettingManager().addSetting(this.hvh);
		mc.hackedClient.getSettingManager().addSetting(this.click);
		mc.hackedClient.getSettingManager().addSetting(this.miss);
		mc.hackedClient.getSettingManager().addSetting(this.keepSprint);
		mc.hackedClient.getSettingManager().addSetting(this.middleClickReset);
		mc.hackedClient.getSettingManager().addSetting(this.teams);
		mc.hackedClient.getSettingManager().addSetting(this.minSpeed);
		mc.hackedClient.getSettingManager().addSetting(this.maxSpeed);
		mc.hackedClient.getSettingManager().addSetting(this.distance);
		mc.hackedClient.getSettingManager().addSetting(this.spinbot);
		mc.hackedClient.getSettingManager().addSetting(this.fakeab);
		mc.hackedClient.getSettingManager().addSetting(players);
        mc.hackedClient.getSettingManager().addSetting(mobs);
        mc.hackedClient.getSettingManager().addSetting(villagers);
		
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
        final float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), entity.getCreatureAttribute());
        final boolean vanillaCrit = mc.thePlayer.fallDistance > 0.0f && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.ridingEntity == null;
        this.swingItem();
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
		this.setSuffix(this.mode.getValString());
		
		if(getCurrentTarget() == null) {
			aps = (1000 / (int)maxSpeed.getValDouble());
		}
		
		if (getCurrentTarget() != null) {
            if (abMode.getValString().equalsIgnoreCase("Ghostly")) {
                mc.gameSettings.keyBindUseItem.pressed = false;
            }
        }

        if (mode.getValString().equalsIgnoreCase("Priority")) {
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
//                                        entityPlayer.setInvisible(true);
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
                if (mc.thePlayer.getDistanceToEntity(targetList.get(targetIndex)) > (.1 + Math.abs(mc.thePlayer.posY - targetList.get(targetIndex).posY) * .1)) {
                    if (!click.getValBoolean()) {
                        float randomr = (float) MathUtils.getRandomInRange(0.01, 0.999999);
                        if (!spinbot.getValBoolean()) {
                            event.setYaw(serverSidePitch = randomr + Aim(targetList.get(targetIndex), event, false)[0]);
                            event.setPitch(serverSidePitch = randomr + Aim(targetList.get(targetIndex), event, false)[1]);

                        } else {
                            float randommmm = MathUtils.getRandomInRange(1, 360);
                            event.setYaw(randommmm);

                        }
                    } else {
                        event.setYaw(serverSideYaw);
                        event.setPitch(serverSidePitch);
                    }
                    if (click.getValBoolean()) {
                        if (mc.gameSettings.keyBindAttack.pressed) {
                            float randomr = (float) MathUtils.getRandomInRange(0.01, 0.999999);
                            event.setYaw(serverSidePitch = randomr + Aim(targetList.get(targetIndex), event, false)[0]);
                            event.setPitch(serverSidePitch = randomr + Aim(targetList.get(targetIndex), event, false)[1]);
                        } else {
                            event.setYaw(serverSideYaw);
                            event.setPitch(serverSidePitch);
                        }
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
                if (mode.getValString().equalsIgnoreCase("Priority")) {
                    if (abMode.getValString().equalsIgnoreCase("Hypixel")) {
                        attemptStopAutoblock_Watchdog();
                    }
                }
                
                double minValue = this.minSpeed.getValDouble(), maxValue=this.maxSpeed.getValDouble();
                Random theRandom = new Random();
                double randAps = 0.0;
                blockDelay++;
             // Checking for a valid range-
                if( Double.valueOf(maxValue - minValue).isInfinite() == false) 
                	randAps = minValue + (maxValue - minValue) * theRandom.nextDouble(); randAps = randAps;
                if (mode.getValString().equalsIgnoreCase("Priority")) {
                    if (apsStopwatch.hasPassed((hvh.getValBoolean() ? 50 : randAps))) {
                        // no click, just hit normally
                        if (!click.getValBoolean()) {
                            attackexecute(event);
                            apsStopwatch.updateLastTime();
                        } else {
                            // is holding attack kb?
                            if (mc.gameSettings.keyBindAttack.pressed) {
                                attackexecute(event);
                                apsStopwatch.updateLastTime();
                            }
                        }
                    }
                }
            } else
                attemptStartAutoblock_Watchdog();
            


            yaw = event.getYaw();
            pitch = event.getPitch();
        }
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
                                    attack((EntityLivingBase) entity);
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
        if (!mode.getValString().equalsIgnoreCase("Multi")) {
            if (!targetList.isEmpty() && targetIndex != -1) {
                return targetList.get(targetIndex);
            } else {
                return null;
            }
        } else {
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

        if (mc.thePlayer.getDistanceToEntity(targetList.get(targetIndex)) <= (hvh.getValBoolean() ? 4.5 : distance.getValDouble())) {

            rand = MathUtils.getRandomInRange(1, 20);
            //ChatUtil.chat(rand + " is random; " + "has to be 1 to miss!");
            if (!miss.getValBoolean()) {
                //UPlayer.sendPackets(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                attack(event);
            } else {
                if (!(rand == 1)) {
                    //UPlayer.sendPackets(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                    attack(event);
                } else {
                    ClientUtils.addConsoleMessage("�cMissed a hit!");
                }
            }
            targetList.get(targetIndex).attacks += 1;
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
        if (mc.thePlayer.getDistanceToEntity(targetList.get(targetIndex)) <= (hvh.getValBoolean() ? 4.5 : distance.getValDouble())) {
            mc.thePlayer.swingItem();

            rand = MathUtils.getRandomInRange(1, 20);
            //ChatUtil.chat(rand + " is random; " + "has to be 1 to miss!");
            if (!miss.getValBoolean()) {
                //UPlayer.sendPackets(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                attack((EntityLivingBase) target);
            } else {
                if (!(rand == 1)) {
                    //UPlayer.sendPackets(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                    attack((EntityLivingBase) target);
                } else {
                    ClientUtils.addConsoleMessage("�cMissed a hit!");
                }
            }
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
                || (entity instanceof EntityMob && mobs.getValBoolean())
                || (entity instanceof EntitySlime && mobs.getValBoolean())
                || (entity instanceof EntityAnimal && mobs.getValBoolean())
                || (entity instanceof EntityPig && mobs.getValBoolean())
                || (entity instanceof EntityVillager && villagers.getValBoolean())
                || (entity instanceof EntityGolem && mobs.getValBoolean())) {
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
            if (!targetList.isEmpty() && targetIndex <= targetList.size() - 1 && UPlayer.getDistanceToEntity(targetList.get(targetIndex)) <= distance.getValDouble() + (hvh.getValBoolean() ? 2 : 0) && holdingSword() && !isBlocking) {
                if (abMode.getValString().equalsIgnoreCase("Hypixel")) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-.8, -.8, -.8), -1, mc.thePlayer.getHeldItem(), 0, 0, 0));
                } else if (abMode.getValString().equalsIgnoreCase("Fake")) {

                }
                if(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                	isBlocking = true;
                } else {
                	isBlocking = false;
                }
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

	