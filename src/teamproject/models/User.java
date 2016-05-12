package teamproject.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name = "user")
public class User implements DatabaseObject {
	private int id; 	  	
	private String StudentName;
	private boolean Gender;
	private Date Birthdate;  	  	
	private String Address;	  	  	
	private String Nationality;  	  	
	private String Telephone;	  	  	
	private String PostCode;	  	
	private String Email;  	  	
	private String Password; 	  	  	
	private String EducationLevel;
	private List<String> roles = new ArrayList<String>();
	
	private DatabaseConnector db;
	
	public User() {
		this.id = -1;
	}

	

	public User(ResultSet result) {
		try {
			this.setId(result.getInt("id"));
			this.setStudentName(result.getString("StudentName"));
			this.setGender(result.getBoolean("Gender") ? "Male":"Female");
			this.setBirthdate(result.getDate("Birthdate"));
			this.setAddress(result.getString(" Address"));
			this.setNationality(result.getString("Nationality"));
			this.setTelephone(result.getString("Telephone"));
			this.setPostCode(result.getString("PostCode"));
			this.setEmail(result.getString("Email"));
			this.setPassword(result.getString("Password"));
			this.setEducationLevel(result.getString("EducationLevel"));
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	private void setId(int id) {
		this.id = id;
	}



	public User(int id) {
		this.id = id;
		updateFromDB();
	}
	
	public String getStudentName() {
		return StudentName;
	}

	public void setStudentName(String studentName) {
		StudentName = studentName;
	}


	public String getGender() {
		return Gender ? "male":"female";
	}

	public void setGender(String gender) {
		Gender = gender.equals("male");
	}

	public Date getBirthdate() {
		return Birthdate;
	}

	public void setBirthdate(Date birthdate) {
		Birthdate = birthdate;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getNationality() {
		return Nationality;
	}

	public void setNationality(String nationality) {
		this.Nationality = nationality;
	}

	public String getTelephone() {
		return Telephone;
	}

	public void setTelephone(String telephone) {
		Telephone = telephone;
	}

	public String getPostCode() {
		return PostCode;
	}

	public void setPostCode(String postCode) {
		PostCode = postCode;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getEducationLevel() {
		return EducationLevel;
	}

	public void setEducationLevel(String educationLevel) {
		EducationLevel = educationLevel;
	}
	
	

	public List<String> getRoles() {
		return roles;
	}



	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void addRole(String role){
		roles.add(role);
	}

	public int getId() {
		return id;
	}
	
	public String toString() {
		return StudentName + " : " + Email;
	}
	
	@Override
	public void updateFromDB() {
		if (id >= 0) {
			connectToDB();
			ResultSet result = db
					.executeQuery("select * from users where id = " + id);
			ResultSet rolesResult = db
					.executeQuery("select Role from user_roles where UserId = " + id);
			try {
				result.first();
				this.setStudentName(result.getString("StudentName"));
				this.setGender(result.getBoolean("Gender") ? "Male":"Female");
				this.setBirthdate(result.getDate("Birthdate"));
				this.setAddress(result.getString(" Address"));
				this.setNationality(result.getString("Nationality"));
				this.setTelephone(result.getString("Telephone"));
				this.setPostCode(result.getString("PostCode"));
				this.setEmail(result.getString("Email"));
				this.setPassword(result.getString("Password"));
				this.setEducationLevel(result.getString("EducationLevel"));
				while(rolesResult.next()){
					roles.add(rolesResult.getString("Role"));
				}
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
		if (id >= 0) {
			insertQuery = "INSERT INTO users VALUES(" + id + ",'" + 	  	 
					StudentName + "'," + 	  	  	
					getGender() + ",'" + 
					format.format(Birthdate) + "','" +  	  	 
					Address	+ "','" +   	  	 
					Nationality + "','" +  	  	 
					Telephone + "','" + 	  	  	 
					PostCode + "','" + 	  	 
					Email + "','" +   	  	 
					Password + "','" +  	  	  	 
					EducationLevel + "');";
		} else {
			insertQuery = "INSERT INTO users VALUES( NULL ,'" + StudentName + "','" + 	  	  	 	 
					getGender() + "','" + 
					format.format(Birthdate) + "','" +  	  	 
					Address	+ "','" +   	  	 
					Nationality + "','" +  	  	 
					Telephone + "','" + 	  	  	 
					PostCode + "','" + 	  	 
					Email + "','" +   	  	 
					Password + "','" +  	  	  	 
					EducationLevel + "');";
		}
		db.executeInsert(insertQuery);
		for(String role: roles) {
			db.executeInsert("INSERT INTO user_roles (SELECT id, '"+role+"' FROM users WHERE email ='"+Email+"');" );
		}
	}
	
	private void connectToDB() {
		FacesContext context = FacesContext.getCurrentInstance();
		db = (DatabaseConnector) context.getApplication()
				.evaluateExpressionGet(context, "#{dbConnector}",
						DatabaseConnector.class);
	}
}
