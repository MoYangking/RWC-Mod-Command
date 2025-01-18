package command.commands

import com.corrodinggames.rts.appFramework.MultiplayerBattleroomActivity
import com.moyang.rustedwarfare.js.api.GameEngine
import com.moyang.rustedwarfare.js.api.command.GameCommand
import command.core.AbstractCommand
import command.core.CommandContext
import command.core.CommandManager
import command.permission.PermissionLevel
import command.permission.PermissionManager
import command.utils.NetTools
import command.alias.CommandAliasManager
class HelpCommand(private val commandManager: CommandManager) : AbstractCommand() {
    override fun execute(args: List<String>, context: CommandContext) {
        val stringBuilder = StringBuilder("可用指令列表:\n")
    
        commandManager.getCommands().forEach { (name, command) ->
            if (PermissionManager.hasPermission(context.player.name!!, command.getPermissionLevel())) {
                val description = command.getDescription()
                
                val aliases = CommandAliasManager.getAliases(name).joinToString(", ")
                stringBuilder.append(".$name - $description")
                if (aliases.isNotEmpty()) {
                    stringBuilder.append(" (别名: $aliases)")
                }
                stringBuilder.append("\n")
            }
        }
        
        context.reply(stringBuilder.toString())
    }

    override fun getDescription(): String {
        return "显示可用指令列表，示例: .h 查看指令列表"
    }

    override fun getPermissionLevel(): PermissionLevel {
        return PermissionLevel.PLAYER
    }
}