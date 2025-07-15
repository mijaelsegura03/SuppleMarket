package com.mijaelsegura.eCommerceSpring.services;

import com.mijaelsegura.eCommerceSpring.dto.supplement.ResultSupplement;
import com.mijaelsegura.eCommerceSpring.dto.supplement.ResultSupplementList;
import com.mijaelsegura.eCommerceSpring.dto.supplement.SupplementDto;
import com.mijaelsegura.eCommerceSpring.dto.supplement.SupplementImage;
import com.mijaelsegura.eCommerceSpring.exceptions.ImageException;
import com.mijaelsegura.eCommerceSpring.exceptions.PropertyValidationException;
import com.mijaelsegura.eCommerceSpring.exceptions.ResourceNotFoundException;
import com.mijaelsegura.eCommerceSpring.models.Supplement;

import com.mijaelsegura.eCommerceSpring.repositories.ISupplementRepository;
import com.mijaelsegura.eCommerceSpring.utils.results.GenerateResultSupplement;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SupplementService implements ISupplementService {
    private final ISupplementRepository supplementRepository;
    private final GenerateResultSupplement resultGenerator;

    public SupplementService(ISupplementRepository supplementRepository, GenerateResultSupplement resultGenerator) {
        this.supplementRepository = supplementRepository;
        this.resultGenerator = resultGenerator;
    }

    @Override
    public ResultSupplement GetSupplementById(long id) {
        Optional<Supplement> supplementOptional = supplementRepository.findById(id);
        if (supplementOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found any supplement with ID " + id);
        }
        Supplement supplement = supplementOptional.get();
        return resultGenerator.getSuccessResult(new SupplementDto(supplement.getName(), supplement.getDescription(), supplement.getUnitaryPrice(), supplement.getUnitaryCost()));
    }

    @Override
    public SupplementImage GetSupplementImageById(long id) {
        Optional<Supplement> supplementOptional = supplementRepository.findById(id);
        if (supplementOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found any supplement with ID " + id);
        }
        Supplement supplement = supplementOptional.get();
        return new SupplementImage(supplement.getImageName(), supplement.getImageType(), supplement.getImageData());
    }

    @Override
    public ResultSupplementList GetAllSupplements() {
        ResultSupplementList res = new ResultSupplementList();
        List<Supplement> supplements = supplementRepository.findAll();
        res.setMessage("");
        res.setSuccess(true);
        res.setTypeError("");
        if (supplements.isEmpty()) {
            res.setSupplements(new ArrayList<>());
            return res;
        }
        res.setSupplements(supplements.stream().map(supplement -> new SupplementDto(supplement.getId(),supplement.getName(), supplement.getDescription(), supplement.getUnitaryPrice(), supplement.getUnitaryCost())).toList());
        return res;
    }

    @Override
    public ResultSupplement PostSupplement(SupplementDto supplementDto, MultipartFile imageFile) {
        String propertyValidation = supplementDto.validateAllProperties();
        if (!Objects.equals(propertyValidation, "")) {
            throw new PropertyValidationException("Could not validate supplement: " + propertyValidation);
        }
        Supplement supplement = new Supplement(supplementDto.getName(), supplementDto.getDescription(), supplementDto.getUnitaryPrice(), supplementDto.getUnitaryCost());
        supplement.setImageName(imageFile.getOriginalFilename());
        supplement.setImageType(imageFile.getContentType());
        try {
            supplement.setImageData(imageFile.getBytes());
        } catch (IOException e) {
            throw new ImageException("Could not get bytes of the image.");
        }
        supplementRepository.save(supplement);
        return resultGenerator.getSuccessResult(supplementDto);
    }

    @Override
    public ResultSupplement PutSupplement(long id, SupplementDto supplementDto) {
        String propertyValidation = supplementDto.validateAllProperties();
        if (!Objects.equals(propertyValidation, "")) {
            throw new PropertyValidationException("Could not validate supplement: " + propertyValidation);
        }
        Optional<Supplement> supplementOptional = supplementRepository.findById(id);
        if (supplementOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found any supplement with ID " + id + " to edit.");
        }
        Supplement supplementFromDB = supplementOptional.get();
        Supplement editedSupplement = new Supplement(supplementDto.getName(), supplementDto.getDescription(), supplementDto.getUnitaryPrice(), supplementDto.getUnitaryCost());
        editedSupplement.setImageName(supplementFromDB.getImageName());
        editedSupplement.setImageType(supplementFromDB.getImageType());
        editedSupplement.setImageData(supplementFromDB.getImageData());
        editedSupplement.setId(id);
        supplementRepository.save(editedSupplement);

        return resultGenerator.getSuccessResult(supplementDto);
    }

    @Override
    public ResultSupplement PutSupplementImage(long id, MultipartFile imageFile) {
        Optional<Supplement> supplementOptional = supplementRepository.findById(id);
        if (supplementOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found any supplement with ID " + id + " to edit its image.");
        }
        Supplement supplement = supplementOptional.get();
        try {
            supplement.setImageData(imageFile.getBytes());
        } catch (IOException e) {
            throw new ImageException("Could not get bytes of the image.");
        }
        supplement.setImageType(imageFile.getContentType());
        supplement.setImageName(imageFile.getOriginalFilename());
        supplement.setId(id);
        supplementRepository.save(supplement);
        return resultGenerator.getSuccessResult(new SupplementDto(supplement.getName(), supplement.getDescription(), supplement.getUnitaryPrice(), supplement.getUnitaryCost()));
    }

    @Override
    public ResultSupplement DeleteSupplement(long id) {
        Optional<Supplement> supplementOptional = supplementRepository.findById(id);
        if (supplementOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found any supplement with ID " + id + " to delete.");
        }
        Supplement supplement = supplementOptional.get();
        supplementRepository.delete(supplement);
        return resultGenerator.getSuccessResult(new SupplementDto(supplement.getName(), supplement.getDescription(), supplement.getUnitaryPrice(), supplement.getUnitaryCost()));
    }
}
