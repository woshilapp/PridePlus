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

    private val scaffoldModeValue = ListValue("Scaffold Mode", arrayOf("Scaffold", "ScaffoldNew","LBScaffold"), "ScaffoldNew")

    private val jumpModeValue = ListValue("Jump Mode", arrayOf("mc", "mc2","MotionY","Key", "Parkour", "Off"), "Off")

    private val timerValue = BoolValue("On Ground Timer", true)

    val Scaffold = LiquidBounce.moduleManager[Scaffold::class.java] as Scaffold
    val LBScaffold = LiquidBounce.moduleManager[LBScaffold::class.java] as LBScaffold
    val ScaffoldNew = LiquidBounce.moduleManager[ScaffoldNew::class.java] as ScaffoldNew
    val Timer = LiquidBounce.moduleManager[Timer::class.java] as Timer
    val Parkour = LiquidBounce.moduleManager[Parkour::class.java] as Parkour

    fun Jump(){
        if (mc2.player.onGround || !mc2.player.isAirBorne) {
            when (jumpModeValue.get().toLowerCase()) {
                "mc" -> mc.thePlayer!!.jump()
                "mc2" -> mc2.player.jump()
                "motiony" -> mc.thePlayer!!.motionY = 0.42
                "key" -> mc.gameSettings.keyBindJump.onTick(mc.gameSettings.keyBindJump.keyCode)
            }
        }
    }

    override fun onDisable() {
        Parkour.state = false
        Scaffold.state = false
        LBScaffold.state = false
        ScaffoldNew.state = false
        Timer.state = false
        mc.gameSettings.keyBindJump.pressed = false
        super.onDisable()
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent){
        if(jumpModeValue.get() == "Parkour") {
            Parkour.state = true
        }else{
            Parkour.state = false
            Jump()
        }

        if (mc.thePlayer!!.onGround){
            if(timerValue.get())
                Timer.state = true

            if (modeValue.get().toLowerCase() == "state") {
                when (scaffoldModeValue.get().toLowerCase()) {
                    "scaffold" -> Scaffold.state = false
                    "scaffoldnew" -> ScaffoldNew.state = false
                    "lbscaffold" -> LBScaffold.state = false
                }
            }
        }else {
            if (timerValue.get())
                Timer.state = false
            if (modeValue.get().toLowerCase() == "state") {
                when (scaffoldModeValue.get().toLowerCase()) {
                    "scaffold" -> Scaffold.state = true
                    "scaffoldnew" -> ScaffoldNew.state = true
                    "lbscaffold" -> LBScaffold.state = true
                }
            }
        }
    }
}
