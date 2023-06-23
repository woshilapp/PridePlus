/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.LiquidBounce.CLIENT_NAME
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.ClientUtils.displayChatMessage
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.server.SPacketChat
import java.util.regex.Pattern

/*
    Made By RyF
    这就是HytGetName的Super无敌加强版
    有logStyle创意火速Vivo50
    删注释我操你妈。

    Dev Log:
    <1.03>
    [+] 注释所有value
    [+] 测试自动切换模式
    [+] 支持职业战争自定义延迟
    [~] 删除matcher的非null检测

    <1.02>
    [+] 显示自己连杀消息
    [+] 短Tag设置
    [+] 丰富注释+114514
    [+] isFriend的测试和日志
    [+] displayable写全
    [+] 测试职业战争AntiFP
    [+] 加了不知道什么几把 和职业战争相关

    Shared at QQ群
    QQ群号     // 名字
    126633401 // PridePlus 1
    754320222 // PridePlus 2
    494810833 // 米哈游游戏玩家交流群丨zs.
    717795492 // TG公益 | BUG++++ | 纯公益 | 禁

    Now State: 公开,稳定,测试
	Now Version: 1.03
    Last updated at 2023/06/10
*/

/**
 * Skid By WaWa
 * Date 20230621
 * @author RyF
 */

@ModuleInfo(name = "AntiFakePlayer", description = "AntiBot in HuaYuTing. By RyF", category = ModuleCategory.MISC)
class AntiFakePlayer : Module() {

    private val logStyles = arrayOf(
        "RyFNew",
        "FDPAntibot",
        "FDPChat",
        "Leave",
        "Special",
        "Special2",
        "Shitty",
        "WaWa1",
        "WaWa2",
        "WaWa3",
        "NullClient",
        "Normal",
        "WindX",
        "Old",
        "Old1",
        "Arene"
    )
    private val multiKillMessageList = arrayOf(
        "正在大杀特杀！",
        "主宰服务器！",
        "杀人如麻！",
        "无人能挡！",
        "杀得变态了！",
        "正在像妖怪般杀戮！",
        "如同神一般！",
        "已经超越神了！拜托谁去杀了他吧！"
    )
    private val kitSpecialDeathChats = arrayOf(
        "走着走着突然暴毙了!",
        "Boom！！!",
        "",
        ""
    )
    private val kitSpecialSkillChats = arrayOf(
        "对你眨眼了!",
        "诅咒了!",
        "并没有使用作弊!",
        "",
        ""
    )

    // 主功能
    private val botGetterModeValue = ListValue("Mode", arrayOf("4v4/2v2/1v1", "BWXP32", "BWXP16","KitBattle"), "4V4/2v2/1V1")

    // ?
    private val autoBotGetter = BoolValue("AutoSwitchMode(Test",false)

    // ?
    private val isFriendDebuggerChat = BoolValue("isFriendDebuggerChat",false)

    // 日志模式
    private val printLogger = BoolValue("ShowChatMessage", false)
    private val logStyleValue = ListValue("LogStyle", logStyles, "Normal")

    // 隐藏击杀消息, 支持起床&&职业战争
    private val hideKillChatValue = BoolValue("HideKillChat", false)
    private val showMyKillDeathChatValue = BoolValue("ShowMyKillDeathChat", false)

    // 职业战争自定义延迟
    private val kitCustomDelay = IntegerValue("KitCustomDelay",4700,4000,8000)
    // 职业战争隐藏银币消息
    private val hideKitCoinGetChat = BoolValue("HideKitBattleCoinChat",true)
    // 职业战争隐藏连死消息
    private val hideKitDeathStreakChat = BoolValue("HideKitDeathStreakChat",false)
    // 职业奇死
    private val hideKitSpecialDeathChat = BoolValue("HideKitSpecialDeathChat",false)
    // 职业技能
    private val hideKitSkillChat = BoolValue("HideKitSkillChat",false)
    private val hideKitUpgradeChat = BoolValue("HideKitUpgradeChat",false)

    // 隐藏连杀消息, 支持职业战争
    private val hideMultiKillChat = BoolValue("HideMultiKillChat",true)
    private val showMyMultiKillChat = BoolValue("ShowMyMultiKillChat",true)

    // 用原版 §k 隐藏英文
    private val protectDeadPlayerName = BoolValue("ShittyNameProtect(OnlyEnglish", false)

    // 屏蔽Antigetname消息 不影响无效化
    private val hideValue = BoolValue("IgnoreAntiGetname", false)
    private val showHideChat = BoolValue("IgnoredChat", false)

