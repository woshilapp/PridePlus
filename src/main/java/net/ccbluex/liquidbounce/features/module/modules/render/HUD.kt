/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render

import com.mojang.realmsclient.gui.ChatFormatting
import me.utils.render.VisualUtils
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.value.*
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.ColorUtil
import net.ccbluex.liquidbounce.utils.render.Colors
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.ResourceLocation
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*

@ModuleInfo(name = "HUD", description = "Toggles visibility of the HUD.", category = ModuleCategory.RENDER, array = false)
class HUD : Module() {
    val hotbar = BoolValue("Hotbar", true)
    val blackHotbarValue = BoolValue("BlackHotbar", true)
    val inventoryParticle = BoolValue("InventoryParticle", false)
    private val blurValue = BoolValue("Blur", false)
    val fontChatValue = BoolValue("FontChat", false)

    val logValue = ListValue("LogMode", arrayOf("idk", "141Sense", "Jello", "PowerX", "None", "Novoline"), "None")
    val shadowValue = ListValue("TextShadowMode", arrayOf("Good", "Long", "D1ck"), "Good")
    val ClientName = TextValue("ClientName", "PridePlus")
    val DevName = TextValue("DevName", "WaWa")

    val radius = FloatValue("Radius",15F,0F,100F)

    val textRedValue = IntegerValue("Text-Red", 255, 0, 255)
    val textGreenValue = IntegerValue("Text-Green", 255, 0, 255)
    val textBlueValue = IntegerValue("Text-Blue", 255, 0, 255)

    val redValue = IntegerValue("NovolineRed", 255, 0, 255)
    val greenValue = IntegerValue("NovolineGreen", 255, 0, 255)
    val blueValue = IntegerValue("NovolineBlue", 255, 0, 255)
    @JvmField
    val domainValue = TextValue("Scoreboard-Domain", "PridePlus-2K23")
    val hueInterpolation = BoolValue("DoubleColor-Interpolate", false)
    val simpleFPS = BoolValue("Simple-FPS-Render", false)
    val prideBackCN = BoolValue("PrideLogoTextCN", false)
    val prideBack = ListValue("PrideLogoTextBack-Mode", arrayOf("FPS","miHoYo","Pro","114514","idan","Custom","Off"),"Custom")
    //val prideBackValue = TextValue("PrideLogoBack-CustomText", "But Pa1m0n i love u")

    val customColor = Color(redValue.get(), greenValue.get(), blueValue.get())
    val customTextColor = Color(textRedValue.get(), textGreenValue.get(), textBlueValue.get()).rgb

    private fun mixColors(color1: Color, color2: Color): Color {
        return ColorUtil.interpolateColorsBackAndForth(
            15,
            1,
            color1,
            color2,
            hueInterpolation.get()
        )
    }


