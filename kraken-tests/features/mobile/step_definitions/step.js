const { Given, When, Then } = require('@cucumber/cucumber');

Given('I wait for the view with id {string} to be displayed', async function (id) {
    let element = await this.driver.$(`android=new UiSelector().resourceIdMatches(".*id/${id}")`);
    await element.waitForDisplayed({ timeout: 10000 });
});

When('I click on the view with id {string}', async function (id) {
    let element = await this.driver.$(`android=new UiSelector().resourceIdMatches(".*id/${id}")`);
    await element.click();
});

Then('I scroll down', async function () {
    await this.driver.$(`android=new UiScrollable(new UiSelector().scrollable(true)).scrollForward()`);
});

