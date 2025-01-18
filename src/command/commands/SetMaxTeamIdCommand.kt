package command.commands

import command.core.AbstractCommand
import command.core.CommandContext
import command.utils.NetTools
import com.moyang.rustedwarfare.js.api.game.Player

class SetMaxTeamIdCommand : AbstractCommand() {
    override fun execute(args: List<String>, context: CommandContext) {
        try {
            val maxTeamId = args[0].toInt()
            if (maxTeamId <= 0) {
                context.reply("错误: 最大队伍ID不能小于等于0")
                return
            }
            Player.setMaxTeamId(maxTeamId)
            context.broadcast("最大队伍ID已设置为 $maxTeamId")
        } catch (e: NumberFormatException) {
            context.reply("错误: 无法将队伍ID转换为数字")
        } catch (e: Exception) {
            context.reply("错误: ${e.message}")
        }
    }

    override fun getDescription(): String {
        return "设置最大队伍ID，参数: 最大队伍ID，示例: .smti 100 设置房间人数为100"
    }
}