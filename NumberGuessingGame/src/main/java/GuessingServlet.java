
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import javax.servlet.http.Cookie;
import java.util.Random;
import java.io.*;

@WebServlet("/GuessingServlet")
public class GuessingServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cookie Number = null;
		Cookie attempt = null;
		boolean isExist = false;
		boolean attempExist = false;
		Cookie cookies[] = request.getCookies();
		response.setContentType("application/json");
		JSONObject jsonres = new JSONObject();
		PrintWriter out = response.getWriter();
		BufferedReader reader = request.getReader();
		JSONObject jsondata = new JSONObject(reader.readLine());
		int num = jsondata.getInt("num");
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals("Number")) {
					isExist = true;
					Number = c;
				} else if (c.getName().equals("attempt")) {
					attempExist = true;
					attempt = c;
				}
			}
		}
		if (!isExist) {
			Number = new Cookie("Number", (new Random().nextInt(99) + 1) + "");
			response.addCookie(Number);
		}
		if (!attempExist) {
			attempt = new Cookie("attempt", 0 + "");
			response.addCookie(attempt);
		}
		if (num == Integer.parseInt(Number.getValue())) {
			jsonres.put("stat", 0);
			Number.setMaxAge(0);
			response.addCookie(Number);
			attempt.setValue(0 + "");
			
		} else if (num > Integer.parseInt(Number.getValue())) {
			jsonres.put("stat", 1);
			attempt.setValue((Integer.parseInt(attempt.getValue()) + 1) + "");

		} else {
			jsonres.put("stat", 2);
			attempt.setValue((Integer.parseInt(attempt.getValue()) + 1) + "");
		}
		response.addCookie(attempt);

		out.print(jsonres.toString());
		out.flush();
	}

}
