package nl.fontys.s3.daclothes.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserResponse {
    private long userId;
    private long cartId;
//    private Set<Product> favouriteProducts ;

}