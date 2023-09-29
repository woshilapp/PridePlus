/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client


import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import op.wawa.utils.render.BlurUtils
import java.awt.Color

class GuiMainMenu : GuiScreen() {

    private val backgroundResource = ResourceLocation("wawa/mainmenu.png")

    private val icon = ResourceLocation("pride/big.png")

    // Background
    private var currentX = 0f
    private var currentY = 0f

    override fun initGui() {
        val defaultHeight = height / 2f - 20 / 2f - 57

        buttonList.add(GuiButton(100, 20,
            (defaultHeight + 96).toInt(), 100, 20, "AltManager"))
        buttonList.add(GuiButton(102, 20,
            (defaultHeight + 72).toInt(), 100, 20, "Background"))

        buttonList.add(GuiButton(1, 20,
            (defaultHeight+24).toInt(), 100, 20, "SinglePlayer"))
        buttonList.add(GuiButton(2, 20,
            (defaultHeight+48).toInt(), 100, 20,"MulitPlayer"))

        buttonList.add(GuiButton(0, 20,
            (defaultHeight + 120).toInt(), 100, 20, "Options"))
        buttonList.add(GuiButton(4, 20,
            (defaultHeight + 144).toInt(), 100, 20, "Quit")
        )
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {

        val res = ScaledResolution(mc)
        val width = res.scaledWidth
        val height = res.scaledHeight

        // Background
        val xDiff: Float = ((mouseX - height / 2).toFloat() - this.currentX) / res.scaleFactor.toFloat()
        val yDiff: Float = ((mouseY - width / 2).toFloat() - this.currentY) / res.scaleFactor.toFloat()
        this.currentX += xDiff * 0.2f
        this.currentY += yDiff * 0.2f

        GlStateManager.translate(this.currentX / 30.0f, this.currentY / 15.0f, 0.0f)
        RenderUtils.drawImage(backgroundResource, -30, -30, width + 60, height + 60)
        GlStateManager.translate(-this.currentX / 30.0f, -this.currentY / 15.0f, 0.0f)

        // Rect
        BlurUtils.blurArea(0F, 0F, width.toFloat(), height.toFloat(), 10F)
        //RenderUtils.drawShadow(0, 0, 140, height)
        RenderUtils.drawRoundRect(10F, height / 2F - 140F, 120F, height / 2f - 20 / 2f - 57 + 160F, 40F, Color(0,0,0,100).rgb)
        RenderUtils.drawOutlinedRoundedRect(10.0, height / 2.0 - 140.0, 120.0, height / 2f - 20 / 2f - 57 + 160.0, 40.0, 2F, Color.WHITE.rgb)

        // Logo
        RenderUtils.drawImage(icon,35, (height / 2f - 120).toInt(), 70, 70)

        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(GuiOptions(this, mc.gameSettings))
            1 -> mc.displayGuiScreen(GuiCreateWorld(this))
            2 -> mc.displayGuiScreen(GuiMultiplayer(this))
            4 -> mc.shutdown()
            100 -> mc.displayGuiScreen((GuiAltManager(this)))
            102 -> mc.displayGuiScreen((GuiBackground(this)))
            103 -> mc.displayGuiScreen((GuiModsMenu(this)))
        }
    }
}