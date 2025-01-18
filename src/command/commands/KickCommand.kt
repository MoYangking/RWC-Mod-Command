package command.commands

import command.core.AbstractCommand
import command.core.CommandContext
import command.utils.NetTools
import com.moyang.rustedwarfare.js.api.game.Player
import com.moyang.rustedwarfare.js.api.GameEngine

class KickCommand : AbstractCommand() {

    override fun execute(args: List<String>, context: CommandContext) {
        val player = try {
            val index = args[0].toInt()
            Player.getPlayer(index)
        } catch (e: NumberFormatException) {
            NetTools.getPlayerByName(args[0])
        }

        if (player?.name == GameEngine.getInstance().netWorkEngine!!.self?.name) {
            context.reply("错误，不能踢出房主")
            return
        }

        if (player?.name == context.player.name) {
            context.reply("错误，不能踢出自己")
            return
        }

        val reason = if (args.size > 1) args.subList(1, args.size).joinToString(" ") else "你被踢出了"
        player?.let { NetTools.kickPlayer(it, reason) }
        context.broadcast("${player?.name} 被踢出了，理由: $reason")
    }

    override fun getDescription(): String {
        return "踢出玩家，参数: 玩家名称或玩家索引，示例: .k 1 辱骂 踢出玩家1，理由: 辱骂"
    }
}