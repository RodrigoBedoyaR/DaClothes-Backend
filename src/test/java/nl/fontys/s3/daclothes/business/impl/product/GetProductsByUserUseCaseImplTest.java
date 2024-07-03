package nl.fontys.s3.daclothes.business.impl.product;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import nl.fontys.s3.daclothes.business.exceptions.UserNotFoundException;
import nl.fontys.s3.daclothes.domain.GetProductsResponse;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import nl.fontys.s3.daclothes.persistence.UserRepository;
import nl.fontys.s3.daclothes.persistence.entity.CartEntity;
import nl.fontys.s3.daclothes.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {GetProductsByUserUseCaseImpl.class})
@ExtendWith(SpringExtension.class)
class GetProductsByUserUseCaseImplTest {
    @Autowired
    private GetProductsByUserUseCaseImpl getProductsByUserUseCaseImpl;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testGetProductsByUser () {
        CartEntity cart = new CartEntity();
        cart.setCartProductList(new ArrayList<>());
        cart.setId(1L);
        cart.setTotalPrice(10.0d);

        UserEntity userEntity = new UserEntity();
        userEntity.setCart(cart);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setName("Name");
        userEntity.setOrders(new ArrayList<>());
        userEntity.setPassword("iloveyou");
        userEntity.setType(new HashSet<>());
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(productRepository.findAllByUserId_Id(anyLong())).thenReturn(new ArrayList<>());

        GetProductsResponse actualProductsByUser = getProductsByUserUseCaseImpl.getProductsByUser(1L);

        verify(productRepository).findAllByUserId_Id(anyLong());
        verify(userRepository).findById(Mockito.<Long>any());
        assertTrue(actualProductsByUser.getProducts().isEmpty());
    }


    @Test
    void testGetProductsByUser_throwsUserNotFoundException () {
        // Arrange
        Optional<UserEntity> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> getProductsByUserUseCaseImpl.getProductsByUser(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }
}
