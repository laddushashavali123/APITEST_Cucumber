Feature: /business-accounts/accept-invitation-POST
  This method is called after ‘/inviteuser-email’ or ‘/inviteuser-mobile’. If the invited user already has an account,
  this method must validate token and link user and business accounts. To access this method the client (application) must have
  “CLI-AUTH-IDENTIFIED” authentication and “CLI-1STPARTY” role and user must have ‘AUTH-IDENTIFIED’ level and ‘USER’ role

  Background:
    Given I run the test case "<testDescription>"

  @Positivos
  Scenario: Aceitar convite - convite por mobile
    Given I run the test case "<testDescription>"
    And I have a token with the following data:
      | authorities                                                                    |
      | "ROLE_CLI-AUTH-IDENTIFIED", "ROLE_CLI-1STPARTY", "ROLE_AUTH-MFA", "ROLE_ADMIN" |
    And user adds a company and user account
    And user informs new phone number
    And user informs the following data for company to register email:
      | companyName                 | Empresa business-accounts-register-email |
      | nationalRegistrationId      | 84936955000145                           |
      | nationalRegistrationCountry | BRA                                      |
      | stateRegistrationId         | 01                                       |
      | state                       | SP                                       |
      | municipalRegistrationId     | 0001                                     |
      | city                        |                                          |
      | email                       | ${phoneNumber}@email.com.br              |
      | fullName                    | Qualidade Tester                         |
      | documentId                  | 01758532238                              |
      | documentCountry             | BRA                                      |
    When user saves data
    Then user should see "save with success" message
    And user saves businessId and userId created

    Given user has permission
    And I use the route "/user-accounts/register-email"
    And user informs new phone number
    And user informs the following data for user to register email:
      | regionCode      | 55                       |
      | areaCode        | 11                       |
      | phoneNumber     | ${phoneNumber}           |
      | email           | ${phoneNumber}@gmail.com |
      | fullName        | User Email               |
      | documentId      | 54176387068              |
      | documentCountry | BRA                      |
    When I send the POST request
    Then user should see "save with success" message
    And I save the response value "/id" as "user_id2"

    Given I have a token with the following data:
      | business_id    | user_id    | authorities                                                                                                                                           |
      | ${business_id} | ${user_id} | "ROLE_CLI-AUTH-IDENTIFIED","ROLE_CLI-1STPARTY","ROLE_AUTH-IDENTIFIED","ROLE_USER","ROLE_BUSINESSADMIN","ROLE_ADMIN","ROLE_AUTH-MFA","ROLE_AUTH-BASIC" |
    And I use the route "/my-business-accounts/inviteuser-mobile"
    And user informs the following data to invite user mobile:
      | regionCode  | 55             |
      | areaCode    | 11             |
      | phoneNumber | ${phoneNumber} |
      | fullName    | User test      |
    When I send the POST request
    Then user should see "success" message

    Given I use the route "/my-business-accounts/invitations"
    When I send the GET request
    Then user should see "success" message
    And I save the response value "/0/inviteId" as "inviteId"

    And I have a token with the following data:
      | business_id    | user_id     | authorities                                                                                                                                           |
      | ${business_id} | ${user_id2} | "ROLE_CLI-AUTH-IDENTIFIED","ROLE_CLI-1STPARTY","ROLE_AUTH-IDENTIFIED","ROLE_USER","ROLE_BUSINESSADMIN","ROLE_ADMIN","ROLE_AUTH-MFA","ROLE_AUTH-BASIC" |
    And I use the route "/business-accounts/accept-invitation"
    And user informs the following data to accept invitations:
      | token     | 123456      |
      | invitedId | ${inviteId} |
    When I send the POST request
    Then user should see "success" message

    Given user deletes last user
    Then user should see "success" message

    Given user deletes last business
    Then user should see "success" message