package injector.View

import javafx.beans.binding.Bindings
import javafx.scene.control.TreeItem
import tornadofx.*
import java.util.concurrent.Callable

class Tree {
    enum class CheckStatus { NOT, PARTIAL, FULL }
    data class Node(val item: String = "ROOT", val children: MutableList<Node> = mutableListOf()) {
        var checkStatus: CheckStatus = CheckStatus.NOT
            set(value) {
                when (value) {
                    CheckStatus.FULL -> children.forEach { it.checkStatus = CheckStatus.FULL }
                    CheckStatus.NOT -> children.forEach { it.checkStatus = CheckStatus.NOT }
                }
                println("CHECKED $item")
                field = value
            }

        fun dump(lvl: Int = 0, sb: StringBuilder) {
            sb.append("|" + ".".repeat(lvl) + item + "\n")
            children.forEach { it.dump(2 + lvl, sb) }
        }


    }

    val root = Node()
    fun insert(path: String) {
        path
                .split(".")
                .fold(root) { parent, subPath ->
                    val newParent = parent.children.find { it.item == subPath }
                    return@fold if (newParent == null) {
                        val child = Node(subPath, mutableListOf())
                        parent.children.add(child)
                        child
                    } else {
                        newParent
                    }
                }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        root.dump(sb = sb)
        return sb.toString()
    }
}


fun main() {
    val paths = listOf("A.B.C.D", "A.B.C.D", "F.C", "F.X.V.B")
    val tree = Tree()
    paths.forEach { tree.insert(it) }
    println(tree)
    launch<TreeApp>()
}

class TreeApp : App(SelectionScreen::class) {

}

class SelectionScreen : View("Selection") {
    val tree by lazy {
        val paths = listOf("A.B.C.D", "A.B.C.D", "F.C", "F.X.V.B")
        val tree = Tree()
        paths.forEach { tree.insert(it) }
        tree
    }

    override val root = treeview<Tree.Node> {
        root = TreeItem(tree.root)
        root.isExpanded = true

        cellFormat {
            text = it.item

            graphic = hbox(spacing = 5) {
                label(Bindings.createStringBinding(
                        Callable {
                            when (it.checkStatus) {
                                Tree.CheckStatus.FULL -> "-"
                                Tree.CheckStatus.PARTIAL -> "|"
                                Tree.CheckStatus.NOT -> "+"
                            }
                        }
                ))

            }

        }
        populate(itemFactory = {
            val item = TreeItem(it)
            item.expandedProperty().addListener { prop, old, new -> it.checkStatus = if (item.isExpanded) Tree.CheckStatus.FULL else Tree.CheckStatus.NOT }
            return@populate item
        }) { it.value.children }

    }
}