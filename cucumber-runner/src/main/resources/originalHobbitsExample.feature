Feature: ORIGINAL - POST /business-accounts/register-email?clientId=5a33ec690adaeda2cb8d3001
  Como um testador
  Eu quero testar a API do business-accounts/register-email da Us  Débitos técnicos
  E validar que estão funcionando corretamente

  @Positivos
  Scenario Outline: business-accounts/register-email - Casos Positivos
    Given I run the test case "<testDescription>"
    And I set header "Content-Type" as "application/json"
    Given I generate a miliseconds date "iat" with "0" minutes
    Given I generate a miliseconds date "exp" with "15" minutes
    Given I use the json and generate a JWT Token
      """
        {
         "jti": "9e74d1f6-7095-4718-8f11-47dc9ec99c63",
         "iat": ${iat},
         "scope": [
           "read",
           "write"
         ],
         "client_id": "5a33ec690adaeda2cb8d3001",
         "authorities": [
           "ROLE_CLI-AUTH-IDENTIFIED",
           "ROLE_CLI-1STPARTY",
           "ROLE_AUTH-MFA",
           "ROLE_ADMIN"
         ],
         "exp": ${exp}
       }
      """
    Given I set the Bearer Authorization header
    Then Stop "2" seconds
     I use the route "/business-accounts/register-email?clientId=5a33ec690adaeda2cb8d3001"
    Then I print the path
    Given I use the json body
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
    Then I print the response
    Then Http response should be <responseCode>
    When I save the response value "/businessId" as "businessId"
    When I save the response value "/userId" as "userId"
    Given I set the Bearer Authorization header
    Given I use the route "/business-accounts/${businessId}"
    When I send the DELETE request
    Then Http response should be 200
    Given I set the Bearer Authorization header
    Given I use the route "/user-accounts/${userId}"
    Then I print the response
    When I send the DELETE request
    Then Http response should be 200

    Examples:
      | testDescription                   | companyName                              | nationalRegistrationId | nationalRegistrationCountry | stateRegistrationId | state | municipalRegistrationId | city | email                                | fullName                         | documentId  | documentCountry | responseCode | Mensagem |
      | Todos os campos                   | Empresa business-accounts-register-email | 00289199000110         | BRA                         | 0001                | 01    | 00001                   |      | bussinessaccount401_099@email.com.br | ESPOLIO DE FILON RODRIGUES COSTA | 09021916088 | BRA             | 201          |          |
      | Campos Obrigatórios               |                                          | 15322725000173         | BRA                         |                     |       |                         |      | bussinessaccount402_099@email.com.br | IZABEL BORHER MELLO              | 51256352004 | BRA             | 201          |          |
      | Campos Obrigatórios + companyName | Empresa 2                                | 63619332000179         | BRA                         |                     |       |                         |      | bussinessaccount403_099@email.com.br | EMILLIANE VARGAS PESSINI STOFEL  | 79746409018 | FRA             | 201          |          |