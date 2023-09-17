package net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.aac

import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.VelocityMode
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.minecraft.client.Minecraft
import net.minecraft.network.play.server.SPacketEntityVelocity

class AAC4ReduceVelocity : VelocityMode("AAC4Reduce") {
    val mc: Minecraft = Minecraft.getMinecraft()
    override fun onVelocity(event: UpdateEvent) {
        if (mc.player.hurtTime> 0 && !mc.player.onGround && velocity.velocityInput && velocity.velocityTimer.hasTimePassed(80L)) {
            mc.player.motionX *= 0.62
            mc.player.motionZ *= 0.62
        }
        if (velocity.velocityInput && (mc.player.hurtTime <4 || mc.player.onGround) && velocity.velocityTimer.hasTimePassed(120L)) {
            velocity.velocityInput = false
        }
    }

    override fun onVelocityPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        if(packet is SPacketEntityVelocity) {
            velocity.velocityInput = true
            packet.motionX = (packet.getMotionX() * 0.6).toInt()
            packet.motionZ = (packet.getMotionZ() * 0.6).toInt()
        }
    }
}