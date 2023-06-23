/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client


import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color

class GuiMainMenu : WrappedGuiScreen() {
    private var currentX = 0f
    private var currentY = 0f
    var arrayList: ArrayList<me.ui.IGuiButton> = ArrayList<me.ui.IGuiButton>()
    override fun initGui() {
        val defaultHeight = representedScreen.height / 4.5 + 18

        representedScreen.buttonList.add(classProvider.createGuiButton(100, representedScreen.width - 120,
            (defaultHeight + 96).toInt(), 100, 20, "AltManager"))
        representedScreen.buttonList.add(classProvider.createGuiButton(102, representedScreen.width - 120,
            (defaultHeight + 72).toInt(), 100, 20, "Background"))

        representedScreen.buttonList.add(classProvider.createGuiButton(1, representedScreen.width - 120,
            (defaultHeight+24).toInt(), 100, 20, "SinglePlayer"))
        representedScreen.buttonList.add(classProvider.createGuiButton(2, representedScreen.width - 120,
            (defaultHeight+48).toInt(), 100, 20,"MulitPlayer"))
        // Minecraft Realms
        //		this.buttonList.add(new classProvider.createGuiButton(14, this.width / 2 - 100, j + 24 * 2, I18n.format("menu.online", new Object[0])));
        representedScreen.buttonList.add(classProvider.createGuiButton(0, representedScreen.width - 120,
            (defaultHeight + 120).toInt(), 100, 20, "Options"))
        representedScreen.buttonList.add(classProvider.createGuiButton(4, representedScreen.width - 120,
            (defaultHeight + 144).toInt(), 100, 20, "Quit"))
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        //representedScreen.drawBackground(0)

        val h = representedScreen.height
        val w = representedScreen.width
        //val sr = ScaledResolution(mc2)
        val res = ScaledResolution(mc2)
        val xDiff: Float = ((mouseX - h / 2).toFloat() - this.currentX) / res.scaleFactor.toFloat()
        val yDiff: Float = ((mouseY - w / 2).toFloat() - this.currentY) / res.scaleFactor.toFloat()
        this.currentX += xDiff * 0.3f
        this.currentY += yDiff * 0.3f
        GlStateManager.translate(this.currentX / 30.0f, this.currentY / 15.0f, 0.0f)
        RenderUtils.drawImage4("pride/bg.jpg", -30, -30, res.scaledWidth + 60, res.scaledHeight + 60)
        GlStateManager.translate(-this.currentX / 30.0f, -this.currentY / 15.0f, 0.0f)

        RenderUtils.drawRect(representedScreen.width - 142,
            0,
            representedScreen.width,
            representedScreen.height, Color(0,0,0,80).rgb)
        Fonts.bold95.drawCenteredString("PridePlus",
            ((95 / 2) - (Fonts.bold95.getStringWidth("PridePlus") / 2) + representedScreen.width - 70.5).toFloat(),
            representedScreen.height / 6F, Color.WHITE.rgb, true)
        FontLoaders.F14.drawString(" 一定是狼牙干的！",2,h - FontLoaders.F14.height,Color.WHITE.rgb)

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