package command.core

import command.alias.CommandAliasManager
import command.permission.PermissionManager

class CommandManager {
    private val commands = mutableMapOf<String, Command>()
    private val bkTree = BKTree(::levenshteinDistance)

    fun registerCommand(name: String, command: Command) {
        commands[name] = command
        bkTree.add(name)
        
        // 注册别名
        CommandAliasManager.getAliases(name).forEach {
            bkTree.add(it)
        }
    }
    
    fun getCommand(name: String): Command? {
        return commands[name]
    }

    fun getCommands(): Map<String, Command> {
        return commands
    }

    // 使用BK树进行模糊匹配
    fun findBestMatch(input: String, tolerance: Int): String? {
        val results = bkTree.search(input, tolerance)
        return results.minByOrNull { levenshteinDistance(input, it) }
    }
}