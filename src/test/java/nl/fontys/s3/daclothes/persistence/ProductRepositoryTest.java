//package nl.fontys.s3.daclothes.Persistence;
//
//import nl.fontys.s3.daclothes.Persistence.Entity.ProductEntity;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@DataJpaTest
//class ProductRepositoryTest {
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private TestEntityManager entityManager;
//
//
//    @Test
//    void save_shouldSaveProductsWithALlFields(){
//
//        ProductEntity product = ProductEntity.builder()
//                .brand("Nike")
//                .description("Beautiful blue Sunglasses")
//                .name("Nike sunglasses")
//                .price(100)
//                .size("Normal")
//                .category("Accessories")
//                .productCondition("Good")
//                .build();
//        ProductEntity savedProduct = productRepository.save(product);
//        assertNotNull(savedProduct);
//
//        savedProduct = entityManager.find(ProductEntity.class, savedProduct.getId());
//        ProductEntity expectedProduct = ProductEntity.builder()
//                .id(1)
//                .brand("Nike")
//                .description("Beautiful blue Sunglasses")
//                .name("Nike sunglasses")
//                .price(100)
//                .size("Normal")
//                .category("Accessories")
//                .productCondition("Good")
//                .build();
//        assertEquals(1, productRepository.findAll().size());
//        assertEquals(expectedProduct.getName(),savedProduct.getName());
//        assertEquals(expectedProduct.getProductCondition(),savedProduct.getProductCondition());
//        assertEquals(expectedProduct.getPrice(),savedProduct.getPrice());
//        assertEquals(expectedProduct.getBrand(),savedProduct.getBrand());
//
//    }
//
//    @Test
//    void deleteById(){
//        ProductEntity product = ProductEntity.builder()
//                .brand("Nike")
//                .description("Beautiful blue Sunglasses")
//                .name("Nike sunglasses")
//                .price(100)
//                .size("Normal")
//                .category("Accessories")
//                .productCondition("Good")
//                .build();
//        ProductEntity savedProduct = productRepository.save(product);
//        productRepository.deleteById(savedProduct.getId());
//        assertFalse(productRepository.findById(savedProduct.getId()).isPresent());
//
//
//    }
//
//    @Test
//    void findByCategory () {
//        ProductEntity product = ProductEntity.builder()
//                .brand("Nike")
//                .description("Beautiful blue Sunglasses")
//                .name("Nike sunglasses")
//                .price(100)
//                .size("Normal")
//                .category("Accessories")
//                .productCondition("Good")
//                .build();
//        ProductEntity savedProduct = productRepository.save(product);
//        ProductEntity expectedProduct = productRepository.findByCategory("Accessories").get(0);
//        assertEquals(savedProduct, expectedProduct);
//    }
//
//    @Test
//    void findBySize () {
//        ProductEntity product = ProductEntity.builder()
//                .brand("Nike")
//                .description("Beautiful blue Sunglasses")
//                .name("Nike sunglasses")
//                .price(100)
//                .size("Normal")
//                .category("Accessories")
//                .productCondition("Good")
//                .build();
//        ProductEntity savedProduct = productRepository.save(product);
//        ProductEntity expectedProduct = productRepository.findBySize("Normal").get(0);
//        assertEquals(savedProduct, expectedProduct);
//    }
//
//    @Test
//    void findByProductCondition () {
//        ProductEntity product = ProductEntity.builder()
//                .brand("Nike")
//                .description("Beautiful blue Sunglasses")
//                .name("Nike sunglasses")
//                .price(100)
//                .size("Normal")
//                .category("Accessories")
//                .productCondition("Good")
//                .build();
//        ProductEntity savedProduct = productRepository.save(product);
//        ProductEntity expectedProduct = productRepository.findByProductCondition("Good").get(0);
//        assertEquals(savedProduct, expectedProduct);
//    }
//
//    @Test
//    void findByCategoryAndProductConditionAndSize () {
//        ProductEntity product = ProductEntity.builder()
//                .brand("Nike")
//                .description("Beautiful blue Sunglasses")
//                .name("Nike sunglasses")
//                .price(100)
//                .size("Normal")
//                .category("Accessories")
//                .productCondition("Good")
//                .build();
//        ProductEntity savedProduct = productRepository.save(product);
//        ProductEntity expectedProduct = productRepository.findByCategoryAndProductConditionAndSize("Accessories", "Good", "Normal").get(0);
//        assertEquals(savedProduct, expectedProduct);
//    }
//
//    @Test
//    void findByProductConditionAndCategory () {
//        ProductEntity product = ProductEntity.builder()
//                .brand("Nike")
//                .description("Beautiful blue Sunglasses")
//                .name("Nike sunglasses")
//                .price(100)
//                .size("Normal")
//                .category("Accessories")
//                .productCondition("Good")
//                .build();
//        ProductEntity savedProduct = productRepository.save(product);
//        ProductEntity expectedProduct = productRepository.findByProductConditionAndCategory("Good", "Accessories").get(0);
//        assertEquals(savedProduct, expectedProduct);
//    }
//
//    @Test
//    void findByCategoryAndSize () {
//        ProductEntity product = ProductEntity.builder()
//                .brand("Nike")
//                .description("Beautiful blue Sunglasses")
//                .name("Nike sunglasses")
//                .price(100)
//                .size("Normal")
//                .category("Accessories")
//                .productCondition("Good")
//                .build();
//        ProductEntity savedProduct = productRepository.save(product);
//        ProductEntity expectedProduct = productRepository.findByCategoryAndSize("Accessories", "Normal").get(0);
//        assertEquals(savedProduct, expectedProduct);
//    }
//
//    @Test
//    void findByProductConditionAndSize () {
//        ProductEntity product = ProductEntity.builder()
//                .brand("Nike")
//                .description("Beautiful blue Sunglasses")
//                .name("Nike sunglasses")
//                .price(100)
//                .size("Normal")
//                .category("Accessories")
//                .productCondition("Good")
//                .build();
//        ProductEntity savedProduct = productRepository.save(product);
//        ProductEntity expectedProduct = productRepository.findByProductConditionAndSize("Good", "Normal").get(0);
//        assertEquals(savedProduct, expectedProduct);
//    }
//}