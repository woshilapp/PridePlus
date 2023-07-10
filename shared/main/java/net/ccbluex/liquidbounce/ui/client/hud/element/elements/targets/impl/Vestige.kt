package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.impl

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.TargetStyle
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color

class Vestige(inst: Target): TargetStyle("Vestige",inst,true) {
    override fun drawTarget(entity: IEntityLivingBase) {
        val width = (Fonts.font30.getStringWidth(entity.name.toString()) + 50).coerceAtLeast(125).toFloat()

        RenderUtils.drawRoundedRect(0F,0F,width,50F, 3,Color(0,0,0,targetInstance.bgAlphaValue.get()).rgb)
        Fonts.font35.drawString(entity.name.toString(),5F,10F,Color.WHITE.rgb)
        updateAnim(entity.health)
        Fonts.font35.drawString(decimalFormat3.format(entity.health) + "HP",5F,Fonts.font30.fontHeight + 12F,Color.WHITE.rgb)
        val height = (Fonts.font35.fontHeight * 2) + 17F
        RenderUtils.drawRoundGradientSideways2(5.0F,
            height, (5 + (entity.health/entity.maxHealth) * 70), (height + 8F),2F,Color(0,190,250).rgb,Color(100,0,250).rgb)
        val playerInfo = mc.netHandler.getPlayerInfo(entity.uniqueID)
        if (playerInfo != null) {
            // Draw head
            val locationSkin = playerInfo.locationSkin
            drawHead(locationSkin,
                80F,
                5F,
                1F,
                40, 40,
                1F, 0.4F + 0.6F, 0.4F + 0.6F)
        }
    }
    override fun handleBlur(entity: IEntityLivingBase) {
        val width = (Fonts.font30.getStringWidth(entity.name.toString()) + 50).coerceAtLeast(125).toFloat()

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        RenderUtils.quickDrawRect(0F, 0F, width, 50F)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }
    override fun getBorder(entity: IEntityLivingBase?): Border {
        return Border(0F,0F,125F,50F)
    }

}

