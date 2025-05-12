package com.mijaelsegura.eCommerceSpring.services;

import com.mijaelsegura.eCommerceSpring.dto.supplement.ResultSupplement;
import com.mijaelsegura.eCommerceSpring.dto.supplement.ResultSupplementList;
import com.mijaelsegura.eCommerceSpring.dto.supplement.SupplementDto;
import com.mijaelsegura.eCommerceSpring.dto.supplement.SupplementImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ISupplementService {
    ResultSupplement GetSupplementById(long id);
    SupplementImage GetSupplementImageById(long id);
    ResultSupplementList GetAllSupplements();
    ResultSupplement PostSupplement(SupplementDto supplementDto, MultipartFile imageFile);
    ResultSupplement PutSupplement(long id, SupplementDto supplementDto);
    ResultSupplement PutSupplementImage(long id, MultipartFile imageFile);
    ResultSupplement DeleteSupplement(long id);
}
