/*
 * PridePlus Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MolokyMC/PridePlus/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.features.value.BoolValue;
import net.minecraft.init.MobEffects;

@ModuleInfo(name = "Sprint", description = "Automatically sprints all the time.", category = ModuleCategory.MOVEMENT)
public class Sprint extends Module {

    public final BoolValue allDirectionsValue = new BoolValue("AllDirections", false);
    public final BoolValue blindnessValue = new BoolValue("Blindness", true);
    public final BoolValue foodValue = new BoolValue("Food", true);

    public final BoolValue checkServerSide = new BoolValue("CheckServerSide", false);
    public final BoolValue checkServerSideGround = new BoolValue("CheckServerSideOnlyGround", false);

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!MovementUtils.isMoving() || mc.player.isSneaking() ||
                (blindnessValue.get() && mc.player.isPotionActive(MobEffects.BLINDNESS)) ||
                (foodValue.get() && !(mc.player.getFoodStats().getFoodLevel() > 6.0F || mc.player.capabilities.allowFlying))
                || (checkServerSide.get() && (mc.player.onGround || !checkServerSideGround.get())
                && !allDirectionsValue.get() && RotationUtils.targetRotation != null &&
                RotationUtils.getRotationDifference(new Rotation(mc.player.rotationYaw, mc.player.rotationPitch)) > 30)) {
            mc.player.setSprinting(false);
            return;
        }

        if (allDirectionsValue.get() || mc.player.movementInput.moveForward >= 0.8F)
            mc.player.setSprinting(true);
    }
}
