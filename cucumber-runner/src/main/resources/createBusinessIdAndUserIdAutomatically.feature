Feature: Testes extras - NEW USER

  @Positivo
  Scenario Outline: POST - consults -  validando autorizações
    Given I run the test case "<testDescription>"
    And I have a JWT token with the following data:
      | business_id | authorities   |
      | NEW         | <authorities> |

    Given I use the route "/my-consults"
    And user informs the following data for my consults into recommends:
    |document|<document>|
    When I send the POST request
    Then user should see "success" message

    Examples:
      | testDescription     | document    | authorities                                                                                                                                                                   |
      | authorities correto | 21202668674 | "ROLE_CLI-AUTH-IDENTIFIED","ROLE_CLI-1STPARTY","ROLE_USER","ROLE_ADMIN", "ROLE_BUSINESSOWNER","ROLE_BUSINESSADMIN", "ROLE_AUTH-MFA", "ROLE_AUTH-BASIC","ROLE_AUTH-IDENTIFIED" |

#  Scenario: Gerar Documentos
#    Given I generate documents
