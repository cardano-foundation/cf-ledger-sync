package org.cardanofoundation.pojos.wallets.shelly.sequential.walletsapi.createrestore.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateRestoreWallet {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("mnemonic_sentence")
    @Expose
    private List<String> mnemonicSentence;

    @SerializedName("mnemonic_second_factor")
    @Expose
    private List<String> mnemonicSecondFactor;

    @SerializedName("passphrase")
    @Expose
    private String passphrase;

    @SerializedName("address_pool_gap")
    @Expose
    private Integer addressPoolGap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMnemonicSentence() {
        return mnemonicSentence;
    }

    public void setMnemonicSentence(List<String> mnemonicSentence) {
        this.mnemonicSentence = mnemonicSentence;
    }

    public List<String> getMnemonicSecondFactor() {
        return mnemonicSecondFactor;
    }

    public void setMnemonicSecondFactor(List<String> mnemonicSecondFactor) {
        this.mnemonicSecondFactor = mnemonicSecondFactor;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public Integer getAddressPoolGap() {
        return addressPoolGap;
    }

    public void setAddressPoolGap(Integer addressPoolGap) {
        this.addressPoolGap = addressPoolGap;
    }

}
