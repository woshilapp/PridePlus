 package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils.Recorder
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.server.SPacketChat
import java.io.File

 @ModuleInfo(name = "AutoL", description = "AutoL", category = ModuleCategory.COMBAT)
class AutoLFix : Module() {
    private val enablement = BoolValue ("EnableAutoL", true)
    private val delayValue = IntegerValue("ChatDelay",3000,2400,6000)
    private val mode = ListValue("Mode", arrayOf("WithWords","RawWord","Clear","Custom","HeyGuy"),"Clear")
    private val waterMark = BoolValue ("WaterMark", true)
    private val enableHYTAtall = BoolValue ("Prefix@", true)
    private val textValue = TextValue("Text", "ExampleChat")
    private val chatTotalKill = BoolValue("ChatTotalKill",false)
    private val suffixTextBeforeRecord = TextValue("TextBeforeKillRecord","我已经击杀了")
    private val suffixTextAfterRecord = TextValue("TextAfterKillRecord","人!")
    private val showNotification = BoolValue("ShowNotificationsOnKill",false)

    // Target
    private val lastAttackTimer = MSTimer()
    var target: IEntity? = null
    private var kill = 0
    private var tempkill = 0
    private var text = ""
    private var inCombat = false
    private val attackedEntityList = mutableListOf<IEntity>()
    private val insultFile = File(LiquidBounce.fileManager.dir, "filter.json")
    private var insultWords = mutableListOf<String>()
    private val ms = MSTimer()
    private val delay = MSTimer()

    @EventTarget
    fun onAttack(event: AttackEvent) {
        val target = event.targetEntity

        if ((target is IEntity) && EntityUtils.isSelected(target, true)) {
            this.target = target
            if (!attackedEntityList.contains(target)) {
                attackedEntityList.add(target)
            }
        }
        lastAttackTimer.reset()
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        attackedEntityList.filter { it.isDead }.forEach {
            playerDeathEvent(it.name!!)
            attackedEntityList.remove(it)
        }

        inCombat = false

        if (!lastAttackTimer.hasTimePassed(1000)) {
            inCombat = true
            return
        }

        if (target != null) {
            if (mc.thePlayer!!.getDistanceToEntity(target!!) > 7 || !inCombat || target!!.isDead) {
                target = null
            } else {
                inCombat = true
            }
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is SPacketChat) {
            val chat = packet.chatComponent.unformattedText
            if (chat.contains("起床战争") && chat.contains(">>") && chat.contains("游戏开始")) {
                attackedEntityList.clear()
            }
        }
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        inCombat = false
        target = null
        attackedEntityList.clear()
    }

    private fun showNotifications (number: Int) {
        if (showNotification.get()) {
            LiquidBounce.hud.addNotification(
                Notification(
                    (if (number > 1) "$number Kills!" else "Kill!"),
                    ("You killed $number ${if (number > 1) "players" else "player"}"),
                    NotifyType.INFO
                )
            )
        }
    }
    private fun playerDeathEvent (name: String) {
        kill++
        Recorder.killCounts++
        tempkill++
        if (delay.hasTimePassed(delayValue.get().toLong())) {
            playerChat(name)
            delay.reset()
        }
        if (ms.hasTimePassed(5000)) {
            showNotifications(tempkill)
            tempkill = 0
            ms.reset()
        }
        if (!inCombat && tempkill != 0 && ms.hasTimePassed(5000)) {
            showNotifications(tempkill)
            tempkill = 0
            ms.reset()

        }
    }

    private fun playerChat(name: String) {
        if (enablement.get()) {
            when (mode.get().toLowerCase()) {
                "custom" -> {
                    text = (textValue.get())
                    text = (text.replace("%name%",name))
                    text = ("$text ")
                }
                "clear" -> {
                    text = ("Ｌ $name")
                }
                "rawwords" -> {
                    text = (getRandomOne())
                }
                "withwords" -> {
                    text = ("Ｌ $name | " + getRandomOne())
                }
                "heyguy" -> {
                    var a = (Math.random()*12).toInt()
                    when(a){
                        1 -> text = ("Ｌ $name | 嗨，我是风动，这是我的neibu神器，3000收neibu是我的秘密武器，花钱一分钟，赚钱两个月，不要告诉别人哦")
                        2 -> text = ("Ｌ $name | 嗨，我是Pro，这是我的neibu神器，200整30个conf是我的秘密武器，花钱一分钟，赚钱两年半，不要告诉别人哦")
                        3 -> text = ("Ｌ $name | 嗨，我是瓦瓦，这是我的pride+神器，grimvel是我的秘密武器，vel一分钟，死号两小时，不要告诉瓦瓦哦")
                        4 -> text = ("Ｌ $name | 嗨，我是_RyF,这是我的彩色字节，彩色字节是我的秘密武器，出击一分钟，殴打两小时，不要告诉瓦瓦哦")
                        5 -> text = ("Ｌ $name | 嗨，我是小手冰凉,这是我的CoolSense，CoolSense是我的秘密武器，出击一分钟，殴打两小时，不要告诉小手哦")
                        6 -> text = ("Ｌ $name | 嗨，我是风动，这是我的抽烟神器，3000买下锐刻114514代是我的秘密武器，花钱一秒钟，抽烟一辈子，不要告诉丁真哦")
                        7 -> text = ("Ｌ $name | 嗨，我是狼牙，这是我的狼牙神器，一定是狼牙干的是我的秘密武器，花钱一秒钟，抽烟一辈子，不要告诉xiatian哦")
                        8 -> text = ("Ｌ $name | 嗨，我是原批，这是我的启动神器，你说的对，但是原神启动是我的秘密武器，启动十分钟，充电五小时，不要告诉别人哦")
                        9 -> text = ("Ｌ $name | 嗨，我是SLA，这是我的开户神器，开假户是我的秘密武器，假户十分钟，高兴一个月，不要告诉李瓦琦哦")
                        10 -> text = ("Ｌ $name | 嗨，我是小职，这是我的cookies神器，3000+cookies是我的秘密武器，获取一秒钟，游戏一小时，不要告诉别人哦")
                        11 -> text = ("Ｌ $name | 嗨，我是回想，这是我的fix神器，fix各种端是我的秘密武器，fix一小时，高兴一个月，不要告诉别人哦")
                        12 -> text = ("Ｌ $name | 嗨，我是刘梦(执剑)，这是我的圈钱神器，圈钱造谣自大是我的秘密武器，圈钱一秒钟，高兴一个月，不要告诉别人哦")
                    }
                }
            }
            replaceFilterWords()
            if (chatTotalKill.get()) text = ("$text | " + suffixTextBeforeRecord.get() + kill + suffixTextAfterRecord.get())
            if (waterMark.get()) text = ("[${LiquidBounce.CLIENT_NAME}] $text")
            if (enableHYTAtall.get()) text = ("@a$text")
            mc.thePlayer!!.sendChatMessage(text)
        }
    }

    private fun getRandomOne(): String {
        return insultWords[RandomUtils.nextInt(0, insultWords.size - 1)]
    }

    fun resetAttackedList() {
        attackedEntityList.clear()
    }

    private fun replaceFilterWords() {
        text = (text.replace("%L%","Ｌ"))
        text = (text.replace("%l%","Ｌ"))

    }


    override val tag: String
        get() = "Kills $kill"
}
