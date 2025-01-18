package command.utils

import command.core.CommandContext
import com.moyang.rustedwarfare.js.api.GameEngine
import com.corrodinggames.rts.gameFramework.k5
import android.widget.Toast
import com.moyang.rustedwarfare.js.RWApplication
import com.moyang.rustedwarfare.js.api.command.GameCommand
import com.corrodinggames.rts.gameFramework.j.j

object CommandUtils {
    //添加一个数据结构用来储存玩家想要召唤的单位
    val summonUnitMap = HashMap<String, String>()

    fun formatHelpMessage(command: String, description: String, aliases: List<String>): String {
        return buildString {
            append(".$command - $description")
            if (aliases.isNotEmpty()) {
                append(" (别名: ${aliases.joinToString(", ")})")
            }
        }
    }
    
    fun parseArgs(args: List<String>, index: Int, default: String? = null): String? {
        return args.getOrNull(index) ?: default
    }
    
    fun requireArgs(args: List<String>, minCount: Int, context: CommandContext): Boolean {
        if (args.size < minCount) {
            context.reply("参数不足")
            return false
        }
        return true
    }

    fun parseBooleanArgument(arg: String): Boolean? {
        return when (arg.lowercase()) {
           "true", "on" -> true
           "false", "off" -> false
           else -> null
       }
   }

   fun print(tag: String, message: String) {
        k5.d(tag, message)
   }
   fun showToast(message: String) {
       val context = RWApplication.getContextObject()
       android.os.Handler(android.os.Looper.getMainLooper()).post {
           Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
       }
   }

    fun summonUnit(unitName: String, x: Float, y: Float, team: Int) {
    val gameEngine = GameEngine.getInstance()
    val netEngine = gameEngine.netWorkEngine ?: throw IllegalStateException("网络引擎未初始化")
    val commandController = gameEngine.commandController ?: throw IllegalStateException("命令控制器未初始化")

    // 创建召唤数据包
    val packet = NetTools.gameSummon(unitName, x, y, team)
    val input = j(packet.originalObject)

    // 发送召唤命令
    val gameCommand = commandController.originalObject.b()
    val net = netEngine.originalObject
    
    
    gameCommand.a(input)
    gameCommand.c = net.Z + net.S
    
    // 添加并执行命令
    val command = GameCommand(gameCommand)
    commandController.addCommand(command)
    
    // 发送成功消息
    netEngine.sendSysMessage("召唤单位: $unitName")
}

} 