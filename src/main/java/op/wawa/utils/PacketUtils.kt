package op.wawa.utils

import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer

object PacketUtils : MinecraftInstance() {

    @JvmStatic
    fun sendPacket(packet: Packet<INetHandlerPlayServer>) {
        //packets.add(packet)
        //mc.netHandler.addToSendQueue(packet as IPacket)
        mc.connection!!.sendPacket(packet)//我草
    }


}