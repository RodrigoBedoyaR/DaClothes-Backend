package nl.fontys.s3.daclothes.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.cart.*;
import nl.fontys.s3.daclothes.domain.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
@CrossOrigin("http://localhost:5173")
public class CartController {
    private final AddProductToCartUseCase addProductToCartUseCase;
    private final ClearCartUseCase clearCartUseCase;
    private final GetCartContentUseCase getCartContentUseCase;
    private final GetTotalPriceUseCase getTotalPriceUseCase;
    private final RemoveProductFromCartUseCase removeProductFromCartUseCase;


    @PostMapping("/{productId}")
    @RolesAllowed({"BUYER"})
    public ResponseEntity<Void> addToCart(@PathVariable(value = "productId") final long productId){
        addProductToCartUseCase.addToCart(productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{productId}")
    @RolesAllowed({"BUYER"})
    public ResponseEntity<Void> removeFromCart(@PathVariable(value = "productId") final long productId){
        removeProductFromCartUseCase.removeFromCart(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    @RolesAllowed({"BUYER"})
    public ResponseEntity<List<Product>> getCartContent(@PathVariable(value = "userId") final long userId){
        return ResponseEntity.ok(getCartContentUseCase.getCartContent(userId));
    }

    @DeleteMapping("/clear/{userId}")
    @RolesAllowed({"BUYER"})
    public ResponseEntity<Void> clearCart(@PathVariable(value = "userId") final long userId){
        clearCartUseCase.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/totalPrice/{userId}")
    @RolesAllowed({"BUYER"})
    public double getTotalPrice(@PathVariable(value = "userId") long userId){
        return getTotalPriceUseCase.totalPrice(userId);
    }
}
