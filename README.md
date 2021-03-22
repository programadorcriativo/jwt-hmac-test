# O que é esperado

Entender como criar, assinar e validar um token JWT seguindo a especificação 

##  Partes de um token JWT

Um token é composto por 3 partes. 

### Header
O Header é um objeto JSON que especifica informações sobre o tipo do token `typ` e o algorítmo de criptografia usado para gerar a sua assinatura `alg`

### Payload
O Payload é um objeto JSON com as informações específicas de uma entidade também conhecidas como `claims`. 
Além disso, o payload pode possui claims reservadas que podem ser utilizadas na validação de segurança de uma API.
São elas:
- sub (subject): Valor que identifica unicamente o payload
- iss (issuer): Emissor do token
- exp (expiration): Timestamp de quando o token irá expirar
- iat (issued at): Timestamp de quando o token foi criado
- aud (audience): A aplicação que irá usá-lo.

Todas as claims podem ser verificadas na própria especificação https://tools.ietf.org/html/rfc7519

### Assinatura
A assinatura é um hash gerado a partir da concatenação do Header com Payload usando base64UrlEncode utilizando uma chave secreta ou certificado RSA. 

### Projeto

Nesse projeto foi utilizado somente algorítmo HMAC com sha256. 

O projeto possui testes que validam:
1. Geração de token
2. Validação de assinatura
3. Validação de expiração de token utilizando a claim exp