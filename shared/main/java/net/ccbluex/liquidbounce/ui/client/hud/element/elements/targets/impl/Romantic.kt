package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.impl;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.TargetStyle
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.pow

class Romantic(inst:Target): TargetStyle("Romantic",inst,true) {
    private fun drawRomanticHead(skin: IResourceLocation, width: Int, height: Int, hurtPercent: Float) {
        GL11.glColor4f(1F, 1F - hurtPercent, 1F - hurtPercent, 1F)
        mc.textureManager.bindTexture(skin)
        Gui.drawScaledCustomSizeModalRect(2, 2, 8F, 8F, 8, 8, width, height,
            64F, 64F)
    }
    val width = 26f + Fonts.font30.getStringWidth(mc.thePlayer!!.name!!).coerceAtLeast(90)

    override fun drawTarget(entity: IEntityLivingBase) {
        val target = Target()
        val size = 24f
        val hurtPercent = mc.thePlayer!!.hurtTime / 10f
        val scale = if (hurtPercent == 0f) { 1f } else if (hurtPercent < 0.5f) {
            1 - (0.2f * hurtPercent * 2)
        } else {
            0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
        }
        RenderUtils.drawRect(0f,0f,width,38f, Color(0,0,0,30))
        RenderUtils.drawShadow(0, 0, width.toInt(), 38)
        GL11.glPushMatrix()
        GL11.glTranslatef(5f, 5f, 0f)
        GL11.glScalef(scale, scale, scale)
        GL11.glTranslatef(((size * 0.5f * (1 - scale)) / scale), ((size * 0.5f * (1 - scale)) / scale), 0f)
        val playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)
        if (playerInfo != null) {
            val locationSkin = playerInfo.locationSkin
            drawRomanticHead(locationSkin, 24, 24,hurtPercent)
        }
        GL11.glPopMatrix()
        //Damage Anim
        if (easingHealth > mc.thePlayer!!.health)
            RenderUtils.drawRect(34F, 20F,34f+(width-42f) * (easingHealth / mc.thePlayer!!.maxHealth),28F,
                Color(255,10,10)
            )
        GlStateManager.resetColor()
        Fonts.font30.drawString(mc.thePlayer!!.name.toString(),34f,20f-Fonts.font30.fontHeight,Color(255,255,255).rgb)
        RenderUtils.drawRect(34f,20f,width - 8f,28f,Color(61,61,61,50))
        RenderUtils.drawRect(34f,20f,34f+(mc.thePlayer!!.health/ mc.thePlayer!!.maxHealth)*(width - 42f),28f,Color(255,255,255))
        // Heal animation
        if (easingHealth < mc.thePlayer!!.health)
            RenderUtils.drawRect((easingHealth / mc.thePlayer!!.maxHealth) * (width-42f) + 34f, 20F,
                (mc.thePlayer!!.health / mc.thePlayer!!.maxHealth) * (width-42f) + 34f, 28F, Color(255,255,255).rgb)
        easingHealth += ((mc.thePlayer!!.health - easingHealth) / 2.0F.pow(10.0F - target.fadeSpeed.get())) * RenderUtils.deltaTime

    }

    override fun getBorder(entity: IEntityLivingBase?): Border? {
        return Border(0f,0f,width,28f)
    }
}
