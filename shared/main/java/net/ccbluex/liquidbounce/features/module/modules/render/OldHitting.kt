package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.value.ListValue


@ModuleInfo(name = "OldHitting", description = "faq", category = ModuleCategory.RENDER)
public class OldHitting : Module() {
    private val modeValue = ListValue("Mode", arrayOf( "Pride", "Vanilla", "WindMill" ), "Vanilla")
    fun getModeValue(): ListValue? {
        return  modeValue
    }

}