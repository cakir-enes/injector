package injector

import com.google.protobuf.Empty
import io.grpc.Channel
import io.grpc.Context
import io.grpc.ManagedChannelBuilder
import io.grpc.injector.InjectorGrpc
import io.grpc.injector.MultiGetReq

class Client(val address: String) {

    val channel: Channel
    val stub: InjectorGrpc.InjectorBlockingStub

    init {
        channel = ManagedChannelBuilder.forTarget(address).usePlaintext().build()
        stub = InjectorGrpc.newBlockingStub(channel)
    }

    fun multigetParameters(paths: List<String>, callback: (String, String) -> Unit):  () -> Boolean {
        val ctx = Context.current().withCancellation()
        val req = MultiGetReq.newBuilder().addAllParamPaths(paths). build()
        ctx.run {
            stub.multiGet(req).forEach {
                it.pairsList.forEach{ callback(it.path, it.value)}
            }
        }
        val cancelFunc = { ctx.cancel(InterruptedException("Cancelled")) }
        return cancelFunc
    }

    fun getParameterInfos() = stub.getParameterInfos(Empty.getDefaultInstance())
}


