package net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.aac

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.VelocityMode
import net.minecraft.client.Minecraft

class AACZeroVelocity : VelocityMode("AACZero") {

    val mc: Minecraft = Minecraft.getMinecraft()
    override fun onVelocity(event: UpdateEvent) {
        if (mc.player.hurtTime > 0) {
            if (!velocity.velocityInput || mc.player.onGround || mc.player.fallDistance > 2F) {
                return
            }

            mc.player.addVelocity(0.0, -1.0, 0.0)
            mc.player.onGround = true
        } else {
            velocity.velocityInput = false
        }
    }

    override fun onVelocityPacket(event: PacketEvent) {
        velocity.velocityInput = true
    }

    override fun onJump(event: JumpEvent) {
        if (mc.player.isInWater || mc.player.isInLava || mc.player.isInWeb || (velocity.onlyGroundValue.get() && !mc.player.onGround)) {
            return
        }

        if ((velocity.onlyGroundValue.get() && !mc.player.onGround) || (velocity.onlyCombatValue.get() && !LiquidBounce.combatManager.inCombat)) {
            return
        }

        if (mc.player.hurtTime > 0) {
            event.cancelEvent()
        }
    }
}