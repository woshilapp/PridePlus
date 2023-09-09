package op.wawa.utils

import net.ccbluex.liquidbounce.api.minecraft.network.IPacket
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer

object PacketUtils : MinecraftInstance() {

    @JvmStatic
    fun sendPacketNoEvent(packet: Packet<INetHandlerPlayServer>) {
        //packets.add(packet)
        //mc.netHandler.addToSendQueue(packet as IPacket)
        mc2.connection!!.sendPacket(packet)//我草
    }

    @JvmStatic
    fun sendPacket(pac: IPacket) {//有必要吗
        //iPackets.add(pac)
        mc.netHandler.networkManager.sendPacket(pac)
    }

    @JvmStatic
    fun sendPacket(pac: Packet<INetHandlerPlayServer>) {
        sendPacketNoEvent(pac)//?
    }


}