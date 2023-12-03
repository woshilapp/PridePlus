package net.ccbluex.liquidbounce.features.module.modules.combat.antikbs.grim

import net.ccbluex.liquidbounce.Pride
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.TickEvent
import net.ccbluex.liquidbounce.features.module.modules.combat.AntiKnockback
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.combat.antikbs.AntiKBMode
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.minecraft.client.Minecraft
import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraft.network.Packet
import net.minecraft.network.play.client.*
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.network.play.server.SPacketExplosion
import net.minecraft.network.play.server.SPacketPlayerPosLook
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos

class NoXZVelocity : AntiKBMode("NoXZ") {
    private var isVelocity = false
    private var isTeleport = false
    override fun onEnable() {
        isVelocity = false
        isTeleport = false
    }
    override fun onPacket(event: PacketEvent) {
        val aura = Pride.moduleManager[KillAura::class.java] as KillAura

        val packet = event.packet

        if (packet is SPacketEntityVelocity){
            if (mc.player.onGround) {
                mc.gameSettings.keyBindJump.pressed = true
            }
            if (aura.state && aura.target != null && mc.player.getDistanceToEntityBox(aura.target!!) <= 3.01) {
                //是否疾跑
                if (mc.player.movementInput.moveForward > 0.9f && mc.player.isSprinting && mc.player.serverSprintState) {
                    repeat(5) {
                        mc.connection!!.sendPacket(CPacketConfirmTransaction(100, 100, true))
                        mc.connection!!.sendPacket(CPacketUseEntity(aura.target!!))
                        mc.connection!!.sendPacket(CPacketAnimation())
                    }
                    packet.motionX = ((0.077760000 * 8000).toInt())
                    packet.motionZ = ((0.077760000 * 8000).toInt())
                } else {
                    if (mc.player.movementInput.moveForward > 0.9f) {
                        repeat(5) {
                            mc.connection!!.sendPacket(CPacketConfirmTransaction(100, 100, true))
                            mc.connection!!.networkManager.sendPacket(CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING))
                            mc.connection!!.networkManager.sendPacket(CPacketUseEntity(aura.target!!))
                            mc.connection!!.networkManager.sendPacket(CPacketAnimation())
                        }
                        packet.motionX = ((0.077760000 * 8000).toInt())
                        packet.motionZ = ((0.077760000 * 8000).toInt())
                    }
                }
            }
        }

    }
}
