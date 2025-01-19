package command.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.*
import command.suggestion.Suggestion

class SuggestionAdapter(
    context: Context,
    private val allSuggestions: List<Suggestion>
) : ArrayAdapter<Suggestion>(context, android.R.layout.simple_dropdown_item_1line), Filterable {
    
    private var filteredSuggestions: List<Suggestion> = allSuggestions

    init {
        addAll(allSuggestions)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val container = (convertView as? LinearLayout) ?: LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16.dpToPx(context), 8.dpToPx(context), 16.dpToPx(context), 8.dpToPx(context))
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        if (container.childCount != 2) {
            container.removeAllViews()
            
            val valueText = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setTextColor(Color.GREEN)
                textSize = 16f
                typeface = Typeface.DEFAULT_BOLD
            }
            container.addView(valueText)

            val descriptionText = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setTextColor(Color.GRAY)
                textSize = 14f
                setPadding(8.dpToPx(context), 4.dpToPx(context), 0, 0)
            }
            container.addView(descriptionText)
        }

        val item = getItem(position)
        if (item != null) {
            (container.getChildAt(0) as TextView).text = item.value
            (container.getChildAt(1) as TextView).text = item.description
        }

        return container
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint == null || constraint.isEmpty()) {
                    filterResults.values = allSuggestions
                    filterResults.count = allSuggestions.size
                } else {
                    val query = constraint.toString()
                    val filteredList = allSuggestions.filter { 
                        it.value.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
                    }
                    filterResults.values = filteredList
                    filterResults.count = filteredList.size
                }
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredSuggestions = (results?.values as? List<Suggestion>) ?: emptyList()
                clear()
                if (filteredSuggestions.isNotEmpty()) {
                    addAll(filteredSuggestions)
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }

    override fun getCount(): Int = filteredSuggestions.size
    override fun getItem(position: Int): Suggestion? = filteredSuggestions.getOrNull(position)
    override fun getItemId(position: Int): Long = position.toLong()

    private fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density).toInt()
    }
} 