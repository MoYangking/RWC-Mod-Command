package command

import com.moyang.rustedwarfare.js.api.event.*
import com.moyang.rustedwarfare.js.api.event.network.NewPacketReceivedEvent 
import com.moyang.rustedwarfare.js.api.event.game.GameSessionEndEvent
import com.moyang.rustedwarfare.js.api.event.game.NewCommandEvent
import com.moyang.rustedwarfare.js.api.event.player.PlayerChatEvent
import com.moyang.rustedwarfare.js.api.event.player.PlayerJoinEvent
import com.moyang.rustedwarfare.js.mod.Mod
import com.moyang.rustedwarfare.js.mod.Plugin
import command.core.CommandManager
import command.core.CommandParser
import command.events.GameSessionEndEventHandler
import command.events.NewCommandEventHandler
import command.events.PlayerChatEventHandler
import command.events.PlayerJoinEventHandler
import command.init.CommandInitializer
import command.utils.CommandUtils
import command.Config
import android.widget.Toast
import com.moyang.rustedwarfare.js.RWApplication
import command.ui.hook.UiHook

class Main : Mod() {
    private lateinit var commandManager: CommandManager
    private lateinit var commandParser: CommandParser
    private lateinit var commandInitializer: CommandInitializer

    private lateinit var playerChatEventHandler: PlayerChatEventHandler
    private lateinit var playerJoinEventHandler: PlayerJoinEventHandler
    private lateinit var gameSessionEndEventHandler: GameSessionEndEventHandler
    private lateinit var newCommandEventHandler: NewCommandEventHandler

    companion object {
        private lateinit var instance: Main
        
        fun getInstance(): Main {
            return instance
        }
    }

    override fun init(plugin: Plugin) {
        Config.summonUnit = plugin.config.getBoolean("summonunit")
    }

    override fun loadContent() {
        instance = this
        initializeComponents()
        registerEventHandlers()
        UiHook(commandManager).init()
    }

    private fun initializeComponents() {
        commandManager = CommandManager()
        commandParser = CommandParser(commandManager)
        commandInitializer = CommandInitializer(commandManager, commandParser)
        commandInitializer.initializeCommands()
    }

    private fun registerEventHandlers() {
        playerChatEventHandler = PlayerChatEventHandler(commandParser)
        playerJoinEventHandler = PlayerJoinEventHandler()
        gameSessionEndEventHandler = GameSessionEndEventHandler()
        newCommandEventHandler = NewCommandEventHandler()

        Events.on(PlayerChatEvent::class.java) { event ->
            playerChatEventHandler.handle(event)
        }

        Events.on(PlayerJoinEvent::class.java) { event ->
            playerJoinEventHandler.handle(event)
        }

        Events.on(GameSessionEndEvent::class.java) { event ->
            gameSessionEndEventHandler.handle(event)
        }

        Events.on(NewCommandEvent::class.java) { event ->
            newCommandEventHandler.handle(event)
        }
    }

    fun getCommandManager(): CommandManager {
        return commandManager
    }
}
