package nl.fontys.s3.daclothes.persistence.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;

@Entity
@Table(name = "user_type")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private USER_TYPE user_type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserEntity user_id;
}