    @EventTarget
    fun onRender2D(event: Render2DEvent?) {
        val sr = ScaledResolution(mc)
        val height = sr.scaledHeight
        val width = sr.scaledWidth
        var p = " fds"
        var i = "dsfs "
        var p2 = "sdfds "
        var u = " dsff"
        p = "Pr"
        i = "ide"
        p2 = "Pl"
        u = "us"
        var back:String = "FPS:" + Minecraft.getDebugFPS()

        when(prideBack.get().toLowerCase()){
            "fps" -> back = "FPS:" + Minecraft.getDebugFPS() + "."
            "mihoyo" -> back = "By miHoYo-Team"
            "pro" -> back = "WaWa我给你跪下了"
            "114514" -> back = "哼啊啊啊啊啊啊啊啊"
            "idan" -> back = "idan正义集团为你保驾护航"
            "off" -> back = ""
        }
        var text = ""
        if (prideBackCN.get()){
            text = "骄傲加"
        }else{
            text = p+i+p2+u
        }
        if (prideBack.get().toLowerCase() == "off"){
            FontLoaders.F16.drawString(text+" "+LiquidBounce.CLIENT_VERSION,2F,height - (Fonts.font35.fontHeight + 2F),customTextColor,true)
        }else{
            FontLoaders.F16.drawString(text+" "+LiquidBounce.CLIENT_VERSION+", "+ back,2F,height - (Fonts.font35.fontHeight + 2F),Color.WHITE.rgb,true)
        }

        if (simpleFPS.get()) mc.fontRenderer.drawString("FPS: " + Minecraft.getDebugFPS(),width / 2F - (mc.fontRenderer.getStringWidth("FPS: " + Minecraft.getDebugFPS()) / 2F),1F,Color.WHITE.rgb,true)

        when (logValue.get().toLowerCase()) {

            "novoline" -> {
                val time = SimpleDateFormat("HH:mm").format(Calendar.getInstance().time)
                Fonts.font35.drawString(
                        ClientName.get() + ChatFormatting.GRAY + " (" + ChatFormatting.WHITE + time + ChatFormatting.GRAY + ")",
                        3f,
                        4f,
                        customColor.rgb,
                        true
                )
            }

            "idk" -> {
                Fonts.fontSFUI35.drawStringWithShadow(ClientName.get() + "#0810", 7F, 10F,   Color(255, 255, 255, 245).rgb)
            }
            "powerx" ->{
                Gui.drawRect(2, 1, 78, 18, Color(10, 10, 10, 180).rgb)
                Gui.drawRect(2, 1, 4, 18, Color(240, 240, 240, 245).rgb)
                Fonts.fontSFUI56.drawStringWithShadow(ClientName.get(), 7F, 3F,   Color(255, 255, 255, 245).rgb)
            }
            "jello" ->{
                Fonts.fontSFUI120 .drawString(ClientName.get(), 10.0f, 10f, VisualUtils.reAlpha(Colors.WHITE.c, 0.75f))
            }
            "141sense" ->{
                RenderUtils.drawRect(3.0f, 3.0f, 142.0f, 63.0f, Color(255, 255, 255, 120).rgb)
                Fonts.font80.drawCenteredString(ClientName.get(), 71.0f, 7.0f, java.awt.Color(0, 0, 0, 180).rgb)
                Fonts.font35.drawCenteredString("by" + DevName.get() , 71.0f, 25.0f, java.awt.Color(0, 0, 0, 180).rgb)
                Fonts.font80.drawString("_______________", 6.0f, 19.0f, java.awt.Color(0, 0, 0, 180).rgb)
                Fonts.font35.drawString("UserName:" + mc.player!!.name, 45.0f - Fonts.font35.getStringWidth("UserName:" + mc.player!!.name).toFloat() / 2.0f + 28.0f, 40.0f, java.awt.Color(0, 0, 0, 180).rgb)
                Fonts.font35.drawString("FPS:" + Minecraft.getDebugFPS(), 45.0f - Fonts.font35.getStringWidth("FPS:" + Minecraft.getDebugFPS()).toFloat() / 2.0f + 28.0f, 52.0f, java.awt.Color(0, 0, 0, 180).rgb)
            }
        }
        if (mc.currentScreen is GuiHudDesigner)
            return

        LiquidBounce.hud.render(false)
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        LiquidBounce.hud.update()
    }

    @EventTarget
    fun onKey(event: KeyEvent) {
        LiquidBounce.hud.handleKey('a', event.key)
    }

    @EventTarget(ignoreCondition = true)
    fun onScreen(event: ScreenEvent) {
        if (mc.world == null || mc.player == null) return
        if (state && blurValue.get() && !mc.entityRenderer.isShaderActive() && event.guiScreen != null &&
                !(event.guiScreen is GuiChat || event.guiScreen is GuiHudDesigner)) mc.entityRenderer.loadShader(
            ResourceLocation("pride/blur.json")
        ) else if (mc.entityRenderer.shaderGroup != null &&
                mc.entityRenderer.shaderGroup.shaderGroupName.contains("pride/blur.json")) mc.entityRenderer.stopUseShader()
    }

    init {
        state = true
    }
}