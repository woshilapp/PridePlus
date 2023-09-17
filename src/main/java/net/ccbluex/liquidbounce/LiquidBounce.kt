/*                        _ooOoo_
                         o8888888o
                         88" . "88
                         (| ^_^ |)
                         O\  =  /O
                      ____/`---'\____
                    .'  \\|     |//  `.
                   /  \\|||  :  |||//  \
                  /  _||||| -:- |||||-  \
                  |   | \\\  -  /// |   |
                  | \_|  ''\---/''  |   |
                  \  .-\__  `-`  ___/-. /
                ___`. .'  /--.--\  `. . ___
              ."" '<  `.___\_<|>_/___.'  >'"".
            | | :  `- \`.;`\ _ /`;.`/ - ` : | |
            \  \ `-.   \_ __\ /__ _/   .-` /  /
      ========`-.____`-.___\_____/___.-`____.-'========
                           `=---='
      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
           佛祖保佑       永无Exception     永不修改           */
package net.ccbluex.liquidbounce

import net.ccbluex.liquidbounce.api.Wrapper
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation
import net.ccbluex.liquidbounce.cape.CapeAPI.registerCapeService
import net.ccbluex.liquidbounce.event.ClientShutdownEvent
import net.ccbluex.liquidbounce.event.EventManager
import net.ccbluex.liquidbounce.features.command.CommandManager
import net.ccbluex.liquidbounce.features.module.ModuleManager
import net.ccbluex.liquidbounce.features.special.AntiForge
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof
import net.ccbluex.liquidbounce.features.special.DonatorCape
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.injection.backend.Backend
import net.ccbluex.liquidbounce.script.ScriptManager
import net.ccbluex.liquidbounce.script.remapper.Remapper.loadSrg
import net.ccbluex.liquidbounce.tabs.BlocksTab
import net.ccbluex.liquidbounce.tabs.ExploitsTab
import net.ccbluex.liquidbounce.tabs.HeadsTab
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui
import net.ccbluex.liquidbounce.ui.client.hud.HUD
import net.ccbluex.liquidbounce.ui.client.hud.HUD.Companion.createDefault
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ClassUtils.hasForge
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.InventoryUtils
import net.ccbluex.liquidbounce.utils.RotationUtils
import op.wawa.manager.CombatManager
import op.wawa.sound.Sound
import op.wawa.utils.QQUtils
import op.wawa.utils.sound.TipSoundManager
import java.awt.SystemTray
import java.awt.TrayIcon
import java.util.*
import javax.imageio.ImageIO

object LiquidBounce {

    // Client information
    var CLIENT_NAME = "PridePlus"
    const val CLIENT_VERSION = "NextGen1.0"
    const val CLIENT_CREATOR = "WaWa"
    const val MINECRAFT_VERSION = Backend.MINECRAFT_VERSION
    const val CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce"
    @JvmField
    val CLIENT_TITLE = listOf(
        "你玩原神吗？",
        "RainyFall233",
        "Reborn",
        "不行我得拷打你",
        "LangYa233",
        "Bi an Fl0w0w",
        "Paim0n233",
        "imCzf",
        "崩坏：星穹铁道",
        "Honkai: Star Rail"
    ).random()

    var isStarting = false

    // Managers
    lateinit var moduleManager: ModuleManager
    lateinit var commandManager: CommandManager
    lateinit var eventManager: EventManager
    lateinit var fileManager: FileManager
    lateinit var scriptManager: ScriptManager
    lateinit var combatManager: CombatManager
    lateinit var tipSoundManager: TipSoundManager
    lateinit var userQQ: String

    // HUD & ClickGUI
    lateinit var hud: HUD

    lateinit var clickGui: ClickGui

    // Menu Background
    var background: IResourceLocation? = null

    lateinit var wrapper: Wrapper

    fun displayTray(title: String, text: String, type: TrayIcon.MessageType?) {
        val tray = SystemTray.getSystemTray()
        val trayIcon = TrayIcon(ImageIO.read(Objects.requireNonNull(javaClass.getResourceAsStream("/assets/minecraft/pride/icon128.png"))))
        trayIcon.isImageAutoSize = true
        trayIcon.toolTip = "PridePlus NextGen"
        tray.add(trayIcon)
        trayIcon.displayMessage(title, text, type)
    }

