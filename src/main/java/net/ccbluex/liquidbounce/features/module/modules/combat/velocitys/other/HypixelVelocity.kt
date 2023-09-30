package net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.other

import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.AntiKBMode
import net.minecraft.network.play.server.SPacketEntityVelocity

class HypixelVelocity : AntiKBMode("Hypixel") {
    override fun onVelocityPacket(event: PacketEvent) {
        val packet = event.packet
        if(packet is SPacketEntityVelocity) {
          event.cancelEvent()
          mc.player!!.motionY = packet.motionY.toDouble() / 8000.0
        }
    }
}
