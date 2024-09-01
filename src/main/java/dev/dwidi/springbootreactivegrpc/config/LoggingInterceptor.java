package dev.dwidi.springbootreactivegrpc.config;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingInterceptor implements ServerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        logger.info("Received call: {}", call.getMethodDescriptor().getFullMethodName());

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(
                next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
                    @Override
                    public void sendMessage(RespT message) {
                        logger.info("Response: {}", message);
                        super.sendMessage(message);
                    }

                    @Override
                    public void close(Status status, Metadata trailers) {
                        logger.info("Call completed with status: {}", status);
                        super.close(status, trailers);
                    }
                }, headers)) {
            @Override
            public void onMessage(ReqT message) {
                logger.info("Request: {}", message);
                super.onMessage(message);
            }
        };
    }
}
