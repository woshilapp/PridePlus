package net.ccbluex.liquidbounce.features.module.modules.combat

import me.utils.PacketUtils
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.network.play.client.CPacketClientStatus
import net.minecraft.network.play.client.CPacketConfirmTransaction
import net.minecraft.network.play.client.CPacketKeepAlive
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.server.SPacketConfirmTransaction
import net.minecraft.network.play.server.SPacketPlayerPosLook
import kotlin.math.sqrt

@ModuleInfo(name = "HytVelocity",description = "Test By WaWa",category = ModuleCategory.COMBAT)
class HytVelocity:Module() {
    val canSendSize = IntegerValue("CanSendProbabilityBoundary",3,0,10)
    val onlyGround = BoolValue("OnlyGround",true)
    val onlyMove = BoolValue("OnlyMove",false)
    private var canCancel = false
    private var send = 0
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        val packetEntityVelocity = packet.asSPacketEntityVelocity()
        if ((onlyGround.get() && !mc2.player.onGround) || (onlyMove.get() && !MovementUtils.isMoving) || mc2.player.isDead || mc2.player.isInWater)
            return

        send++
        if (classProvider.isSPacketEntityVelocity(packet)) {
            event.cancelEvent()
            mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SNEAKING))
            mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SNEAKING))
            packetEntityVelocity.motionX = 0
            packetEntityVelocity.motionY = 0
            packetEntityVelocity.motionZ = 0
            canCancel = true
        }
        if (packet is SPacketPlayerPosLook && canCancel) {
            val x = packet.x - mc.thePlayer?.posX!!
            val y = packet.y - mc.thePlayer?.posY!!
            val z = packet.z - mc.thePlayer?.posZ!!
            val diff = sqrt(x * x + y * y + z * z)
            event.cancelEvent()
            if (diff <= 8) {
                PacketUtils.sendPacketNoEvent(CPacketPlayer.PositionRotation(packet.x, packet.y, packet.z, packet.getYaw(), packet.getPitch(), true))
            }
            mc.netHandler.addToSendQueue(
                classProvider.createCPacketPlayerLook(packet.yaw,packet.pitch,mc.thePlayer!!.onGround))
            canCancel = false
        }
        if ((packet is SPacketConfirmTransaction || packet is CPacketKeepAlive || packet is CPacketClientStatus) && canCancel){
            if (send > canSendSize.get()){
                send = 0
            }else{
                event.cancelEvent()
            }
            canCancel = false
        }
    }
}