    /**
     * Execute if client will be started
     */
    fun startClient() {
        isStarting = true

        ClientUtils.getLogger().info("Starting $CLIENT_NAME $CLIENT_VERSION, by $CLIENT_CREATOR")

        // Thread, to make loading faster
        Thread {
            userQQ = QQUtils.getLoginQQNumber()
            // info
            ClientUtils.getLogger().info("PridePlus >> QQNumber has been read.")
        }.start()

        // Create file manager
        fileManager = FileManager()

        // Crate event manager
        eventManager = EventManager()

        // Create Combat Manager
        combatManager = CombatManager()

        // Create SoundManager
        tipSoundManager = TipSoundManager()

        // Thread 2, to make loading faster
        Thread {
            // Register listeners
            eventManager.registerListener(combatManager)
            eventManager.registerListener(RotationUtils())
            eventManager.registerListener(AntiForge())
            eventManager.registerListener(BungeeCordSpoof())
            eventManager.registerListener(DonatorCape())
            eventManager.registerListener(InventoryUtils())
        }.start()

        // Create command manager
        commandManager = CommandManager()

        // Load client fonts
        Fonts.loadFonts()
        FontLoaders.initFonts()
        // info
        ClientUtils.getLogger().info("PridePlus >> Fonts Loaded.")

        // Setup module manager and register modules
        moduleManager = ModuleManager()
        moduleManager.registerModules()
        // info
        ClientUtils.getLogger().info("PridePlus >> Modules Loaded.")

        // Thread 3, to make loading faster
        Thread {
            try {
                // Remap
                loadSrg()

                // ScriptManager
                scriptManager = ScriptManager()
                scriptManager.loadScripts()
                scriptManager.enableScripts()

                // info
                ClientUtils.getLogger().info("PridePlus >> Scripts Loaded.")
            } catch (throwable: Throwable) {
                ClientUtils.getLogger().error("Failed to load scripts.", throwable)
            }
        }.start()

        // Register commands
        commandManager.registerCommands()
        // info
        ClientUtils.getLogger().info("PridePlus >> Commands Loaded.")

        // Load configs
        fileManager.loadConfigs(fileManager.modulesConfig, fileManager.valuesConfig, fileManager.accountsConfig,
                fileManager.friendsConfig, fileManager.xrayConfig, fileManager.shortcutsConfig)
        // info
        ClientUtils.getLogger().info("PridePlus >> Configs Loaded.")

        // Thread 4, to make loading faster
        Thread {
            // ClickGUI
            clickGui = ClickGui()
            fileManager.loadConfig(fileManager.clickGuiConfig)

            // Tabs (Only for Forge!)
            if (hasForge()) {
                BlocksTab()
                ExploitsTab()
                HeadsTab()
            }

            // Register capes service
            try {
                registerCapeService()
            } catch (throwable: Throwable) {
                ClientUtils.getLogger().error("Failed to register cape service", throwable)
            }

            // Set HUD
            hud = createDefault()
            fileManager.loadConfig(fileManager.hudConfig)

            // Load generators
            GuiAltManager.loadGenerators()

            // info
            ClientUtils.getLogger().info("PridePlus >> All Loaded.")

            // System Information
            displayTray("PridePlus 已加载完成", "使用即同意用户协议及隐私政策 \n链接: kdocs.cn/l/cmwaN2cwjvAl", TrayIcon.MessageType.INFO)
            ClientUtils.getLogger().info("PridePlus Client >> 使用本ForgeMod即代表你同意我们的用户协议及隐私政策.")

        }.start()

        // Set is starting status
        isStarting = false
        //Sound
        Sound.INSTANCE.Spec()

    }

    /**
     * Execute if client will be stopped
     */
    fun stopClient() {
        // Call client shutdown
        eventManager.callEvent(ClientShutdownEvent())

        // Save all available configs
        fileManager.saveAllConfigs()
    }

}