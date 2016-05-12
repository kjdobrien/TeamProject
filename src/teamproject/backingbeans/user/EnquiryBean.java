package teamproject.backingbeans.user;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.shiro.SecurityUtils;

import teamproject.models.Enquiry;
import teamproject.models.User;

@ManagedBean(name = "enquiry")
@SessionScoped
public class EnquiryBean implements Serializable {
	private String subject;
	private String message;
	
	
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
	
	public void sendEnquiry() {
		Enquiry enquiry = new Enquiry();
		User user = new User();
		user.setEmail(SecurityUtils.getSubject().getPrincipal().toString());
		enquiry.setUser(user);
		enquiry.setSubject(subject);
		enquiry.setMessage(message);
		enquiry.storeToDB();
		
	}
	
}
