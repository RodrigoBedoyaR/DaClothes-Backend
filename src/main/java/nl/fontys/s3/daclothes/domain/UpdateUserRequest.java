package nl.fontys.s3.daclothes.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateUserRequest {
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String password;
//    @NotNull
//    private Set<String> userType;
//    private Set<UserProduct> favouriteProducts;
}
