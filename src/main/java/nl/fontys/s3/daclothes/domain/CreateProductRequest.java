package nl.fontys.s3.daclothes.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String size;
    @NotNull
    private String category;
    @NotNull
    private String brand;
    @NotNull
    private String productCondition;
    @NotNull
    private double price;
    @NotNull
    private Long userId;
}
