package nl.fontys.s3.daclothes.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.product.*;
import nl.fontys.s3.daclothes.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@CrossOrigin("http://localhost:5173")
public class ProductController {
    private final CreateProductUseCase createProductUseCase;
    private final GetProductsUseCase getProductsUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final GetProductsByFilteringUseCase getProductsByFilteringUseCase;
    private final GetProductsByUserUseCase getProductsByUserUseCase;
    private final GetProductsByOrderIdUseCase getProductsByOrderIdUseCase;
    private final GetProductsBySearchingUseCase getProductsBySearchingUseCase;



    @PostMapping
    @RolesAllowed({"SELLER"})
    public ResponseEntity<CreateProductResponse> createProduct( @RequestBody @Validated CreateProductRequest request ){
        CreateProductResponse response = createProductUseCase.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @RolesAllowed({"BUYER"})
    public ResponseEntity<GetProductsResponse> getProducts(){
        return ResponseEntity.ok(getProductsUseCase.getProducts());
    }

    @GetMapping("/filter")
    @RolesAllowed({"BUYER", "SELLER"})
    public ResponseEntity<GetProductsResponse> getProductByFiltering(@RequestParam(value = "productCondition", required = false)final String condition,
                                                                     @RequestParam(value = "size", required = false) final String size,
                                                                     @RequestParam(value = "category", required = false) final String category){
        if (getProductsByFilteringUseCase.getProductsByFilters(category, size, condition).getProducts().isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(getProductsByFilteringUseCase.getProductsByFilters(category, size, condition));
    }


    @GetMapping("/{id}")
    @RolesAllowed({"BUYER", "SELLER"})
    public ResponseEntity<Product> getProductById(@PathVariable (value = "id")final long id){
        final Optional<Product> productOptional = getProductByIdUseCase.getProductById(id);
        return productOptional.map(product -> ResponseEntity.ok().body(product)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{productId}")
    @RolesAllowed({"SELLER"})
    public ResponseEntity<Void> deleteProduct(@PathVariable long productId){
        deleteProductUseCase.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @RolesAllowed({"SELLER"})
    public ResponseEntity<Void> updateProduct(@PathVariable("id") long productId , @RequestBody @Valid UpdateProductRequest request){
        request.setId(productId);
        updateProductUseCase.updateProduct(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @RolesAllowed({"BUYER", "SELLER"})
    public ResponseEntity<GetProductsResponse> getProductsByUser(@PathVariable("userId") long userId){
        return ResponseEntity.ok(getProductsByUserUseCase.getProductsByUser(userId));
    }

    @GetMapping("/order/{orderId}")
    @RolesAllowed({"BUYER"})
    public ResponseEntity<GetProductsResponse> getProductsByOrderId(@PathVariable("orderId") long orderId){
        return ResponseEntity.ok(getProductsByOrderIdUseCase.getProductsByOrderId(orderId));
    }

    @GetMapping("/search/{keyword}")
    @RolesAllowed({"BUYER"})
    public ResponseEntity<GetProductsResponse> getProductsBySearching(@PathVariable("keyword") String keyword){
        return ResponseEntity.ok(getProductsBySearchingUseCase.getProductsBySearching(keyword));
    }
}
