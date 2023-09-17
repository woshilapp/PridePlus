package net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.other

import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.VelocityMode
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.minecraft.network.play.server.SPacketEntityVelocity

class HypixelVelocity : VelocityMode("Hypixel") {
    override fun onVelocityPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        if(packet is SPacketEntityVelocity) {
          event.cancelEvent()
          mc.thePlayer!!.motionY = packet.getMotionY().toDouble() / 8000.0
        }
    }
}
