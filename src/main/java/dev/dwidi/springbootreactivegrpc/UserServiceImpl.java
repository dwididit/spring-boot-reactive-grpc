package dev.dwidi.springbootreactivegrpc;
import io.grpc.stub.StreamObserver;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void getUser(UserServiceProto.GetUserRequest request, StreamObserver<UserServiceProto.GetUserResponse> responseObserver) {
        UserServiceProto.GetUserResponse response = UserServiceProto.GetUserResponse.newBuilder()
                .setUserId(request.getUserId())
                .setFullName("John Doe")
                .setUsername("johndoe")
                .setEmail("johndoe@example.com")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(UserServiceProto.CreateUserRequest request, StreamObserver<UserServiceProto.CreateUserResponse> responseObserver) {
        UserServiceProto.CreateUserResponse response = UserServiceProto.CreateUserResponse.newBuilder()
                .setUserId(1)
                .setFullName(request.getFullName())
                .setUsername(request.getUsername())
                .setEmail(request.getEmail())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UserServiceProto.UpdateUserRequest request, StreamObserver<UserServiceProto.UpdateUserResponse> responseObserver) {
        UserServiceProto.UpdateUserResponse response = UserServiceProto.UpdateUserResponse.newBuilder()
                .setUserId(request.getUserId())
                .setFullName("Updated Name")
                .setUsername("updatedusername")
                .setEmail("updated.email@example.com")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteUser(UserServiceProto.DeleteUserRequest request, StreamObserver<UserServiceProto.DeleteUserResponse> responseObserver) {
        UserServiceProto.DeleteUserResponse response = UserServiceProto.DeleteUserResponse.newBuilder()
                .setUserId(request.getUserId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}