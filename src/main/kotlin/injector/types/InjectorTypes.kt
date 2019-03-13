package injector.types

data class Parameter(
    val path: String = "",
    val type: injector.types.Parameter.ParameterType = injector.types.Parameter.ParameterType.fromValue(0),
    val value: String = "",
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Parameter> {
    override operator fun plus(other: Parameter?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Parameter> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Parameter.protoUnmarshalImpl(u)
    }

    data class ParameterType(override val value: Int) : pbandk.Message.Enum {
        companion object : pbandk.Message.Enum.Companion<ParameterType> {
            val UNKNOWN = ParameterType(0)
            val STR = ParameterType(1)
            val INT = ParameterType(2)
            val FLOAT = ParameterType(3)
            val BOOL = ParameterType(4)
            val ARR = ParameterType(5)
            val OBJ = ParameterType(6)
            val ENUM = ParameterType(7)

            override fun fromValue(value: Int) = when (value) {
                0 -> UNKNOWN
                1 -> STR
                2 -> INT
                3 -> FLOAT
                4 -> BOOL
                5 -> ARR
                6 -> OBJ
                7 -> ENUM
                else -> ParameterType(value)
            }
        }
    }
}

data class Request(
    val type: injector.types.Request.RequestType = injector.types.Request.RequestType.fromValue(0),
    val args: List<String> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Request> {
    override operator fun plus(other: Request?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Request> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Request.protoUnmarshalImpl(u)
    }

    data class RequestType(override val value: Int) : pbandk.Message.Enum {
        companion object : pbandk.Message.Enum.Companion<RequestType> {
            val UNKNOWN = RequestType(0)
            val MULTIGET = RequestType(1)
            val SET = RequestType(2)
            val DISCOVER = RequestType(3)

            override fun fromValue(value: Int) = when (value) {
                0 -> UNKNOWN
                1 -> MULTIGET
                2 -> SET
                3 -> DISCOVER
                else -> RequestType(value)
            }
        }
    }
}

data class Response(
        val type: Type? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Response> {
    sealed class Type {
        data class Error(val error: injector.types.Response.Error) : Type()
        data class ParameterList(val parameterList: injector.types.Response.ParameterList) : Type()
        data class ModuleInfo(val moduleInfo: injector.types.Response.ModuleInfo) : Type()
    }

    override operator fun plus(other: Response?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)

    companion object : pbandk.Message.Companion<Response> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Response.protoUnmarshalImpl(u)
    }

    data class Error(
            val type: injector.types.Response.Error.ErrorType = injector.types.Response.Error.ErrorType.fromValue(0),
            val msg: String = "",
            val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
    ) : pbandk.Message<Error> {
        override operator fun plus(other: Error?) = protoMergeImpl(other)
        override val protoSize by lazy { protoSizeImpl() }
        override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)

        companion object : pbandk.Message.Companion<Error> {
            override fun protoUnmarshal(u: pbandk.Unmarshaller) = Error.protoUnmarshalImpl(u)
        }

        data class ErrorType(override val value: Int) : pbandk.Message.Enum {
            companion object : pbandk.Message.Enum.Companion<ErrorType> {
                val UNKNOWN = ErrorType(0)
                val PATH_NOT_FOUND = ErrorType(1)
                val INVALID_VALUE = ErrorType(2)

                override fun fromValue(value: Int) = when (value) {
                    0 -> UNKNOWN
                    1 -> PATH_NOT_FOUND
                    2 -> INVALID_VALUE
                    else -> ErrorType(value)
                }
            }
        }
    }

    data class ModuleInfo(
        val paramPaths: List<String> = emptyList(),
        val enumValues: Map<String, String> = emptyMap(),
        val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
    ) : pbandk.Message<ModuleInfo> {
        override operator fun plus(other: ModuleInfo?) = protoMergeImpl(other)
        override val protoSize by lazy { protoSizeImpl() }
        override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
        companion object : pbandk.Message.Companion<ModuleInfo> {
            override fun protoUnmarshal(u: pbandk.Unmarshaller) = ModuleInfo.protoUnmarshalImpl(u)
        }

        data class EnumValuesEntry(
            override val key: String = "",
            override val value: String = "",
            val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
        ) : pbandk.Message<EnumValuesEntry>, Map.Entry<String, String> {
            override operator fun plus(other: EnumValuesEntry?) = protoMergeImpl(other)
            override val protoSize by lazy { protoSizeImpl() }
            override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
            companion object : pbandk.Message.Companion<EnumValuesEntry> {
                override fun protoUnmarshal(u: pbandk.Unmarshaller) = EnumValuesEntry.protoUnmarshalImpl(u)
            }
        }
    }

    data class ParameterList(
        val value: List<injector.types.Parameter> = emptyList(),
        val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
    ) : pbandk.Message<ParameterList> {
        override operator fun plus(other: ParameterList?) = protoMergeImpl(other)
        override val protoSize by lazy { protoSizeImpl() }
        override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
        companion object : pbandk.Message.Companion<ParameterList> {
            override fun protoUnmarshal(u: pbandk.Unmarshaller) = ParameterList.protoUnmarshalImpl(u)
        }
    }
}

private fun Parameter.protoMergeImpl(plus: Parameter?): Parameter = plus?.copy(
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Parameter.protoSizeImpl(): Int {
    var protoSize = 0
    if (path.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(path)
    if (type.value != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.enumSize(type)
    if (value.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.stringSize(value)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Parameter.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (path.isNotEmpty()) protoMarshal.writeTag(10).writeString(path)
    if (type.value != 0) protoMarshal.writeTag(16).writeEnum(type)
    if (value.isNotEmpty()) protoMarshal.writeTag(26).writeString(value)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Parameter.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Parameter {
    var path = ""
    var type: injector.types.Parameter.ParameterType = injector.types.Parameter.ParameterType.fromValue(0)
    var value = ""
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Parameter(path, type, value, protoUnmarshal.unknownFields())
        10 -> path = protoUnmarshal.readString()
        16 -> type = protoUnmarshal.readEnum(injector.types.Parameter.ParameterType.Companion)
        26 -> value = protoUnmarshal.readString()
        else -> protoUnmarshal.unknownField()
    }
}

private fun Request.protoMergeImpl(plus: Request?): Request = plus?.copy(
    args = args + plus.args,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Request.protoSizeImpl(): Int {
    var protoSize = 0
    if (type.value != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.enumSize(type)
    if (args.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(2) * args.size) + args.sumBy(pbandk.Sizer::stringSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Request.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (type.value != 0) protoMarshal.writeTag(8).writeEnum(type)
    if (args.isNotEmpty()) args.forEach { protoMarshal.writeTag(18).writeString(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Request.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Request {
    var type: injector.types.Request.RequestType = injector.types.Request.RequestType.fromValue(0)
    var args: pbandk.ListWithSize.Builder<String>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Request(type, pbandk.ListWithSize.Builder.fixed(args), protoUnmarshal.unknownFields())
        8 -> type = protoUnmarshal.readEnum(injector.types.Request.RequestType.Companion)
        18 -> args = protoUnmarshal.readRepeated(args, protoUnmarshal::readString, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Response.protoMergeImpl(plus: Response?): Response = plus?.copy(
        type = when {
            type is Response.Type.Error && plus.type is Response.Type.Error ->
                Response.Type.Error(type.error + plus.type.error)
            type is Response.Type.ParameterList && plus.type is Response.Type.ParameterList ->
                Response.Type.ParameterList(type.parameterList + plus.type.parameterList)
            type is Response.Type.ModuleInfo && plus.type is Response.Type.ModuleInfo ->
                Response.Type.ModuleInfo(type.moduleInfo + plus.type.moduleInfo)
        else ->
            plus.type ?: type
    },
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Response.protoSizeImpl(): Int {
    var protoSize = 0
    when (type) {
        is Response.Type.Error -> protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.messageSize(type.error)
        is Response.Type.ParameterList -> protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.messageSize(type.parameterList)
        is Response.Type.ModuleInfo -> protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.messageSize(type.moduleInfo)
    }
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Response.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (type is Response.Type.Error) protoMarshal.writeTag(10).writeMessage(type.error)
    if (type is Response.Type.ParameterList) protoMarshal.writeTag(18).writeMessage(type.parameterList)
    if (type is Response.Type.ModuleInfo) protoMarshal.writeTag(26).writeMessage(type.moduleInfo)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Response.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Response {
    var type: Response.Type? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Response(type, protoUnmarshal.unknownFields())
        10 -> type = Response.Type.Error(protoUnmarshal.readMessage(injector.types.Response.Error.Companion))
        18 -> type = Response.Type.ParameterList(protoUnmarshal.readMessage(injector.types.Response.ParameterList.Companion))
        26 -> type = Response.Type.ModuleInfo(protoUnmarshal.readMessage(injector.types.Response.ModuleInfo.Companion))
        else -> protoUnmarshal.unknownField()
    }
}

private fun Response.Error.protoMergeImpl(plus: Response.Error?): Response.Error = plus?.copy(
        unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Response.Error.protoSizeImpl(): Int {
    var protoSize = 0
    if (type.value != 0) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.enumSize(type)
    if (msg.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(msg)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Response.Error.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (type.value != 0) protoMarshal.writeTag(8).writeEnum(type)
    if (msg.isNotEmpty()) protoMarshal.writeTag(18).writeString(msg)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Response.Error.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Response.Error {
    var type: injector.types.Response.Error.ErrorType = injector.types.Response.Error.ErrorType.fromValue(0)
    var msg = ""
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Response.Error(type, msg, protoUnmarshal.unknownFields())
        8 -> type = protoUnmarshal.readEnum(injector.types.Response.Error.ErrorType.Companion)
        18 -> msg = protoUnmarshal.readString()
        else -> protoUnmarshal.unknownField()
    }
}

private fun Response.ModuleInfo.protoMergeImpl(plus: Response.ModuleInfo?): Response.ModuleInfo = plus?.copy(
    paramPaths = paramPaths + plus.paramPaths,
    enumValues = enumValues + plus.enumValues,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Response.ModuleInfo.protoSizeImpl(): Int {
    var protoSize = 0
    if (paramPaths.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(1) * paramPaths.size) + paramPaths.sumBy(pbandk.Sizer::stringSize)
    if (enumValues.isNotEmpty()) protoSize += pbandk.Sizer.mapSize(2, enumValues, injector.types.Response.ModuleInfo::EnumValuesEntry)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Response.ModuleInfo.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (paramPaths.isNotEmpty()) paramPaths.forEach { protoMarshal.writeTag(10).writeString(it) }
    if (enumValues.isNotEmpty()) protoMarshal.writeMap(18, enumValues, injector.types.Response.ModuleInfo::EnumValuesEntry)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Response.ModuleInfo.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Response.ModuleInfo {
    var paramPaths: pbandk.ListWithSize.Builder<String>? = null
    var enumValues: pbandk.MessageMap.Builder<String, String>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Response.ModuleInfo(pbandk.ListWithSize.Builder.fixed(paramPaths), pbandk.MessageMap.Builder.fixed(enumValues), protoUnmarshal.unknownFields())
        10 -> paramPaths = protoUnmarshal.readRepeated(paramPaths, protoUnmarshal::readString, true)
        18 -> enumValues = protoUnmarshal.readMap(enumValues, injector.types.Response.ModuleInfo.EnumValuesEntry.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Response.ModuleInfo.EnumValuesEntry.protoMergeImpl(plus: Response.ModuleInfo.EnumValuesEntry?): Response.ModuleInfo.EnumValuesEntry = plus?.copy(
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Response.ModuleInfo.EnumValuesEntry.protoSizeImpl(): Int {
    var protoSize = 0
    if (key.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(key)
    if (value.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.stringSize(value)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Response.ModuleInfo.EnumValuesEntry.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (key.isNotEmpty()) protoMarshal.writeTag(10).writeString(key)
    if (value.isNotEmpty()) protoMarshal.writeTag(18).writeString(value)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Response.ModuleInfo.EnumValuesEntry.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Response.ModuleInfo.EnumValuesEntry {
    var key = ""
    var value = ""
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Response.ModuleInfo.EnumValuesEntry(key, value, protoUnmarshal.unknownFields())
        10 -> key = protoUnmarshal.readString()
        18 -> value = protoUnmarshal.readString()
        else -> protoUnmarshal.unknownField()
    }
}

private fun Response.ParameterList.protoMergeImpl(plus: Response.ParameterList?): Response.ParameterList = plus?.copy(
    value = value + plus.value,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Response.ParameterList.protoSizeImpl(): Int {
    var protoSize = 0
    if (value.isNotEmpty()) protoSize += (pbandk.Sizer.tagSize(1) * value.size) + value.sumBy(pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Response.ParameterList.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (value.isNotEmpty()) value.forEach { protoMarshal.writeTag(10).writeMessage(it) }
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Response.ParameterList.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Response.ParameterList {
    var value: pbandk.ListWithSize.Builder<injector.types.Parameter>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Response.ParameterList(pbandk.ListWithSize.Builder.fixed(value), protoUnmarshal.unknownFields())
        10 -> value = protoUnmarshal.readRepeatedMessage(value, injector.types.Parameter.Companion, true)
        else -> protoUnmarshal.unknownField()
    }
}
