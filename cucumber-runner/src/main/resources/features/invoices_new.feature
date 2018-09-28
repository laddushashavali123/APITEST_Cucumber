Feature: Test swagger Invoice API - New


# - /invoices/new
  @200
  Scenario Outline: GET - /invoices/new
    Given I use the route "/invoices/new"
    And I set header "Content-Type" as "application/json"
    And I generate a miliseconds date "iat" with "0" minutes
    And I generate a miliseconds date "exp" with "15" minutes
    And I use the json and generate a JWT Token
		      """
		          {
							  "jti": "9e74d1f6-7095-4718-8f11-47dc9ec99c63",
							  "iat": ${iat},
							  "scope": [
							    "read",
							    "write"
							  ],
							  "user_id": "5b0348d18e74db0008fb9a78",
							  "client_id": "5b042fb73d7a170009572ba7",
							  "app_id": "5b042cd58e74db0008fb9b47",
							  "service_id": "5b042fb68e74db0008fb9b53",
							  "authorities": [
							    "ROLE_CLI-AUTH-IDENTIFIED",
							    "ROLE_CLI-1STPARTY",
							    "ROLE_CLI-AUTH-BASIC",
							    "ROLE_CLI-3RDPARTY",
							    "ROLE_AUTH-MFA",
							    "ROLE_AUTH-BASIC",
							    "ROLE_AUTH-IDENTIFIED",
							    "ROLE_SECADMIN",
							    "ROLE_ADMIN",
							    "ROLE_USER",
							    "ROLE_PERSON"
							  ],
							  "exp": ${exp}
							}
		     """
    And I set the Bearer Authorization header
    And I clean queryparameter
    And I set queryparameter "ip" as "<ip>"
    And I set queryparameter "document" as "<document>"
    And I set queryparameter "dueDate" as "<dueDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I print the path
    When I send the GET request
    Then I print the response
#    Validating - Successful
#    Positive responses validation
    Then Http response should be 200
    Then the response JSON must have "/nfNumber" as the String "<nfNumber>"
    Then the response JSON must have "/originalDueDate" as the String "<originalDueDate>"
    Then the response JSON must have "/newInvoices/0/dueDate" as tomorrow
    Then the response JSON must have "/newInvoices/0/originalValue" as the String "<originalValue>"
    Then the response JSON must have "/newInvoices/0/openValue" as the String "<openValue>"
    Then the response JSON must have "/newInvoices/0/fineValue" as the String "<fineValue>"
    Then the response JSON must have "/newInvoices/0/interestValue" as the String "<interestValue>"
    Then the response JSON must have "/newInvoices/0/totalValue" as the String "<totalValue>"
#    Validating - Bad Request
#    Negatives responses validation
    Examples:
      | ip          | document  | dueDate   | nfeNumber     |nfNumber|originalDueDate      |originalValue|openValue  |fineValue|interestValue|totalValue|code|message|
      | 54.76.98.192| 000000208 | 2018-08-29|  123456       |        | 01/01/1900          |0.0          |0.0        |    0.0  |          0.0|0.0       |   |      |


  @400
  Scenario Outline: GET - /invoices/new - 400
    Given I use the route "/invoices/new"
    And I set header "Content-Type" as "application/json"
    And I generate a miliseconds date "iat" with "0" minutes
    And I generate a miliseconds date "exp" with "15" minutes
    And I use the json and generate a JWT Token
		      """
		          {
							  "jti": "9e74d1f6-7095-4718-8f11-47dc9ec99c63",
							  "iat": ${iat},
							  "scope": [
							    "read",
							    "write"
							  ],
							  "user_id": "5b0348d18e74db0008fb9a78",
							  "client_id": "5b042fb73d7a170009572ba7",
							  "app_id": "5b042cd58e74db0008fb9b47",
							  "service_id": "5b042fb68e74db0008fb9b53",
							  "authorities": [
							    "ROLE_CLI-AUTH-IDENTIFIED",
							    "ROLE_CLI-1STPARTY",
							    "ROLE_CLI-AUTH-BASIC",
							    "ROLE_CLI-3RDPARTY",
							    "ROLE_AUTH-MFA",
							    "ROLE_AUTH-BASIC",
							    "ROLE_AUTH-IDENTIFIED",
							    "ROLE_SECADMIN",
							    "ROLE_ADMIN",
							    "ROLE_USER",
							    "ROLE_PERSON"
							  ],
							  "exp": ${exp}
							}
		      """
    And I set the Bearer Authorization header
    And I clean queryparameter
    And I set queryparameter "ip" as "<ip>"
    And I set queryparameter "document" as "<document>"
    And I set queryparameter "dueDate" as "<dueDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I print the path
    When I send the GET request
    Then I print the response

    Then Http response should be 400
    Then the response JSON must have "/code" as the String "<code>"
    Then the response JSON must have "/message" as the String "<message>"

    Examples:
      |ip           | document  | dueDate   | nfeNumber     |
      |54.76.98.192 |           | 2018-08-29|  123456       |


