package com.github.leozin;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "Authentication Service API",
                version = "1.0-SNAPSHOT",
                contact = @Contact(
                        name = "Leo",
                        url = "http://github.com/leozinnn",
                        email = "leozinnn@gmail.com"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class AuthServiceApplication extends Application {
}
