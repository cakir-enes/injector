package injector.model

import injector.Client
//import injector.types.Parameter
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import tornadofx.EventBus.RunOn.BackgroundThread
import io.grpc.injector.Parameter
import java.util.concurrent.TimeUnit

object ParamListRequest : FXEvent(BackgroundThread)
object RefreshParams : FXEvent(BackgroundThread)
object StopRefresh : FXEvent(BackgroundThread)
object SelectionsChanged : FXEvent()
class UpdateLog(val text: String) : FXEvent()
class InjectParameter(val path: String, val value: String) : FXEvent(BackgroundThread)
class UninjectParameter(val path: String) : FXEvent(BackgroundThread)



class Store : Controller() {
    val client = Client("localhost:7777")
    private val parameters = mutableMapOf<String, UIParameter>()
    private val injections = hashMapOf<String, String>()
    val tree by lazy { Tree().also { tree -> parameters.keys.forEach { tree.insert(it) } } }
    val selectedParameters = observableListOf<UIParameter>()
    var last = 0L
    val enumValuesMap = hashMapOf<String, List<String>>()
    private var refresh: Boolean = false

    init {
        runAsync {
            client.getParameterInfos().paramInfosList.forEach {
                parameters[it.path] = UIParameter(it.path, it.value, it.type)
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
            runLater {
                param?.valueProperty?.set(evt.value)
                param?.isInjectedProperty?.set(true)
            }
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


class UIParameter(path: String, value: String, type: Parameter.Type) {
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