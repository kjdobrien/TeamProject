package teamproject.models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "userFinder")
@SessionScoped
public class UserFinder implements Serializable {

	public String getCourse() {
		Course course = new Course(1);
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("CreatedBy", "Ruairí");
		
		System.out.println(findUsersByAttribute(attributes));
		return course.toString();
	}

	public List<User> findUsersByAttribute(Map<String, String> attributes) {
		List<User> userList = new ArrayList<User>();
		FacesContext context = FacesContext.getCurrentInstance();
		DatabaseConnector db = (DatabaseConnector) context.getApplication()
				.evaluateExpressionGet(context, "#{dbConnector}",
						DatabaseConnector.class);
		String query = "Select * from users where ";
		for (String attribute : attributes.keySet()) {
			query += attribute + " = '" + attributes.get(attribute) + "' AND ";
		}
		query = query.substring(0, query.length() - 5)+";";
		System.out.println(query);
		ResultSet result = db.executeQuery(query);
		try {
			while (result.next()) {
				User user = new User(result);
				userList.add(user);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return userList;
	}

}