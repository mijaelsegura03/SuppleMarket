package com.mijaelsegura.eCommerceSpring.auth.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse (
        @JsonProperty("access_token") String accessToken
) {

}
