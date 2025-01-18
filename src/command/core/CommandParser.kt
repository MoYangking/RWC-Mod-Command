package command.core

import command.alias.CommandAliasManager
import command.permission.PermissionManager
import command.utils.CommandUtils

class CommandParser(private val commandManager: CommandManager) {
    fun parse(context: CommandContext, input: String) {
        if (!input.startsWith(".")) return
        try {
            val parts = input.substring(1).split(" ")
            val commandName = parts[0]
            val args = parts.drop(1)
            
            // 解析别名
            val resolvedCommandName = CommandAliasManager.getCommand(commandName) ?: commandName
            
            // 获取指令
            val resolvedCommand = commandManager.getCommand(resolvedCommandName)
            
            if (resolvedCommand != null) {
                if (context.hasPermission(resolvedCommand.getPermissionLevel())) {
                    resolvedCommand.execute(args, context)
                } else {
                    context.reply("权限不足")
                }
            } else {
                // 使用模糊匹配查找最相近的命令
                val bestMatch = commandManager.findBestMatch(commandName, 3)
                if (bestMatch != null) {
                    context.reply("没找到命令，是否输入的是 '.${bestMatch}' 命令？")
                } else {
                    context.reply("没找到命令，请检查命令是否正确")
                }
            }
        } catch (e: Exception) {
            context.reply("执行命令时发生错误: ${e.message}")
        }
    }
}