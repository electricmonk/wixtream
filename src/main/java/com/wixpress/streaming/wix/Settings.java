package com.wixpress.streaming.wix;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class Settings {

    private String displayName;
    private String paypalMerchantEmail;
    private Double pricePerSessionInUSD = 10.0;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPaypalMerchantEmail() {
        return paypalMerchantEmail;
    }

    public void setPaypalMerchantEmail(String paypalMerchantEmail) {
        this.paypalMerchantEmail = paypalMerchantEmail;
    }

    public Double getPricePerSessionInUSD() {
        return pricePerSessionInUSD;
    }

    public void setPricePerSessionInUSD(Double pricePerSessionInUSD) {
        this.pricePerSessionInUSD = pricePerSessionInUSD;
    }
}
