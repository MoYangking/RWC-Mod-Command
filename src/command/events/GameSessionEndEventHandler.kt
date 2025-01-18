package command.events

import com.moyang.rustedwarfare.js.api.event.game.GameSessionEndEvent
import command.permission.PermissionManager

class GameSessionEndEventHandler {
    fun handle(event: GameSessionEndEvent) {
        PermissionManager.unmuteAllPlayers()
        PermissionManager.isGlobalMute = false
    }
} 