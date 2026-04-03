package jung.grpc;

import io.grpc.stub.StreamObserver;
import jung.api.grpc.UserProto;
import jung.api.grpc.UserProto.UserRequest;   // proto 생성 클래스
import jung.api.grpc.UserProto.UserResponse;  // proto 생성 클래스
import jung.api.grpc.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse response = UserResponse.newBuilder()
               .setUserId(request.getUserId())
                .setName("John Doe")
                .setEmail("email is special text")
                .build();
        responseObserver.onNext(response);  // 응답 전송
        responseObserver.onCompleted();
    }

    @Override
    public void listUsers(UserProto.Empty request, StreamObserver<UserResponse> responseObserver) {
        super.listUsers(request, responseObserver);
    }

}
