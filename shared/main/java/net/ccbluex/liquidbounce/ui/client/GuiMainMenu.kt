/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client


import me.utils.render.BlurUtils
import me.utils.render.GaussianBlur
import me.utils.render.StencilUtil
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RoundedUtil
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color

class GuiMainMenu : WrappedGuiScreen() {

    private val backgroundResource = classProvider.createResourceLocation("pride/menu/funny.png")

    private val blurredRect = classProvider.createResourceLocation("pride/menu/rect-test.png")

    private var currentX = 0f
    private var currentY = 0f
    var arrayList: ArrayList<me.ui.IGuiButton> = ArrayList<me.ui.IGuiButton>()
    override fun initGui() {
        val defaultHeight = representedScreen.height / 2f - 20 / 2f - 57

        representedScreen.buttonList.add(classProvider.createGuiButton(100, representedScreen.width / 2 - 70,
            (defaultHeight + 96).toInt(), 140, 20, "AltManager"))
        representedScreen.buttonList.add(classProvider.createGuiButton(102, representedScreen.width / 2 - 70,
            (defaultHeight + 72).toInt(), 140, 20, "Background"))

        representedScreen.buttonList.add(classProvider.createGuiButton(1, representedScreen.width / 2 - 70,
            (defaultHeight+24).toInt(), 140, 20, "SinglePlayer"))
        representedScreen.buttonList.add(classProvider.createGuiButton(2, representedScreen.width / 2 - 70,
            (defaultHeight+48).toInt(), 140, 20,"MulitPlayer"))
        // Minecraft Realms
        //		this.buttonList.add(new classProvider.createGuiButton(14, this.width / 2 - 100, j + 24 * 2, I18n.format("menu.online", new Object[0])));
        representedScreen.buttonList.add(classProvider.createGuiButton(0, representedScreen.width / 2 - 70,
            (defaultHeight + 120).toInt(), 140, 20, "Options"))
        representedScreen.buttonList.add(classProvider.createGuiButton(4, representedScreen.width / 2 - 70,
            (defaultHeight + 144).toInt(), 140, 20, "Quit"))
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val sr = ScaledResolution(mc2)
        val width = sr.scaledWidth
        val height = sr.scaledHeight
        RenderUtils.drawImage(backgroundResource, 0, 0, width, height)
        val outlineImgWidth = 688 / 2f
        val outlineImgHeight = 681 / 2f
        RenderUtils.drawImage(
            blurredRect, (width / 2f - outlineImgWidth / 2f).toInt(), (height / 2f - outlineImgHeight / 2f).toInt(),
            outlineImgWidth.toInt(), outlineImgHeight.toInt()
        )
/*        if (dev.tenacity.ui.mainmenu.CustomMainMenu.animatedOpen) {
            //    tenacityFont80.drawCenteredString("Tenacity", width / 2f, height / 2f - 110, Color.WHITE.getRGB());
            //    tenacityFont32.drawString(Tenacity.VERSION, width / 2f + tenacityFont80.getStringWidth("Tenacity") / 2f - (tenacityFont32.getStringWidth(Tenacity.VERSION) / 2f), height / 2f - 113, Color.WHITE.getRGB());
        }*/
        GL11.glEnable(GL11.GL_BLEND)
        StencilUtil.initStencilToWrite()
        StencilUtil.readStencilBuffer(1)
        val circleW = 174 / 2f
        val circleH = 140 / 2f
        val rs = classProvider.createResourceLocation("pride/menu/circle-funny.png")
        mc.textureManager.bindTexture(rs)
        RenderUtils.drawImage(rs, (mouseX - circleW / 2F).toInt(), (mouseY - circleH / 2f).toInt(),
            circleW.toInt(), circleH.toInt()
        )
        StencilUtil.uninitStencilBuffer()
        Fonts.fontBold120.drawCenteredString("PridePlus", width / 2f, height / 2f - 110, Color.WHITE.rgb,true)
        Fonts.bold35.drawString(
            "B"+LiquidBounce.CLIENT_VERSION,
            width / 2f + Fonts.fontBold120.getStringWidth("PridePlus") / 2f - Fonts.bold35.getStringWidth("B"+LiquidBounce.CLIENT_VERSION) / 2f,
            height / 2f - 113,
            Color.WHITE.rgb,true
        )

        Fonts.bold30.drawCenteredString("by WaWa", width / 2f, height / 2f - 68, Color.WHITE.rgb,true)
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