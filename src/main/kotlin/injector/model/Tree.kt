package injector.model

import javafx.beans.property.SimpleObjectProperty


class Tree {
    val root = Node(parent = null)
    val selectedItems: List<String>
        get() = mutableListOf<String>().also { list ->
            root.traversePreorder {
                if (it.isLeaf() && it.checkStatusProperty.get() == CheckStatus.FULL) list.add(it.data!!)
            }
        }

    enum class CheckStatus { NOT, PARTIAL, FULL }
    data class Node(val item: String = "ROOT", val parent: Node?, val children: MutableList<Node> = mutableListOf(), var data: String? = null) {
        var checkStatusProperty = SimpleObjectProperty<CheckStatus>(CheckStatus.NOT)

        fun updateChildNodes(check: CheckStatus) {
            checkStatusProperty.set(check)
            children.forEach { it.updateChildNodes(check) }
        }

        fun updateParentNodes() {
            if (children.size == 0) {
                parent?.updateParentNodes()
                return
            }
            var allChildChecked = true
            var noChildChecked = true

            children.forEach {
                if (it.checkStatusProperty.get() != CheckStatus.FULL) allChildChecked = false
                else noChildChecked = false
                if (it.checkStatusProperty.get() == CheckStatus.PARTIAL) noChildChecked = false
            }

            when {
                allChildChecked -> checkStatusProperty.set(CheckStatus.FULL)
                noChildChecked -> checkStatusProperty.set(CheckStatus.NOT)
                else -> checkStatusProperty.set(CheckStatus.PARTIAL)
            }
            parent?.updateParentNodes()
        }

        override fun toString(): String {
            return "$item"
        }

        fun traversePreorder(f: (Node) -> Unit) {
            f(this)
            children.forEach { it.traversePreorder(f) }
        }

        fun isLeaf() = children.size == 0
    }

    fun insert(path: String) {
        val subPaths = path.split(".")
        val lastParent = subPaths.subList(0, subPaths.size - 1)
                .foldIndexed(root) { idx, parent, subPath ->
                    val newParent = parent.children.find { it.item == subPath }
                    return@foldIndexed if (newParent == null) {
                        val child = Node(subPath, parent, mutableListOf())
                        parent.children.add(child)
                        child
                    } else {
                        newParent
                    }
                }
        lastParent.children.add(Node(subPaths.last(), lastParent, mutableListOf(), path))
    }
}
