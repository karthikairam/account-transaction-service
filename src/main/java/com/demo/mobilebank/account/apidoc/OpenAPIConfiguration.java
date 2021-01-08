package com.demo.mobilebank.account.apidoc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Mobile Challenger Bank - Demo Service - OpenAPI 3.0 documentation",
                description = "These APIs are used to create/retrieve account details," +
                        " and transfer money from one account to another account.",
                contact = @Contact(
                        name = "Karthikaiselvan R",
                        url = "https://www.linkedin.com/in/karthikairam",
                        email = "karthikairam@gmail.com"
                ),
                license = @License(
                        name = "MIT Licence",
                        url = "")),
        servers = @Server(url = "http://localhost:8080")
)
public class OpenAPIConfiguration {
}
