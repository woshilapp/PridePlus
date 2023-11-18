/*
 * PridePlus Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MolokyMC/PridePlus/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.value.BoolValue
import net.ccbluex.liquidbounce.features.value.IntegerValue
import net.ccbluex.liquidbounce.features.value.ListValue
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.CPacketEntityAction

@ModuleInfo(name = "SuperKnockback", description = "Increases knockback dealt to other entities.", category = ModuleCategory.COMBAT)
class SuperKnockback : Module() {

    private val modeValue = ListValue("Mode", arrayOf("Sprint-Packet","W-Tap"),"Sprint-Packet")
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)
    val onlyGround = BoolValue("OnlyGround", true)
    val onlyMove = BoolValue("OnlyMove", true)

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (event.targetEntity is EntityLivingBase) {
            if (event.targetEntity.hurtTime > hurtTimeValue.get() || (onlyGround.get() && !mc.player!!.onGround) || (onlyMove.get() && !MovementUtils.isMoving))
                return

            val player = mc.player ?: return

            when(modeValue.get().toLowerCase()){
                "sprint-packet" -> {
                    if (player.isSprinting)
                        mc.connection!!.sendPacket(CPacketEntityAction(player, CPacketEntityAction.Action.STOP_SPRINTING))

                    mc.connection!!.sendPacket(CPacketEntityAction(player, CPacketEntityAction.Action.START_SPRINTING))
                    mc.connection!!.sendPacket(CPacketEntityAction(player, CPacketEntityAction.Action.STOP_SPRINTING))
                    mc.connection!!.sendPacket(CPacketEntityAction(player, CPacketEntityAction.Action.START_SPRINTING))
                    player.isSprinting = true
                    player.serverSprintState = true
                }
                "w-tap" -> {
                    if (player.isSprinting)
                        mc.connection!!.sendPacket(CPacketEntityAction(player, CPacketEntityAction.Action.STOP_SPRINTING))

                    mc.connection!!.sendPacket(CPacketEntityAction(player, CPacketEntityAction.Action.START_SPRINTING))
                    player.isSprinting = true
                    player.serverSprintState = true
                }
            }
        }
    }
    override val tag: String?
        get() = "Bypass"
}