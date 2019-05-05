package injector.View

import injector.model.Tree
import javafx.beans.binding.Bindings
import javafx.scene.control.TreeItem
import javafx.scene.input.KeyCode
import tornadofx.*
import java.util.concurrent.Callable

class SelectionScreen : View("Selection") {
    var tree: Tree by singleAssign()
    val store: Store by inject()

    init {
        tree = store.tree
    }

    override val root = borderpane {
        center = treeview<Tree.Node> {
            root = TreeItem(tree.root)

            root.isExpanded = true

            cellFormat {
                text = it.item
                graphic = hbox(spacing = 5) {

                    val cb = checkbox {
                        isAllowIndeterminate = true
                        isDisable = true
                    }

                    cb.selectedProperty().bind(Bindings.createBooleanBinding(Callable {
                        when (it.checkStatusProperty.value) {
                            Tree.CheckStatus.FULL -> true
                            else -> false
                        }
                    }, it.checkStatusProperty))

                    cb.indeterminateProperty().bind(Bindings.createBooleanBinding(Callable {
                        when (it.checkStatusProperty.value) {
                            Tree.CheckStatus.PARTIAL -> true
                            else -> false
                        }
                    }, it.checkStatusProperty))

                    val checkLabel = label("-")
                    checkLabel.bind(Bindings.createStringBinding(Callable {
                        when (it.checkStatusProperty.value) {
                            Tree.CheckStatus.FULL -> "+"
                            Tree.CheckStatus.PARTIAL -> "|"
                            Tree.CheckStatus.NOT -> "-"
                            else -> "WTF"
                        }
                    }, it.checkStatusProperty))
                }
            }

            this.setOnKeyPressed { keyPress ->
                val item = this.selectionModel.selectedItem
                if (item != null && (keyPress.code == KeyCode.ENTER || keyPress.code == KeyCode.S)) {
                    when (item.value.checkStatusProperty.value) {
                        Tree.CheckStatus.PARTIAL, Tree.CheckStatus.NOT -> item.value.updateChildNodes(Tree.CheckStatus.FULL)
                        Tree.CheckStatus.FULL -> item.value.updateChildNodes(Tree.CheckStatus.NOT)
                        null -> println("STH WENT WRONG")
                    }
                    item.value.updateParentNodes()
                    println("ITEM: ${item.value.item}")
                }
            }
            populate({ TreeItem(it) }, { it.value.children })
        }
    }
}