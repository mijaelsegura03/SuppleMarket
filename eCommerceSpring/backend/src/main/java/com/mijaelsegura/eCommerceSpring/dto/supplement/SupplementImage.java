package com.mijaelsegura.eCommerceSpring.dto.supplement;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SupplementImage {
    private String imageName;
    private String imageType;
    private byte[] imageData;
}
