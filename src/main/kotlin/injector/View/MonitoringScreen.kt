package injector.View

import injector.types.Parameter.ParameterType.Companion.INT
import injector.types.Parameter.ParameterType.Companion.STR
import javafx.scene.layout.Priority
import tornadofx.*
import java.util.*
import kotlin.concurrent.fixedRateTimer

class MonitoringView : View("Params") {

    val parameters = mutableListOf<UIParameter>().observable()
    lateinit var timer: Timer

    init {
        subscribe<ParamListEvent> {
            parameters.setAll(it.params)
            if (::timer.isInitialized) timer.cancel()
            timer = fixedRateTimer("Refresher", false, 0L, 1000 / 10) { fire(RefreshParams) }
        }
    }

    override val root = vbox {

        button("Fetch") {
            action {
                fire(ParamListRequest)
            }
        }

        button("Stop") {
            action {
                println("Canceling")
                timer.cancel()
            }
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
                                        isSelected = false
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
        println("UNDOCKING")
        timer.cancel()
    }

}