package net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.grim

import net.ccbluex.liquidbounce.event.EventState
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.modules.combat.velocitys.VelocityMode
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.minecraft.block.BlockSlab
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.client.CPacketPlayerDigging
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos

class GrimVelocity : VelocityMode("GrimAC") {
    private var sendC07 = false

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()

        if (packet is SPacketEntityVelocity && mc2.player.hurtTime > 0) {
            event.cancelEvent()
            sendC07 = true
        }
    }

    override fun onMotion(event: MotionEvent) {
        if (event.eventState == EventState.PRE && !mc2.playerController.isHittingBlock && mc2.player.hurtTime > 0 && !isPlayerOnSlab(mc2.player) && sendC07) {
            val blockPos = BlockPos(mc2.player.posX, mc2.player.posY, mc2.player.posZ)
            mc2.connection!!.sendPacket(
                CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                    blockPos,
                    EnumFacing.NORTH
                )
            )
            sendC07 = false
        }
    }

    private fun isPlayerOnSlab(player: EntityPlayer): Boolean {
        val playerPos = BlockPos(player.posX, player.posY, player.posZ)

        val block = player.world.getBlockState(playerPos).block
        val boundingBox = player.entityBoundingBox

        return block is BlockSlab && player.posY - playerPos.y <= boundingBox.minY + 0.1
    }
}
