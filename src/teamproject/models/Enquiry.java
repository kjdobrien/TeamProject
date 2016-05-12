package teamproject.models;

import java.sql.Date;
import java.sql.ResultSet;

import javax.faces.context.FacesContext;

import teamproject.backingbeans.user.BrowseCourseBean;

public class Enquiry implements DatabaseObject {
	
	private int id;
	private Course course;
	private User user;
	private String subject;
	private String message;
	private Date date;
	
	private DatabaseConnector db;
	
	public Enquiry() {
		this.id = -1;
		FacesContext context = FacesContext.getCurrentInstance();
		BrowseCourseBean browseCourses = (BrowseCourseBean) context.getApplication()
				.evaluateExpressionGet(context, "#{browseCourses}",
						BrowseCourseBean.class);
		course = browseCourses.getSelectedCourse();
		user = new User(1);
	}

	public Enquiry(ResultSet result) {
		try {
			this.setId(result.getInt("id"));
			this.setSubject(result.getString("Subject"));
			this.setMessage(result.getString("Message"));
			this.setCourse(new Course(result.getInt("course_id")));
			this.setUser(new User(result.getInt("User")));
			this.setDate(result.getDate("Timestamp"));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public Enquiry(int id) {
		this.id = id;
		updateFromDB();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}
	

	private void setDate(Date date) {
		this.date = date;
	}

	@Override
	public void updateFromDB() {
		if (id >= 0) {
			connectToDB();
			ResultSet result = db
					.executeQuery("select * from enquiries where id = " + id);
			try {
				result.first();
				this.setSubject(result.getString("Subject"));
				this.setMessage(result.getString("Message"));
				this.setCourse(new Course(result.getInt("course_id")));
				this.setUser(new User(result.getInt("User")));
				this.setDate(result.getDate("Timestamp"));
				
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		} else {
			System.err
					.println("Can't update: This object is not currently in the database");
		}

	}
	
	

	@Override
	public void storeToDB() {
		connectToDB();
		String insertQuery;
		insertQuery = "INSERT INTO enquiries(course_id, User, Subject, Message) VALUES("
				+ course.getId()
				+ ","
				+ user.getId()
				+ ",'"
				+ subject
				+ "','"
				+ message + "');";
		db.executeInsert(insertQuery);
	}

	private void connectToDB() {
		FacesContext context = FacesContext.getCurrentInstance();
		db = (DatabaseConnector) context.getApplication()
				.evaluateExpressionGet(context, "#{dbConnector}",
						DatabaseConnector.class);
	}
}
