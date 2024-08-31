package dev.dwidi.springbootreactivegrpc.service;

import dev.dwidi.springbootreactivegrpc.UserServiceGrpc;
import dev.dwidi.springbootreactivegrpc.UserServiceProto;
import dev.dwidi.springbootreactivegrpc.entity.User;
import dev.dwidi.springbootreactivegrpc.repository.UserRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserServiceProto.CreateUserRequest request, StreamObserver<UserServiceProto.CreateUserResponse> responseObserver) {
        try {
            // Check if user already exists
            if (userRepository.findByUsername(request.getUsername()).isPresent() || userRepository.findByEmail(request.getEmail()).isPresent()) {
                responseObserver.onError(
                        Status.ALREADY_EXISTS
                                .withDescription("User with this username or email already exists")
                                .asRuntimeException()
                );
                return;
            }

            // Create and save the new user
            User userToSave = new User();
            userToSave.setFullName(request.getFullName());
            userToSave.setUsername(request.getUsername());
            userToSave.setEmail(request.getEmail());
            userRepository.save(userToSave);

            // Build the response
            UserServiceProto.CreateUserResponse response = UserServiceProto.CreateUserResponse.newBuilder()
                    .setUserId(userToSave.getUserId().intValue())
                    .setUsername(userToSave.getUsername())
                    .setFullName(userToSave.getFullName())
                    .setEmail(userToSave.getEmail())
                    .build();

            // Send the response
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            // Handle unexpected errors
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Unexpected error occurred: " + e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }


    @Override
    public void getUser(UserServiceProto.GetUserRequest request, StreamObserver<UserServiceProto.GetUserResponse> responseObserver) {
        try {
            // Fetch the user by ID
            Optional<User> user = userRepository.findByUserId((long) request.getUserId());

            if (user.isPresent()) {
                User foundUser = user.get();
                // Build the response
                UserServiceProto.GetUserResponse response = UserServiceProto.GetUserResponse.newBuilder()
                        .setUserId(foundUser.getUserId().intValue())
                        .setUsername(foundUser.getUsername())
                        .setFullName(foundUser.getFullName())
                        .setEmail(foundUser.getEmail())
                        .build();
                // Send the response
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                // User not found, return an error
                responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription("User not found with ID: " + request.getUserId())
                                .asRuntimeException()
                );
            }
        } catch (Exception e) {
            // Handle any unexpected errors
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Unexpected error occurred: " + e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }


    @Override
    public void updateUser(UserServiceProto.UpdateUserRequest request, StreamObserver<UserServiceProto.UpdateUserResponse> responseObserver) {
        try {
            // Fetch the user by ID
            Optional<User> user = userRepository.findByUserId((long) request.getUserId());

            if (user.isPresent()) {
                User userToEdit = user.get();
                // Update user details
                userToEdit.setUsername(request.getUsername());
                userToEdit.setFullName(request.getFullName());
                userToEdit.setEmail(request.getEmail());

                // Save updated user details
                userRepository.save(userToEdit);

                // Build the response
                UserServiceProto.UpdateUserResponse response = UserServiceProto.UpdateUserResponse.newBuilder()
                        .setUserId(userToEdit.getUserId().intValue())
                        .setUsername(userToEdit.getUsername())
                        .setFullName(userToEdit.getFullName())
                        .setEmail(userToEdit.getEmail())
                        .build();

                // Send the response
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                // User not found, return an error
                responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription("User not found with ID: " + request.getUserId())
                                .asRuntimeException()
                );
            }
        } catch (Exception e) {
            // Handle any unexpected errors
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Unexpected error occurred: " + e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }


    @Override
    public void deleteUser(UserServiceProto.DeleteUserRequest request, StreamObserver<UserServiceProto.DeleteUserResponse> responseObserver) {
        try {
            // Fetch the user by ID
            Optional<User> user = userRepository.findByUserId((long) request.getUserId());

            if (user.isPresent()) {
                // Delete the user by ID
                userRepository.deleteById(String.valueOf(user.get().getUserId()));

                // Build the response
                UserServiceProto.DeleteUserResponse response = UserServiceProto.DeleteUserResponse.newBuilder()
                        .setUserId(request.getUserId())
                        .build();

                // Send the response
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                // User not found, return an error
                responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription("User not found with ID: " + request.getUserId())
                                .asRuntimeException()
                );
            }
        } catch (Exception e) {
            // Handle any unexpected errors
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Unexpected error occurred: " + e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }

}