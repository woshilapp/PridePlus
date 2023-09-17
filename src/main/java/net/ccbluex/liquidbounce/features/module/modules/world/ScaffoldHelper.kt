/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.world

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Parkour
import net.ccbluex.liquidbounce.features.value.BoolValue
import net.ccbluex.liquidbounce.features.value.FloatValue
import net.ccbluex.liquidbounce.features.value.ListValue
/**
 *
 * By WaWa
 * @Date on 2023/5/27
 *
 */
@ModuleInfo(name = "ScaffoldHelper", description = "For GrimAC",category = ModuleCategory.WORLD)
class ScaffoldHelper : Module() {
    private val modeValue = ListValue("Mode", arrayOf("State"), "State")

    private val scaffoldModeValue = ListValue("ScaffoldMode", arrayOf("Scaffold"/*, "Scaffold3","Scaffold2"*/), "Scaffold")

    private val jumpModeValue = ListValue("JumpMode", arrayOf("mc", "mc2","MotionY","Key", "Parkour", "Off"), "Off")

    private val timerValue = BoolValue("OnGroundTimer", true)

    private val rotationValue = BoolValue("SmartRotation", true)

    private val customPitchValue = FloatValue("CustomPitch",26.5F,0F,90F).displayable { rotationValue.get() }

//    val Scaffold = LiquidBounce.moduleManager[Scaffold::class.java]
//    val LBScaffold = LiquidBounce.moduleManager[LBScaffold::class.java]
//    val ScaffoldNew = LiquidBounce.moduleManager[ScaffoldNew::class.java]
//    val Timer = LiquidBounce.moduleManager[Timer::class.java]
//    val Parkour = LiquidBounce.moduleManager[Parkour::class.java]

    fun jump(){
        if (mc2.player.onGround || !mc2.player.isAirBorne) {
            when (jumpModeValue.get().toLowerCase()) {
                "mc" -> mc.thePlayer!!.jump()
                "mc2" -> mc2.player.jump()
                "motiony" -> mc.thePlayer!!.motionY = 0.42
                "key" -> {
                    mc2.gameSettings.keyBindJump.pressed = true
                    mc2.gameSettings.keyBindJump.pressed = false
                }
            }
        }
    }

    override fun onDisable() {
        when (scaffoldModeValue.get().toLowerCase()) {
            "scaffold" -> LiquidBounce.moduleManager[Scaffold::class.java].state = false
        }
        LiquidBounce.moduleManager[Parkour::class.java].state = false
        LiquidBounce.moduleManager[Timer::class.java].state = false

        mc.gameSettings.keyBindJump.pressed = false
        super.onDisable()
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent){
        if(jumpModeValue.get() == "Parkour") {
            LiquidBounce.moduleManager[Parkour::class.java].state = true
        }else{
            LiquidBounce.moduleManager[Parkour::class.java].state = false
            jump()
        }

        if (rotationValue.get()){
            setYaw()
            mc.thePlayer!!.rotationPitch = customPitchValue.get()
        }

        if (mc.thePlayer!!.onGround){
            if(timerValue.get())
                LiquidBounce.moduleManager[Timer::class.java].state = true

            if (modeValue.get().toLowerCase() == "state") {
                when (scaffoldModeValue.get().toLowerCase()) {
                    "scaffold" -> LiquidBounce.moduleManager[Scaffold::class.java].state = false
                }
            }
        }else {
            if (timerValue.get())
                LiquidBounce.moduleManager[Timer::class.java].state = false
            if (modeValue.get().toLowerCase() == "state") {
                when (scaffoldModeValue.get().toLowerCase()) {
                    "scaffold" -> LiquidBounce.moduleManager[Scaffold::class.java].state = true
                }
            }
        }
    }

    private fun setYaw() {
        val thePlayer = mc.thePlayer!!
        val x = java.lang.Double.valueOf(thePlayer.motionX)
        val y = java.lang.Double.valueOf(thePlayer.motionZ)
        if (mc.gameSettings.keyBindForward.isKeyDown) {
            if (y != null &&
                y.toDouble() > 0.1
            ) {
                thePlayer.rotationYaw = 0.0f
            }
            if (y != null &&
                y.toDouble() < -0.1
            ) {
                thePlayer.rotationYaw = 180.0f
            }
            if (x != null &&
                x.toDouble() > 0.1
            ) {
                thePlayer.rotationYaw = -90.0f
            }
            if (x != null &&
                x.toDouble() < -0.1
            ) {
                thePlayer.rotationYaw = 90.0f
            }
        }

    }
}
