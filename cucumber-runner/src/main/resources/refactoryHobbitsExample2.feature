Feature: REFACTORY 2 - POST /business-accounts/register-email?clientId=5a33ec690adaeda2cb8d3001
  Como um testador
  Eu quero testar a API do business-accounts/register-email da Us  Débitos técnicos
  E validar que estão funcionando corretamente

  @Positivos
  Scenario Outline: business-accounts/register-email - Casos Positivos
    Given I run the test case "<testDescription>"
    And I have a token with the following data:
      | authorities                                                                    |
      | "ROLE_CLI-AUTH-IDENTIFIED", "ROLE_CLI-1STPARTY", "ROLE_AUTH-MFA", "ROLE_ADMIN" |
    And user adds a company and user account with predefined client
    And user informs aleatory email
    And user informs the following data for company to register email:
      | companyName                 | <companyName>                 |
      | nationalRegistrationId      | <nationalRegistrationId>      |
      | nationalRegistrationCountry | <nationalRegistrationCountry> |
      | stateRegistrationId         | <stateRegistrationId>         |
      | state                       | <state>                       |
      | municipalRegistrationId     | <municipalRegistrationId>     |
      | city                        | <city>                        |
      | email                       | <email>                       |
      | fullName                    | <fullName>                    |
      | documentId                  | <documentId>                  |
      | documentCountry             | <documentCountry>             |

    When user saves data
    Then user should see "<message>" message
    And user saves businessId and userId created

    Given user has permission
    When user deletes last business
    Then user should see "success" message

    Given user has permission
    When user deletes last user
    Then user should see "success" message

    Examples:
      | testDescription | companyName                              | nationalRegistrationId | nationalRegistrationCountry | stateRegistrationId | state | municipalRegistrationId | city | email    | fullName                         | documentId  | documentCountry | message           |
      | Todos os campos | Empresa business-accounts-register-email | 64435105000156         | BRA                         | 0001                | 01    | 00001                   |      | ${email} | ESPOLIO DE FILON RODRIGUES COSTA | 01758532238 | BRA             | save with success |
#      | Campos Obrigatórios               |                                          | 33144536000130         | BRA                         |                     |       |                         |      | bus12417357437@email.com.br | IZABEL BORHER MELLO              | 12417357437 | BRA             | save with success |
#      | Campos Obrigatórios + companyName | Empresa 2                                | 54416215000117         | BRA                         |                     |       |                         |      | bus78884837715@email.com.br | EMILLIANE VARGAS PESSINI STOFEL  | 78884837715 | FRA             | save with success |