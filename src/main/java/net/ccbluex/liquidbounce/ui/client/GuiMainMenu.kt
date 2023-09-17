/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client


import op.wawa.utils.render.BlurUtils
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager

class GuiMainMenu : WrappedGuiScreen() {

    private val backgroundResource = classProvider.createResourceLocation("wawa/mainmenu.png")

    private val icon = classProvider.createResourceLocation("pride/big.png")

    // Background
    private var currentX = 0f
    private var currentY = 0f

    override fun initGui() {
        val defaultHeight = representedScreen.height / 2f - 20 / 2f - 57

        representedScreen.buttonList.add(classProvider.createGuiButton(100, 20,
            (defaultHeight + 96).toInt(), 100, 20, "AltManager"))
        representedScreen.buttonList.add(classProvider.createGuiButton(102, 20,
            (defaultHeight + 72).toInt(), 100, 20, "Background"))

        representedScreen.buttonList.add(classProvider.createGuiButton(1, 20,
            (defaultHeight+24).toInt(), 100, 20, "SinglePlayer"))
        representedScreen.buttonList.add(classProvider.createGuiButton(2, 20,
            (defaultHeight+48).toInt(), 100, 20,"MulitPlayer"))

        representedScreen.buttonList.add(classProvider.createGuiButton(0, 20,
            (defaultHeight + 120).toInt(), 100, 20, "Options"))
        representedScreen.buttonList.add(classProvider.createGuiButton(4, 20,
            (defaultHeight + 144).toInt(), 100, 20, "Quit"))
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {

        val res = ScaledResolution(mc2)
        val width = representedScreen.width
        val height = representedScreen.height

        // Background
        val xDiff: Float = ((mouseX - height / 2).toFloat() - this.currentX) / res.scaleFactor.toFloat()
        val yDiff: Float = ((mouseY - width / 2).toFloat() - this.currentY) / res.scaleFactor.toFloat()
        this.currentX += xDiff * 0.2f
        this.currentY += yDiff * 0.2f

        GlStateManager.translate(this.currentX / 30.0f, this.currentY / 15.0f, 0.0f)
        RenderUtils.drawImage(backgroundResource, -30, -30, width + 60, height + 60)
        GlStateManager.translate(-this.currentX / 30.0f, -this.currentY / 15.0f, 0.0f)

        // Rect
        BlurUtils.blurArea(0F, 0F, 140F, height.toFloat(), 25F)
        RenderUtils.drawShadow(0, 0, 140, height)

        // Logo
        op.wawa.utils.render.RenderUtils.drawImage(icon,35, (height / 2f - 120).toInt(), 70, 70)

        representedScreen.superDrawScreen(mouseX, mouseY, partialTicks)
    }

    override fun actionPerformed(button: IGuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(classProvider.createGuiOptions(this.representedScreen, mc.gameSettings))
            1 -> mc.displayGuiScreen(classProvider.createGuiSelectWorld(this.representedScreen))
            2 -> mc.displayGuiScreen(classProvider.createGuiMultiplayer(this.representedScreen))
            4 -> mc.shutdown()
            100 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiAltManager(this.representedScreen)))
            101 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiServerStatus(this.representedScreen)))
            102 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiBackground(this.representedScreen)))
            103 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiModsMenu(this.representedScreen)))
            108 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiContributors(this.representedScreen)))
        }
    }
}