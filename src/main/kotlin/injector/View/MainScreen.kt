package injector.View

import javafx.beans.property.SimpleStringProperty
import javafx.scene.text.TextAlignment
import javafx.stage.Stage
import tornadofx.*


fun main() {
    launch<EventApp>()
}

class EventApp : App(MainView::class) {
    init {
        find(Store::class)
    }

    override fun start(stage: Stage) {
        reloadViewsOnFocus()
        reloadStylesheetsOnFocus()
        super.start(stage)
    }
}

class MainView : View("Main") {
    val monitoringView: MonitoringView by inject()
    val statusBar: StatusBar by inject()

    override val root = borderpane {
        center = monitoringView.root
        left = vbox {
            button("Selection").setOnAction {
                center.replaceWith(find(SelectionScreen::class).root)
            }
            button("Monitoring").setOnAction {
                center.replaceWith(find(MonitoringView::class).root)
            }
        }
        bottom = statusBar.root
        prefHeight = 600.0
        prefWidth = 800.0
    }
}

class StatusBar : View("StatusBar") {
    private val statusText = SimpleStringProperty()
    override val root = label("STATUS: ") {
        textAlignment = TextAlignment.CENTER
        textProperty().bind(statusText)
    }

    init {
        subscribe<UpdateLog> {
            statusText.set(it.text)
        }
    }
}


