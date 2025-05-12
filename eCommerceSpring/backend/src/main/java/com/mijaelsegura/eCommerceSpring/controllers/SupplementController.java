package com.mijaelsegura.eCommerceSpring.controllers;

import com.mijaelsegura.eCommerceSpring.dto.supplement.ResultSupplement;
import com.mijaelsegura.eCommerceSpring.dto.supplement.ResultSupplementList;
import com.mijaelsegura.eCommerceSpring.dto.supplement.SupplementDto;
import com.mijaelsegura.eCommerceSpring.dto.supplement.SupplementImage;
import com.mijaelsegura.eCommerceSpring.services.ISupplementService;
import com.mijaelsegura.eCommerceSpring.services.SupplementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/supplements")
public class SupplementController {
    private final ISupplementService supplementService;

    public SupplementController(ISupplementService supplementService) {
        this.supplementService = supplementService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultSupplement> GetSupplementById(@PathVariable long id) {
        ResultSupplement res = supplementService.GetSupplementById(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<SupplementImage> GetSupplementImageById(@PathVariable long id) {
        SupplementImage res = supplementService.GetSupplementImageById(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResultSupplementList> GetAllSupplements() {
        ResultSupplementList res = supplementService.GetAllSupplements();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResultSupplement> PostSupplement(@RequestPart SupplementDto supplementDto, @RequestPart MultipartFile imageFile) {
        ResultSupplement res = supplementService.PostSupplement(supplementDto, imageFile);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultSupplement> PutSupplement(@PathVariable long id, @RequestBody SupplementDto supplementDto) {
        ResultSupplement res = supplementService.PutSupplement(id, supplementDto);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<ResultSupplement> PutSupplementImage(@PathVariable long id, @RequestBody MultipartFile imageFile) {
        ResultSupplement res = supplementService.PutSupplementImage(id, imageFile);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultSupplement> DeleteSupplement(@PathVariable long id) {
        ResultSupplement res = supplementService.DeleteSupplement(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
