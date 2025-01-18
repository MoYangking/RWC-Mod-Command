package command.commands

import command.core.AbstractCommand
import command.core.CommandContext
import command.utils.NetTools
import command.permission.PermissionManager

class BanUnitCommand : AbstractCommand() {
    override fun execute(args: List<String>, context: CommandContext) {
        if (args.isEmpty()) {
            context.reply("错误: 需要指定单位名称")
            return
        }
        PermissionManager.banUnit(args[0])
        context.broadcast("${args[0]} 已被禁用")
    }

    override fun getDescription(): String {
        return "禁用单位，参数: 单位名称，示例: .bu fabricatort1 禁蛋"
    }
}