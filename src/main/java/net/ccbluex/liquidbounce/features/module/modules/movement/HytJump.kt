/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.block.BlockUtils
import net.ccbluex.liquidbounce.features.value.FloatValue

@ModuleInfo(name = "HytJump", description = "Bypass?", category = ModuleCategory.MOVEMENT)
class HytJump : Module() {
    private val yjumpvalue = FloatValue("EveryJumpMotion", 0.1f,0.1f,1.0f)

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer != null && mc.theWorld != null && classProvider.isBlockSlime(BlockUtils.getBlock(mc.thePlayer!!.position.down())) && mc.thePlayer!!.onGround) {
            mc.thePlayer!!.jump()
            mc.thePlayer!!.motionY += yjumpvalue.get().toDouble()
        }
    }
}