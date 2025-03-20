import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import org.json.JSONObject;
import java.io.PrintWriter;

public class struct_test extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

  
        String name = request.getParameter("username");
        String password = request.getParameter("password");


        JSONObject jsonResponse = new JSONObject();
        PrintWriter out = response.getWriter();

        if ("joel".equals(name) && "joel@1234".equals(password)) {
            jsonResponse.put("status", "success");
            jsonResponse.put("message", "Login successful");
            jsonResponse.put("username", name);
        } else {
            jsonResponse.put("status", "fail");
            jsonResponse.put("message", "Invalid username or password");
        }


        out.print(jsonResponse.toString());
        out.flush();

   
        return null;
    }
}
