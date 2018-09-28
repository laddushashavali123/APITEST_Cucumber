
Variáveis de Ambiente para execução do Cucumber

Configuração para testes de Sandbox APIGEE:
SANDBOX_HOST_SCHEMA - http ou https
SANDBOX_HOST_NAME - Host do Sandbox no APIGEE - exemplo: test-br-api.experian.com
SANDBOX_HOST_PORT - Porta de conexão - exemplo: 8080, 443

Configuração para testes no ambiente que foi realizado o deploy (ambiente default):
HOST_SCHEMA - http ou https
HOST_NAME - Host do deploy ou APIGEE - exemplo: stg-br-api.experian.com
HOST_PORT - Porta de conexão - exemplo: 8080, 443

API_VERSION - Basepath da API - exemplo: /persona/v1


Configuração para os domínios, utilizado nos testes integrados
Dominio iam
IAM_HOST_SCHEMA - http ou https
IAM_HOST_NAME - Host do deploy ou APIGEE - exemplo: stg-br-api.experian.com, localhost
IAM_HOST_PORT - Porta de conexão - exemplo: 8080, 443
IAM_API - Basepath da API - exemplo: /security/iam/v1 ou /digital/identity-access-management/v1

Dominio accounts
ACCOUNT_HOST_SCHEMA - http ou https
ACCOUNT_HOST_NAME - Host do deploy ou APIGEE - exemplo: stg-br-api.experian.com, localhost
ACCOUNT_HOST_PORT - Porta de conexão - exemplo: 8080, 443
ACCOUNT_API - Basepath da API - exemplo: /cross-domain/account/v1

Dominio persona
PERSONA_HOST_SCHEMA - http ou https
PERSONA_HOST_NAME - Host do deploy ou APIGEE - exemplo: stg-br-api.experian.com, localhost
PERSONA_HOST_PORT - Porta de conexão - exemplo: 8080, 443
PERSONA_API - Basepath da API - exemplo: /persona/v1

Dominio loan-management
LOAN_HOST_SCHEMA - http ou https
LOAN_HOST_NAME - Host do deploy ou APIGEE - exemplo: stg-br-api.experian.com, localhost
LOAN_HOST_PORT - Porta de conexão - exemplo: 8080, 443
LOAN_API - Basepath da API - exemplo: /loan-management/v1

Dominio video-management
VIDEO_HOST_SCHEMA - http ou https
VIDEO_HOST_NAME - Host do deploy ou APIGEE - exemplo: stg-br-api.experian.com, localhost
VIDEO_HOST_PORT - Porta de conexão - exemplo: 8080, 443
VIDEO_API - Basepath da API - exemplo: /vsbusiness/video-management/v1

Dominio eloqua
ELOQUA_HOST_SCHEMA - https
ELOQUA_HOST_NAME - Host do eloqua - Exemplo: secure.p04.eloqua.com
ELOQUA_HOST_PORT - Porta de conexão - exemplo: 443
ELOQUA_API - Basepath da API - exemplo: /api/REST

Domínios que o token JWT gerado deve ser extraído automaticamente pelo framework:
DOMAINS_EXTRACT_ACCESS_TOKEN Exemplo: /security/iam/v1,/digital/identity-access-management/v1

Caminho das features
FEATURE_SOURCE - Caminho onde estão disponíveis os arquivos feature, terminando com barra para pegar todos os arquivos e caso seja passado um arquivo será executado apenas aquela feature - exemplo: /test/resources/features/
REPORT_PATH - Caminho onde será gerado o relatário - exemplo: /test_cucumber


Exemplo de linha de comando no APIGEE de STG com o módulo accounts como default
> -Denvironment.name=LO -DFEATURE_SOURCE=C:/workspaceInteliJ/DDTP/experian-periscope-frontend/src/test/resources/features/teste2.feature -DREPORT_PATH=C:/workspaceInteliJ/DDTP/experian-cucumber-runner/target/report -DHOST_SCHEMA=https -DHOST_NAME=stg-br-api.experian.com -DHOST_PORT=443 -DAPI_VERSION=/cross-domain/account/v1 -DIAM_HOST_SCHEMA=https -DIAM_HOST_NAME=stg-br-api.experian.com -DIAM_HOST_PORT=443 -DIAM_API_VERSION=/security/iam/v1 -DACCOUNT_HOST_SCHEMA=https -DACCOUNT_HOST_NAME=stg-br-api.experian.com -DACCOUNT_HOST_PORT=443 -DACCOUNT_API_VERSION=/cross-domain/account/v1 -DPERSONA_HOST_SCHEMA=https -DPERSONA_HOST_NAME=stg-br-api.experian.com -DPERSONA_HOST_PORT=443 -DPERSONA_API_VERSION=/persona/v1 -DLOAN_HOST_SCHEMA=https -DLOAN_HOST_NAME=stg-br-api.experian.com -DLOAN_HOST_PORT=443 -DLOAN_API_VERSION=/loan-management/v1 -DVIDEO_HOST_SCHEMA=https -DVIDEO_HOST_NAME=stg-br-api.experian.com -DVIDEO_HOST_PORT=443 -DVIDEO_API_VERSION=/vsbusiness/video-management/v1 -DELOQUA_HOST_SCHEMA=https -DELOQUA_HOST_NAME=secure.p04.eloqua.com -DELOQUA_HOST_PORT=443 -DELOQUA_API_VERSION=/api/REST -DDOMAINS_EXTRACT_ACCESS_TOKEN=/security/iam/v1,/digital/identity-access-management/v1


Existem testes que estão realizando alteração no banco de dados, para executar esses testes informar as variáveis abaixo:
MONGO_HOST
MONGO_PORT
MONGO_DATABASE
MONGO_USER
MONGO_PASSWORD

Tags para serem executadas ou ignoradas
CUCUMBER_TAGS - Tags que devem ou não ser executadas. Exemplo: ~@NoQueue - @NoQueue é um tag que marca um cenário no arquivo feature, o ~(til) passado indica para não executar essa tag, para passar mais tags separar com vírgula.


Para execução local passar o environment.name=LO para usar configurações de proxy:
> java -Denvironment.name=LO -DAPI_VERSION=/persona/v1 -DFEATURE_SOURCE=/experian-persona-domain-services/src/test/resources/features/ -DREPORT_PATH=/test_cucumber -jar experian-cucumber-runner-0.0.1-SNAPSHOT.jar


Variáveis API_VERSION, FEATURE_SOURCE e REPORT_PATH pode também ser definidas com variáveis de ambiente, não precisando passar no comando:
> java -Denvironment.name=LO -jar experian-cucumber-runner-0.0.1-SNAPSHOT.jar


Para execução no ambiente de deploy passar o profile properties que deve ser usado, há um profile para deploy com o nome de deploy, segue comando:
> java -Dcucumber.profiles.active=deploy -jar target\experian-cucumber-runner-0.0.1-SNAPSHOT.jar

#Para ambiente de deploy todas as variáveis devem ser definidas.