package net.ccbluex.liquidbounce.features.module.modules.misc


import me.utils.PacketUtils
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.CPacketConfirmTransaction
import net.minecraft.network.play.client.CPacketKeepAlive
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.server.SPacketPlayerPosLook
import kotlin.math.sqrt


@ModuleInfo(name = "Disabler", description = "By WaWa", category = ModuleCategory.MISC)
class Disabler : Module() {

    val modeValue = ListValue(
        "Mode",
        arrayOf(
            "HuaYuTing",
            "LessLag"
        ), "HuaYuTing"
    )


    // variables
    private val keepAlives = arrayListOf<CPacketKeepAlive>()
    private val transactions = arrayListOf<CPacketConfirmTransaction>()
    private val msTimer = MSTimer()

    @EventTarget
    fun onPacket(event: PacketEvent) {

        val packet = event.packet
        when (modeValue.get().toLowerCase()) {
            "lesslag" -> {
                if (packet is SPacketPlayerPosLook) {
                    val x = packet.x - mc.thePlayer?.posX!!
                    val y = packet.y - mc.thePlayer?.posY!!
                    val z = packet.z - mc.thePlayer?.posZ!!
                    val diff = sqrt(x * x + y * y + z * z)
                    if (diff <= 8) {
                        event.cancelEvent()
                        PacketUtils.sendPacketNoEvent(
                            CPacketPlayer.PositionRotation(
                                packet.x,
                                packet.y,
                                packet.z,
                                packet.getYaw(),
                                packet.getPitch(),
                                true
                            )
                        )
                    }
                }
            }
            "huayuting" -> {
                if (packet is CPacketKeepAlive && (keepAlives.size <= 0 || packet != keepAlives[keepAlives.size - 1])) {
                    keepAlives.add(packet)
                    event.cancelEvent()
                }
                if (packet is CPacketConfirmTransaction && (transactions.size <= 0 || packet != transactions[transactions.size - 1])) {
                    transactions.add(packet)
                    event.cancelEvent()
                }
            }
        }
    }
    @EventTarget
    fun onUpdate() {
        when (modeValue.get().toLowerCase()) {
            "huayuting" -> {
                if (msTimer.hasTimePassed(3000L) && keepAlives.size > 0 && transactions.size > 0) {
                    PacketUtils.sendPacketNoEvent(keepAlives[keepAlives.size - 1])
                    PacketUtils.sendPacketNoEvent(transactions[transactions.size - 1])

                    keepAlives.clear()
                    transactions.clear()
                    msTimer.reset()
                }
            }
        }
    }
}

