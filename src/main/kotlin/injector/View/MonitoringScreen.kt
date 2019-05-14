package injector.View

import injector.View.Styles.Companion.green
import injector.View.Styles.Companion.red
import injector.types.Parameter
import injector.types.Parameter.ParameterType.Companion.INT
import injector.types.Parameter.ParameterType.Companion.STR
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import org.controlsfx.glyphfont.FontAwesome
import tornadofx.*
import tornadofx.controlsfx.toggleswitch
import java.util.concurrent.Callable

class Styles : Stylesheet() {
    companion object {
        val red by cssclass()
        val green by cssclass()
    }

    init {
        red { backgroundColor += c("#282828") }
        green { backgroundColor += c("#aaaaaa") }
    }
}

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
                                if (it.isInjected) Color.INDIANRED
                                else if (selectedItem == it) Color.CADETBLUE
                                else Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))
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
