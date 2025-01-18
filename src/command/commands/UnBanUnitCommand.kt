package command.commands

import command.core.AbstractCommand
import command.core.CommandContext
import command.utils.NetTools
import command.permission.PermissionManager

class UnBanUnitCommand : AbstractCommand() {
    override fun execute(args: List<String>, context: CommandContext) {
        if (args.isEmpty()) {
            context.reply("错误: 需要指定单位名称")
            return
        }
        PermissionManager.unbanUnit(args[0])
        context.broadcast("${args[0]} 已被解禁")
    }

    override fun getDescription(): String {
        return "解禁单位，参数: 单位名称，示例: .ubu fabricatort1 解禁蛋"
    }
}