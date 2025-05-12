package com.mijaelsegura.eCommerceSpring.services;

import com.mijaelsegura.eCommerceSpring.dto.purchase.PurchaseDetailDto;
import com.mijaelsegura.eCommerceSpring.dto.purchase.PurchaseDto;
import com.mijaelsegura.eCommerceSpring.dto.purchase.ResultPurchase;
import com.mijaelsegura.eCommerceSpring.dto.purchase.ResultPurchaseList;
import com.mijaelsegura.eCommerceSpring.exceptions.PropertyValidationException;
import com.mijaelsegura.eCommerceSpring.exceptions.ResourceNotFoundException;
import com.mijaelsegura.eCommerceSpring.models.*;

import com.mijaelsegura.eCommerceSpring.repositories.IPurchaseRepository;
import com.mijaelsegura.eCommerceSpring.repositories.ISupplementRepository;
import com.mijaelsegura.eCommerceSpring.repositories.IUserRepository;
import com.mijaelsegura.eCommerceSpring.utils.results.GenerateResultPurchase;

import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class PurchaseService implements IPurchaseService {

    private final IPurchaseRepository purchaseRepository;
    private final IUserRepository userRepository;
    private final ISupplementRepository supplementRepository;
    private final GenerateResultPurchase resultGenerator;
    private final CartService cartService;

    public PurchaseService(IPurchaseRepository purchaseRepository, IUserRepository userRepository, ISupplementRepository supplementRepository, GenerateResultPurchase resultGenerator, CartService cartService){
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.supplementRepository = supplementRepository;
        this.resultGenerator = resultGenerator;
        this.cartService = cartService;
    }

    @Override
    public ResultPurchase GetPurchaseById(long id) {
        Optional<Purchase> purchaseOptional = purchaseRepository.findById(id);
        if (purchaseOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found any purchase with ID: " + id);
        }
        Purchase purchase = purchaseOptional.get();
        return resultGenerator.getSuccessResult(new PurchaseDto(purchase.getId(), purchase.getTotalPrice(), purchase.getPurchaseDate(), purchase.getUser().getDNI()));
    }

    @Override
    public ResultPurchaseList GetAllPurchases() {
        ResultPurchaseList res = new ResultPurchaseList();
        List<Purchase> purchases = purchaseRepository.findAll();
        if (purchases.isEmpty()) {
            throw new ResourceNotFoundException("Not found any purchases");
        }
        res.setMessage("");
        res.setSuccess(true);
        res.setPurchases(purchases.stream().map(purchase -> new PurchaseDto(purchase.getId(), purchase.getTotalPrice(), purchase.getPurchaseDate(), purchase.getUser().getDNI())).toList());
        res.setTypeError("");
        return res;
    }

    @Override
    public ResultPurchase PostPurchase(PurchaseDto purchaseDto) {
        String propertyValidation = purchaseDto.validateAllProperties();
        if (!Objects.equals(propertyValidation, "")) {
            throw new PropertyValidationException("Could not validate purchase: " + propertyValidation);
        }
        Optional<User> userOptional = userRepository.findById(purchaseDto.getUserDNI());
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found user with DNI " + purchaseDto.getUserDNI());
        }
        User user = userOptional.get();
        Purchase purchase = new Purchase();
        purchase.setPurchaseDate(purchaseDto.getPurchaseDate());
        purchase.setTotalPrice(purchaseDto.getTotalPrice());
        purchase.setUser(user);
        purchaseRepository.save(purchase);
        List<PurchaseDetail> purchaseDetails = new ArrayList<>();
        for (PurchaseDetailDto detailDto : purchaseDto.getPurchaseDetails()) {
            Optional<Supplement> supplementOptional = supplementRepository.findById(detailDto.getSupplementId());
            PurchaseDetail detail = getPurchaseDetail(detailDto, supplementOptional, purchase);
            purchaseDetails.add(detail);
        }
        purchase.setPurchaseDetails(purchaseDetails);
        purchaseRepository.save(purchase);
        purchaseDto.setId(purchase.getId());
        for (PurchaseDetailDto purchaseDetail : purchaseDto.getPurchaseDetails()) {
            purchaseDetail.setPurchaseId(purchase.getId());
        }
        cartService.emptyCartAfterPurchase(user.getDNI());
        return resultGenerator.getSuccessResult(purchaseDto);
    }

    private static PurchaseDetail getPurchaseDetail(PurchaseDetailDto detailDto, Optional<Supplement> supplementOptional, Purchase purchase) {
        if (supplementOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found supplement with ID: " + detailDto.getSupplementId() + " to buy.");
        }
        Supplement supplement = supplementOptional.get();
        IdPurchaseDetail idPurchaseDetail = new IdPurchaseDetail();
        idPurchaseDetail.setPurchaseId(purchase.getId());
        idPurchaseDetail.setSupplementId(supplement.getId());
        PurchaseDetail detail = new PurchaseDetail();
        detail.setId(idPurchaseDetail);
        detail.setSupplement(supplement);
        detail.setPurchase(purchase);
        detail.setQuantity(detailDto.getQuantity());
        return detail;
    }

    @Override
    public ResultPurchase DeletePurchase(long id) {
        Optional<Purchase> purchaseOptional = purchaseRepository.findById(id);
        if (purchaseOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found any purchase with ID: " + id + " to delete");
        }
        Purchase purchase = purchaseOptional.get();
        purchaseRepository.delete(purchase);
        return resultGenerator.getSuccessResult(new PurchaseDto(purchase.getId(), purchase.getTotalPrice(), purchase.getPurchaseDate(), purchase.getUser().getDNI()));
    }
}
