import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

public class Read extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject responseObject = new JSONObject();
        
        try {
            Connectdb db = Connectdb.getInstance();
            Connection connection = db.getConnection();

            String query = "SELECT * FROM user_details";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            JSONArray jsonArray = new JSONArray();
            while (resultSet.next()) {
                JSONObject object = new JSONObject();
                object.put("id", resultSet.getInt("U_no"));
                object.put("name", resultSet.getString("Name"));
                object.put("email", resultSet.getString("Email"));
                jsonArray.put(object);
            }
            responseObject.put("status", "success");
            responseObject.put("data", jsonArray);
        } catch (Exception e) {
            responseObject.put("status", "error");
            responseObject.put("message", e.getMessage());
        }
        out.println(responseObject);
        return null;
    }
}

