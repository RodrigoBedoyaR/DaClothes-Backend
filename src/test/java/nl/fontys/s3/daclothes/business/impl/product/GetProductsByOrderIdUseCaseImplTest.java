package nl.fontys.s3.daclothes.business.impl.product;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.domain.GetProductsResponse;
import nl.fontys.s3.daclothes.persistence.OrderRepository;
import nl.fontys.s3.daclothes.persistence.entity.OrderEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {GetProductsByOrderIdUseCaseImpl.class})
@ExtendWith(SpringExtension.class)
class GetProductsByOrderIdUseCaseImplTest {
    @MockBean
    private AccessToken accessToken;

    @Autowired
    private GetProductsByOrderIdUseCaseImpl getProductsByOrderIdUseCaseImpl;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    void testGetProductsByOrderId_throwsError () {
        when(accessToken.getRoles()).thenReturn(new HashSet<>());

        assertThrows(UnauthorizedDataAccessException.class, () -> getProductsByOrderIdUseCaseImpl.getProductsByOrderId(1L));
        verify(accessToken).getRoles();
    }

    @Test
    void testGetProductsByOrderId () {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreated_at(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        orderEntity.setId(1L);
        orderEntity.setOrder_product_list(new ArrayList<>());
        orderEntity.setTotal_price(10.0d);
        Optional<OrderEntity> ofResult = Optional.of(orderEntity);
        when(orderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        HashSet<String> stringSet = new HashSet<>();
        stringSet.add("PLEASE_LOGIN");
        when(accessToken.getRoles()).thenReturn(stringSet);

        GetProductsResponse actualProductsByOrderId = getProductsByOrderIdUseCaseImpl.getProductsByOrderId(1L);

        verify(accessToken).getRoles();
        verify(orderRepository).findById(Mockito.<Long>any());
        assertTrue(actualProductsByOrderId.getProducts().isEmpty());
    }

}
