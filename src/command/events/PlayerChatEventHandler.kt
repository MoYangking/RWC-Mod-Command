package command.events

import com.moyang.rustedwarfare.js.api.event.player.PlayerChatEvent
import com.moyang.rustedwarfare.js.api.game.Player
import command.core.CommandContext
import command.core.CommandParser
import command.permission.PermissionManager
import command.utils.NetTools
import command.utils.CommandUtils

class PlayerChatEventHandler(private val commandParser: CommandParser) {
    private val namePrefixRegex = Regex("^\\(\\d+号\\)\\s*")

    fun handle(event: PlayerChatEvent) {
        val context = CommandContext(event.message, event.player)
        
        commandParser.parse(context, event.message)
        if (event.message.contains("move")) {
            for (player in Player.getPlayers()) {
                if (!player.name?.matches(namePrefixRegex)!!) {
                    val cleanName = player.name?.replace(namePrefixRegex, "")
                    player.name = "(${player.playerIndex}号) $cleanName"
                }
            }
        }

        
        if (PermissionManager.isPlayerMuted(event.player.name!!)) {
            if (!event.message.startsWith(".")) {
                context.reply("你已被禁言，无法发送消息")
            }
            event.execute = false
        }

        
    }
} 