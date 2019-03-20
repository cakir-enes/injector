package injector

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import tornadofx.*

class MyyApp : App(PersonEditor::class) {
//    val dc = DataAccessor("127.0.0.1:4222", "SNR")


}

fun main(args: Array<String>) {
    launch<MyyApp>(args)
}

class Person(name: String? = null, title: String? = null) {
    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val titleProperty = SimpleStringProperty(this, "title", title)
    var title by titleProperty
}

class PersonEditor : View("Person Editor") {
    override val root = BorderPane()
    var nameField: TextField by singleAssign()
    var titleField: TextField by singleAssign()
    var personTable: TableView<Person> by singleAssign()
    // Some fake data for our table
    val persons = listOf(Person("John", "Manager"), Person("Jay", "Worker bee")).observable()

    var prevSelection: Person? = null

    init {
        with(root) {
            // TableView showing a list of people
            center {
                tableview(persons) {
                    personTable = this
                    column("Name", Person::nameProperty)
                    column("Title", Person::titleProperty)

                    // Edit the currently selected person
                    selectionModel.selectedItemProperty().onChange {
                        editPerson(it)
                        prevSelection = it
                    }
                }
            }

            right {
                form {
                    fieldset("Edit person") {
                        field("Name") {
                            textfield() {
                                nameField = this
                            }
                        }
                        field("Title") {
                            textfield() {
                                titleField = this
                            }
                        }
                        button("Save").action {
                            save()
                        }
                    }
                }
            }
        }
    }

    private fun editPerson(person: Person?) {
        if (person != null) {
            prevSelection?.apply {
                nameProperty.unbindBidirectional(nameField.textProperty())
                titleProperty.unbindBidirectional(titleField.textProperty())
            }
            nameField.bind(person.nameProperty)
            titleField.bind(person.titleProperty)
            prevSelection = person
        }
    }

    private fun save() {
        // Extract the selected person from the tableView
        val person = personTable.selectedItem!!

        // A real application would persist the person here
        println("Saving ${person.name} / ${person.title}")
    }
}