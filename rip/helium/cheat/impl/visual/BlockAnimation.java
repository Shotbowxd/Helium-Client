package rip.helium.cheat.impl.visual;

import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.utils.property.impl.StringsProperty;

public class BlockAnimation extends Cheat {
    public StringsProperty mode;

    public BlockAnimation() {
        super("Animations", "Different Sword block animations", CheatCategory.VISUAL);
        this.mode = new StringsProperty("Mode", "How this cheat will function.", null, false, true, new String[]{"Helium", "Matt", "Remix", "Shotbowxd", "1.7", "Sigma", "Slide", "Kansio", "Poke", "oHare", "Swang"}, new Boolean[]{true, false, false, false, false, false, false, false, false, false, false, false, false, false, false});
        this.registerProperties(this.mode);
    }
}
