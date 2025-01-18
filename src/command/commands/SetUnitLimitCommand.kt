package command.commands

import command.core.AbstractCommand
import command.core.CommandContext
import command.utils.NetTools
import com.moyang.rustedwarfare.js.api.GameEngine

class SetUnitLimitCommand : AbstractCommand() {
    override fun execute(args: List<String>, context: CommandContext) {
        
        if (args.isEmpty()) {
            context.reply("错误: 需要指定单位上限数量")
            return
        }

        val unitLimit = try {
            args[0].toInt()
        } catch (e: NumberFormatException) {
            context.reply("错误: 参数必须是数字")
            return
        }

        if (unitLimit <= 0) {
            context.reply("错误: 单位上限必须大于0")
            return
        }

        val gameEngine = GameEngine.getInstance()
        gameEngine.netWorkEngine?.originalObject?.ay = unitLimit
        gameEngine.netWorkEngine?.originalObject?.az = unitLimit

        context.broadcast("单位上限已设置为 $unitLimit")
    }

    override fun getDescription(): String {
        return "设置单位数量限制，参数: 数量, 示例: .setunitlimit 9999 设置单位数量限制为9999"
    }
}