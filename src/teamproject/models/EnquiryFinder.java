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

@ManagedBean(name = "enquiryFinder")
@SessionScoped
public class EnquiryFinder implements Serializable {

	public List<Enquiry> getEnquiries() {
		List<Enquiry> enquiryList = new ArrayList<Enquiry>();
		FacesContext context = FacesContext.getCurrentInstance();
		DatabaseConnector db = (DatabaseConnector) context.getApplication()
				.evaluateExpressionGet(context, "#{dbConnector}",
						DatabaseConnector.class);
		String query = "SELECT * FROM enquiries";
		ResultSet result = db.executeQuery(query);
		try {
			while (result.next()) {
				Enquiry enquiry = new Enquiry(result);
				enquiryList.add(enquiry);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return enquiryList;
	}

}