#    Validating - Unauthorized
#    Negatives responses validation
  @401
  Scenario Outline: GET - /invoices/new - 401
    Given I use the route "/invoices/new"
    And I set header "Content-Type" as "application/json"
    And I generate a miliseconds date "iat" with "0" minutes
    #And I generate a miliseconds date "exp" with "<exp>" minutes
    And I use the json and generate a JWT Token
		      """
		          {
							  "jti": "9e74d1f6-7095-4718-8f11-47dc9ec99c63",
							  "iat": ${iat},
							  "scope": [
							    "read",
							    "write"
							  ],
							  "user_id": "5b0348d18e74db0008fb9a78",
							  "client_id": "5b042fb73d7a170009572ba7",
							  "app_id": "5b042cd58e74db0008fb9b47",
							  "service_id": "5b042fb68e74db0008fb9b53",
							  "authorities": [
							    "ROLE_CLI-AUTH-IDENTIFIED",
							    "ROLE_CLI-1STPARTY",
							    "ROLE_CLI-AUTH-BASIC",
							    "ROLE_CLI-3RDPARTY",
							    "ROLE_AUTH-MFA",
							    "ROLE_AUTH-BASIC",
							    "ROLE_AUTH-IDENTIFIED",
							    "ROLE_SECADMIN",
							    "ROLE_ADMIN",
							    "ROLE_USER",
							    "ROLE_PERSON"
							  ],
							  "exp": "<exp>"
							}
		      """
    And I set the Bearer Authorization header
    And I clean queryparameter
    And I set queryparameter "ip" as "<ip>"
    And I set queryparameter "document" as "<document>"
    And I set queryparameter "dueDate" as "<dueDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I print the path
    When I send the GET request
    Then I print the response

    Then Http response should be 401
    #Then the response JSON must have "/code" as the String "<code>"
    #Then the response JSON must have "/message" as the String "<message>"

    Examples:
      |exp          | ip          | document  | dueDate   | nfeNumber     |
      |11           | 54.76.98.192| 000000208 | 2018-08-29|  123456       |


#    Validating - Not found
#    Negatives responses validation
  @404
  Scenario Outline: GET - /invoices/new - 404
    Given I use the route "<route>"
    And I set header "Content-Type" as "application/json"
    And I generate a miliseconds date "iat" with "0" minutes
    And I generate a miliseconds date "exp" with "15" minutes
    And I use the json and generate a JWT Token
		      """
		          {
							  "jti": "9e74d1f6-7095-4718-8f11-47dc9ec99c63",
							  "iat": ${iat},
							  "scope": [
							    "read",
							    "write"
							  ],
							  "user_id": "5b0348d18e74db0008fb9a78",
							  "client_id": "5b042fb73d7a170009572ba7",
							  "app_id": "5b042cd58e74db0008fb9b47",
							  "service_id": "5b042fb68e74db0008fb9b53",
							  "authorities": [
							    "ROLE_CLI-AUTH-IDENTIFIED",
							    "ROLE_CLI-1STPARTY",
							    "ROLE_CLI-AUTH-BASIC",
							    "ROLE_CLI-3RDPARTY",
							    "ROLE_AUTH-MFA",
							    "ROLE_AUTH-BASIC",
							    "ROLE_AUTH-IDENTIFIED",
							    "ROLE_SECADMIN",
							    "ROLE_ADMIN",
							    "ROLE_USER",
							    "ROLE_PERSON"
							  ],
							  "exp": ${exp}
							}
		      """
    And I set the Bearer Authorization header
    And I clean queryparameter
    And I set queryparameter "ip" as "<ip>"
    And I set queryparameter "document" as "<document>"
    And I set queryparameter "dueDate" as "<dueDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I print the path
    When I send the GET request
    Then I print the response
    Then Http response should be 404
#Then the response JSON must have "/code" as the String "<code>"
#Then the response JSON must have "/message" as the String "<message>"
    Examples:
      |route| ip          | document  | dueDate   | nfeNumber     |
      |/invoices/news| 54.76.98.192| 000000208 | 2018-08-29|  123456       |

