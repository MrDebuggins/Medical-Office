package medical.idm.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: main.proto")
public final class IdentityManagementServiceGrpc {

  private IdentityManagementServiceGrpc() {}

  public static final String SERVICE_NAME = "model.IdentityManagementService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<medical.idm.proto.Main.Account,
      medical.idm.proto.Main.Token> getAuthorizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Authorize",
      requestType = medical.idm.proto.Main.Account.class,
      responseType = medical.idm.proto.Main.Token.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medical.idm.proto.Main.Account,
      medical.idm.proto.Main.Token> getAuthorizeMethod() {
    io.grpc.MethodDescriptor<medical.idm.proto.Main.Account, medical.idm.proto.Main.Token> getAuthorizeMethod;
    if ((getAuthorizeMethod = IdentityManagementServiceGrpc.getAuthorizeMethod) == null) {
      synchronized (IdentityManagementServiceGrpc.class) {
        if ((getAuthorizeMethod = IdentityManagementServiceGrpc.getAuthorizeMethod) == null) {
          IdentityManagementServiceGrpc.getAuthorizeMethod = getAuthorizeMethod = 
              io.grpc.MethodDescriptor.<medical.idm.proto.Main.Account, medical.idm.proto.Main.Token>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "model.IdentityManagementService", "Authorize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medical.idm.proto.Main.Account.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medical.idm.proto.Main.Token.getDefaultInstance()))
                  .setSchemaDescriptor(new IdentityManagementServiceMethodDescriptorSupplier("Authorize"))
                  .build();
          }
        }
     }
     return getAuthorizeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medical.idm.proto.Main.Account,
      medical.idm.proto.Main.Token> getRegisterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Register",
      requestType = medical.idm.proto.Main.Account.class,
      responseType = medical.idm.proto.Main.Token.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medical.idm.proto.Main.Account,
      medical.idm.proto.Main.Token> getRegisterMethod() {
    io.grpc.MethodDescriptor<medical.idm.proto.Main.Account, medical.idm.proto.Main.Token> getRegisterMethod;
    if ((getRegisterMethod = IdentityManagementServiceGrpc.getRegisterMethod) == null) {
      synchronized (IdentityManagementServiceGrpc.class) {
        if ((getRegisterMethod = IdentityManagementServiceGrpc.getRegisterMethod) == null) {
          IdentityManagementServiceGrpc.getRegisterMethod = getRegisterMethod = 
              io.grpc.MethodDescriptor.<medical.idm.proto.Main.Account, medical.idm.proto.Main.Token>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "model.IdentityManagementService", "Register"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medical.idm.proto.Main.Account.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medical.idm.proto.Main.Token.getDefaultInstance()))
                  .setSchemaDescriptor(new IdentityManagementServiceMethodDescriptorSupplier("Register"))
                  .build();
          }
        }
     }
     return getRegisterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medical.idm.proto.Main.Token,
      medical.idm.proto.Main.IdentityResponse> getValidateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Validate",
      requestType = medical.idm.proto.Main.Token.class,
      responseType = medical.idm.proto.Main.IdentityResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medical.idm.proto.Main.Token,
      medical.idm.proto.Main.IdentityResponse> getValidateMethod() {
    io.grpc.MethodDescriptor<medical.idm.proto.Main.Token, medical.idm.proto.Main.IdentityResponse> getValidateMethod;
    if ((getValidateMethod = IdentityManagementServiceGrpc.getValidateMethod) == null) {
      synchronized (IdentityManagementServiceGrpc.class) {
        if ((getValidateMethod = IdentityManagementServiceGrpc.getValidateMethod) == null) {
          IdentityManagementServiceGrpc.getValidateMethod = getValidateMethod = 
              io.grpc.MethodDescriptor.<medical.idm.proto.Main.Token, medical.idm.proto.Main.IdentityResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "model.IdentityManagementService", "Validate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medical.idm.proto.Main.Token.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medical.idm.proto.Main.IdentityResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new IdentityManagementServiceMethodDescriptorSupplier("Validate"))
                  .build();
          }
        }
     }
     return getValidateMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medical.idm.proto.Main.Token,
      medical.idm.proto.Main.InvalidationResponse> getInvalidateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Invalidate",
      requestType = medical.idm.proto.Main.Token.class,
      responseType = medical.idm.proto.Main.InvalidationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medical.idm.proto.Main.Token,
      medical.idm.proto.Main.InvalidationResponse> getInvalidateMethod() {
    io.grpc.MethodDescriptor<medical.idm.proto.Main.Token, medical.idm.proto.Main.InvalidationResponse> getInvalidateMethod;
    if ((getInvalidateMethod = IdentityManagementServiceGrpc.getInvalidateMethod) == null) {
      synchronized (IdentityManagementServiceGrpc.class) {
        if ((getInvalidateMethod = IdentityManagementServiceGrpc.getInvalidateMethod) == null) {
          IdentityManagementServiceGrpc.getInvalidateMethod = getInvalidateMethod = 
              io.grpc.MethodDescriptor.<medical.idm.proto.Main.Token, medical.idm.proto.Main.InvalidationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "model.IdentityManagementService", "Invalidate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medical.idm.proto.Main.Token.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medical.idm.proto.Main.InvalidationResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new IdentityManagementServiceMethodDescriptorSupplier("Invalidate"))
                  .build();
          }
        }
     }
     return getInvalidateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static IdentityManagementServiceStub newStub(io.grpc.Channel channel) {
    return new IdentityManagementServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static IdentityManagementServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new IdentityManagementServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static IdentityManagementServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new IdentityManagementServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class IdentityManagementServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void authorize(medical.idm.proto.Main.Account request,
        io.grpc.stub.StreamObserver<medical.idm.proto.Main.Token> responseObserver) {
      asyncUnimplementedUnaryCall(getAuthorizeMethod(), responseObserver);
    }

    /**
     */
    public void register(medical.idm.proto.Main.Account request,
        io.grpc.stub.StreamObserver<medical.idm.proto.Main.Token> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterMethod(), responseObserver);
    }

    /**
     */
    public void validate(medical.idm.proto.Main.Token request,
        io.grpc.stub.StreamObserver<medical.idm.proto.Main.IdentityResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getValidateMethod(), responseObserver);
    }

    /**
     */
    public void invalidate(medical.idm.proto.Main.Token request,
        io.grpc.stub.StreamObserver<medical.idm.proto.Main.InvalidationResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getInvalidateMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getAuthorizeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                medical.idm.proto.Main.Account,
                medical.idm.proto.Main.Token>(
                  this, METHODID_AUTHORIZE)))
          .addMethod(
            getRegisterMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                medical.idm.proto.Main.Account,
                medical.idm.proto.Main.Token>(
                  this, METHODID_REGISTER)))
          .addMethod(
            getValidateMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                medical.idm.proto.Main.Token,
                medical.idm.proto.Main.IdentityResponse>(
                  this, METHODID_VALIDATE)))
          .addMethod(
            getInvalidateMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                medical.idm.proto.Main.Token,
                medical.idm.proto.Main.InvalidationResponse>(
                  this, METHODID_INVALIDATE)))
          .build();
    }
  }

  /**
   */
  public static final class IdentityManagementServiceStub extends io.grpc.stub.AbstractStub<IdentityManagementServiceStub> {
    private IdentityManagementServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private IdentityManagementServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected IdentityManagementServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new IdentityManagementServiceStub(channel, callOptions);
    }

    /**
     */
    public void authorize(medical.idm.proto.Main.Account request,
        io.grpc.stub.StreamObserver<medical.idm.proto.Main.Token> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAuthorizeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void register(medical.idm.proto.Main.Account request,
        io.grpc.stub.StreamObserver<medical.idm.proto.Main.Token> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void validate(medical.idm.proto.Main.Token request,
        io.grpc.stub.StreamObserver<medical.idm.proto.Main.IdentityResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getValidateMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void invalidate(medical.idm.proto.Main.Token request,
        io.grpc.stub.StreamObserver<medical.idm.proto.Main.InvalidationResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getInvalidateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class IdentityManagementServiceBlockingStub extends io.grpc.stub.AbstractStub<IdentityManagementServiceBlockingStub> {
    private IdentityManagementServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private IdentityManagementServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected IdentityManagementServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new IdentityManagementServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public medical.idm.proto.Main.Token authorize(medical.idm.proto.Main.Account request) {
      return blockingUnaryCall(
          getChannel(), getAuthorizeMethod(), getCallOptions(), request);
    }

    /**
     */
    public medical.idm.proto.Main.Token register(medical.idm.proto.Main.Account request) {
      return blockingUnaryCall(
          getChannel(), getRegisterMethod(), getCallOptions(), request);
    }

    /**
     */
    public medical.idm.proto.Main.IdentityResponse validate(medical.idm.proto.Main.Token request) {
      return blockingUnaryCall(
          getChannel(), getValidateMethod(), getCallOptions(), request);
    }

    /**
     */
    public medical.idm.proto.Main.InvalidationResponse invalidate(medical.idm.proto.Main.Token request) {
      return blockingUnaryCall(
          getChannel(), getInvalidateMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class IdentityManagementServiceFutureStub extends io.grpc.stub.AbstractStub<IdentityManagementServiceFutureStub> {
    private IdentityManagementServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private IdentityManagementServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected IdentityManagementServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new IdentityManagementServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medical.idm.proto.Main.Token> authorize(
        medical.idm.proto.Main.Account request) {
      return futureUnaryCall(
          getChannel().newCall(getAuthorizeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medical.idm.proto.Main.Token> register(
        medical.idm.proto.Main.Account request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medical.idm.proto.Main.IdentityResponse> validate(
        medical.idm.proto.Main.Token request) {
      return futureUnaryCall(
          getChannel().newCall(getValidateMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medical.idm.proto.Main.InvalidationResponse> invalidate(
        medical.idm.proto.Main.Token request) {
      return futureUnaryCall(
          getChannel().newCall(getInvalidateMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_AUTHORIZE = 0;
  private static final int METHODID_REGISTER = 1;
  private static final int METHODID_VALIDATE = 2;
  private static final int METHODID_INVALIDATE = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final IdentityManagementServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(IdentityManagementServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_AUTHORIZE:
          serviceImpl.authorize((medical.idm.proto.Main.Account) request,
              (io.grpc.stub.StreamObserver<medical.idm.proto.Main.Token>) responseObserver);
          break;
        case METHODID_REGISTER:
          serviceImpl.register((medical.idm.proto.Main.Account) request,
              (io.grpc.stub.StreamObserver<medical.idm.proto.Main.Token>) responseObserver);
          break;
        case METHODID_VALIDATE:
          serviceImpl.validate((medical.idm.proto.Main.Token) request,
              (io.grpc.stub.StreamObserver<medical.idm.proto.Main.IdentityResponse>) responseObserver);
          break;
        case METHODID_INVALIDATE:
          serviceImpl.invalidate((medical.idm.proto.Main.Token) request,
              (io.grpc.stub.StreamObserver<medical.idm.proto.Main.InvalidationResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class IdentityManagementServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    IdentityManagementServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return medical.idm.proto.Main.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("IdentityManagementService");
    }
  }

  private static final class IdentityManagementServiceFileDescriptorSupplier
      extends IdentityManagementServiceBaseDescriptorSupplier {
    IdentityManagementServiceFileDescriptorSupplier() {}
  }

  private static final class IdentityManagementServiceMethodDescriptorSupplier
      extends IdentityManagementServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    IdentityManagementServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (IdentityManagementServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new IdentityManagementServiceFileDescriptorSupplier())
              .addMethod(getAuthorizeMethod())
              .addMethod(getRegisterMethod())
              .addMethod(getValidateMethod())
              .addMethod(getInvalidateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
