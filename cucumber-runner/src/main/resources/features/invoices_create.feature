Feature: Test swagger Invoice API - Create

# - /invoices/create

#    Validating - Successful
#    Positive responses validation
  @200
  Scenario Outline: POST - /invoices/create-200
    Given I use the route "/invoices/create"
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
    And I use the json body
  """
  {
  "barCode": "11111.11111 222222.22222",
  "linkInvoice": "www.invoice.com/get?id=WAOBYA21ZI"
}
  """
    And I set queryparameter "ip" as "<ip>"
    And I set queryparameter "document" as "<document>"
    And I set queryparameter "dueDate" as "<dueDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I set queryparameter "newDueDate" as "<newDueDate>"
    And I set queryparameter "newValue" as "<newValue>"
    And I print the path
    When I send the POST request
    Then I print the response

    Then Http response should be 200
    Then the response JSON must have "/barCode" as the String "<barCode>"
    Then the response JSON must have "/linkInvoice" as the String "<linkInvoice>"
    Examples:
      | ip          | document  | dueDate   | nfeNumber  | newDueDate | newValue |barCode                                                  | linkInvoice|
      | 54.76.98.192| 000000208 | 2018-09-20 |123456     |2018-09-21  | 644.35   |03399.83819 34800.000126 34567.801013 1 76540000064435   |        0    |


#    Validating - Bad Request
#    Negatives responses validation
  @400
  Scenario Outline: POST - /invoices/create-400
    Given I use the route "/invoices/create"
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
    And I use the json body
  """
  {
  "barCode": "11111.11111 222222.22222",
  "linkInvoice": "www.invoice.com/get?id=WAOBYA21ZI"
}
  """
    And I set queryparameter "ip" as "<ip>"
    And I set queryparameter "document" as "<document>"
    And I set queryparameter "dueDate" as "<dueDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I set queryparameter "newDueDate" as "<newDueDate>"
    And I set queryparameter "newValue" as "<newValue>"
    And I print the path
    When I send the POST request
    Then I print the response

    Then Http response should be 400
#Then the response JSON must have "/barCode" as the String "<barCode>"
#Then the response JSON must have "/linkInvoice" as the String "<linkInvoice>"
    Examples:
      | ip          | document  | dueDate   | nfeNumber  | newDueDate | newValue |
      | 54.76.98.192| 000000208 | 2018-09-20 |123456     |2018-09-21  |          |


#    Validating - Unauthorized
#    Negatives responses validation
  @401
  Scenario Outline: POST - /invoices/create-401
    Given I use the route "/invoices/create"
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
    And I use the json body
  """
  {
  "barCode": "11111.11111 222222.22222",
  "linkInvoice": "www.invoice.com/get?id=WAOBYA21ZI"
}
  """
    And I set queryparameter "ip" as "<ip>"
    And I set queryparameter "document" as "<document>"
    And I set queryparameter "dueDate" as "<dueDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I set queryparameter "newDueDate" as "<newDueDate>"
    And I set queryparameter "newValue" as "<newValue>"
    And I print the path
    When I send the POST request
    Then I print the response

    Then Http response should be 401
#Then the response JSON must have "/code" as the String "<code>"
#Then the response JSON must have "/message" as the String "<message>"
    Examples:
      |exp | ip          | document  | dueDate   | nfeNumber  | newDueDate | newValue |
      |0   | 54.76.98.192| 000000208 | 2018-09-20 |123456     |2018-09-21  |   644.35 |


#    Validating - Not found
#    Negatives responses validation
  @404
  Scenario Outline: POST - /invoices/create-404
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
    And I use the json body
  """
  {
  "barCode": "11111.11111 222222.22222",
  "linkInvoice": "www.invoice.com/get?id=WAOBYA21ZI"
}
  """
    And I set queryparameter "ip" as "<ip>"
    And I set queryparameter "document" as "<document>"
    And I set queryparameter "dueDate" as "<dueDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I set queryparameter "newDueDate" as "<newDueDate>"
    And I set queryparameter "newValue" as "<newValue>"
    And I print the path
    When I send the POST request
    Then I print the response

    Then Http response should be 404
#Then the response JSON must have "/code" as the String "<code>"
#Then the response JSON must have "/message" as the String "<message>"
    Examples:
      |route             | ip          | document  | dueDate   | nfeNumber  | newDueDate | newValue |
      |/invoices/crea    | 54.76.98.192| 000000208 | 2018-09-20 |123456     |2018-09-21  |   644.35 |


#    Validating - Unprocessable Entity
#    Negatives responses validation
  @422
  Scenario Outline: POST - /invoices/create-422
    Given I use the route "/invoices/create"
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
    And I use the json body
  """
  {
  "barCode": "11111.11111 222222.22222",
  "linkInvoice": "www.invoice.com/get?id=WAOBYA21ZI"
}
  """
    And I set queryparameter "ip" as "<ip>"
    And I set queryparameter "document" as "<document>"
    And I set queryparameter "dueDate" as "<dueDate>"
    And I set queryparameter "nfeNumber" as "<nfeNumber>"
    And I set queryparameter "newDueDate" as "<newDueDate>"
    And I set queryparameter "newValue" as "<newValue>"
    And I print the path
    When I send the POST request
    Then I print the response

    Then Http response should be 422

#Then the response JSON must have "/code" as the String "<code>"
#Then the response JSON must have "/message" as the String "<message>"
    Examples:
      | ip          | document    | dueDate   | nfeNumber  | newDueDate | newValue |
      | 54.76.98.192|             | 2018-09-25 |123456     |2018-09-26  |   644.35 |

#    Validating - Internal error
#    Negatives responses validation
#Then Http response should be 500
#Then the response JSON must have "/code" as the String "<code>"
#Then the response JSON must have "/message" as the String "<message>"



# Only required fields
#    | ip | document | dueDate | nfeNumber | newDueDate | newValue |


# Only validation fields
#    | barCode | linkInvoice | code | message | code | message | code | message | code | message | code | message |