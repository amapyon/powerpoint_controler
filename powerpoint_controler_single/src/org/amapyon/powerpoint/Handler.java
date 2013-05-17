package org.amapyon.powerpoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

		System.out.println("path:" + uri.getPath());
		System.out.println("query:" + uri.getQuery());

		int pageNo = process(uri.getQuery());

 		InputStream is = t.getRequestBody();
 		read(is);

		String response = "";
		if ("/".equals(uri.getPath())) {
			response = html(pageNo);
		} else if ("/ajax".equals(uri.getPath())) {
			response = "" + pageNo;
		}
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();

	}

	private void read(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		try {
			String input = reader.readLine();
			int i = 0;
			while (input != null) {
				System.out.println("" + i + ":" + input);
				input = reader.readLine();
			}
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}

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
				if (slideShow != null) {
					slideShow.prev();
				}
			} else if ("NEXT".equals(command)) {
				if (slideShow != null) {
					slideShow.next();
				}
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
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"ja\" lang=\"ja\">\n"
				+ "<HEAD>\n"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\" />\n"
				+ "<script type=\"text/javascript\">\n"
				+ "function sendCommand(command) {"
				+ "  var ajax = new XMLHttpRequest();"
				+ "  ajax.onreadystatechange = function() {"
				+ "    if (ajax.readyState == 4 && ajax.status == 200) {"
				+ "      var pageNo = ajax.responseText;"
				+ "      setPageNo(pageNo);"
				+ "    }"
				+ "  };"
				+ "  ajax.open('get', '/ajax?CMD=' + command, true );"
				+ "  ajax.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');"
				+ "  ajax.send(null);"
				+ "};\n"
				+ "function setPageNo(pageNo) {"
				+ "  document.PANEL.PAGE.value=pageNo;"
				+ "};\n"
				+ "function getPageNo() {"
				+ "  return document.PANEL.PAGE.value;"
				+ "};\n"
				+ "</script>"
				+ "<TITLE>PowerPoint Controler</TITLE>\n"
				+ "</HEAD>\n"
				+ "<BODY>\n"
				+ "<FORM METHOD=\"GET\" NAME=\"PANEL\">\n"
				+ "<INPUT TYPE=\"BUTTON\" NAME=\"CMD\" VALUE=\"START\" STYLE=\"font-size:56px;width:300px;height:120px\" onClick=\"sendCommand('START')\">\n"
				+ "<INPUT TYPE=\"BUTTON\" NAME=\"CMD\" VALUE=\"CONT\" STYLE=\"font-size:56px;width:300px;height:120px\" onClick=\"sendCommand('CONT')\">\n"
				+ "<INPUT TYPE=\"BUTTON\" NAME=\"CMD\" VALUE=\"STOP\" STYLE=\"font-size:56px;width:300px;height:120px\" onClick=\"sendCommand('STOP')\">\n"
				+ "<BR />\n"
				+ "<BR />\n"
				+ "<INPUT TYPE=\"BUTTON\" NAME=\"CMD\" VALUE=\"FIRST\" STYLE=\"font-size:56px;width:300px;height:120px\" onClick=\"sendCommand('FIRST')\">\n"
				+ "<INPUT TYPE=\"BUTTON\" NAME=\"CMD\" VALUE=\"LAST\" STYLE=\"font-size:56px;width:300px;height:120px\" onClick=\"sendCommand('LAST')\">\n"
				+ "<BR />\n"
				+ "<BR />\n"
				+ "<INPUT TYPE=\"BUTTON\" NAME=\"CMD\" VALUE=\"BLACK\" STYLE=\"font-size:56px;width:300px;height:120px\" onClick=\"sendCommand('BLACK')\">\n"
				+ "<INPUT TYPE=\"BUTTON\" NAME=\"CMD\" VALUE=\"WHITE\" STYLE=\"font-size:56px;width:300px;height:120px\" onClick=\"sendCommand('WHITE')\">\n"
				+ "<BR />\n"
				+ "<BR />\n"
				+ "<INPUT TYPE=\"TEXT\" NAME=\"PAGE\" VALUE=\"" + page + "\" STYLE=\"font-size:56px;width:300px;height:80px\">\n"
				+ "<INPUT TYPE=\"BUTTON\" NAME=\"CMD\" VALUE=\"JUMP\" STYLE=\"font-size:56px;width:300px;height:120px\" onClick=\"sendCommand('JUMP&PAGE=' + getPageNo())\">\n"
				+ "<BR />\n"
				+ "<BR />\n"
				+ "<INPUT TYPE=\"BUTTON\" NAME=\"CMD\" VALUE=\"PREV\" STYLE=\"font-size:56px;width:300px;height:240px\" onClick=\"sendCommand('PREV')\">\n"
				+ "<INPUT TYPE=\"BUTTON\" NAME=\"CMD\" VALUE=\"NEXT\" STYLE=\"font-size:56px;width:300px;height:240px\" onClick=\"sendCommand('NEXT')\">\n"
				+ "<BR />\n"
				+ "<BR />\n"
				+ "</FORM>\n"
				+ "</BODY>\n"
				+ "</html>\n";

		return html;
	}

}