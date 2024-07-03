package nl.fontys.s3.daclothes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.fontys.s3.daclothes.business.product.*;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import nl.fontys.s3.daclothes.configuration.security.token.AccessTokenDecoder;
import nl.fontys.s3.daclothes.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.daclothes.domain.*;

import nl.fontys.s3.daclothes.persistence.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccessToken accessToken;
    @MockBean
    private AccessTokenDecoder accessTokenDecoder;
    @MockBean
    private AccessTokenEncoder accessTokenEncoder;
    @MockBean
    private CreateProductUseCase createProductUseCase;
    @MockBean
    private GetProductsUseCase getProductsUseCase;
    @MockBean
    private GetProductByIdUseCase getProductByIdUseCase;
    @MockBean
    private GetProductsByOrderIdUseCase getProductsByOrderIdUseCase;
    @MockBean
    private GetProductsByFilteringUseCase getProductsByFilteringUseCase;
    @MockBean
    private GetProductsByUserUseCase getProductsByUserUseCase;
    @MockBean
    private DeleteProductUseCase deleteProductUseCase;
    @MockBean
    private UpdateProductUseCase updateProductUseCase;
    @MockBean
    private GetProductsBySearchingUseCase getProductsBySearchingUseCase;
    @MockBean
    private ProductRepository productRepository;
    @Autowired
    private ProductController productController;

    @Test
    @WithMockUser(roles = "SELLER")

    void createProduct () throws Exception{


        GetProductsResponse.GetProductsResponseBuilder builderResult = GetProductsResponse.builder();
        GetProductsResponse buildResult = builderResult.products(new ArrayList<>()).build();
        when(getProductsUseCase.getProducts()).thenReturn(buildResult);

        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setBrand("Brand");
        createProductRequest.setCategory("Category");
        createProductRequest.setDescription("The characteristics of someone or something");
        createProductRequest.setName("Name");
        createProductRequest.setPrice(10.0d);
        createProductRequest.setProductCondition("Product Condition");
        createProductRequest.setSize("Size");
        createProductRequest.setUserId(1L);
        String content = (new ObjectMapper()).writeValueAsString(createProductRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"products\":[]}"));
    }


    @Test
    @WithMockUser
    void getProducts () throws Exception {
        GetProductsResponse response = GetProductsResponse.builder()
                .products(List.of(
                        Product.builder().id(1L).brand("Nike")
                                .description("Beautiful blue Sunglasses")
                                .name("Nike sunglasses")
                                .price(100)
                                .size("Normal")
                                .category("Accessories")
                                .productCondition("Good")
                                .build(),
                        Product.builder()
                                .id(2L)
                                .brand("Nike")
                                .category("Shoes")
                                .productCondition("Brand New")
                                .description("Really nice shoes. I don't use them anymore.")
                                .price(23.49)
                                .name("Airforce Max")
                                .size("46")
                                .build()
                )).build();
        when(getProductsUseCase.getProducts()).thenReturn(response);
        mockMvc.perform(get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
{"products": [{"id": 1, "brand": "Nike", "description": "Beautiful blue Sunglasses", "name": "Nike sunglasses", "price": 100, "size": "Normal", "category": "Accessories",
"productCondition": "Good"}, {"id": 2, "name": "Airforce Max", "description": "Really nice shoes. I don't use them anymore.", "size": "46",
"category": "Shoes", "brand": "Nike", "productCondition": "Brand New", "price": 23.49}]}
"""));
        verify(getProductsUseCase).getProducts();
        verifyNoInteractions(createProductUseCase);
    }





    @Test
    @WithMockUser
    void getProductById () throws Exception {
        GetProductsResponse response = GetProductsResponse.builder()
                .products(List.of(
                        Product.builder().id(1L).brand("Nike")
                                .description("Beautiful blue Sunglasses")
                                .name("Nike sunglasses")
                                .price(100)
                                .size("Normal")
                                .category("Accessories")
                                .productCondition("Good")
                                .build(),
                        Product.builder()
                                .id(2L)
                                .brand("Nike")
                                .category("Shoes")
                                .productCondition("Brand New")
                                .description("Really nice shoes. I don't use them anymore.")
                                .price(23.49)
                                .name("Airforce Max")
                                .size("46")
                                .build()
                )).build();
        when(getProductByIdUseCase.getProductById(2L)).thenReturn(Optional.ofNullable(response.getProducts().get(1)));
        mockMvc.perform(get("/products/2"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
{"id": 2, "name": "Airforce Max", "description": "Really nice shoes. I don't use them anymore.", "size": "46",
"category": "Shoes", "brand": "Nike", "productCondition": "Brand New", "price": 23.49}
"""));
    }
}