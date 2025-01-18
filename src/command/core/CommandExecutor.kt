package command.core

// 使用策略模式执行指令
object CommandExecutor {
    fun execute(command: Command, args: List<String>, context: CommandContext) {
        command.execute(args, context)
    }
}