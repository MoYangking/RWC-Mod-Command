package command.events

import com.moyang.rustedwarfare.js.api.event.game.NewCommandEvent
import command.permission.PermissionManager
import command.utils.CommandUtils
import com.moyang.rustedwarfare.js.api.game.PingType

class NewCommandEventHandler {
    fun handle(event: NewCommandEvent) {
        PermissionManager.checkAndRemoveCommand(event.command)

        if (event.command.pingType == PingType.normal) {
            //召唤单位
            val playerName = event.command.targetPlayer?.name ?: return
            val unitName = CommandUtils.summonUnitMap[playerName]
            if (unitName != null) {
                CommandUtils.summonUnitMap.remove(playerName)
                val targetPoint = event.command.targetPoint
                if (targetPoint != null) {
                    CommandUtils.summonUnit(unitName, targetPoint.x, targetPoint.y, -1)
                }
            }
        }
        
    }
} 