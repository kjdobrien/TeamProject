package teamproject.backingbeans.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

import teamproject.models.Course;

/**
 * The Class EditCourseBean.
 */
@ManagedBean(name = "editCourse")
@SessionScoped
public class EditCourseBean implements Serializable {
	
	private Course course;
	
	public EditCourseBean() {
		FacesContext context = FacesContext.getCurrentInstance();
		AdminBrowseCourseBean browseCourse = (AdminBrowseCourseBean) context.getApplication()
				.evaluateExpressionGet(context, "#{adminBrowseCourses}",
						AdminBrowseCourseBean.class);
		course = browseCourse.getSelectedCourse();
		if(course == null) {
			course = new Course();
		}
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	public void saveCourse() {
		course.storeToDB();
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		String path = FacesContext.getCurrentInstance().getExternalContext()
	            .getRealPath("/");
		
		String name = event.getFile().getFileName().substring(
	                  event.getFile().getFileName().lastIndexOf('.'));
		
		File file = new File(path + "images/courses/" + name);
		try {
			InputStream is = event.getFile().getInputstream();
			OutputStream out = new FileOutputStream(file);

			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			is.close();
			out.close();
			course.setImage(name);
		} catch (Exception e) {
			System.err.println("Problem writing file to disk");
		}
	}
	
	public void newCourse() {
		course = new Course();
	}
	
}
