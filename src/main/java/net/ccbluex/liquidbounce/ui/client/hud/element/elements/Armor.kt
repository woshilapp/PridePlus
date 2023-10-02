package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.features.value.IntegerValue
import net.ccbluex.liquidbounce.features.value.ListValue
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color


/**
 * CustomHUD Armor element
 *
 * Shows a horizontal display of current armor
 */
@ElementInfo(name = "Armor")
class Armor(
    x: Double = -8.0, y: Double = 57.0, scale: Float = 1F,
    side: Side = Side(Side.Horizontal.MIDDLE, Side.Vertical.DOWN)
) : Element(x, y, scale, side) {

    private val modeValue = ListValue("Alignment", arrayOf("Horizontal", "Vertical"), "Horizontal")

    override fun drawElement(): Border {
        GL11.glPushMatrix()

        val mode = modeValue.get()

        val renderItem = mc.renderItem
        var x = 1
        var y = 0

        for (index in 3 downTo 0) {
            val stack = mc.player!!.inventory.armorInventory[index]

            renderItem.renderItemIntoGUI(stack, x, y)
            renderItem.renderItemOverlays(mc.fontRenderer, stack, x, y)
            GlStateManager.pushMatrix()
            GlStateManager.popMatrix()
            if (mode.equals("Horizontal", true))
                x += 18
            else if (mode.equals("Vertical", true))
                y += 18
        }

        GlStateManager.enableAlpha()
        GlStateManager.disableBlend()
        GlStateManager.disableLighting()
        GlStateManager.disableCull()
        GL11.glPopMatrix()

        return Border(0F, 0F, 72F, 17F)

    }
}