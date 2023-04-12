package org.cardanofoundation.pages.explorer;

import org.cardanofoundation.utils.WebUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

public class ExplorerBasePage {

    @FindBy(xpath = "//img[@alt='logo desktop']")
    public WebElement cardanoLogo;

    @FindBy(xpath = "//img[@alt='logo desktop']/following-sibling::small")
    public WebElement selectedNet;

//    Improve the locator for net slection dropdown, will it available in Prod?
//    @FindBy(xpath = "//button/span[normalize-space(text())='Connect Wallet']/ancestor::[normalize-space(text()) = 'Preprod']")
//    public WebElement netSelectionDropdown;
    @FindBy(xpath = "//span[normalize-space(text())='Connect Wallet']")
    public WebElement connectWallet;

    @FindBy(xpath = "//div/h1[normalize-space(text()) = 'Cardano Blockchain Explorer']")
    public WebElement pageHeader;

    @FindBy(xpath = "//img[@alt='Blockchain']/following-sibling::div/")
    public WebElement blockchain;

    @FindBy(xpath = "//img[@alt='Blockchain']")
    public WebElement blockchainIcon;

    @FindBy(xpath = "//img[@alt='Staking']/following-sibling::div/")
    public WebElement staking;

    @FindBy(xpath = "//img[@alt='Staking']")
    public WebElement stakingIcon;

    @FindBy(xpath = "//img[@alt='Browse']/following-sibling::div/")
    public WebElement browse;

    @FindBy(xpath = "//img[@alt='Browse']")
    public WebElement browseIcon;

    @FindBy(xpath = "//img[@alt='Resources']")
    public WebElement resourcesIcon;

    @FindBy(xpath = "//div[text()='All Filters']")
    public WebElement filter;

    @FindBy(xpath = "//ul[@role='listbox']/li")
    public List<WebElement> filterOptions;

    @FindBy(xpath = "//input[@placeholder='Search transactions, address, blocks, epochs, pools...'][@type='search']")
    public WebElement searchField;




}