#    Validating - Unprocessable Entity
#    Negatives responses validation
  @422
  Scenario Outline: GET - /invoices/new - 422
    Given I use the route "/invoices/new"
    And I set header "Content-Type" as "application/json"
    And I generate a miliseconds date "iat" with "0" minutes
    And I generate a miliseconds date "exp" with "15" minutes
    And I use the json and generate a JWT Token
		      """
		          {
							  "jti": "9e74d1f6-7095-4718-8f11-47dc9ec99c63",
							  "iat": ${iat},
							  "scope": [
							    "read",
							    "write"
							  ],
							  "user_id": "5b0348d18e74db0008fb9a78",
							  "client_id": "5b042fb73d7a170009572ba7",
							  "app_id": "5b042cd58e74db0008fb9b47",
							  "service_id": "5b042fb68e74db0008fb9b53",
							  "authorities": [
							    "ROLE_CLI-AUTH-IDENTIFIED",
							    "ROLE_CLI-1STPARTY",
							    "ROLE_CLI-AUTH-BASIC",
							    "ROLE_CLI-3RDPARTY",
							    "ROLE_AUTH-MFA",
							    "ROLE_AUTH-BASIC",
							    "ROLE_AUTH-IDENTIFIED",
							    "ROLE_SECADMIN",
							    "ROLE_ADMIN",
							    "ROLE_USER",
							    "ROLE_PERSON"
							  ],
							  "exp": ${exp}
							}
		      """
    And I set the Bearer Authorization header
    And I clean queryparameter
    And I set queryparameter "ip" as "<ip>"
    And I set queryparameter "document" as "<document>"
    And I set queryparameter "dueDate" as "<dueDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I print the path
    When I send the GET request
    Then I print the response


    Then Http response should be 422
    Then the response JSON must have "/code" as the String "<code>"
    Then the response JSON must have "/message" as the String "<message>"

    Examples:
      | testDescription                    | ip           | document   | code | message                |
      | 001 - CNPJ with fourteen positions | 54.76.98.192 | 0000000208 | 422  | Document Base invalid. |
      | 002 - CNPJ with special character  | 54.76.98.192 | 00@000802  | 422  | Document Base invalid. |
      | 003 - CNPJ with fourteen positions | 54.76.98.192 | 2000025048 | 422  | Document Base invalid. |
      | 004 - CNPJ with a letter           | 54.76.98.192 | a000058757 | 422  | Document Base invalid. |
      | 005 - CNPJ with several letters    | 54.76.98.192 | abcdefghij | 422  | Document Base invalid. |

#    Validating - Internal error
#    Negatives responses validation
  @500
  Scenario Outline: GET - /invoices/new - 500
    Given I use the route "/invoices/new"
    And I set header "Content-Type" as "application/json"
    And I generate a miliseconds date "iat" with "0" minutes
    And I generate a miliseconds date "exp" with "15" minutes
    And I use the json and generate a JWT Token
		      """
		          {
							  "jti": "9e74d1f6-7095-4718-8f11-47dc9ec99c63",
							  "iat": ${iat},
							  "scope": [
							    "read",
							    "write"
							  ],
							  "user_id": "5b0348d18e74db0008fb9a78",
							  "client_id": "5b042fb73d7a170009572ba7",
							  "app_id": "5b042cd58e74db0008fb9b47",
							  "service_id": "5b042fb68e74db0008fb9b53",
							  "authorities": [
							    "ROLE_CLI-AUTH-IDENTIFIED",
							    "ROLE_CLI-1STPARTY",
							    "ROLE_CLI-AUTH-BASIC",
							    "ROLE_CLI-3RDPARTY",
							    "ROLE_AUTH-MFA",
							    "ROLE_AUTH-BASIC",
							    "ROLE_AUTH-IDENTIFIED",
							    "ROLE_SECADMIN",
							    "ROLE_ADMIN",
							    "ROLE_USER",
							    "ROLE_PERSON"
							  ],
							  "exp": ${exp}
							}
		      """
    And I set the Bearer Authorization header
    And I clean queryparameter
    And I set queryparameter "ip" as "<ip>"
    And I set queryparameter "document" as "<document>"
    And I set queryparameter "dueDate" as "<dueDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I print the path
    When I send the GET request
    Then I print the response

    Then Http response should be 500


    Then the response JSON must have "/code" as the String "<code>"
    Then the response JSON must have "/message" as the String "<message>"

    Examples:
      | ip          | document  | dueDate   | nfeNumber     |nfNumber|originalDueDate   | newDueDate      |originalValue|openValue  |fineValue|interestValue|totalValue|code|message|
      | 54.76.98.192| 000000208 | 2018-08-29|  123456       |        | 01/01/1900       |    12/09/2018   |0.0          |0.0        |    0.0  |          0.0|0.0       |   |      |


# Only required fields
#    | ip | document | dueDate | nfeNumber |


# Only validation fields
#Examples:
 #   | nfNumber | originalDueDate | dueDate | originalValue | openValue | fineValue | interestValue | totalValue | newInvoices | code | message   | code | message | code | message | code | message | code | message |
    #|string    |string           | string  |0              |  0        |  0        |0              |0           |             |200   |Successfull|      |         |      |         |      |         |      |         |