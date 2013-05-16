package org.amapyon.powerpoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Handler implements HttpHandler {
	private static SlideShow slideShow = null;
	private static int lastPage = 0;

	@Override
	public void handle(HttpExchange t) throws IOException {
		Headers headers = t.getRequestHeaders();
		for (Map.Entry<String, List<String>> h : headers.entrySet()) {
			System.out.println(h.getKey() + ":" + h.getValue());
		}
		URI uri = t.getRequestURI();

		System.out.println(uri.getQuery());

		int page = process(uri.getQuery());

// 		InputStream is = t.getRequestBody();
// 		read(is)
		String response = html(page);
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();

	}

	private int process(String query) {
		try {
			String command = getParameter(query, "CMD");
			if (command == null) {
				return 0;
			}


			if ("START".equals(command)) {
				slideShow = Application.getInstance().getActivePresentation().run();
			} else if ("CONT".equals(command)) {
				slideShow = Application.getInstance().getActivePresentation().run();
				slideShow.jump(lastPage);
			} else if ("STOP".equals(command)) {
				if (slideShow != null) {
					lastPage = slideShow.getCurrentPage();
					slideShow.quit();
					return lastPage;
				}
			} else if ("PREV".equals(command)) {
				if (slideShow != null) slideShow.prev();
			} else if ("NEXT".equals(command)) {
				if (slideShow != null) slideShow.next();
			} else if ("JUMP".equals(command)) {
				if (slideShow != null) {
					int page = Integer.parseInt(getParameter(query, "PAGE"));
					slideShow.jump(page);
				}
			} else if ("BLACK".equals(command)) {
				if (slideShow != null) {
					slideShow.blackScreen();
				}
			} else if ("WHITE".equals(command)) {
				if (slideShow != null) {
					slideShow.whiteScreen();
				}
			} else if ("FIRST".equals(command)) {
				if (slideShow != null) {
					slideShow.first();
				}
			} else if ("LAST".equals(command)) {
				if (slideShow != null) {
					slideShow.last();
				}
			}


			if (slideShow == null) {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return slideShow.getCurrentPage();
	}

	private String getParameter(String query, String name) {
		if (query == null) {
			return null;
		}
		String[] params = query.split("&");
		for (String param : params) {
			if (param.startsWith(name)) {
				return param.substring(name.length() + 1);
			}
		}
		return null;
	}

	private String html(int page) {
		String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
				+ "<HTML>\n"
				+ "<HEAD>\n"
				+ "<TITLE>PowerPoint Controler</TITLE>\n"
				+ "</HEAD>\n"
				+ "<BODY>\n"
				+ "<FORM METHOD=\"GET\">\n"
				+ "<INPUT TYPE=\"SUBMIT\" NAME=\"CMD\" VALUE=\"START\" STYLE=\"font-size:56px;width:300px;height:120px\">\n"
				+ "<INPUT TYPE=\"SUBMIT\" NAME=\"CMD\" VALUE=\"CONT\" STYLE=\"font-size:56px;width:300px;height:120px\">\n"
				+ "<INPUT TYPE=\"SUBMIT\" NAME=\"CMD\" VALUE=\"STOP\" STYLE=\"font-size:56px;width:300px;height:120px\">\n"
				+ "<BR />\n"
				+ "<BR />\n"
				+ "<INPUT TYPE=\"SUBMIT\" NAME=\"CMD\" VALUE=\"FIRST\" STYLE=\"font-size:56px;width:300px;height:120px\">\n"
				+ "<INPUT TYPE=\"SUBMIT\" NAME=\"CMD\" VALUE=\"LAST\" STYLE=\"font-size:56px;width:300px;height:120px\">\n"
				+ "<BR />\n"
				+ "<BR />\n"
				+ "<INPUT TYPE=\"SUBMIT\" NAME=\"CMD\" VALUE=\"BLACK\" STYLE=\"font-size:56px;width:300px;height:120px\">\n"
				+ "<INPUT TYPE=\"SUBMIT\" NAME=\"CMD\" VALUE=\"WHITE\" STYLE=\"font-size:56px;width:300px;height:120px\">\n"
				+ "<BR />\n"
				+ "<BR />\n"
				+ "<INPUT TYPE=\"TEXT\" NAME=\"PAGE\" VALUE=\"" + page + "\" STYLE=\"font-size:56px;width:300px;height:80px\">\n"
				+ "<INPUT TYPE=\"SUBMIT\" NAME=\"CMD\" VALUE=\"JUMP\" STYLE=\"font-size:56px;width:300px;height:120px\">\n"
				+ "<BR />\n"
				+ "<BR />\n"
				+ "<INPUT TYPE=\"SUBMIT\" NAME=\"CMD\" VALUE=\"PREV\" STYLE=\"font-size:56px;width:300px;height:240px\">\n"
				+ "<INPUT TYPE=\"SUBMIT\" NAME=\"CMD\" VALUE=\"NEXT\" STYLE=\"font-size:56px;width:300px;height:240px\">\n"
				+ "<BR />\n"
				+ "</FORM>\n"
				+ "</BODY>\n"
				+ "</HTML>\n";

		return html;
	}

}
