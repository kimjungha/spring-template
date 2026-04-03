package jung.api.login.service;

import jung.api.grpc.UserProto;
import jung.api.grpc.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserClientService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    public UserProto.UserResponse getUser(long userId) {
        return userStub.getUser(
                UserProto.UserRequest.newBuilder().setUserId(userId).build()
        );
    }
}
