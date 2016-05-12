package teamproject.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name = "course")
public class Course implements DatabaseObject {
	private int id;
	private String title;
	private String description;
	private String image;
	private String video;
	private Date startDate;
	private Date endDate;
	private String venue;
	private int fee;
	private String department;
	private int maxStudents;
	private int currentStudents;
	private String createdBy;
	
	private DatabaseConnector db;
	
	public Course() {
		this.id = -1;
	}

	public Course(ResultSet result) {
		try {
			this.setId(result.getInt("id"));
			this.setTitle(result.getString("Title"));
			this.setDescription(result.getString("Description"));
			this.setImage(result.getString("Image"));
			this.setVideo(result.getString("VideoInformation"));
			this.setVenue(result.getString("Venue"));
			this.setFee(result.getInt("Fee"));
			this.setStartDate(result.getDate("StartDate"));
			this.setEndDate(result.getDate("EndDate"));
			this.setMaxStudents(result.getInt("MaxStudents"));
			this.setCurrentStudents(result.getInt("CurrentStudent"));
			this.setCreatedBy(result.getString("CreatedBy"));
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public Course(int id) {
		this.id = id;
		updateFromDB();
	}

	public int getId() {
		return id;
	}

	private void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getMaxStudents() {
		return maxStudents;
	}

	public void setMaxStudents(int maxStudents) {
		this.maxStudents = maxStudents;
	}

	public int getCurrentStudents() {
		return currentStudents;
	}

	public void setCurrentStudents(int currentStudents) {
		this.currentStudents = currentStudents;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String toString() {
		return id + "," + title + "," + description + "," + image + "," + video
				+ "," + startDate + "," + endDate + "," + venue + "," + fee
				+ "," + department + "," + maxStudents + "," + currentStudents
				+ "," + createdBy;
	}

	@Override
	public void updateFromDB() {
		if (id >= 0) {
			connectToDB();
			ResultSet result = db
					.executeQuery("select * from course_view where id = " + id);
			try {
				result.first();
				this.setTitle(result.getString("title"));
				this.setDescription(result.getString("Description"));
				this.setImage(result.getString("Image"));
				this.setVideo(result.getString("VideoInformation"));
				this.setVenue(result.getString("Venue"));
				this.setFee(result.getInt("Fee"));
				this.setStartDate(result.getDate("StartDate"));
				this.setEndDate(result.getDate("EndDate"));
				this.setMaxStudents(result.getInt("MaxStudents"));
				this.setCurrentStudents(result.getInt("CurrentStudent"));
				this.setCreatedBy(result.getString("CreatedBy"));
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
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String insertQuery;
		String descQuery="";
		if (id >= 0) {
			insertQuery = "UPDATE courses set Image='" + image + "',VideoInformation='" + video + "',StartDate='"
					+ format.format(startDate) + "',EndDate='" + format.format(endDate) + "',Venue='" + venue + "',Fee=" + fee + ",Department="
					+ 1 + ",MaxStudents=" + maxStudents
					+ ",CreatedBy=" + createdBy + " WHERE id = "+id+";";
			descQuery = "UPDATE course_info SET Title='" + title
					+ "',Description='" + description + "' WHERE CourseId="+id+";";
		} else {
			insertQuery = "INSERT INTO courses VALUES( NULL ,'" + image + "','" + video + "','" + format.format(startDate)
					+ "','" + format.format(endDate) + "','" + venue + "'," + fee + ","
					+ 1 + "," + maxStudents + "," + currentStudents
					+ "," + createdBy + ");";
		}
		long newId = db.executeInsert(insertQuery);
		if(descQuery == "") {
			descQuery = "INSERT INTO course_info VALUES(" + newId + ",'en','" + title
					+ "','" + description + "');";
		}
		db.executeInsert(descQuery);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Course saved!"));
	}
	
	private void connectToDB() {
		FacesContext context = FacesContext.getCurrentInstance();
		db = (DatabaseConnector) context.getApplication()
				.evaluateExpressionGet(context, "#{dbConnector}",
						DatabaseConnector.class);
	}
}
