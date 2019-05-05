package injector.View

import injector.types.Parameter.ParameterType.Companion.INT
import injector.types.Parameter.ParameterType.Companion.STR
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import org.controlsfx.glyphfont.FontAwesome
import tornadofx.*
import tornadofx.controlsfx.toggleswitch
import java.awt.Color

class MonitoringView : View("Params") {
    val controller: Store by inject()
    val parameters = mutableListOf<UIParameter>().observable()

    init {
        subscribe<ParamListEvent> {
            parameters.setAll(it.params)
            fire(RefreshParams)
        }
    }

    override val root = vbox {

        tableview<UIParameter> {

            column("Path", UIParameter::pathProperty)
                    .minWidth(120.0)
                    .style { borderColor += box(c("#282828")) }
            column("Value", UIParameter::valueProperty)
                    .remainingWidth()
                    .style { borderColor += box(c("#132433")) }
            column("Inject", UIParameter::pathProperty) {
                    contentWidth(60.0)
                    cellFormat {
                        graphic = hbox(spacing = 5, alignment = Pos.CENTER) {
                            val param = this@tableview.items[this@cellFormat.index]
                            val selectedProp = SimpleBooleanProperty(false)
                            when (param.typeEnum) {
                                INT, STR -> {
                                    val field = textfield()
                                    toggleswitch("", selectedProp) {
                                        selectedProp.addListener { _, _, new ->
                                            if (new) fire(InjectParameter(param.path, field.text))
                                            else fire(UninjectParameter(param.path))
                                        }
                                    }
                                }
                            }
                        }
                        style {
                            borderColor += box(c("#282828"))
                        }
                    }
            }
            columnResizePolicy = SmartResize.POLICY
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS
            items.bind(parameters) { it }
        }
    }

    override fun onUndock() {
        println("CANCELING TIMER")
        fire(StopRefresh)
    }

    override fun onDock() {
        fire(ParamListRequest)
    }


}