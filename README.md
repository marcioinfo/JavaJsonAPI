# JavaJsonAPI

## About the REST APIs

Oracle Field Service Cloud Service provides multiple public REST APIs that can be used to access data stored in Field Service 
Cloud and construct integrations to other systems. These systems could be other Oracle Cloud applications or offerings, or they
could be external systems in a customer or partner network. 
Secure access to these services requires appropriate authentication for the environment, standard HTTP methods, and JSON syntax.

### Conceptual overview

This API was designed to provide integration between the Oracle field service cloud and the company infrastructure. 
API was developed in JAVA and using the native resources of JAVA language.
The code is split into two sections, first the API data integration with Oracle Cloud in order to get the data in a JSON format
to a local directory. And the parse engine responsible to transformer any JSON structure that eventually cames from the cloud.


## Authentication:

Oracle Field Service Cloud REST APIs support the following list of mechanisms that the client uses to send authentication credentials:
[HTTP Basic Authentication](https://docs.oracle.com/en/cloud/saas/field-service/18a/cxfsc/OFSC_Authentication.html#OFSC_Authentication-E6D1293C__concept-100-2CC891DF): Ensures secure and encrypted access to data over a network.

