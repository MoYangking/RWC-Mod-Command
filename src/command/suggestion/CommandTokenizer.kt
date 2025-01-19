package command.suggestion

import android.widget.MultiAutoCompleteTextView
import com.moyang.rustedwarfare.js.api.game.Player
import command.permission.PermissionManager
import command.data.unit.UnitData

class CommandTokenizer : MultiAutoCompleteTextView.Tokenizer {
    override fun findTokenStart(text: CharSequence, cursor: Int): Int {
        var i = cursor
        while (i > 0 && text[i - 1] != ' ') {
            i--
        }
        return i
    }

    override fun findTokenEnd(text: CharSequence, cursor: Int): Int {
        var i = cursor
        val len = text.length
        while (i < len) {
            if (text[i] == ' ') {
                return i
            }
            i++
        }
        return len
    }

    override fun terminateToken(text: CharSequence): CharSequence {
        // 如果是命令（以点号开头且不包含空格），添加空格
        if (text.startsWith(".") && !text.contains(" ")) {
            return "$text "
        }
        // 如果是参数，不添加空格
        return text
    }

    companion object {
        // 获取当前命令
        fun getCurrentCommand(text: String): String? {
            val words = text.trim().split(" ")
            if (words.isEmpty()) return null
            return words[0]
        }

        // 获取命令参数位置
        fun getArgumentPosition(text: String, cursor: Int): Int {
            val beforeCursor = text.substring(0, cursor)
            return beforeCursor.count { it == ' ' }
        }

        // 获取当前正在输入的参数
        fun getCurrentArgument(text: String, cursor: Int): String {
            if (text.endsWith(".")) return ""
            val lastSpace = text.lastIndexOf(' ', cursor - 1)
            return if (lastSpace == -1) text else text.substring(lastSpace + 1, cursor)
        }

        // 获取玩家建议列表
        fun getPlayerSuggestions(): List<Suggestion> {
            val suggestions = mutableListOf<Suggestion>()
            Player.getPlayers().forEach { player ->
                player.name?.let { name ->
                    suggestions.add(Suggestion(name, "玩家名称"))
                    suggestions.add(Suggestion(player.playerIndex.toString(), "玩家索引 - $name"))
                }
            }
            return suggestions
        }

        // 获取被禁言玩家建议列表
        fun getMutedPlayerSuggestions(): List<Suggestion> {
            return PermissionManager.getMutedPlayers().map { 
                Suggestion(it, "被禁言的玩家")
            }
        }

        // 获取被禁用单位建议列表
        fun getBannedUnitSuggestions(): List<Suggestion> {
            return PermissionManager.bannedUnits.map { 
                Suggestion(it, "被禁用的单位")
            }
        }

        // 获取所有单位建议列表
        fun getUnitSuggestions(): List<Suggestion> {
            return UnitData.getAllUnits().map { unit ->
                Suggestion(unit.id, "${unit.name} (${getCategoryName(unit.category)})")
            }
        }

        private fun getCategoryName(category: command.data.unit.UnitCategory): String {
            return when (category) {
                command.data.unit.UnitCategory.AIR -> "空军"
                command.data.unit.UnitCategory.LAND -> "陆军"
                command.data.unit.UnitCategory.NAVY -> "海军"
                command.data.unit.UnitCategory.STRUCTURE -> "建筑"
                command.data.unit.UnitCategory.BUG -> "虫族"
                command.data.unit.UnitCategory.HIDDEN -> "隐藏"
                command.data.unit.UnitCategory.SPECIAL -> "特殊"
            }
        }
    }
} 