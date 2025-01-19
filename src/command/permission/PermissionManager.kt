package command.permission

import com.moyang.rustedwarfare.js.api.game.Player
import com.moyang.rustedwarfare.js.api.GameEngine
import com.moyang.rustedwarfare.js.api.command.GameCommand

object PermissionManager {
    private val playerPermissions = mutableMapOf<String, PermissionLevel>()
    private val mutedPlayers = mutableSetOf<String>()
    val bannedUnits = mutableSetOf<String>()
    var isGlobalMute = false // 全体禁言状态

    fun setPermission(playerName: String, level: PermissionLevel) {
        playerPermissions[playerName] = level
    }

    fun getPermission(playerName: String): PermissionLevel {
        if (playerName == GameEngine.getInstance().netWorkEngine!!.self?.name) {
            return PermissionLevel.SUPER_ADMIN
        }
        return playerPermissions[playerName] ?: PermissionLevel.PLAYER
    }

    fun hasPermission(playerName: String, requiredLevel: PermissionLevel): Boolean {
        val level = getPermission(playerName)
        return level.ordinal >= requiredLevel.ordinal
    }


    fun mutePlayer(playerName: String) {
        mutedPlayers.add(playerName)
    }

    fun unmutePlayer(playerName: String) {
        mutedPlayers.remove(playerName)
    }

    fun banUnit(unitName: String) {
        bannedUnits.add(unitName)
    }

    fun unbanUnit(unitName: String) {
        bannedUnits.remove(unitName)
    }

    fun muteAllPlayers() {
        Player.getPlayers().forEach {
            if (it.name == GameEngine.getInstance().netWorkEngine!!.self?.name){
                //房主不加入禁言
                return
            }
            mutePlayer(it.name!!)
        }
        isGlobalMute = true
    }

    fun unmuteAllPlayers() {
        isGlobalMute = false
        mutedPlayers.clear()
    }

    fun isPlayerMuted(playerName: String): Boolean {
        return mutedPlayers.contains(playerName)
    }

    fun getMutedPlayers(): Set<String> {
        return mutedPlayers.toSet()
    }

    fun checkAndRemoveCommand(command: GameCommand) {
        if (bannedUnits.contains(command.actionId?.replace("u_", ""))) {
            command.removeCommand()
        }
        for (unit in command.getAllControlUnits()) {
            if (bannedUnits.contains(unit.name)) {
                command.removeControlUnits(unit)
            }
        }
        if (command.targetPosition?.target != null) {
            if (bannedUnits.contains(command.targetPosition?.target?.name)) {
                command.removeCommand()
            }
        }
    }
}