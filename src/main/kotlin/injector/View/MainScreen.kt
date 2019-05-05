package injector.View

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.text.TextAlignment
import javafx.stage.Stage
import jfxtras.styles.jmetro8.JMetro
import tornadofx.*

class EventApp : App(MainView::class) {
    init {
        find(Store::class)
    }

    override fun start(stage: Stage) {
        super.start(stage)
        JMetro(JMetro.Style.LIGHT).applyTheme(stage.scene)
    }
}

class MainView : View("Main") {
    val monitoringView: MonitoringView by inject()
    val statusBar: StatusBar by inject()

    override val root = borderpane {
        center = monitoringView.root
        top = hbox(5, Pos.CENTER) {
            button("Selection (F7)") {
                action {
                    center.replaceWith(find(SelectionScreen::class).root)
                }
                shortcut("F7")
            }
            button("Monitoring (F8)") {
                action {
                    center.replaceWith(find(MonitoringView::class).root)
                    fire(SelectionsChanged)
                }
                shortcut("F8")
            }
        }
        bottom = statusBar.root
        prefHeight = 768.0
        prefWidth = 1024.0
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


