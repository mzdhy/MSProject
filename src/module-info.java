module linuxController {
	requires ganymed.ssh2;
	requires jdk.httpserver;
	requires java.desktop;
	requires com.google.gson;

	opens linuxController;
}