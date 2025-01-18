package command.events

import com.moyang.rustedwarfare.js.api.event.player.PlayerJoinEvent
import command.permission.PermissionManager
import com.moyang.rustedwarfare.js.api.game.Player
import command.commands.SetAutoTeamCommand

class PlayerJoinEventHandler {

    private val namePrefixRegex = Regex("^\\(\\d+号\\)\\s*")

    fun handle(event: PlayerJoinEvent) {
        val uniqueName = generateUniqueName(event.player.name!!)
        event.player.name = "(${event.player.playerIndex}号) $uniqueName"

        // 为其他玩家更新名字格式，只处理还没有index前缀的玩家
        for (player in Player.getPlayers()) {
            if (player == event.player) continue
            if (!player.name?.matches(namePrefixRegex)!!) {
                val cleanName = player.name?.replace(namePrefixRegex, "")
                player.name = "(${player.playerIndex}号) $cleanName"
                player.originalObject.D = player.playerIndex?.rem(10) ?: 0
            }
        }

        // 自动分配队伍
        event.player.team = SetAutoTeamCommand.getNextTeam()

        if (PermissionManager.isGlobalMute) {
            PermissionManager.mutePlayer(event.player.name!!)
        }
    }

    private fun generateUniqueName(baseName: String): String {
        val cleanBaseName = baseName.replace(namePrefixRegex, "")
        var newName = cleanBaseName
        var suffix = 1

        val existingNames = Player.getPlayers().map { it.name?.replace(namePrefixRegex, "") }.toSet()

        while (newName in existingNames) {
            newName = "$cleanBaseName$suffix"
            suffix++
        }

        return newName
    }
}