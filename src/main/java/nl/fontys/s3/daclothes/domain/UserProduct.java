package nl.fontys.s3.daclothes.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProduct {
    private Long id;
    private User user;
    private Product product;
}
