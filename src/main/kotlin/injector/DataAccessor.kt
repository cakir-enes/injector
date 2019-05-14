/*
package injector

import arrow.core.Either
import arrow.core.Try
import injector.types.Request
import injector.types.Request.RequestType
import injector.types.Response
import injector.types.Response.Error.ErrorType
import injector.types.Response.Type
import io.nats.client.*
import mu.KotlinLogging
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

sealed class DomainError(val msg: String, val cause: Throwable = Throwable()) {
    class GeneralError(msg: String, cause: Throwable = Throwable()) : DomainError(msg, cause)
    class TimeoutError(msg: String, cause: Throwable) : DomainError(msg, cause)
    class WrongReturnTypeError(msg: String) : DomainError(msg)
    class InvalidValueError(msg: String) : DomainError(msg)
    class PathNotFoundError(msg: String) : DomainError(msg)
}

class DataAccessor(address: String, module: String) {
    private val logger = KotlinLogging.logger {}
    private val nc: Connection
    private val module: String

    init {
        val options = Options.Builder()
                .server(address)
                .connectionListener { conn, type -> logger.debug { "$conn and $type" } }
                .errorListener(object : ErrorListener {
                    override fun errorOccurred(conn: Connection?, error: String?) {
                        logger.error { "[$module] - Something went wrong, error: $error" }
                    }

                    override fun exceptionOccurred(conn: Connection?, exp: Exception?) {
                        logger.debug { "Exception Thrown! $exp" }
                    }

                    override fun slowConsumerDetected(conn: Connection?, consumer: Consumer?) {
                        logger.info { "Slow Consumer $consumer" }
                    }
                })
                .build()
        this.nc = Nats.connect(options)
        this.module = module
    }


    fun fetchModuleInfo(): Either<DomainError, Response.ModuleInfo> {
        val req = Request(RequestType.DISCOVER)

        return Try {
            val resp = nc.request(module, req.protoMarshal()).get(2, TimeUnit.SECONDS)
            return when (val msg = Response.protoUnmarshal(resp.data).type) {
                is Type.ModuleInfo -> Either.right(msg.moduleInfo)
                is Type.Error -> Either.left(mapError(msg.error))
                else -> Either.left(DomainError.WrongReturnTypeError("Fetch Module Wrong Return Type!"))
            }
        }.toEither {
            mapException(it, "Fetch Module")
        }
    }

    fun fetchParameters(paths: List<String>): Either<DomainError, Response.ParameterList> {
        val req = Request(RequestType.MULTIGET, paths)
        return Try {
            val resp = nc.request(module, req.protoMarshal()).get(2, TimeUnit.SECONDS)
            return when (val msg = Response.protoUnmarshal(resp.data).type) {
                is Type.ParameterList -> Either.right(msg.parameterList)
                is Type.Error -> Either.left(mapError(msg.error))
                else -> Either.left(DomainError.WrongReturnTypeError("Fetch Parameters Wrong Return Type"))
            }
        }.toEither {
            mapException(it, "Fetch Parameters")
        }
    }

//    private fun<T : Type> findReturnValue(reqType: RequestType, respType: T): Either<DomainError, T> {
//        val req = Request(reqType)
//
//        return Try {
//            val resp = nc.request(module, req.protoMarshal()).get(2, TimeUnit.SECONDS)
//            return when (val msg = Response.protoUnmarshal(resp.data).type) {
//                is Type.ModuleInfo -> Either.right(msg.moduleInfo)
//                is Type.Error -> Either.left(mapError(msg.error))
//                else -> Either.left(DomainError.WrongReturnTypeError("Fetch Module Wrong Return Type!"))
//            }
//        }.toEither { mapException(it, "Fetch Module") }
//
//
//    }

    private fun mapError(error: Response.Error): DomainError {
        return when (error.type) {
            ErrorType.INVALID_VALUE -> DomainError.InvalidValueError(error.msg)
            ErrorType.PATH_NOT_FOUND -> DomainError.PathNotFoundError(error.msg)
            ErrorType.UNKNOWN -> DomainError.GeneralError(error.msg)
            else -> DomainError.GeneralError("Shouldnt be here")
        }
    }

    private fun mapException(ex: Throwable, caller: String): DomainError {
        return when (ex) {
            is TimeoutException -> DomainError.TimeoutError("$caller Info Timeout", ex)
            else -> DomainError.GeneralError("$caller General Error")
        }
    }

}*/
