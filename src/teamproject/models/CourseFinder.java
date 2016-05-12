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

@ManagedBean(name = "courseFinder")
@SessionScoped
public class CourseFinder implements Serializable {

	

//	public String getCourse() {
//		Course course = new Course(1);
//		Map<String, String> attributes = new HashMap<String, String>();
//		attributes.put("CreatedBy", "Ruairí");
//		
//		System.out.println(findCoursesByAttribute(attributes));
//		return course.toString();
//	}

	public List<Course> findCoursesByAttribute(Map<String, String> attributes) {
		List<Course> courseList = new ArrayList<Course>();
		FacesContext context = FacesContext.getCurrentInstance();
		DatabaseConnector db = (DatabaseConnector) context.getApplication()
				.evaluateExpressionGet(context, "#{dbConnector}",
						DatabaseConnector.class);
		String query;
		if(attributes != null) {
			query = "Select * from course_view where ";
			for (String attribute : attributes.keySet()) {
				query += attribute + " = '" + attributes.get(attribute) + "' AND ";
			}
			query = query.substring(0, query.length() - 5)+";";
			System.out.println(query);
		} else {
			query = "Select * from course_view;";
		}
		ResultSet result = db.executeQuery(query);
		try {
			while (result.next()) {
				Course course = new Course(result);
				courseList.add(course);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return courseList;
	}

}