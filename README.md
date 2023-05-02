# cf-qa-testsuite

This is Spring based framework. All the framework related code, selenium driver, rest controller, page objects, POJOs, api helpers etc lie under src/main folder.
Testcases lie under src/test folder. Take reference from existing test classes and page object classes to make sure all the required annotations are added otherwise Spring won't be able to initialize them.

The workflow files contains the steps to execute api and web tests in api actions.
To execute tests in a dev repository, take a look this https://github.com/cardano-foundation/cf-explorer-frontend/blob/main/.github/workflows/TriggerSeleniumTestSuite.yml workflow.

Once this workflow is complete it deploys the allure report to GitHub pages, latest report can be found under
https://github.com/cardano-foundation/cf-explorer-frontend/actions/workflows/pages/pages-build-deployment
