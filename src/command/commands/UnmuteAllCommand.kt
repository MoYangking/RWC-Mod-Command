package command.commands

import command.core.AbstractCommand
import command.core.CommandContext
import command.permission.PermissionManager
import command.utils.NetTools

class UnmuteAllCommand : AbstractCommand() {
    override fun execute(args: List<String>, context: CommandContext) {
        PermissionManager.unmuteAllPlayers() // 解除全体禁言
        context.broadcast("所有玩家的禁言已解除")
    }

    override fun getDescription(): String {
        return "解除全体禁言，示例: .uma 解除全体禁言"
    }
} 