    // 自动切日志模式
    private val autoSwitchLogger = BoolValue("AutoSwitchLogger",false)
    private val autoSwitchMode = ListValue("AutoSwitchMode",arrayOf(
        "Random",
        "List"
    ),"Random")
    private val autoSwitchDelay = IntegerValue("AutoSwitchDelay",3000,1500,7000)


    // 隐藏所有
    private val sTHideAll = BoolValue("HideAllTag",false)

    // 隐藏模式
    private val sTHideMode = BoolValue("HideMode",false)

    // 隐藏日志模式
    private val sTHideLogStyle = BoolValue("HideLogStyle",false)

    // 隐藏无敌人数量开关
    private val sTHideBotsCounter = BoolValue("HideBotsCounter",false)
    // 设置
    private val sTHideBotsCounterSetting = ListValue("HideBotsCounterMode", arrayOf("FullHide","OnlyNumber"),"OnlyNumber")

    // 隐藏所有自动切日志
    private val sTHideAllAutoSwitcher = BoolValue("HideAllAutoSwitcher",false)

    // 隐藏状态开关
    private val sTHideAutoSwitcherState = BoolValue("HideAutoSwitcherState",false)
    // 隐藏状态设置
    private val sTHideAutoSwitcherStateSetting = ListValue("HideAutoSwitcherMode", arrayOf("FullHide","OnlyShowState"),"FullHide")

    // 隐藏模式
    private val sTHideAutoSwitcherMode = BoolValue("HideAutoSwitcherMode",false)

    // 隐藏延迟
    private val sTHideAutoSwitcherDelay = BoolValue("HideAutoSwitcherDelay",false)

    // 变量
    private var bots = 0
    private var logNumber = 0
    private val ms = MSTimer()
    private var protectedname = ""

    // 关闭模块清除bot
    @EventTarget
    override fun onDisable() {
        bots = 0
        clearAll()
        super.onDisable()
    }

    /*
    主功能
    playerDeathAction 是无敌人
    playerDeathMsgAction 是日志
     */
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        if ((packet is SPacketChat) && !packet.chatComponent.unformattedText.contains(":") && (packet.chatComponent.unformattedText.startsWith(
                "起床战争"
            ) || packet.chatComponent.unformattedText.startsWith("[起床战争") || packet.chatComponent.unformattedText.startsWith("花雨庭"))
        ) {
            val chat = packet.chatComponent.unformattedText
            when (botGetterModeValue.get().toLowerCase()) {
                // 4v4 2v2 1v1 起床
                "4v4/2v2/1v1" -> {
                    val matcher = Pattern.compile("杀死了 (.*?)\\(").matcher(chat)
                    val matcher2 = Pattern.compile("起床战争>> (.*?) (\\(((.*?)死了!))").matcher(chat)
                    if (matcher.find()) {
                        val name = matcher.group(1).trim()
                        playerDeathAction(name, 4988)
                        playerDeathMsgAction(event)
                    }
                    if (matcher2.find()) {
                        val name = matcher2.group(1).trim()
                        playerDeathAction(name, 4988)
                        playerDeathMsgAction(event)
                    }
                }

                // 经验32 起床
                "bwxp32" -> {
                    val matcher = Pattern.compile("杀死了 (.*?)\\(").matcher(chat)
                    val matcher2 = Pattern.compile("起床战争 >> (.*?) (\\(((.*?)死了!))").matcher(chat)
                    if (matcher.find()) {
                        val name = matcher.group(1).trim()
                        playerDeathAction(name, 7400)
                        playerDeathMsgAction(event)
                    }
                    if (matcher2.find()) {
                        val name = matcher2.group(1).trim()
                        playerDeathAction(name, 4988)
                        playerDeathMsgAction(event)
                    }
                }

                // 经验16 起床
                "bwxp16" -> {
                    val matcher = Pattern.compile("击败了 (.*?)!").matcher(chat)
                    val matcher2 = Pattern.compile("玩家 (.*?)死了！").matcher(chat)
                    if (matcher.find()) {
                        val name = matcher.group(1).trim()
                        playerDeathAction(name, 9700)
                        playerDeathMsgAction(event)
                    }
                    if (matcher2.find()) {
                        val name = matcher2.group(1).trim()
                        playerDeathAction(name, 9700)
                        playerDeathMsgAction(event)
                    }
                }

                // 职业战争 不用测试了 有效
                "kitbattle" -> {
                    val matcher = Pattern.compile("击杀了(.*?) !").matcher(chat)
                    val matcher2 = Pattern.compile("花雨庭 >>(.*?) 被").matcher(chat)
                    if (matcher.find()) {
                        val name = matcher.group(1).trim()
                        playerDeathAction(name, kitCustomDelay.get().toLong())
                        playerDeathMsgAction(event)
                    }
                    if (matcher2.find()) {
                        val name = matcher2.group(1).trim()
                        playerDeathAction(name, kitCustomDelay.get().toLong())
                        playerDeathMsgAction(event)
                    }
                }
            }
        }

