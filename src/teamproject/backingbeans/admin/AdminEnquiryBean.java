package teamproject.backingbeans.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import teamproject.email.GMailSender;
import teamproject.models.Course;
import teamproject.models.Enquiry;
import teamproject.models.EnquiryFinder;

/**
 * The Class AdminEnquiryBean.
 */
@ManagedBean(name = "adminEnquiry")
@SessionScoped
public class AdminEnquiryBean implements Serializable {
	
	/** The enquiries. */
	private List<Enquiry> enquiries;
	
	/** The filtered enquiries. */
	private List<Enquiry> filteredEnquiries;
	
	/** The selected enquiry. */
	private Enquiry selectedEnquiry;
	
	private String replySubject = "Response to enquiry";
	
	private String replyMessage;
	
	/**
	 * Instantiates a new admin enquiry bean.
	 */
	public AdminEnquiryBean() {
		enquiries = new ArrayList<Enquiry>();
		FacesContext context = FacesContext.getCurrentInstance();
		EnquiryFinder enquiryfinder = (EnquiryFinder) context.getApplication()
				.evaluateExpressionGet(context, "#{enquiryFinder}",
						EnquiryFinder.class);
		enquiries = enquiryfinder.getEnquiries();
	}

	/**
	 * Gets the enquiries.
	 *
	 * @return the enquiries
	 */
	public List<Enquiry> getEnquiries() {
		return enquiries;
	}

	/**
	 * Sets the enquiries.
	 *
	 * @param enquiries the new enquiries
	 */
	public void setEnquiries(List<Enquiry> enquiries) {
		this.enquiries = enquiries;
	}

	/**
	 * Gets the filtered enquiries.
	 *
	 * @return the filtered enquiries
	 */
	public List<Enquiry> getFilteredEnquiries() {
		return filteredEnquiries;
	}

	/**
	 * Sets the filtered enquiries.
	 *
	 * @param filteredEnquiries the new filtered enquiries
	 */
	public void setFilteredEnquiries(List<Enquiry> filteredEnquiries) {
		this.filteredEnquiries = filteredEnquiries;
	}

	/**
	 * Gets the selected enquiry.
	 *
	 * @return the selected enquiry
	 */
	public Enquiry getSelectedEnquiry() {
		return selectedEnquiry;
	}

	/**
	 * Sets the selected enquiry.
	 *
	 * @param selectedEnquiry the new selected enquiry
	 */
	public void setSelectedEnquiry(Enquiry selectedEnquiry) {
		this.selectedEnquiry = selectedEnquiry;
	}
	
	public String getReplySubject() {
		return replySubject;
	}

	public void setReplySubject(String replySubject) {
		this.replySubject = replySubject;
	}

	public String getReplyMessage() {
		return replyMessage;
	}

	public void setReplyMessage(String replyMessage) {
		this.replyMessage = replyMessage;
	}

	public void reply() {
		GMailSender.Send("ruairi.kenny@umail.ucc.ie",replySubject, replyMessage);
	}
	
	
}
