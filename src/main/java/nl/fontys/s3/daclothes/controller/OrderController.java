package nl.fontys.s3.daclothes.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import nl.fontys.s3.daclothes.business.order.CreateOrderUseCase;
import nl.fontys.s3.daclothes.business.order.GetOrdersFromUserUseCase;
import nl.fontys.s3.daclothes.domain.CreateOrderResponse;
import nl.fontys.s3.daclothes.domain.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
@CrossOrigin("http://localhost:5173")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrdersFromUserUseCase getOrdersFromUserUseCase;

    @PostMapping("/create")
    @RolesAllowed({"BUYER"})
    public ResponseEntity<CreateOrderResponse> createOrder(){
        CreateOrderResponse response = createOrderUseCase.createOrder();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get")
    @RolesAllowed({"BUYER"})
    public List<Order> getOrdersByUser(){
        return getOrdersFromUserUseCase.getOrdersByUser();
    }

}
