package command.commands

import com.moyang.rustedwarfare.js.api.GameEngine
import com.moyang.rustedwarfare.js.api.command.GameCommand
import command.core.AbstractCommand
import command.core.CommandContext
import command.core.CommandManager
import command.permission.PermissionLevel
import command.permission.PermissionManager

class TestCommand() : AbstractCommand() {
    override fun execute(args: List<String>, context: CommandContext) {
        GameEngine.getInstance().netWorkEngine!!.sendSysMessage("执行成功")
    }

    override fun getPermissionLevel(): PermissionLevel {
        return PermissionLevel.PLAYER
    }
}