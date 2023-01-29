module linuxController {
	requires ganymed.ssh2;
	requires jdk.httpserver;
	requires java.desktop;
	requires com.google.gson;
	requires jsch;
	requires commons.vfs2;
	requires apache.commons.net;

	opens linuxController;
}