Feature: Test swagger Invoice API - Detail

# - /invoices/detail
  @200
  Scenario Outline: GET - /invoices/detail-200
    Given I use the route "/invoices/detail"
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
    And I set queryparameter "emissionDate" as "<emissionDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I set queryparameter "nfNumber" as "<nfNumber>"
    And I set queryparameter "agencyCode" as "<agencyCode>"
    And I set queryparameter "sourceSystem" as "<sourceSystem>"
    And I print the path
    When I send the GET request
    Then I print the response
#    Validating - Successful
#    Positive responses validation
    Then Http response should be 200
    Then the response JSON must have "/startDateCicle" as the String "<startDateCicle>"
    Then the response JSON must have "/endDateCicle" as the String "<endDateCicle>"
    Then the response JSON must have "/0/city" as the String "<city>"
    Then the response JSON must have "/0/verificationCode" as the String "<verificationCode>"
    Then the response JSON must have "/0/discountValue" as the String "<discountValue>"
    Then the response JSON must have "/0/totalValue" as the String "<totalValue>"
    Then the response JSON must have "/0/vcm" as the String "<vcm>"
    Then the response JSON must have "/0/installments" as the String "<installments>"
    Then the response JSON must have "/0/products/0/name" as the String "<name>"
    Then the response JSON must have "/0/products/0/quantity" as the String "<quantity>"
    Then the response JSON must have "/0/products/0/unityValue" as the String "<unityValue>"
    Then the response JSON must have "/0/products/0/discountValue" as the String "<discountValue>"
    Then the response JSON must have "/0/products/0/totalValue" as the String "<totalValue>"
    Examples:
      | ip           | document  | emissionDate | nfeNumber | nfNumber | agencyCode | sourceSystem | startDateCicle | endDateCicle | city | verificationCode | discountValue | totalValue | vcm   | installments | name      | quantity | unityValue | discountValue | totalValue |
      | 54.76.98.192 | 000000208 | 2018-04-01   | 682179    |72017     |54          |BRM           | 01/01/1900     | 01/01/1900   |      |                  |               |            |       |              |           |          |            |               |            |



 #    Validating - Bad Request
#    Negatives responses
  @400
  Scenario Outline: GET - /invoices/detail-400
    Given I use the route "/invoices/detail"
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
    And I set queryparameter "emissionDate" as "<emissionDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I set queryparameter "nfNumber" as "<nfNumber>"
    And I set queryparameter "agencyCode" as "<agencyCode>"
    And I set queryparameter "sourceSystem" as "<sourceSystem>"
    And I print the path
    When I send the GET request
    Then I print the response
    Then Http response should be 400
    #Then the response JSON must have "/code" as the String "<code>"
    #Then the response JSON must have "/message" as the String "<message>"

    Examples:
      | ip           | document  | emissionDate | nfeNumber | nfNumber | agencyCode | sourceSystem |
      | 54.76.98.192 |           | 2018-04-01   | 682179    |72017     |54          |BRM           |



#    Validating - Unauthorized
#    Negatives responses validation
  @401
  Scenario Outline: GET - /invoices/detail-401
    Given I use the route "/invoices/detail"
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
							  "exp": "<exp>"
							}
		      """
    And I set the Bearer Authorization header
    And I clean queryparameter
    And I set queryparameter "ip" as "<ip>"
    And I set queryparameter "document" as "<document>"
    And I set queryparameter "emissionDate" as "<emissionDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I set queryparameter "nfNumber" as "<nfNumber>"
    And I set queryparameter "agencyCode" as "<agencyCode>"
    And I set queryparameter "sourceSystem" as "<sourceSystem>"
    And I print the path
    When I send the GET request
    Then I print the response
    Then Http response should be 401
  #Then the response JSON must have "/code" as the String "<code>"
    Examples:
      |exp         | ip           | document  | emissionDate | nfeNumber | nfNumber | agencyCode | sourceSystem |
      |1           | 54.76.98.192 | 000000208 | 2018-04-01   | 682179    |72017     |54          |BRM           |


#    Validating - Not found
#    Negatives responses validation
  @404
  Scenario Outline: GET - /invoices/detail-404
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
    And I set queryparameter "emissionDate" as "<emissionDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I set queryparameter "nfNumber" as "<nfNumber>"
    And I set queryparameter "agencyCode" as "<agencyCode>"
    And I set queryparameter "sourceSystem" as "<sourceSystem>"
    And I print the path
    When I send the GET request
    Then I print the response
    Then Http response should be 404
    #Then the response JSON must have "/code" as the String "<code>"
    #Then the response JSON must have "/message" as the String "<message>"
    Examples:
      |route              | ip           | document  | emissionDate | nfeNumber | nfNumber | agencyCode | sourceSystem |
      |/invoices/detai    | 54.76.98.192 | 000000208 | 2018-04-01   | 682179    |72017     |54          |BRM           |
      |/invoices/det      | 54.76.98.192 | 000000208 | 2018-04-01   | 682179    |72017     |54          |BRM           |
      |/invoice/detail    | 54.76.98.192 | 000000208 | 2018-04-01   | 682179    |72017     |54          |BRM           |
      |/invoic/detail     | 54.76.98.192 | 000000208 | 2018-04-01   | 682179    |72017     |54          |BRM           |

#    Validating - Unprocessable Entity
#    Negatives responses validation
  @422
  Scenario Outline: GET - /invoices/detail-422
    Given I use the route "/invoices/detail"
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
    And I set queryparameter "emissionDate" as "<emissionDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I set queryparameter "nfNumber" as "<nfNumber>"
    And I set queryparameter "agencyCode" as "<agencyCode>"
    And I set queryparameter "sourceSystem" as "<sourceSystem>"
    And I print the path
    When I send the GET request
    Then I print the response
    Then Http response should be 422
#Then the response JSON must have "/code" as the String "<code>"
#Then the response JSON must have "/message" as the String "<message>"
    Examples:
      | ip           | document        | emissionDate | nfeNumber | nfNumber | agencyCode | sourceSystem |
      | 54.76.98.192 |00000002         | 2018-04-01   | 682179    |72017     |54          |BRM           |
      | 54.76.98.192 |0000002080       | 2018-04-01   | 682179    |72017     |54          |BRM           |
      | 54.76.98.192 |0000002080000100 | 2018-04-01   | 682179    |72017     |54          |BRM           |
      | 54.76.98.192 |0000020800001    | 2018-04-01   | 682179    |72017     |54          |BRM           |


#    Validating - Internal error
#    Negatives responses validation
#Then Http response should be 500
#Then the response JSON must have "/code" as the String "<code>"
#Then the response JSON must have "/message" as the String "<message>"
#Examples:
#| ip | document | emissionDate | nfeNumber | nfNumber | agencyCode | sourceSystem |
#| 54.76.98.192| 000000208 |    |           |          |            |              |

# Only required fields
#    | ip | document | emissionDate | nfeNumber | nfNumber | agencyCode | sourceSystem |


# Only validation fields
#    | startDateCicle | endDateCicle | city | verificationCode | discountValue | totalValue | vcm | installments | name | quantity | unityValue | discountValue | totalValue | products | code | message | code | message | code | message | code | message | code | message |
