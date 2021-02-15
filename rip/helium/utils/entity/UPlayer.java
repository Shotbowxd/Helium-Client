package rip.helium.utils.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.util.MathHelper;

public class UPlayer {

    public static void sendPackets(Packet... packets) {
        for (Packet packet : packets) {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
        }
    }

    public static double getDistanceToEntity(Entity entity) {
        if (Minecraft.getMinecraft().thePlayer != null && entity != null) {
            return Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        }
        return Double.NaN;
    }

    public static double getHorizDistanceToEntity(Entity entityIn) {
        float x = (float) (Minecraft.getMinecraft().thePlayer.posX - entityIn.posX);
        float z = (float) (Minecraft.getMinecraft().thePlayer.posZ - entityIn.posZ);
        return MathHelper.sqrt_float(x * x + z * z);
    }

}
