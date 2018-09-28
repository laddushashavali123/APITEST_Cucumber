Feature: REFACTORY 1 - POST /business-accounts/register-email?clientId=5a33ec690adaeda2cb8d3001
  Como um testador
  Eu quero testar a API do business-accounts/register-email da Us  Débitos técnicos
  E validar que estão funcionando corretamente

  @Positivos
  Scenario Outline: business-accounts/register-email - Casos Positivos
    Given I run the test case "<testDescription>"
    And I have a token with the following data:
      | client_id                | authorities                                                                    |
      | 5a33ec690adaeda2cb8d3001 | "ROLE_CLI-AUTH-IDENTIFIED", "ROLE_CLI-1STPARTY", "ROLE_AUTH-MFA", "ROLE_ADMIN" |
    And I use the route "/business-accounts/register-email?clientId=5a33ec690adaeda2cb8d3001"
    And I use the json body
      """
       {
        "companyName": "<companyName>",
        "companyDocument": {
          "nationalRegistrationId": "<nationalRegistrationId>",
          "nationalRegistrationCountry": "<nationalRegistrationCountry>",
          "stateRegistrationId": "<stateRegistrationId>",
          "state": "<state>",
          "municipalRegistrationId": "<municipalRegistrationId>",
          "city": "<city>"
        },
        "userAccount": {
          "email": "<email>",
          "fullName": "<fullName>",
          "document": {
            "documentId": "<documentId>",
            "documentCountry": "<documentCountry>"
          }
        }
      }
      """
    When I send the POST request
    Then  user should see "save with success" message

    Given I save the response value "/businessId" as "businessId"
    And I save the response value "/userId" as "userId"
    And I use the route "/business-accounts/${businessId}"
    When I send the DELETE request
    Then user should see "success" message

    Given I use the route "/user-accounts/${userId}"
    When I send the DELETE request
    Then user should see "success" message

    Examples:
      | testDescription                   | companyName                              | nationalRegistrationId | nationalRegistrationCountry | stateRegistrationId | state | municipalRegistrationId | city | email                       | fullName                         | documentId  | documentCountry |
      | Todos os campos                   | Empresa business-accounts-register-email | 64435105000156         | BRA                         | 0001                | 01    | 00001                   |      | bus01758532299@email.com.br | ESPOLIO DE FILON RODRIGUES COSTA | 01758532238 | BRA             |
      | Campos Obrigatórios               |                                          | 33144536000130         | BRA                         |                     |       |                         |      | bus12417357499@email.com.br | IZABEL BORHER MELLO              | 12417357437 | BRA             |
      | Campos Obrigatórios + companyName | Empresa 2                                | 54416215000117         | BRA                         |                     |       |                         |      | bus78884837799@email.com.br | EMILLIANE VARGAS PESSINI STOFEL  | 78884837715 | FRA             |