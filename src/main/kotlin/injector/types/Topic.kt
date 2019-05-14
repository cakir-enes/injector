package injector.View

typealias Field = String
typealias Value = String

data class Topic(val name: String, val fields: List<Pair<Field, Value>>)
