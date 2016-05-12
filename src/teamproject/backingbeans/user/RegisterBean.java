package teamproject.backingbeans.user;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import teamproject.models.User;

@ManagedBean(name = "register")
@SessionScoped
public class RegisterBean implements Serializable {

	private User newUser;
	
	public RegisterBean() {
		newUser = new User();
	}

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}
	
	public String registerUser() {
		newUser.addRole("user");
		newUser.storeToDB();
		return "login";
	}

}
