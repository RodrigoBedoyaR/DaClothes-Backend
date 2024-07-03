package nl.fontys.s3.daclothes.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotBlank
    @Column(name = "name")
    @Length(min = 2, max = 50)
    private String name;

    @Column(name = "description")
    @NotBlank
    @Length(min = 2, max = 200)
    private String description;

    @Column(name = "size")
    @NotBlank
    @Length(min = 1, max = 10)
    private String size;

    @NotBlank
    @Column(name = "category")
    @Length(min = 2, max = 50)
    private String category;

    @Column(name = "brand")
    @NotBlank
    @Length(min = 2, max = 50)
    private String brand;

    @Column(name = "productCondition")
    @NotBlank
    @Length(min = 2, max = 50)
    private String productCondition;

    @Column(name = "price")
    @NotNull
    @Min(1)
    private double price;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userId;

    @Column(name = "available")
    private boolean available;

    @ManyToMany(mappedBy = "order_product_list")
    private List<OrderEntity> orders;
}
