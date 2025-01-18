package command.core

class BKTree(private val distanceFunction: (String, String) -> Int) {

    data class Node(val word: String, val children: MutableMap<Int, Node> = mutableMapOf())

    private var root: Node? = null

    fun add(word: String) {
        if (root == null) {
            root = Node(word)
            return
        }

        var current = root!!
        while (true) {
            val distance = distanceFunction(word, current.word)
            if (distance == 0) return

            val child = current.children[distance]
            if (child != null) {
                current = child
            } else {
                current.children[distance] = Node(word)
                break
            }
        }
    }

    fun search(word: String, tolerance: Int): List<String> {
        val results = mutableListOf<String>()
        searchRecursive(root, word, tolerance, results)
        return results
    }

    private fun searchRecursive(node: Node?, word: String, tolerance: Int, results: MutableList<String>) {
        if (node == null) return

        val distance = distanceFunction(word, node.word)
        if (distance <= tolerance) {
            results.add(node.word)
        }

        val min = distance - tolerance
        val max = distance + tolerance
        for (i in min..max) {
            searchRecursive(node.children[i], word, tolerance, results)
        }
    }
}


fun levenshteinDistance(s1: String, s2: String): Int {
    val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }

    for (i in 0..s1.length) {
        for (j in 0..s2.length) {
            when {
                i == 0 -> dp[i][j] = j
                j == 0 -> dp[i][j] = i
                else -> dp[i][j] = minOf(
                    dp[i - 1][j - 1] + if (s1[i - 1] == s2[j - 1]) 0 else 1,
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1
                )
            }
        }
    }

    return dp[s1.length][s2.length]
}