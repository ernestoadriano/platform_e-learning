package com.ernesto.monolith.order.controller;

import com.ernesto.monolith.order.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService service;

    @PostMapping("/{id}/confirm")
    public void confirmPayment(@PathVariable("id") Long id) {
        service.confirmPayment(id);
    }
}
