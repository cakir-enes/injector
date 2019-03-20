package injector.View

import injector.types.Parameter.ParameterType.Companion.INT
import injector.types.Parameter.ParameterType.Companion.STR
import javafx.scene.layout.Priority
import tornadofx.*

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
        button("SET").setOnAction {
            controller.state.set(!controller.state.get())
        }
        tableview<UIParameter> {
            column("Path", UIParameter::pathProperty)
            column("Value", UIParameter::valueProperty).remainingWidth()
            column("Action", UIParameter::pathProperty)
                    .contentWidth(padding = 60.0)
                    .cellFormat {
                        graphic = hbox(spacing = 5) {
                            val param = this@tableview.items[this@cellFormat.index]
                            when (param.typeEnum) {
                                INT, STR -> {
                                    val field = textfield()
                                    togglebutton("[U]") {
                                        selectedProperty().bind(controller.state)
                                        action {
                                            text = if (isSelected) "[I]" else "[U]"
                                            if (isSelected)
                                                fire(InjectParameter(param.path, field.text))
                                            else
                                                fire(UninjectParameter(param.path))
                                        }
                                    }
                                }
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