package org.amapyon.powerpoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.amapyon.powerpoint.model.Application;
import org.amapyon.powerpoint.model.Presentation;
import org.amapyon.powerpoint.model.SlideShowView;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Handler implements HttpHandler {
	private static SlideShowView slideShow = null;
	private static int lastPage = 0;

	@Override
	public void handle(HttpExchange t) {
		System.out.println("-------Request---------");
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

 		try {

		t.getResponseHeaders().add("Cache-Control", "no-cache");	// キャッシュしてリクエストが飛ばなくなるのを回避。IE対策。
		if ("/".equals(uri.getPath())) {
			byte[] responseHtml = html(pageNo).getBytes("UTF-8");
			t.sendResponseHeaders(200, responseHtml.length);

			System.out.println("-------HTML Response---------");
			for (Map.Entry<String, List<String>> h : t.getResponseHeaders().entrySet()) {
				System.out.println(h.getKey() + ":" + h.getValue());
			}

			OutputStream os = t.getResponseBody();
			os.write(responseHtml);
			os.close();
		} else if ("/ajax".equals(uri.getPath())) {
			String response = "" + pageNo;
			t.sendResponseHeaders(200, response.length());

			System.out.println("-------Ajax Response---------");
			for (Map.Entry<String, List<String>> h : t.getResponseHeaders().entrySet()) {
				System.out.println(h.getKey() + ":" + h.getValue());
			}
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

 		} catch(Exception e) {
 			e.printStackTrace();
 		}

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
			System.out.println("CMD=" + command);
			if (command == null) {
				return 0;
			}

			if ("START".equals(command)) {
				Presentation p = getSelectedPresentation(query);

				if (p != null) {
					slideShow = p.run();
				}
			} else if ("CONT".equals(command)) {
				Presentation p = getSelectedPresentation(query);

				if (p != null) {
					slideShow = p.run();
					slideShow.gotoSlide(lastPage);
				}
			} else if ("STOP".equals(command)) {
				if (slideShow != null) {
					lastPage = slideShow.getCurrentShowPosition();
					slideShow.exit();
					return lastPage;
				}
			} else if ("PREV".equals(command)) {
				if (slideShow != null) {
					slideShow.previous();
				} else {
					slideShow = Application.getInstance().getActivePresentation().run();
					slideShow.gotoSlide(lastPage);
					slideShow.previous();
				}
			} else if ("NEXT".equals(command)) {
				if (slideShow != null) {
					slideShow.next();
				} else {
					slideShow = Application.getInstance().getActivePresentation().run();
					slideShow.gotoSlide(lastPage);
					slideShow.next();
				}
			} else if ("JUMP".equals(command)) {
				if (slideShow != null) {
					int page = Integer.parseInt(getParameter(query, "PAGE"));
					slideShow.gotoSlide(page);
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
		return slideShow.getCurrentShowPosition();
	}

	private Presentation getSelectedPresentation(String query) {
		String presentationNo = getParameter(query, "PRESENTATION");
		System.out.println("PRESENTSTION=" + presentationNo);
		Presentation p;
		if (presentationNo == null || presentationNo.equals("")) {
			p = Application.getInstance().getActivePresentation();
		} else {
			int presentation = Integer.parseInt(getParameter(query, "PRESENTATION"));
			p = Application.getInstance().getPresentations().getItem(presentation);
		}
		return p;
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
		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
		html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"ja\" lang=\"ja\">\n");
		html.append("<HEAD>\n");
		html.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\" />\n");
		html.append("<script type=\"text/javascript\">\n");
		html.append("function sendCommand(command) {\n");
		html.append("  var ajax = createAjax();\n");
		html.append("  if (ajax == null) return;\n");
		html.append("  ajax.onreadystatechange = function() {\n");
		html.append("    if (ajax.readyState == 4 && ajax.status == 200) {\n");
		html.append("      var pageNo = ajax.responseText;\n");
		html.append("      setPageNo(pageNo);\n");
		html.append("    }\n");
		html.append("  };\n");
		html.append("  ajax.open('get', '/ajax?CMD=' + command, true );");
		html.append("  ajax.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');");
		html.append("  ajax.send(null);");
		html.append("};\n");
		html.append("function setPageNo(pageNo) {");
		html.append("  document.PANEL.PAGE.value=pageNo;");
		html.append("};\n");
		html.append("function getPageNo() {");
		html.append("  return document.PANEL.PAGE.value;");
		html.append("};\n");
		html.append("function getPresentationNo() {");
		html.append("  return document.PANEL.PRESENTATION.value;");
		html.append("};\n");
		html.append("function createAjax() {");
		html.append("  try {\n");
		html.append("    var ajax = new XMLHttpRequest();\n");
		html.append("    return ajax;\n");
		html.append("  } catch(e) {\n");
		html.append("    try {\n");
		html.append("      var ajax = new ActiveXObject('Microsoft.XMLHTTP');\n");
		html.append("      return ajax;\n");
		html.append("    } catch(e) {\n");
		html.append("    }\n");
		html.append("  }\n");
		html.append("  return null;");
		html.append("};\n");
		html.append("</script>");
		html.append("<TITLE>PowerPoint Controler</TITLE>\n");
		html.append("</HEAD>\n");
		html.append("<BODY>\n");
		html.append("<FORM METHOD=\"GET\" NAME=\"PANEL\">\n");
		html.append(listbox() + "\n");
		html.append("<BR />\n");
		html.append(button("START", "font-size:56px;width:300px;height:120px", "sendCommand('START&PRESENTATION=' + getPresentationNo())") + "\n");
		html.append(button("CONT", "font-size:56px;width:300px;height:120px", "sendCommand('CONT&PRESENTATION=' + getPresentationNo())") + "\n");
		html.append(button("STOP") + "\n");
		html.append("<BR />\n");
		html.append("<BR />\n");
		html.append(button("FIRST") + "\n");
		html.append(button("LAST") + "\n");
		html.append("<BR />\n");
		html.append("<BR />\n");
		html.append(button("BLACK") + "\n");
		html.append(button("WHITE") + "\n");
		html.append("<BR />\n");
		html.append("<BR />\n");
		html.append("<INPUT TYPE=\"TEXT\" NAME=\"PAGE\" VALUE=\"" + page + "\" STYLE=\"font-size:56px;width:300px;height:80px\">\n");
		html.append(button("JUMP", "font-size:56px;width:300px;height:120px", "sendCommand('JUMP&PAGE=' + getPageNo())") + "\n");
		html.append("<BR />\n");
		html.append("<BR />\n");
		html.append(button("PREV", "font-size:56px;width:300px;height:240px") + "\n");
		html.append(button("NEXT", "font-size:56px;width:300px;height:240px") + "\n");
		html.append("<BR />\n");
		html.append("<BR />\n");
		html.append("</FORM>\n");
		html.append("</BODY>\n");
		html.append("</html>\n");

		return html.toString();
	}

	private String button(String title) {
		return button(title, "font-size:56px;width:300px;height:120px");
	}

	private String button(String title, String style) {
		return button(title, style, String.format("sendCommand('%s')", title));
	}

	private String button(String title, String style, String command) {
		return  "<INPUT TYPE=\"BUTTON\" NAME=\"CMD\" VALUE=\"" + title + "\" STYLE=\"" + style + "\" onClick=\"" + command + "\">\n";
	}

	private String listbox() {
		String str = "<SELECT name=\"PRESENTATION\" size=\"1\" style=\"font-size:28px\">\n";

		int i = 1;
		for (Presentation p : Application.getInstance().getPresentations()) {
			str += String.format("<OPTION value=\"%d\" style=\"font-size:28px\">%s</OPTION>%n", i++, p.getName());
		}

		str += "</SELECT>\n";
		return str;
	}

}
