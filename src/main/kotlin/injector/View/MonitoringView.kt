package injector.View

import injector.model.StopRefresh
import injector.model.Store
import javafx.beans.binding.Bindings
import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import tornadofx.*
import java.util.concurrent.Callable

class MonitoringView : View("Params") {
    val store: Store by inject()
    //    val parameters = (0..50).map { UIParameter("A.B.$it" , "a", Parameter.ParameterType.INT) }.observable()
    val parameters = store.selectedParameters

    init {
        shortcut("return"){
            val item = root.selectedItem
            if (item != null) {

            }
        }
    }

    override val root = listview(parameters) {
        cellFormat {
            graphic = cache {
                borderpane {
                    left = label(it.path)
                    right = label {
                        textProperty().bind(it.valueProperty)
                    }


                }
            }
            backgroundProperty().bind(
                    Bindings.createObjectBinding(Callable {
                        Background(BackgroundFill(
                                when {
                                    it.isInjected -> Color.INDIANRED
                                    selectedItem == it -> Color.CADETBLUE
                                    else -> Color.DIMGRAY
                                }, CornerRadii.EMPTY, Insets.EMPTY))
                    }, it.isInjectedProperty))


            style {
                borderColor += box(c("#282828"))
            }

        }
        onDoubleClick {
            val item = selectionModel.selectedItem
            if (item != null) {
                item.isInjectedProperty.set(!item.isInjected)
                println("$item ${item.isInjected}")
            }
        }
    }

    override fun onUndock() {
        println("CANCELING TIMER")
        fire(StopRefresh)
    }

}
