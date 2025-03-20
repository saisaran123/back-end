import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

public class Update extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject responseObject = new JSONObject();
        
        try {
            Connectdb db = Connectdb.getInstance();
            Connection connection = db.getConnection();

            BufferedReader reader = request.getReader();
    		JSONObject jsondata = new JSONObject(reader.readLine());
    		int num = jsondata.getInt("id");
            String name = jsondata.getString("name");
            String email = jsondata.getString("email");

            String query = "UPDATE user_details SET Name=?, Email=? WHERE U_no=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setInt(3, num);
            
            int result = statement.executeUpdate();

            if (result > 0) {
                responseObject.put("status", "updated");
            } else {
                responseObject.put("status", "failed");
            }
        } catch (Exception e) {
            responseObject.put("status", "error");
            responseObject.put("message", e.getMessage());
        }
        out.println(responseObject);
        return null;
    }
}


