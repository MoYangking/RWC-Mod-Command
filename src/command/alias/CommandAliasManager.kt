package command.alias

object CommandAliasManager {
    private val aliasMap = mutableMapOf<String, MutableList<String>>()

    fun addAlias(alias: String, command: String) {
        if (!aliasMap.containsKey(command)) {
            aliasMap[command] = mutableListOf()
        }
        aliasMap[command]?.add(alias)
    }

    fun getCommand(alias: String): String? {
        aliasMap.forEach { (command, aliases) ->
            if (aliases.contains(alias)) {
                return command
            }
        }
        return null
    }

    fun getAliases(command: String): List<String> {
        return aliasMap[command] ?: emptyList()
    }
}