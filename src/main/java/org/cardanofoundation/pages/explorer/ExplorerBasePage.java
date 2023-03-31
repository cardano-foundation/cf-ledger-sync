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

    @FindBy(xpath = "//img[@alt='Ada Price']/ancestor::div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-xs-12 MuiGrid-grid-sm-6 MuiGrid-grid-lg-3 MuiGrid-grid-xl-true css-1253ibq-MuiGrid-root']")
    public WebElement adaPriceBox;

    @FindBy(xpath = "//img[@alt='Ada Price']/following-sibling::div/h4")
    public WebElement adaPriceBoxTitle;

    @FindBy(xpath = "//img[@alt='Ada Price']")
    public WebElement adaPriceIcon;

    @FindBy(xpath = "//img[@alt='Market Cap']/ancestor::div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-xs-12 MuiGrid-grid-sm-6 MuiGrid-grid-lg-3 MuiGrid-grid-xl-true css-1253ibq-MuiGrid-root']")
    public WebElement marketCapBox;

    @FindBy(xpath = "//img[@alt='Market cap']")
    public WebElement marketCapIcon;

    @FindBy(xpath = "//img[@alt='Market cap']/following-sibling::div/h4")
    public WebElement marketCapBoxTitle;

    @FindBy(xpath = "//img[@alt='Current Epoch']/ancestor::div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-xs-12 MuiGrid-grid-sm-6 MuiGrid-grid-lg-3 MuiGrid-grid-xl-true css-1253ibq-MuiGrid-root']")
    public WebElement currentEpochBox;

    @FindBy(xpath = "//img[@alt='Current Epoch']")
    public WebElement currentEpochIcon;

    @FindBy(xpath = "//img[@alt='Current Epoch']/following-sibling::div/h4")
    public WebElement currentEpochBoxTitle;


}
