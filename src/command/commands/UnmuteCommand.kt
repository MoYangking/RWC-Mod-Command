package command.commands

import command.core.AbstractCommand
import command.core.CommandContext
import command.permission.PermissionManager
import command.utils.NetTools
import com.moyang.rustedwarfare.js.api.game.Player

class UnmuteCommand : AbstractCommand() {
    override fun execute(args: List<String>, context: CommandContext) {

        if (args.isEmpty()) {
            context.reply("错误: 需要指定玩家名称或索引")
            return
        }

        val targetPlayer = try {
            val index = args[0].toInt()
            Player.getPlayer(index)
        } catch (e: NumberFormatException) {
            NetTools.getPlayerByName(args[0])
        }

        targetPlayer?.let {
            if (it.name != null) {
                PermissionManager.unmutePlayer(targetPlayer.name!!)
                context.broadcast("${targetPlayer.name} 已被解除禁言")
            } else {
                context.reply("错误: 找不到指定的玩家")
            }
        }
        targetPlayer?.let { context.broadcast("${it.name!!} 已被解除禁言") }
    }
       
    

    override fun getDescription(): String {
        return "解除禁言，参数: 玩家名称或玩家索引，示例: .um 1 解除1号玩家禁言"
    }
} 