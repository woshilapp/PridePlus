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
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
/**
 *
 * By WaWa
 * @Date on 2023/5/27
 *
 */
@ModuleInfo(name = "ScaffoldHelper", description = "For GrimAC",category = ModuleCategory.WORLD)
class ScaffoldHelper : Module() {
    private val modeValue = ListValue("Mode", arrayOf("State"), "State")

    private val scaffoldModeValue = ListValue("Scaffold Mode", arrayOf("Scaffold", "Scaffold3","Scaffold2"), "ScaffoldNew")

    private val jumpModeValue = ListValue("Jump Mode", arrayOf("mc", "mc2","MotionY","Key", "Parkour", "Off"), "Off")

    private val timerValue = BoolValue("On Ground Timer", true)

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
            "scaffold" -> LiquidBounce.moduleManager[Scaffold::class.java]!!.state = false
            "scaffold3" -> LiquidBounce.moduleManager[Scaffold3::class.java]!!.state = false
            "scaffold2" -> LiquidBounce.moduleManager[Scaffold2::class.java]!!.state = false
        }
        LiquidBounce.moduleManager[Parkour::class.java].state = false
        LiquidBounce.moduleManager[Timer::class.java].state = false

        mc.gameSettings.keyBindJump.pressed = false
        super.onDisable()
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent){
        if(jumpModeValue.get() == "Parkour") {
            LiquidBounce.moduleManager[Parkour::class.java]!!.state = true
        }else{
            LiquidBounce.moduleManager[Parkour::class.java]!!.state = false
            jump()
        }

        if (mc.thePlayer!!.onGround){
            if(timerValue.get())
                LiquidBounce.moduleManager[Timer::class.java]!!.state = true

            if (modeValue.get().toLowerCase() == "state") {
                when (scaffoldModeValue.get().toLowerCase()) {
                    "scaffold" -> LiquidBounce.moduleManager[Scaffold::class.java]!!.state = false
                    "scaffold3" -> LiquidBounce.moduleManager[Scaffold3::class.java]!!.state = false
                    "scaffold2" -> LiquidBounce.moduleManager[Scaffold2::class.java]!!.state = false
                }
            }
        }else {
            if (timerValue.get())
                LiquidBounce.moduleManager[Timer::class.java].state = false
            if (modeValue.get().toLowerCase() == "state") {
                when (scaffoldModeValue.get().toLowerCase()) {
                    "scaffold" -> LiquidBounce.moduleManager[Scaffold::class.java]!!.state = true
                    "scaffold3" -> LiquidBounce.moduleManager[Scaffold3::class.java]!!.state = true
                    "scaffold2" -> LiquidBounce.moduleManager[Scaffold2::class.java]!!.state = true
                }
            }
        }
    }
}
