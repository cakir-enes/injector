package injector.View

import injector.model.Tree
import injector.types.Parameter
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import tornadofx.EventBus.RunOn.BackgroundThread
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.fixedRateTimer

object ParamListRequest : FXEvent(BackgroundThread)
object RefreshParams : FXEvent(BackgroundThread)
object StopRefresh : FXEvent(BackgroundThread)
object SelectionsChanged : FXEvent()
class UpdateLog(val text: String) : FXEvent()
class InjectParameter(val path: String, val value: String) : FXEvent(BackgroundThread)
class UninjectParameter(val path: String) : FXEvent(BackgroundThread)

class Store : Controller() {
    private val parameters = mutableMapOf<String, UIParameter>()
    private val injections = hashMapOf<String, String>()
    val tree by lazy { Tree().also { tree -> parameters.keys.forEach { tree.insert(it) } } }
    val selectedParameters = observableList<UIParameter>()
    var last = 0L
    private var refresh: Boolean = false

    init {
        runAsync {
            (0..50).map { "A.B.$it" to UIParameter("A.B.$it", "a", Parameter.ParameterType.INT) }.forEach { parameters[it.first] = it.second }
            fixedRateTimer("aa", false, 500, 1000 / 10) {
                runLater {
                    selectedParameters.filter {
                        !injections.containsKey(it.path)
                    }.forEach {
                        if (true) it.valueProperty.set(Random().nextInt(1500).toString())
                    }
                }
            }
        }

        subscribe<RefreshParams> {
            last = System.nanoTime()
            refresh = true
            println("Refreshed -- ${TimeUnit.MILLISECONDS.convert(System.nanoTime() - last, TimeUnit.NANOSECONDS)} passed--")
        }

        subscribe<SelectionsChanged> {
            selectedParameters.clear()
            val list = mutableListOf<String>()
            tree.root.traversePreorder { if (it.isLeaf() && it.checkStatusProperty.value == Tree.CheckStatus.FULL) list.add(it.data!!) }
            selectedParameters.addAll(parameters.values.filter { list.contains(it.path) })
        }

        subscribe<InjectParameter> { evt ->
            val param = parameters[evt.path]
            param?.valueProperty?.set(evt.value)
            param?.isInjectedProperty?.set(true)
            injections += evt.path to evt.value
            fire(UpdateLog("INJECTED ${evt.path} => ${evt.value}"))
        }

        subscribe<UninjectParameter> {
            val param = parameters[it.path]
            param?.isInjectedProperty?.set(false)
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

    val isInjectedProperty = SimpleBooleanProperty(false)
    var isInjected by isInjectedProperty


    override fun toString(): String {
        return "$path: $value"
    }
}


fun Parameter.toUIParameter() = UIParameter(this.path, this.value, this.type)