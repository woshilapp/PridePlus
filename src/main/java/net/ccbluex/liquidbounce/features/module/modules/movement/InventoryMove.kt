/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.features.value.BoolValue
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.settings.KeyBinding
import net.minecraft.network.play.client.CPacketPlayer
import org.lwjgl.input.Keyboard

@ModuleInfo(name = "InvMove", description = "Allows you to walk while an inventory is opened.", category = ModuleCategory.MOVEMENT)
class InventoryMove : Module() {
    private val noDetectableValue = BoolValue("NoDetectable", false)
    private val rotateValue = BoolValue("Rotate", true)
    val aacAdditionProValue = BoolValue("AACAdditionPro", false)
    private val noMoveClicksValue = BoolValue("NoMoveClicks", false)

    private val blinkPacketList = mutableListOf<CPacketPlayer>()
    var lastInvOpen = false
        private set
    var invOpen = false
        private set

    private fun updateKeyState() {
        if (mc.currentScreen != null && mc.currentScreen !is GuiChat && (!noDetectableValue.get() || mc.currentScreen !is GuiContainer)) {
            mc.gameSettings.keyBindForward.pressed = mc.gameSettings.keyBindRight.isKeyDown
            mc.gameSettings.keyBindLeft.pressed = mc.gameSettings.keyBindLeft.isKeyDown
            mc.gameSettings.keyBindJump.pressed = mc.gameSettings.keyBindJump.isKeyDown
            mc.gameSettings.keyBindSprint.pressed = mc.gameSettings.keyBindSprint.isKeyDown

            if (rotateValue.get()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    if (mc.player!!.rotationPitch > -90) {
                        mc.player!!.rotationPitch -= 5
                    }
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    if (mc.player!!.rotationPitch < 90) {
                        mc.player!!.rotationPitch += 5
                    }
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    mc.player!!.rotationYaw -= 5
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    mc.player!!.rotationYaw += 5
                }
            }
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        updateKeyState()
    }

    @EventTarget
    fun onScreen(event: ScreenEvent) {
        updateKeyState()
    }

    @EventTarget
    fun onClick(event: ClickWindowEvent) {
        if (noMoveClicksValue.get() && MovementUtils.isMoving) {
            event.cancelEvent()
        }
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        blinkPacketList.clear()
        invOpen = false
        lastInvOpen = false
    }

    override fun onDisable() {
        if (!mc.gameSettings.keyBindForward.isKeyDown || mc.currentScreen != null) {
            mc.gameSettings.keyBindForward.pressed = false
        }
        if (!mc.gameSettings.keyBindBack.isKeyDown || mc.currentScreen != null) {
            mc.gameSettings.keyBindBack.pressed = false
        }
        if (!mc.gameSettings.keyBindRight.isKeyDown || mc.currentScreen != null) {
            mc.gameSettings.keyBindRight.pressed = false
        }
        if (!mc.gameSettings.keyBindLeft.isKeyDown || mc.currentScreen != null) {
            mc.gameSettings.keyBindLeft.pressed = false
        }
        if (!mc.gameSettings.keyBindJump.isKeyDown || mc.currentScreen != null) {
            mc.gameSettings.keyBindJump.pressed = false
        }
        if (!mc.gameSettings.keyBindSprint.isKeyDown || mc.currentScreen != null) {
            mc.gameSettings.keyBindSprint.pressed = false
        }

    }

    override val tag: String?
        get() = if (aacAdditionProValue.get()) "AACAdditionPro" else "Bypass"
}
