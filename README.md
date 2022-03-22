# A uthentication Microservice
authentication-service is an authentication microservice with jwt
## Keep it simple
The login and registration services are cumbersome and difficult, and they create a huge load on the servers.

The idea of this project is an example of how to make an external service that is in charge of generating jwt tokens with the necessary user data and signed by its own certificate

## How to use
The use of this service is simple, all you have to do is modify the service configuration file, put a different database to the one used in the rest of the services to prevent them from saturating each other and if you want to encrypt plus the password that is stored encrypted in the database. And if you want, add roles to users.

Then in the services that require authentication, you would only have to verify that the jwt token has not expired and that it is signed with the certificate itself.

Once verified, we can obtain the necessary data by accessing the certificate data.