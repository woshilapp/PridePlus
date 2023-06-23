/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.BlockBBEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.ClientUtils

@ModuleInfo(name = "HytJump", description = "must need slime blocks.", category = ModuleCategory.MOVEMENT)
class HytJump : Module() {
    var motion = 0.10F
    var a = false

    override fun onEnable() {
        motion = 0.10F
        a = false
    }

    override fun onDisable() {
        motion = 0.10F
        a = false
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent){
        val thePlayer = mc.thePlayer ?: return


        motion += 0.10F
        if (motion > 1.20F){
            this.state = false
        }
        if(mc.gameSettings.keyBindJump.isKeyDown && thePlayer.onGround) thePlayer.motionY = motion.toDouble()
    }
}