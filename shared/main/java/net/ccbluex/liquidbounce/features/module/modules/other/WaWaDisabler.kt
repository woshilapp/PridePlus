package net.ccbluex.liquidbounce.features.module.modules.other

import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo


/**
 * Skid or Made By WaWa
 * @date 2023/6/30 21:13
 * @author WaWa
 */
@ModuleInfo(name = "WaWaDisabler", description = "wcesu", category = ModuleCategory.MISC)
class WaWaDisabler: Module() {
    @EventTarget
    fun onAttack(e: AttackEvent){

    }

}