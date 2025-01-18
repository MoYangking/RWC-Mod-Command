package command.commands

import com.moyang.rustedwarfare.js.api.GameEngine
import com.moyang.rustedwarfare.js.api.command.GameCommand
import command.core.AbstractCommand
import command.core.CommandContext
import command.core.CommandManager
import command.permission.PermissionLevel
import command.permission.PermissionManager
import command.utils.NetTools
import command.utils.CommandUtils
import command.Config


/**
 * 召唤单位命令
 * 用于在游戏中召唤指定的单位
 */
class SummonUnitCommand : AbstractCommand() {
    

    override fun execute(args: List<String>, context: CommandContext) {
        if (!Config.summonUnit) {
            context.reply("错误: 召唤单位功能已禁用，请在首页点击插件进入设置页面启用")
            return
        }
        // 参数验证
        val unitName = validateAndGetUnitName(args, context) ?: return
        CommandUtils.summonUnitMap[context.player.name!!] = unitName
        context.reply("已将单位 $unitName 添加到召唤列表中，指定一个位置召唤该单位")
    }

    private fun validateAndGetUnitName(args: List<String>, context: CommandContext): String? {
        if (args.isEmpty()) {
            context.reply("错误: 请输入单位名称")
            return null
        }
        return args[0]
    }

    

    override fun getDescription(): String {
        return "召唤指定的单位到游戏中"
    }
}
