package nl.fontys.s3.daclothes.business.impl.converters;

import nl.fontys.s3.daclothes.domain.User;
import nl.fontys.s3.daclothes.domain.enums.USER_TYPE;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class UserConverter {
    private UserConverter() {
    }
    public static User convert( UserEntity entity){
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .type(entity.getType().stream()
                        .map(userTypeEntity -> USER_TYPE.valueOf(userTypeEntity.getUser_type().toString()))
                                .collect(Collectors.toSet()))
                .cart(CartConverter.convert(entity.getCart()))
                .orderList(List.copyOf(entity.getOrders().stream().map(OrderConverter::convert).toList()))
                .build();
    }
}
