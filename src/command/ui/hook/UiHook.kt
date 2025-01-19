package command.ui.hook

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.text.TextWatcher
import android.text.Editable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import com.corrodinggames.rts.appFramework.MultiplayerBattleroomActivity
import android.os.Bundle
import com.moyang.rustedwarfare.js.api.GameEngine
import command.core.CommandManager
import command.alias.CommandAliasManager
import command.suggestion.CommandTokenizer
import command.suggestion.Suggestion
import command.ui.adapter.SuggestionAdapter

class UiHook(private val commandManager: CommandManager) {

    private data class CommandSuggestion(
        val command: String,
        val description: String
    ) {
        override fun toString(): String = command
    }

    // 获取所有命令建议
    private fun getCommandSuggestions(): Array<CommandSuggestion> {
        val suggestions = mutableListOf<CommandSuggestion>()
        
        commandManager.getCommands().forEach { (name, command) ->
            suggestions.add(CommandSuggestion(".$name", command.getDescription()))
            CommandAliasManager.getAliases(name).forEach { alias ->
                suggestions.add(CommandSuggestion(".$alias", command.getDescription()))
            }
        }
        
        return suggestions.toTypedArray()
    }

    fun init() {
        XposedBridge.hookMethod(
            MultiplayerBattleroomActivity::class.java.getDeclaredMethod(
                "onCreate",
                Bundle::class.java
            ),
            object : XC_MethodHook() {
                @SuppressLint("WrongConstant")
                override fun afterHookedMethod(param: MethodHookParam) {
                    val activity = param.thisObject as MultiplayerBattleroomActivity
                    val context = activity as Context

                    // 获取原始控件
                    val chatInput = activity.findViewById<EditText>(
                        activity.resources.getIdentifier(
                            "battleroom_text",
                            "id",
                            activity.packageName
                        )
                    )

                    // 获取原始发送按钮
                    val originalSendButton = activity.findViewById<Button>(
                        activity.resources.getIdentifier(
                            "battleroom_send",
                            "id",
                            activity.packageName
                        )
                    )

                    val parent = chatInput.parent as LinearLayout
                    val index = parent.indexOfChild(chatInput)

                    // 创建新的输入框
                    val newEditorText = MultiAutoCompleteTextView(context).apply {
                        layoutParams = chatInput.layoutParams
                        hint = "输入命令或聊天内容..."
                        imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
                        inputType = InputType.TYPE_CLASS_TEXT
                        maxLines = 1
                        setHintTextColor(Color.GRAY)
                        gravity = Gravity.CENTER_VERTICAL
                        dropDownVerticalOffset = 1.dpToPx(context)
                        dropDownWidth = ViewGroup.LayoutParams.WRAP_CONTENT
                        dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
                        setTokenizer(CommandTokenizer())
                        threshold = 0 // 设置为0，让输入任何字符都触发过滤

                        // 添加项目点击监听器
                        onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
                            post {
                                val text = getText().toString()
                                val command = CommandTokenizer.getCurrentCommand(text)
                                
                                if (command != null) {
                                    val suggestions = when(command) {
                                        ".banunit" -> CommandTokenizer.getUnitSuggestions()
                                        ".unbanunit" -> CommandTokenizer.getBannedUnitSuggestions()
                                        ".kick", ".mute" -> CommandTokenizer.getPlayerSuggestions()
                                        ".unmute" -> CommandTokenizer.getMutedPlayerSuggestions()
                                        ".summon" -> CommandTokenizer.getUnitSuggestions()
                                        else -> emptyList()
                                    }

                                    if (suggestions.isNotEmpty()) {
                                        val adapter = SuggestionAdapter(context, suggestions)
                                        setAdapter(adapter)
                                        showDropDown()
                                    }
                                }
                            }
                        }
                    }

                    // 设置文本变化监听器
                    newEditorText.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                        override fun afterTextChanged(s: Editable?) {
                            val text = s?.toString() ?: ""
                            val cursor = newEditorText.selectionStart
                            val command = CommandTokenizer.getCurrentCommand(text)
                            val argPosition = CommandTokenizer.getArgumentPosition(text, cursor)
                            val currentArg = CommandTokenizer.getCurrentArgument(text, cursor)

                            // 如果文本不是以点号开头且不是在输入参数，不显示任何提示
                            if (!text.startsWith(".") && !text.endsWith(".") && command == null) {
                                newEditorText.dismissDropDown()
                                return
                            }

                            // 如果正在输入命令（以点号开头或结尾）
                            if (text.endsWith(".") || (text.startsWith(".") && !text.contains(" "))) {
                                val commandSuggestions = getCommandSuggestions().map { 
                                    Suggestion(it.command, it.description)
                                }
                                if (commandSuggestions.isNotEmpty()) {
                                    val adapter = SuggestionAdapter(context, commandSuggestions)
                                    newEditorText.setAdapter(adapter)
                                    if (text.endsWith(".")) {
                                        adapter.filter.filter("")
                                    } else {
                                        adapter.filter.filter(text)
                                    }
                                    newEditorText.showDropDown()
                                }
                                return
                            }

                            // 如果正在输入参数
                            if (command != null && argPosition > 0) {
                                val suggestions = when(command) {
                                    ".banunit" -> if (argPosition == 1) CommandTokenizer.getUnitSuggestions() else emptyList()
                                    ".unbanunit" -> if (argPosition == 1) CommandTokenizer.getBannedUnitSuggestions() else emptyList()
                                    ".kick" -> if (argPosition == 1) CommandTokenizer.getPlayerSuggestions() else emptyList()
                                    ".mute" -> if (argPosition == 1) CommandTokenizer.getPlayerSuggestions() else emptyList()
                                    ".unmute" -> if (argPosition == 1) CommandTokenizer.getMutedPlayerSuggestions() else emptyList()
                                    ".summon" -> if (argPosition == 1) CommandTokenizer.getUnitSuggestions() else emptyList()
                                    else -> emptyList()
                                }

                                if (suggestions.isNotEmpty()) {
                                    val adapter = SuggestionAdapter(context, suggestions)
                                    newEditorText.setAdapter(adapter)
                                    if (currentArg.isNotEmpty()) {
                                        adapter.filter.filter(currentArg)
                                        if (!newEditorText.isPopupShowing) {
                                            newEditorText.showDropDown()
                                        }
                                    }
                                } else {
                                    newEditorText.dismissDropDown()
                                }
                            }
                        }
                    })

                    // 创建发送按钮
                    val sendButton = Button(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        text = "发送"
                    }

                    // 设置发送事件
                    sendButton.setOnClickListener {
                        val text = newEditorText.text.toString().trim()
                        if (text.isNotEmpty()) {
                            GameEngine.getInstance().netWorkEngine?.sendChatMessage(text)
                            newEditorText.text.clear()
                        }
                    }

                    // 设置回车发送
                    newEditorText.setOnEditorActionListener { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND) {
                            sendButton.performClick()
                            true
                        } else {
                            false
                        }
                    }

                    // 替换原有控件
                    parent.removeView(chatInput)
                    parent.removeView(originalSendButton)
                    parent.addView(newEditorText, index)
                    parent.addView(sendButton)
                }
            }
        )
    }

    private fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density).toInt()
    }
} 