package teamproject.backingbeans.admin;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import teamproject.models.Course;
import teamproject.models.DatabaseConnector;
import teamproject.models.User;


@ManagedBean(name = "viewUsers")
@SessionScoped
public class ViewUsersBean implements Serializable {
	
	private List<User> users;
	private String filterValue;
	private Course selectedCourse;
	private User selectedUser;

	public ViewUsersBean() {
		
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Course getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(Course selectedCourse) {
		this.selectedCourse = selectedCourse;
		updateUserList();
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}
	
	
	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	private void updateUserList() {
		users = new ArrayList<User>();
		FacesContext context = FacesContext.getCurrentInstance();
		DatabaseConnector db = (DatabaseConnector) context.getApplication()
				.evaluateExpressionGet(context, "#{dbConnector}",
						DatabaseConnector.class);
		ResultSet result = db.executeQuery("SELECT * FROM users WHERE Email IN (SELECT User_Id FROM users_on_courses WHERE Course_id = "+selectedCourse.getId()+" AND NOT removed)");
		try {
		while(result.next()) {
			User user = new User(result);
			users.add(user);
		}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void removeUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		DatabaseConnector db = (DatabaseConnector) context.getApplication()
				.evaluateExpressionGet(context, "#{dbConnector}",
						DatabaseConnector.class);
		db.executeInsert("UPDATE users_on_courses SET removed=true WHERE Course_id = "+selectedCourse.getId()+" AND User_id "+selectedUser.getId()+")");
	}

}