        // 屏蔽Antigetname
        if (packet is SPacketChat && packet.chatComponent.unformattedText.contains(
                ":") && !packet.chatComponent.unformattedText.contains((
                mc.thePlayer!!.displayNameString + ":")) && (packet.chatComponent.unformattedText.contains(
                "起床战争") && !packet.chatComponent.unformattedText.contains(
                "01:00:00 是这个地图的记录!") && !packet.chatComponent.unformattedText.contains(
                "之队队设置一个新的记录:")
                    )) {
            if (hideValue.get()) {
                event.cancelEvent()
                if (showHideChat.get()) displayChatMessage("§b$CLIENT_NAME §7» §c隐藏了AntiGetname消息。")
            }
        }

        // 隐藏连杀消息
        if (packet is SPacketChat) {
            val chat = packet.chatComponent.unformattedText
            for (it in multiKillMessageList) {
                if ((chat.contains(it) || chat.equals(it)) && !(showMyMultiKillChat.get() && chat.contains(mc.thePlayer!!.displayNameString))) {
                    if (hideMultiKillChat.get()) event.cancelEvent()
                }
            }
        }

        // 职业战争相关
        if (packet is SPacketChat) {
            val chat = packet.chatComponent.unformattedText
            if (botGetterModeValue.get().toLowerCase() == "kitbattle" && chat.startsWith("花雨庭")) {
                // 职业隐藏银币消息
                if (hideKitCoinGetChat.get() && (chat.contains("花雨庭 >>你消灭") && chat.contains("% 的伤害并且获得了") && chat.contains(
                        "硬币!"
                    ) || chat.contains("你的 coins 被修正为"))
                ) {
                    event.cancelEvent()
                }

                // 职业隐藏连杀消息
                if ((chat.contains("花雨庭 >>") && chat.contains("完成了") && chat.contains("连杀!")) && !(showMyMultiKillChat.get() && chat.contains(
                        mc.thePlayer!!.displayNameString
                    ))
                ) {
                    if (hideMultiKillChat.get()) event.cancelEvent()
                }

                // 职业隐藏连死消息
                if ((chat.contains("花雨庭 >>") && (
                            chat.contains("has ended his deathstreak and lost his buff") ||
                            chat.contains("is now receiving a buff for his deathstreak") ||
                            chat.contains("终结了他的连续死亡") ||
                            chat.contains("获得了一个buff因为他刚刚完成了")
                        ))
                ) {
                    if (hideKitDeathStreakChat.get()) event.cancelEvent()
                }

                // 职业隐藏奇奇怪怪死亡消息 (不完全
                for (it in kitSpecialDeathChats) {
                    if ((chat.contains(it) || chat.equals(it))) {
                        if (hideKitSpecialDeathChat.get()) event.cancelEvent()
                    }
                }

                // 职业隐藏技能使用消息 (不完全
                for (it in kitSpecialSkillChats) {
                    if ((chat.contains(it) || chat.equals(it))) {
                        if (hideKitSkillChat.get()) event.cancelEvent()
                    }
                }

                // 职业隐藏升级消息
                if (hideKitUpgradeChat.get() && chat.contains("通过击杀获得胜点的方式晋级为"))
                    event.cancelEvent()
            }
        }

