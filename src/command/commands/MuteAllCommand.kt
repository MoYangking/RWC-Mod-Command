package command.commands

import command.core.AbstractCommand
import command.core.CommandContext
import command.permission.PermissionManager
import command.utils.NetTools

class MuteAllCommand : AbstractCommand() {
    override fun execute(args: List<String>, context: CommandContext) {
        PermissionManager.muteAllPlayers() // 全体禁言
        context.broadcast("所有玩家已被禁言")
    }

    override fun getDescription(): String {
        return "全体禁言，示例: .ma 全体禁言"
    }

    

} 