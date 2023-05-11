package com.banking.account;

import com.banking.constants.BankConstants;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "ifscCode", column = @Column(name = "ifsc_code"))
})
public class IFSC {
    private String bankCode = BankConstants.BANK_CODE;

    private String branchCode;

    private String ifscCode;

    public void setIfscCode(String ifscCode) {
        setBranchCode(ifscCode);
        this.ifscCode = getBankCode() + getBranchCode();
    }

    private void setBranchCode(String ifscCode) {
        if (ifscCode != null) {
            this.branchCode = ifscCode.substring(5);
        }
    }

    public String getIfscCode() {
        return this.ifscCode;
    }

}
