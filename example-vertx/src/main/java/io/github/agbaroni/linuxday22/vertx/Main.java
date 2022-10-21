package io.github.agbaroni.linuxday22.vertx;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.SelfSignedCertificate;

public class Main {
    public static void main(String... args) {
	var vertx = Vertx.vertx();
	var options = new HttpServerOptions()
	                    .setUseAlpn(true)
	                    .setSsl(true)
	                    .setKeyCertOptions(SelfSignedCertificate
					         .create()
					         .keyCertOptions())
 	                    .setLogActivity(true);
	var server = vertx.createHttpServer(options);

	server.requestHandler(request -> {
		request.bodyHandler(body -> {
			System.console().writer().println(body);
		    });

		request.response().end();
	    });

	server.listen(8080);
    }
}
