package rip.helium.cheat.impl.visual

import me.hippo.systems.lwjeb.annotation.Collect
import org.lwjgl.input.Keyboard
import rip.helium.ChatUtil
import rip.helium.cheat.Cheat
import rip.helium.cheat.CheatCategory
import rip.helium.event.minecraft.RenderOverlayEvent

/*/
Created by Kansio on 1/16/2021
 */

class Console : Cheat("Console", "A cool console to send chat commands", Keyboard.KEY_INSERT, CheatCategory.VISUAL) {

    override fun onEnable() {
        ChatUtil.chat("not done....")
    }

    @Collect
    fun onRenderOverlay(event: RenderOverlayEvent) {

    }

}