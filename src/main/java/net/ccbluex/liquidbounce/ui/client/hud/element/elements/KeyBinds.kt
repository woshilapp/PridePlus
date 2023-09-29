package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import me.utils.render.ShadowUtils
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.value.BoolValue
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color


@ElementInfo(name = "KeyBinds")
class KeyBinds(x: Double = 85.11, y: Double = 21.11, scale: Float = 1F,
               side: Side = Side(Side.Horizontal.LEFT, Side.Vertical.UP)
) : Element(x, y, scale, side) {
    private val onlyState = BoolValue("OnlyModuleState", true)
    private val shadow = BoolValue("Shadow", true)
    private val chineseValue = BoolValue("Chinese", true)
    private var anmitY = 0F
    override fun drawElement(): Border? {
        var y2 = 0
        anmitY = RenderUtils.getAnimationState2(anmitY.toDouble(),(15 + getmoduley()).toFloat().toDouble(), 250.0).toFloat()
            //draw Background
        RenderUtils.drawRoundRect(
            0f,
            0f,
            114f,
            anmitY,
            5.0F,
            Color(0, 0, 0, 110).rgb
        )
        if (shadow.get()){
            ShadowUtils.shadow(10f,{
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                RenderUtils.originalRoundedRect(
                    0f,
                    0f,
                    114f,
                    anmitY,
                    5.0F,
                    Color(0,0,0).rgb
                )
                GL11.glPopMatrix()

            },{
                GL11.glPushMatrix()
                GL11.glTranslated(renderX, renderY, 0.0)
                GL11.glScalef(scale, scale, scale)
                GlStateManager.enableBlend()
                GlStateManager.disableTexture2D()
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                RenderUtils.fastRoundedRect(                0f,
                    0f,
                    114f,
                    anmitY,
                    5.0F
                )
                GlStateManager.enableTexture2D()
                GlStateManager.disableBlend()
                GL11.glPopMatrix()
            })
        }
//        if (outline.get()){
//            RenderUtils.drawGidentOutlinedRoundedRect(0.0, 0.0, 114.0,anmitY.toDouble(), 8.0,linewidth.get())
//        }



        GlStateManager.resetColor()

        //draw Title
        val fwidth = 10F
        FontLoaders.F16.drawString("按键显示", fwidth, 4.5f, -1, true)

        //draw Module Bind
        for (module in LiquidBounce.moduleManager.modules) {
            if (module.keyBind == 0) continue
            if (onlyState.get()) {
                if (!module.state) continue
            }
            FontLoaders.F16.drawString(module.name, fwidth, y2 + 19f, -1, true)
            FontLoaders.F16.drawString(
                if (chineseValue.get()) {
                    if (module.state) "[开启]" else "[关闭]"
                }else{
                    if (module.state) "[Open]" else "[关闭]"
                     },
                (108 - FontLoaders.F16.getStringWidth(if (module.state) "[开启]" else "[关闭]")).toFloat(),
                y2 + 19f,
                if (module.state) Color(255, 255, 255).rgb else Color(255, 255, 255).rgb,
                true
            )
            y2 += 10
        }
        return Border(0f, 0f, 114f, (17 + getmoduley()).toFloat())
    }

    fun getmoduley(): Int {
        var y = 0
        for (module in LiquidBounce.moduleManager.modules) {
            if (module.keyBind == 0) continue
            if (onlyState.get()) {
                if (!module.state) continue
            }
            y += 12
        }
        return y
    }
}