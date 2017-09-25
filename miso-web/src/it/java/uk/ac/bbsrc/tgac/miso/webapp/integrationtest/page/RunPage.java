package uk.ac.bbsrc.tgac.miso.webapp.integrationtest.page;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import uk.ac.bbsrc.tgac.miso.core.data.type.PlatformType;

public class RunPage extends FormPage<RunPage.Field> {

  public static enum Field implements FormPage.FieldElement {
    ID(By.id("runId"), FieldType.LABEL), //
    NAME(By.id("name"), FieldType.LABEL), //
    ALIAS(By.id("alias"), FieldType.TEXT), //
    PLATFORM(By.id("platform"), FieldType.LABEL), //
    SEQUENCER(By.id("sequencer"), FieldType.LABEL), //
    SEQ_PARAMS(By.id("sequencingParameters"), FieldType.DROPDOWN),//
    DESCRIPTION(By.id("description"), FieldType.TEXT), //
    FILE_PATH(By.id("filePath"), FieldType.TEXT), //
    NUM_CYCLES(By.id("numCycles"), FieldType.TEXT), //
    CALL_CYCLE(By.id("callCycle"), FieldType.TEXT), //
    IMG_CYCLE(By.id("imgCycle"), FieldType.TEXT), //
    SCORE_CYCLE(By.id("scoreCycle"), FieldType.TEXT), //
    PAIRED_END(By.id("pairedEnd"), FieldType.CHECKBOX), //
    STATUS(By.name("health"), FieldType.RADIO), //
    START_DATE(By.id("startDate"), FieldType.DATEPICKER), //
    COMPLETION_DATE(By.id("completionDate"), FieldType.DATEPICKER);

    private final By selector;
    private final FieldType type;

    private Field(By selector, FieldType type) {
      this.selector = selector;
      this.type = type;
    }

    @Override
    public By getSelector() {
      return selector;
    }

    @Override
    public FieldType getType() {
      return type;
    }
  } // end Field enum

  @FindBy(id = "save")
  private WebElement saveButton;

  public RunPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
    waitWithTimeout().until(or(titleContains("Run "), titleContains("New Run ")));
  }

  public static RunPage getForCreate(WebDriver driver, String baseUrl, long sequencerId) {
    driver.get(baseUrl + "miso/run/new/" + sequencerId);
    return new RunPage(driver);
  }

  public static RunPage getForEdit(WebDriver driver, String baseUrl, long runId) {
    driver.get(baseUrl + "miso/run/" + runId);
    return new RunPage(driver);
  }

  public RunPage save() {
    WebElement html = getHtmlElement();
    saveButton.click();
    waitForPageRefresh(html);
    return new RunPage(getDriver());
  }

  public RunPage addContainer(String serialNumber, String platformType) {
    WebElement html = getHtmlElement();
    getDriver().findElement(By.linkText("Add " + PlatformType.get(platformType).getContainerName())).click();
    WebElement dialog = getDriver().findElement(By.id("dialog"));
    waitUntil(visibilityOf(dialog));
    dialog.findElement(By.tagName("input")).sendKeys(serialNumber);
    dialog.findElement(By.id("ok")).click();
    waitForPageRefresh(html);
    return new RunPage(getDriver());
    }

}
