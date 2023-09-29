package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.features.value.IntegerValue
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
    val r = IntegerValue("Red", 0, 0, 255)
    val g = IntegerValue("Green", 0, 0, 255)
    val b = IntegerValue("Blue", 0, 0, 255)
    val alpha = IntegerValue("BG-Alpha", 100, 0, 255)

    override fun drawElement(): Border {
        GL11.glPushMatrix()

        val renderItem = mc.renderItem
        val sb = Color(250, 250, 250, 250).rgb
        var x = 1
        val y = 0
        //draw Background


        RoundedUtil.drawRound(x - 1.5f, -12f, 73.5f, 38.85f, 2f, Color(r.get(), g.get(), b.get(), alpha.get()))

        RoundedUtil.drawRound(x - 2f, -12f, 75f, 10.5f, 2f, Color(250, 250, 250, 255))

        Fonts.font30.drawString("Armor", x.toFloat() + 25.5f, -8f, Color(0, 0, 0).rgb)
        for (index in 3 downTo 0) {
            val stack = mc.player!!.inventory.armorInventory[index] ?: continue
            val stack2 = mc.player.inventory.armorInventory[index]

            renderItem.renderItemIntoGUI(stack, x, y)
            renderItem.renderItemOverlays(mc.fontRenderer, stack, x, y)
            GlStateManager.pushMatrix()
            Fonts.font35.drawString(
                (stack2.maxDamage - stack.itemDamage).toString(), x.toFloat() + 2.7f,
                y.toFloat() + 9.5f + Fonts.font30.fontHeight, sb
            )
            GlStateManager.popMatrix()
            x += 18
        }

        GlStateManager.enableAlpha()
        GlStateManager.disableBlend()
        GlStateManager.disableLighting()
        GlStateManager.disableCull()
        GL11.glPopMatrix()

        return Border(0F, 0F, 72F, 17F)


    }
}