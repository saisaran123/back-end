

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.*;

public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		response.setContentType("application/json");
		
		PrintWriter pw = response.getWriter();
		
		if (username.equals("sai")){
			response.sendRedirect("abc.html");
		}
		else{
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			pw.println("Error accured !");
			rd.include(request,response);
		}
	}

}
