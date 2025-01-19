package command.suggestion

data class Suggestion(
    val value: String,
    val description: String
) {
    override fun toString(): String = value
} 