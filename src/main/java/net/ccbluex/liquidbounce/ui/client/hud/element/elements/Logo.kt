/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import com.google.gson.JsonElement
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.utils.misc.MiscUtils
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.features.value.TextValue
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files
import java.util.*
import javax.imageio.ImageIO


/**
 * CustomHUD image element
 *
 * Draw custom image
 */
@ElementInfo(name = "Logo")
class Logo(x: Double = 20.33, y: Double = 16.00, scale: Float = 0.30F,
           side: Side = Side.default()) : Element(x, y, scale, side) {

    companion object {

        /**
         * Create default element
         */
        fun default(): Logo {
            val image = Logo()

            image.x = 0.0
            image.y = 0.0

            return image
        }

    }

    private var width = 256
    private var height = 256

    /**
     * Draw element
     */
    override fun drawElement(): Border {
        RenderUtils.drawImage4("pride/big.png", 0, 0, width, height)

        return Border(0F, 0F, width.toFloat(), height.toFloat())
    }

}