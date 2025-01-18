package command.commands

import com.moyang.rustedwarfare.js.api.GameEngine
import command.core.AbstractCommand
import command.core.CommandContext
import command.utils.NetTools

class IncomeCommand : AbstractCommand() {
    override fun execute(
        args: List<String>,
        context: CommandContext
    ) {
        
        try {
            if (args.isNotEmpty()) {
                val incomeValue = args[0].toFloat() 
                GameEngine.getInstance().netWorkEngine?.rules?.income = incomeValue
                context.broadcast("资源收入已设置为 $incomeValue")
            } else {
                context.reply("错误: 参数不能为空")
            }
        } catch (e: NumberFormatException) {
            context.reply("错误: 无法将收入转换为数字")
        } catch (e: Exception) {
            context.reply("错误: ${e.message}")
        }

    }

    override fun getDescription(): String {
        return "设置资源收入，参数: 收入值，示例: .si 1000 设置资源收入为1000"
    }
}
