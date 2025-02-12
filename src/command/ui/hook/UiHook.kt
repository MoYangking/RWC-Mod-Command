package command.ui.hook

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import android.view.LayoutInflater
import com.corrodinggames.rts.appFramework.InGameActivity
import com.corrodinggames.rts.game.units.a.t
import com.moyang.rustedwarfare.js.api.GameEngine
import command.core.CommandManager
import command.alias.CommandAliasManager
import command.suggestion.CommandTokenizer
import command.suggestion.Suggestion
import command.ui.adapter.SuggestionAdapter
import command.utils.NetTools
import de.robv.android.xposed.XC_MethodReplacement

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
            InGameActivity::class.java.getDeclaredMethod(
                "makeSendMessagePopup",
                Boolean::class.java
            ),
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam): Any? {
                    val activity = param.thisObject as InGameActivity
                    val z = param.args[0] as Boolean
                    val builder = AlertDialog.Builder(activity)
                    builder.setTitle(if (!z) "Send Message" else "Send Team Message")

                    val inflate = LayoutInflater.from(activity).inflate(
                        activity.resources.getIdentifier(
                            "alert_chat",
                            "layout",
                            activity.packageName
                        ), null
                    )
                    builder.setView(inflate)

                    val textView = inflate.findViewById<TextView>(
                        activity.resources.getIdentifier(
                            "chat_messages",
                            "id",
                            activity.packageName
                        )
                    )
                    var editText = inflate.findViewById<EditText>(
                        activity.resources.getIdentifier(
                            "chat_text",
                            "id",
                            activity.packageName
                        )
                    )
                    val parent = editText.parent as LinearLayout

                    //创建一个自动完成文本框
                    if (!z) {
                        parent.removeView(editText)
                        editText = createNewEditorText(activity, editText)
                        parent.addView(editText)
                    }
                    

                    textView.text = GameEngine.getInstance().netWorkEngine!!.originalObject.aE.a()
                    editText.setText("")
                    editText.requestFocus()

                    builder.setPositiveButton(if (z) "Send Team" else "Send") { _, _ ->
                        val text = editText.text.toString()
                        if (!text.trim().equals(""))
                            if (z)
                                NetTools.sendTeamMessage(text)
                            else
                                GameEngine.getInstance().netWorkEngine!!.sendChatMessage(text)
                    }

                    builder.setNeutralButton("Send & Ping Map") { _, _ ->
                        val text = editText.text.toString()
                        val interfaceEngine = GameEngine.getInstance().interfaceEngine!!
                        if (!text.trim().equals(""))
                            if (z)
                                NetTools.sendTeamMessage(text)
                            else
                                GameEngine.getInstance().netWorkEngine!!.sendChatMessage(text)

                        interfaceEngine.originalObject.u = false;
                        interfaceEngine.originalObject.l();
                    }
                    builder.setNegativeButton("Cancel") { _, _ -> }

                    builder.show()

                    return null
                }
            }
        )

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
                    val newEditorText = createNewEditorText(context, chatInput)

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

                    // 替换原有控件
                    parent.removeView(chatInput)
                    parent.removeView(originalSendButton)
                    parent.addView(newEditorText, index)
                    parent.addView(sendButton)
                }
            }
        )
    }

    @SuppressLint("WrongConstant")
    private fun createNewEditorText(context: Context, chatInput: EditText): MultiAutoCompleteTextView {
        return MultiAutoCompleteTextView(context).apply {
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
                        val suggestions = when (command) {
                            ".banunit", ".bu" -> CommandTokenizer.getGameUnitSuggestions()
                            ".unbanunit", ".ubu" -> CommandTokenizer.getBannedUnitSuggestions()
                            ".kick", ".mute", ".k", ".m" -> CommandTokenizer.getPlayerSuggestions()
                            ".unmute", ".um" -> CommandTokenizer.getMutedPlayerSuggestions()
                            ".summon", ".su" -> CommandTokenizer.getGameUnitSuggestions()
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

            // 设置文本变化监听器
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val text = s?.toString() ?: ""
                    val cursor = selectionStart
                    val command = CommandTokenizer.getCurrentCommand(text)
                    val argPosition = CommandTokenizer.getArgumentPosition(text, cursor)
                    val currentArg = CommandTokenizer.getCurrentArgument(text, cursor)

                    // 如果文本不是以点号开头且不是在输入参数，不显示任何提示
                    if (!text.startsWith(".") && !text.endsWith(".") && command == null) {
                        dismissDropDown()
                        return
                    }

                    // 如果正在输入命令（以点号开头或结尾）
                    if (text.endsWith(".") || (text.startsWith(".") && !text.contains(" "))) {
                        val commandSuggestions = getCommandSuggestions().map {
                            Suggestion(it.command, it.description)
                        }
                        if (commandSuggestions.isNotEmpty()) {
                            val adapter = SuggestionAdapter(context, commandSuggestions)
                            setAdapter(adapter)
                            if (text.endsWith(".")) {
                                adapter.filter.filter("")
                            } else {
                                adapter.filter.filter(text)
                            }
                            showDropDown()
                        }
                        return
                    }

                    // 如果正在输入参数
                    if (command != null && argPosition > 0) {
                        val suggestions = when (command) {
                            ".banunit", ".bu" -> if (argPosition == 1) CommandTokenizer.getGameUnitSuggestions() else emptyList()
                            ".unbanunit", ".ubu" -> if (argPosition == 1) CommandTokenizer.getBannedUnitSuggestions() else emptyList()
                            ".kick", ".k", ".mute", ".m" -> if (argPosition == 1) CommandTokenizer.getPlayerSuggestions() else emptyList()
                            ".unmute", ".um" -> if (argPosition == 1) CommandTokenizer.getMutedPlayerSuggestions() else emptyList()
                            ".summon", ".su" -> if (argPosition == 1) CommandTokenizer.getGameUnitSuggestions() else emptyList()
                            else -> emptyList()
                        }

                        if (suggestions.isNotEmpty()) {
                            val adapter = SuggestionAdapter(context, suggestions)
                            setAdapter(adapter)
                            if (currentArg.isNotEmpty()) {
                                adapter.filter.filter(currentArg)
                                if (!isPopupShowing) {
                                    showDropDown()
                                }
                            }
                        } else {
                            dismissDropDown()
                        }
                    }
                }
            })
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density).toInt()
    }
}