        // 自动切换模式 不清楚是否有效
        if (packet is SPacketChat && autoBotGetter.get()) {
            val ftchat = packet.chatComponent.formattedText
            if (ftchat.contains("§b花雨庭 §7>>")) botGetterModeValue.set("KitBattle")
            if (ftchat.contains("§b起床战争§7>>")) botGetterModeValue.set("4v4/2v2/1v1")
            if (ftchat.contains("§b起床战争 §f>>")) botGetterModeValue.set("BWXP32")
            if (ftchat.contains("§f[起床战争]")) botGetterModeValue.set("BWXP16")
        }
    }

    // 自动切换日志
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (autoSwitchLogger.get() && ms.hasTimePassed(autoSwitchDelay.get().toLong())) {
            when (autoSwitchMode.get().toLowerCase()) {
                "random" -> logStyleValue.set(logStyles[RandomUtils.nextInt(0, logStyles.size - 1)])
                "list" -> {
                    if (logNumber != logStyles.size - 1) {
                        logNumber++
                        logStyleValue.set(logStyles[logNumber])
                    } else {
                        logNumber = 0
                        logStyleValue.set(logStyles[logNumber])
                    }
                }
            }
            ms.reset()
        }
    }

    // 隐藏击杀&死亡消息
    private fun playerDeathMsgAction(event: PacketEvent) {
        val packet = event.packet.unwrap()
        if ((packet is SPacketChat) && hideKillChatValue.get() && !(showMyKillDeathChatValue.get() && packet.chatComponent.unformattedText.contains(
                mc.thePlayer!!.displayNameString
            ))
        ) {
            event.cancelEvent()
        }
    }

    // 加减无敌人
    private fun playerDeathAction(name: String, cd: Long) {
        bots++
        if (!LiquidBounce.fileManager.friendsConfig.isFriend(name)) {
            if (isFriendDebuggerChat.get()) displayChatMessage("§f[§c!§f] §7[§b${CLIENT_NAME}§7] §f判定 §7$name §f是否在好友列表。")
            LiquidBounce.fileManager.friendsConfig.addFriend(name)
        }
        protectedname = if (protectDeadPlayerName.get()) "§7§k$name" else "§7$name"
        if (printLogger.get()) printLogger(protectedname, "add")
        Thread {
            try {
                Thread.sleep(cd)
                protectedname = if (protectDeadPlayerName.get()) "§7§k$name" else "§7$name"
                if (LiquidBounce.fileManager.friendsConfig.isFriend(name)) {
                    if (isFriendDebuggerChat.get()) displayChatMessage("§f[§c!§f] §7[§b${CLIENT_NAME}§7] §f判定 §7$name §f是否在好友列表。")
                    LiquidBounce.fileManager.friendsConfig.removeFriend(name)
                }
                bots--
                if (printLogger.get()) printLogger(protectedname, "remove")
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }
        }.start()
    }

    // 日志
    private fun printLogger(name: String, mode: String) {
        when (mode.toLowerCase()) {
            "add" -> {
                when (logStyleValue.get().toLowerCase()) {
                    "ryfnew" -> displayChatMessage("§b$CLIENT_NAME §7» §aAdded§f HYT Bot §7-> §e$name")
                    "normal" -> displayChatMessage("§7[§6${CLIENT_NAME}§7] §fAdded HYT Bot: §7$name")
                    "old" -> displayChatMessage("§8[§c§l${CLIENT_NAME}提醒您§8] §d添加无敌人：§7$name")
                    "old1" -> displayChatMessage("§8[§c§l${CLIENT_NAME}§8] §d添加无敌人：§7$name")
                    "fdpantibot" -> displayChatMessage("§7[§cAntiBot§7] §fAdded §7$name§f due to it being a bot.")
                    "fdpchat" -> displayChatMessage("§f[§c!§f] §7[§b§l${CLIENT_NAME}§7] §aAdded §6HYT bot§f[$name§f]§6.")
                    "special" -> displayChatMessage("§8[§d${CLIENT_NAME}§8] §a$name§d被§bRyF§d吃掉啦! §bCiallo(∠・ω< )⌒☆")
                    "special2" -> displayChatMessage("§8[§b${CLIENT_NAME}§8] §a$name§b被RyF吃掉啦! Ciallo(∠・ω< )⌒☆")
                    "nullclient" -> displayChatMessage("§7[§cAntiBots§7] §fAdded a bot(§7$name§f)")
                    "windx" -> displayChatMessage("§7[§c!§7] §bColorByte §aClient §7=> §aAdded §fa bot(§7$name§f)")
                    "shitty" -> displayChatMessage("§7[§a${CLIENT_NAME}§7] §7$name§f被§bRyF§f吃掉啦! §aawa~")
                    "wawa1" -> displayChatMessage("§6${CLIENT_NAME} §7=> §fAdded Bot §7$name§f.")
                    "wawa2" -> displayChatMessage("§6${CLIENT_NAME} §7» §f玩家死亡: §7$name")
                    "wawa3" -> displayChatMessage("§6${CLIENT_NAME} §7=» §f玩家 §7$name §f狂暴死去")
					"arene" -> displayChatMessage("§7[§f${CLIENT_NAME}§7] §fAdd a Bot(§7$name§f)")
                    "leave" -> displayChatMessage("§b${CLIENT_NAME} §8[§eWARNING§8] §6添加无敌人: $name")
                }
            }
            "remove" -> {
                when (logStyleValue.get().toLowerCase()) {
                    "ryfnew" -> displayChatMessage("§b$CLIENT_NAME §7» §cRemoved§f HYT Bot §7-> §e$name")
                    "normal" -> displayChatMessage("§7[§6${CLIENT_NAME}§7] §fRemoved HYT Bot: §7$name")
                    "old" -> displayChatMessage("§8[§c§l${CLIENT_NAME}提醒您§8] §d删除无敌人：§7$name")
                    "old1" -> displayChatMessage("§8[§c§l${CLIENT_NAME}§8] §d删除无敌人：§7$name")
                    "fdpantibot" -> displayChatMessage("§7[§cAntiBot§7] §fRemoved §7$name§f due to respawn.")
                    "fdpchat" -> displayChatMessage("§f[§c!§f] §7[§b§l${CLIENT_NAME}§7] §cRemoved §6HYT bot§f[$name§f]§6.")
                    "special" -> displayChatMessage("§8[§d${CLIENT_NAME}§8] §a$name§d被§bRyF§d吐出来咯~ §bCiallo(∠・ω< )⌒☆")
                    "special2" -> displayChatMessage("§8[§b${CLIENT_NAME}§8] §a$name§b被RyF吐出来咯~ Ciallo(∠・ω< )⌒☆")
                    "nullclient" -> displayChatMessage("§7[§cAntiBots§7] §fRemoved a bot(§7$name§f)")
                    "shitty" -> displayChatMessage("§7[§b${CLIENT_NAME}§7] §7$name§f被§bRyF§f吐出来咯~ §dqwq")
                    "windx" -> displayChatMessage("§7[§c!§7] §bColorByte §aClient §7=> §cRemoved §fa bot(§7$name§f)")
                    "wawa1" -> displayChatMessage("§6${CLIENT_NAME} §7=> §fRemoved Bot §7$name§f.")
                    "wawa2" -> displayChatMessage("§6${CLIENT_NAME} §7» §f玩家重生: §7$name")
                    "wawa3" -> displayChatMessage("§6${CLIENT_NAME} §7=» §f玩家 §7$name §f炸裂归来")
                    "arene" -> displayChatMessage("§7[§f${CLIENT_NAME}§7] §fDel a Bot(§7$name§f)")
                    "leave" -> displayChatMessage("§b${CLIENT_NAME} §8[§eWARNING§8] §6删除无敌人: $name")
                }
            }
        }
    }

    // sb
    private fun clearAll() {
        LiquidBounce.fileManager.friendsConfig.clearFriends()
    }

    // tag获取
    private fun tagReturner(): String {
        var tag = ""
        if (sTHideAll.get()) {
            tag = ""
        } else {
            if (!sTHideMode.get()) tag = botGetterModeValue.get()
            if (!sTHideLogStyle.get() && printLogger.get()) tag = if (tag != "") "$tag, ${logStyleValue.get()}" else logStyleValue.get()

            if (!sTHideBotsCounter.get()) {
                if (tag != "") tag = "$tag, Bots: $bots" else "Bots: $bots"
            } else {
                if (sTHideBotsCounterSetting.get().toLowerCase() == "onlynumber") if (tag != "") tag =
                    "$tag, $bots" else bots
            }

            if (autoSwitchLogger.get() && !sTHideAllAutoSwitcher.get()) {
                if (tag != "" && !sTHideAutoSwitcherState.get()) tag = "$tag, AutoSwitch:${if (autoSwitchLogger.get()) "On" else "Off"}"
                if (sTHideAutoSwitcherState.get() && sTHideAutoSwitcherStateSetting.get()
                        .toLowerCase() == "onlyshowstate"
                ) {
                    tag =
                        if (tag != "") "$tag, ${if (autoSwitchLogger.get()) "On" else "Off"}" else if (autoSwitchLogger.get()) "On" else "Off"
                }

                if (!sTHideAutoSwitcherMode.get()) tag =
                    if (tag != "") "$tag, ${autoSwitchMode.get()}" else autoSwitchMode.get()

                if (!sTHideAutoSwitcherDelay.get()) tag =
                    if (tag != "") "$tag, ${autoSwitchDelay.get()}" else "${autoSwitchDelay.get()}"
            }
        }
        return tag
    }

    override val tag: String?
        get() = if (!sTHideAll.get()) tagReturner() else null
}