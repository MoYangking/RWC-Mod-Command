package command.commands

import command.core.AbstractCommand
import command.core.CommandContext
import command.utils.NetTools
import com.moyang.rustedwarfare.js.api.game.Player
import com.moyang.rustedwarfare.js.api.GameEngine
import command.permission.PermissionManager

class MuteCommand : AbstractCommand() {
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

        if (targetPlayer?.name == GameEngine.getInstance().netWorkEngine!!.self?.name) {
            context.reply("错误，不能禁言房主")
            return
        }


        targetPlayer?.let {
            if (it.name == context.player.name) {
                context.reply("错误: 不能禁言自己")
                return
            }
        }

        targetPlayer?.let {
            if (it.name != null) {
                PermissionManager.mutePlayer(targetPlayer.name!!) // 禁言玩家
                context.broadcast("${targetPlayer.name} 已被禁言")
            } else {
                context.reply("错误: 找不到指定的玩家")
            }
        }
    }

    override fun getDescription(): String {
        return "禁言玩家，参数: 玩家名称或玩家索引，示例: .m 1 禁言1号玩家"
    }
}