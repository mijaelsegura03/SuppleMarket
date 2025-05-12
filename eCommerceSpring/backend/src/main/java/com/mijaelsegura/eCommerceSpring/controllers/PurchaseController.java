package com.mijaelsegura.eCommerceSpring.controllers;


import com.mijaelsegura.eCommerceSpring.dto.purchase.PurchaseDto;
import com.mijaelsegura.eCommerceSpring.dto.purchase.ResultPurchase;
import com.mijaelsegura.eCommerceSpring.dto.purchase.ResultPurchaseList;
import com.mijaelsegura.eCommerceSpring.services.IPurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/purchases")
public class PurchaseController {
    private final IPurchaseService purchaseService;

    public PurchaseController(IPurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultPurchase> GetPurchaseById(@PathVariable long id) {
        ResultPurchase res = purchaseService.GetPurchaseById(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResultPurchaseList> GetAllPurchases() {
        ResultPurchaseList res = purchaseService.GetAllPurchases();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResultPurchase> PostPurchase(@RequestBody PurchaseDto purchaseDto) {
        ResultPurchase res = purchaseService.PostPurchase(purchaseDto);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultPurchase> DeletePurchase(@PathVariable long id) {
        ResultPurchase res = purchaseService.DeletePurchase(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
