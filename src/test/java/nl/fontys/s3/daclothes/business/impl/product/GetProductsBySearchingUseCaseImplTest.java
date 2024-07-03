package nl.fontys.s3.daclothes.business.impl.product;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import nl.fontys.s3.daclothes.domain.GetProductsResponse;
import nl.fontys.s3.daclothes.persistence.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {GetProductsBySearchingUseCaseImpl.class})
@ExtendWith(SpringExtension.class)
class GetProductsBySearchingUseCaseImplTest {
    @Autowired
    private GetProductsBySearchingUseCaseImpl getProductsBySearchingUseCaseImpl;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void testGetProductsBySearching () {
        when(productRepository.findAllByNameOrDescriptionOrUserNameContainingIgnoreCase(Mockito.any()))
                .thenReturn(new ArrayList<>());

        GetProductsResponse actualProductsBySearching = getProductsBySearchingUseCaseImpl.getProductsBySearching("Search");

        verify(productRepository).findAllByNameOrDescriptionOrUserNameContainingIgnoreCase(Mockito.any());
        assertTrue(actualProductsBySearching.getProducts().isEmpty());
    }
}
