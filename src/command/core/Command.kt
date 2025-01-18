package command.core

import com.moyang.rustedwarfare.js.api.game.Player
import command.permission.PermissionLevel
import command.utils.NetTools
import command.utils.CommandUtils
import command.permission.PermissionManager

// 指令接口
interface Command {
    fun execute(args: List<String>, context: CommandContext)
    fun getPermissionLevel(): PermissionLevel
    fun getDescription(): String
}

// 指令上下文
data class CommandContext(
    val message: String,
    val player: Player
) {
    fun reply(message: String) {
        NetTools.sendSystemMessageToPlayer(player, message)
    }
    
    fun broadcast(message: String) {
        NetTools.sendSystemMessageToAllPlayer(message)
    }
    
    fun hasPermission(level: PermissionLevel): Boolean {
        return PermissionManager.hasPermission(player.name!!, level)
    }
}

// 抽象指令类
abstract class AbstractCommand : Command {
    override fun getPermissionLevel(): PermissionLevel {
        return PermissionLevel.ADMIN
    }

    override fun getDescription(): String {
        return "无描述" // 默认描述
    }
}