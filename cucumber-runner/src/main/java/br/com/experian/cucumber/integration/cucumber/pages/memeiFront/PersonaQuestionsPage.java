package br.com.experian.cucumber.integration.cucumber.pages.memeiFront;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;

public class PersonaQuestionsPage extends BasePage<PersonaQuestionsPage> {

    @Name("Nickname")
    @FindBy(id = "nickName")
    private WebElement nickname;

    @Name("Create Nickname Button")
    @FindBy(id = "criarApelido")
    private WebElement createNicknameBtn;

    @Name("Finish Questionary")
    @FindBy(id = "finalizarQuestionarioPersona")
    private WebElement finishQuestionaryBtn;


    public PersonaQuestionsPage setNickname(String nickname) throws Throwable {
        Assert.assertTrue("Nickname field not found", sendKeysElement(this.nickname, nickname));
        return this;
    }

    public PersonaQuestionsPage clickCreateNicknameBtn() throws Throwable{
        Assert.assertTrue("Create Nicknam button not found", clickElement(createNicknameBtn));
        return this;
    }

    public PersonaQuestionsPage clickFinishQuestionary() throws Throwable{
        screenShot();
        Assert.assertTrue("Finish Questionary button not found", clickElement(finishQuestionaryBtn));
        waitPageLoadEvents();
        screenShot();

        return this;
    }

    public PersonaQuestionsPage startPersonaQuestionary(String nickname) throws Throwable {
        this.setNickname(nickname)
                .clickCreateNicknameBtn();
        screenShot();
        waitPageLoadEvents();

        return this;
    }

    public PersonaQuestionsPage answerQuestion(String questionNum, String responseNum) throws Throwable {

        String xpathExpression = "//label[@for='op_" + questionNum + "_" + responseNum + "']";
        WebElement responseBtn = driver.findElement(By.xpath(xpathExpression));

        Assert.assertTrue("Response: " + questionNum + " - " + responseNum + " button not found", clickElement(responseBtn));

        waitPageLoadEvents();
        //screenShot();

        return this;
    }

}
