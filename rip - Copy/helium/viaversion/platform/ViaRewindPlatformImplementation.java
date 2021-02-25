package rip.helium.viaversion.platform;

import java.util.logging.Logger;

import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindConfig.CooldownIndicator;
import de.gerrygames.viarewind.api.ViaRewindPlatform;
import us.myles.ViaVersion.api.Via;

public class ViaRewindPlatformImplementation implements ViaRewindPlatform {

    public ViaRewindPlatformImplementation() {
        init(new ViaRewindConfig() {
            @Override
            public CooldownIndicator getCooldownIndicator() {
                return CooldownIndicator.TITLE;
            }

            @Override
            public boolean isReplaceAdventureMode() {
                return true;
            }

            @Override
            public boolean isReplaceParticles() {
                return true;
            }
        });
    }

    @Override
    public Logger getLogger() {
        return Via.getPlatform().getLogger();
    }
}
