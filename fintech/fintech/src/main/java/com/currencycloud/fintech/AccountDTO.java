package com.currencycloud.fintech;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO {
    @Getter(onMethod_ = {@JsonProperty("account_name")})
    @Setter(onMethod_ = {@JsonProperty("accountName")})
    private String accountName;

    @Getter(onMethod_ = {@JsonProperty("legal_entity_type")})
    @Setter(onMethod_ = {@JsonProperty("legalEntityType")})
    private String legalEntityType;

    private String street;
    private String city;
    private String country;

}
