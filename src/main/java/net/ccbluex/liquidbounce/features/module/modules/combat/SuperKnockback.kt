/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.value.BoolValue
import net.ccbluex.liquidbounce.features.value.IntegerValue
import net.ccbluex.liquidbounce.features.value.ListValue
import net.ccbluex.liquidbounce.utils.MovementUtils

@ModuleInfo(name = "SuperKnockback", description = "Increases knockback dealt to other entities.", category = ModuleCategory.COMBAT)
class SuperKnockback : Module() {

    private val modeValue = ListValue("Mode", arrayOf("Sprint-Packet","Sneak-Packet","W-Tap"),"Sprint-Packet")
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)
    val onlyGround = BoolValue("OnlyGround", true)
    val onlyMove = BoolValue("OnlyMove", true)

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (classProvider.isEntityLivingBase(event.targetEntity)) {
            if (event.targetEntity!!.asEntityLivingBase().hurtTime > hurtTimeValue.get() || (onlyGround.get() && !mc.thePlayer!!.onGround) || (onlyMove.get() && !MovementUtils.isMoving))
                return

            val player = mc.thePlayer ?: return

            when(modeValue.get().toLowerCase()){
                "sprint-packet" -> {
                    if (player.sprinting)
                        mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.STOP_SPRINTING))

                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.START_SPRINTING))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.STOP_SPRINTING))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.START_SPRINTING))
                    player.sprinting = true
                    player.serverSprintState = true
                }
                "sneak-packet" -> {
                    if (player.sneaking)
                        mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.STOP_SNEAKING))

                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.START_SNEAKING))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.STOP_SNEAKING))
                }
                "w-tap" -> {
                    if (player.sprinting)
                        mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.STOP_SPRINTING))

                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.START_SPRINTING))
                    player.sprinting = true
                    player.serverSprintState = true
                }
            }
        }
    }
    override val tag: String?
        get() = "Bypass"
}