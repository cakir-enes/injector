package injector.model

import javafx.beans.property.SimpleObjectProperty

class Tree {
    val root = Node(parent = null)
    enum class CheckStatus { NOT, PARTIAL, FULL }
    data class Node(val item: String = "ROOT", val parent: Node?, val children: MutableList<Node> = mutableListOf()) {
        var checkStatusProperty = SimpleObjectProperty<CheckStatus>(CheckStatus.NOT)

        fun updateChildNodes(check: CheckStatus) {
            checkStatusProperty.set(check)
            children.forEach{it.updateChildNodes(check)}
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
    }
    fun insert(path: String) {
        path
                .split(".")
                .fold(root) { parent, subPath ->
                    val newParent = parent.children.find { it.item == subPath }
                    return@fold if (newParent == null) {
                        val child = Node(subPath, parent, mutableListOf())
                        parent.children.add(child)
                        child
                    } else {
                        newParent
                    }
                }
    }
}
