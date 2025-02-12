package command.init

import command.alias.CommandAliasManager
import command.commands.*
import command.core.CommandManager
import command.core.CommandParser

class CommandInitializer(private val commandManager: CommandManager, private val commandParser: CommandParser) {
    fun initializeCommands() {
        registerBaseCommands()
        registerCommandAliases()
    }

    // 将基础命令注册拆分为单独的方法
    private fun registerBaseCommands() {
        with(commandManager) {
            registerCommand("help", HelpCommand(commandManager))
            registerCommand("setmaxteamid", SetMaxTeamIdCommand())
            registerCommand("income", IncomeCommand())
            registerCommand("kick", KickCommand())
            registerCommand("mute", MuteCommand())
            registerCommand("muteall", MuteAllCommand())
            registerCommand("unmute", UnmuteCommand())
            registerCommand("unmuteall", UnmuteAllCommand())
            registerCommand("banunit", BanUnitCommand())
            registerCommand("unbanunit", UnBanUnitCommand())
            registerCommand("unkes", UnkesCommand())
            registerCommand("autoteam", SetAutoTeamCommand())
            registerCommand("summon", SummonUnitCommand())
        }
    }

    // 将命令别名注册拆分为单独的方法
    private fun registerCommandAliases() {
        with(CommandAliasManager) {
            addAlias("h", "help")
            addAlias("si", "income")
            addAlias("k", "kick")
            addAlias("m", "mute")
            addAlias("ma", "muteall")
            addAlias("um", "unmute")
            addAlias("uma", "unmuteall")
            addAlias("smti", "setmaxteamid")
            addAlias("bu", "banunit")
            addAlias("ubu", "unbanunit")
            addAlias("uk", "unkes")
            addAlias("at", "autoteam")
            addAlias("su", "summon")
        }
    }
} 