package teamproject.backingbeans.user;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.shiro.SecurityUtils;

import teamproject.models.Course;
import teamproject.models.DatabaseConnector;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;

@ManagedBean(name = "enroll")
@SessionScoped
public class EnrollBean implements Serializable {
	
	private String cardNo;
	private String cvv;
	private String expiryDate;
	private Course course;
	private DatabaseConnector db;

	public EnrollBean() {
		FacesContext context = FacesContext.getCurrentInstance();
		BrowseCourseBean browseCourses = (BrowseCourseBean) context.getApplication()
				.evaluateExpressionGet(context, "#{browseCourses}",
						BrowseCourseBean.class);
		db = (DatabaseConnector) context.getApplication()
				.evaluateExpressionGet(context, "#{dbConnector}",
						DatabaseConnector.class);
		course = browseCourses.getSelectedCourse();
		
	}
	

	public String getCardNo() {
		return cardNo;
	}


	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}


	public String getCvv() {
		return cvv;
	}


	public void setCvv(String cvv) {
		this.cvv = cvv;
	}


	public String getExpiryDate() {
		return expiryDate;
	}


	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}


	public String enrollInCourse() {		
		return "payment";
	}
	
	public String processPayment() {
		 BraintreeGateway gateway = new BraintreeGateway(
		            Environment.SANDBOX,
		            "bq47fqf6xjj5fq97",
		            "gm497szngxmjqws3",
		            "cf436a0dfcbdb8c95bd2cf393ef940de"
		        );

		        TransactionRequest request = new TransactionRequest()
		          .amount(new BigDecimal(course.getFee()))
		          .creditCard()
		            .number(cardNo)
		            .expirationDate(expiryDate)
		            .done();

		        Result<Transaction> result = gateway.transaction().sale(request);

		        if (result.isSuccess()) {
		            Transaction transaction = result.getTarget();
		            System.out.println("Success!: " + transaction.getId());
		            int courseID = course.getId();
		            String username = SecurityUtils.getSubject().getPrincipal().toString();
		    		String insert = "INSERT INTO users_on_courses(user_id, course_id) VALUES('"+username+"',"+courseID+",'"+transaction.getId()+"')";
		    		db.executeInsert(insert);
		            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Payment successful!"));
		            
		        } else if (result.getTarget() != null) {
		           
		        } else {
		            System.out.println("Message: " + result.getMessage());
		            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
		                System.out.println("Attribute: " + error.getAttribute());
		                System.out.println("  Code: " + error.getCode());
		                System.out.println("  Message: " + error.getMessage());
		            }
		        }
		        return "index";
	}

}
