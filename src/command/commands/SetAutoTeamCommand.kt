package command.commands

import command.core.AbstractCommand
import command.core.CommandContext
import com.moyang.rustedwarfare.js.api.game.Player

class SetAutoTeamCommand : AbstractCommand() {
    companion object {
        private var enabled = false
        private var teamCount = 2

        fun getNextTeam(): Int {
            if (!enabled) return 0
            
            val teamCounts = mutableMapOf<Int, Int>()
            Player.getPlayers().forEach { player ->
                val team = player.team ?: 0
                teamCounts[team] = (teamCounts[team] ?: 0) + 1
            }
            
            // 找到人数最少的队伍
            return (0 until teamCount).minByOrNull { teamCounts[it] ?: 0 } ?: 0
        }
    }

    override fun execute(args: List<String>, context: CommandContext) {
        if (args.isEmpty()) {
            enabled = false
            context.broadcast("已禁用自动分队")
            return
        }

        val count = args[0].toIntOrNull()
        if (count == null || count <= 0) {
            context.reply("错误: 请输入大于0的队伍数，示例: .at 1")
            return
        }

        enabled = true
        teamCount = count
        context.broadcast("已开启自动分队: $count 个队伍")
    }

    override fun getDescription(): String {
        return "设置自动分队，不带参数则关闭自动分队，参数: [队伍数]，示例: .at 1 设置每队1人即混战"
    }
} 