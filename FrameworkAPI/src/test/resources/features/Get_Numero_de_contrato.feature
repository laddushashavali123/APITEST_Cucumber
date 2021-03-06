Feature: Get_Numero_de_contrato.feature
      
      @scenario01
  		Scenario Outline: Validar numero de contratos ativos
  		Given I set the Bearer Authorization header as "eyJhbGciOiJSUzUxMiJ9.ew0KICAianRpIjogIjllNzRkMWY2LTcwOTUtNDcxOC04ZjExLTQ3ZGM5ZWM5OWM2MyIsDQogICJpYXQiOiAxNTI3NjE1NzQwLA0KICAic2NvcGUiOiBbDQogICAgInJlYWQiLA0KICAgICJ3cml0ZSINCiAgXSwNCiAgInVzZXJfaWQiOiAiNWIwMzQ4ZWU4ZTc0ZGIwMDA4ZmI5YTgzIiwNCiAgImJ1c2luZXNzX2lkIjogIjViMDM0OGVlOGU3NGRiMDAwOGZiOWE4MiIsDQogICJjbGllbnRfaWQiOiAiNWIwNDJmYjczZDdhMTcwMDA5NTcyYmE3IiwNCiAgImFwcF9pZCI6ICI1YjA0MmNkNThlNzRkYjAwMDhmYjliNDciLA0KICAic2VydmljZV9pZCI6ICI1YjA0MmZiNjhlNzRkYjAwMDhmYjliNTMiLA0KICAiYXV0aG9yaXRpZXMiOiBbDQogICAgIlJPTEVfQ0xJLUFVVEgtSURFTlRJRklFRCIsDQogICAgIlJPTEVfQ0xJLTFTVFBBUlRZIiwNCiAgICAiUk9MRV9DTEktQVVUSC1CQVNJQyIsDQogICAgIlJPTEVfQ0xJLTNSRFBBUlRZIiwNCiAgICAiUk9MRV9CVVNJTkVTU09XTkVSIiwNCiAgICAiUk9MRV9CVVNJTkVTU0FETUlOIiwNCiAgICAiUk9MRV9CVVNJTkVTU1VTRVIiLA0KICAgICJST0xFX0JVU0lORVNTIiwNCiAgICAiUk9MRV9BVVRILU1GQSIsDQogICAgIlJPTEVfQVVUSC1CQVNJQyIsDQogICAgIlJPTEVfQVVUSC1JREVOVElGSUVEIiwNCiAgICAiUk9MRV9TRUNBRE1JTiIsDQogICAgIlJPTEVfQURNSU4iLA0KICAgICJST0xFX1VTRVIiLA0KICAgICJST0xFX1BFUlNPTiINCiAgXSwNCiAgImV4cCI6IDE1NTkxNTU3NDANCn0.H0hfiQmtFQl-HquRmIS2K64GPUP4vM-GqJMUsWhUDdWfiLWAA9SwSzee0hx6zxfY90ZL3IFHgPOx0GXxESi5xAey4XkBmQZPj3cwrO-L_x3PDwrxwVwIbJLWfeZ_XJk167igAckFzZK6GESlpe4vWFCLcNJ_e2_S1TN_gHUbD5P9tDlRYppDgwZvm9WM45cPXDeUym9SbhQQxvH4jdjJRfjCCOrbqScBw4YsIGfFt7eZ7atwEsAQ_aFGx7-_rdAGj5AXP8p9hon8JF1kDrDDwiq7SGD7-w8bBc-gLRTv-AakMXrla8SofY6V5CHACDr-0YKE3JLIJt9dX5K5Rdnkig"
  		Given I use the route "https://dev-br-api.experian.com/self-service/contract/v1/contracts/active?ip=179.108.90.110&document<DocumentBase>"
		  Given I set the header "bearer" as "0bfc68f0-2514-11e6-b67b-9e71128cae77"
		  When I send the GET request 
		  Then I print the response
		  Then the response JSON must have "/numberOfActiveContract" as the String "<NumberContract>"
		  
		  Examples:
		  				| DocumentBase | NumberContract	|
		  				|				1			 | PIRPO051322010 |


		  				

