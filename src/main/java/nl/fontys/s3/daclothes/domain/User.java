package nl.fontys.s3.daclothes.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long id;
    private String name;
    private String email;
    private String password;
    private Set<USER_TYPE> type;
    private Cart cart;
    private List<Order> orderList;

//    private Set<UserProduct> favouriteProducts;
}
