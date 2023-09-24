package net.ccbluex.liquidbounce.utils

import net.minecraft.item.ItemStack
import net.minecraft.network.Packet
import net.minecraft.network.play.client.CPacketEntityAction
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos

fun createUseItemPacket(itemStack: ItemStack?, hand: EnumHand): Packet<*> {
    return CPacketPlayerTryUseItemOnBlock(BlockPos(-1,-1,-1), EnumFacing.DOWN, hand, 0F, 0F, 0F)
}
fun createOpenInventoryPacket(): Packet<*> {
    return CPacketEntityAction(MinecraftInstance.mc.player, CPacketEntityAction.Action.OPEN_INVENTORY)
}

// CrossVersionUtils
