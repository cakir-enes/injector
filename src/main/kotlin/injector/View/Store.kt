package injector.View

import injector.types.Parameter
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.Controller
import tornadofx.EventBus.RunOn.BackgroundThread
import tornadofx.FXEvent
import tornadofx.getValue
import tornadofx.setValue
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.fixedRateTimer

object ParamListRequest : FXEvent(BackgroundThread)
object RefreshParams : FXEvent(BackgroundThread)
object StopRefresh : FXEvent(BackgroundThread)
class ParamListEvent(val params: List<UIParameter>) : FXEvent()
class UpdateLog(val text: String) : FXEvent()
class InjectParameter(val path: String, val value: String) : FXEvent(BackgroundThread)
class UninjectParameter(val path: String) : FXEvent(BackgroundThread)

class Store : Controller() {

    private val parameters by lazy { (0..10000).map { UIParameter("$it", "25", Parameter.ParameterType.INT) } }
    private val injections = hashMapOf<String, String>()
    var last = 0L
    val state = SimpleBooleanProperty(false)
    private var refresh: Boolean = false

    init {
        subscribe<ParamListRequest> {
            fire(UpdateLog("PARAM LIST REQUESTED"))
            state.set(true)
            fire(ParamListEvent(parameters))
            fixedRateTimer("aa", false, 500, 1000 / 10) {
                parameters.filter {
                    !injections.containsKey(it.path)
                }.forEach {
                    if (refresh) it.valueProperty.set(Random().nextInt(1500).toString())
                }
            }
            state.addListener { prop, old, new -> println("Changed to $new") }
        }

        subscribe<RefreshParams> {
            last = System.nanoTime()
            refresh = true
            println("Refreshed -- ${TimeUnit.MILLISECONDS.convert(System.nanoTime() - last, TimeUnit.NANOSECONDS)} passed--")
        }

        subscribe<InjectParameter> { evt ->
            parameters.find { it.path == evt.path }?.valueProperty?.set(evt.value)
            injections += evt.path to evt.value
            fire(UpdateLog("INJECTED ${evt.path} => ${evt.value}"))
        }

        subscribe<UninjectParameter> {
            injections -= it.path
            fire(UpdateLog("UNINJECTED ${it.path}"))
        }

        subscribe<StopRefresh> {
            println("Stop Refresh")
            refresh = false
        }
    }
}


class UIParameter(path: String, value: String, type: Parameter.ParameterType) {
    val typeEnum: Parameter.ParameterType = type

    val pathProperty = SimpleStringProperty(path)
    var path by pathProperty

    val valueProperty = SimpleStringProperty(value)
    var value by valueProperty

    val typeProperty = SimpleStringProperty(type.toString())
    var type by typeProperty


    override fun toString(): String {
        return "$path: $value"
    }
}


fun Parameter.toUIParameter() = UIParameter(this.path, this.value, this.type)