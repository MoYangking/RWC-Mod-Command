package command.commands

import command.core.AbstractCommand
import command.core.CommandContext
import com.moyang.rustedwarfare.js.api.GameEngine
import command.utils.NetTools
import command.utils.CommandUtils

class UnkesCommand : AbstractCommand() {
    override fun execute(args: List<String>, context: CommandContext) {
        if (args.isEmpty()) {
            context.reply("错误: 请输入参数")
            return
        }

        
        val isNukeEnabled = CommandUtils.parseBooleanArgument(args[0])

        if(isNukeEnabled == null){
            context.reply("错误: 无效的参数")
            return
        }

        val gameEngine = GameEngine.getInstance()
        
        
        setNukeBan(gameEngine, isNukeEnabled)

        
        val message = if (isNukeEnabled) "房间已开启核弹" else "房间已关闭核弹"
        context.broadcast(message)
    }

    private fun setNukeBan(gameEngine: GameEngine, isNukeEnabled: Boolean) {
        gameEngine.netWorkEngine!!.rules!!.banNuke = !isNukeEnabled
        gameEngine.netWorkEngine!!.syncRules()
        gameEngine.netWorkEngine!!.sendRsyncSave(true, false, true)
    }

    override fun getDescription(): String {
        return "设置是否开启核弹，参数: true(on)/false(off)，示例: .uk true 开启核弹"
    }

}

