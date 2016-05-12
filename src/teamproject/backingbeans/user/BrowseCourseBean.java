package teamproject.backingbeans.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import teamproject.models.Course;
import teamproject.models.CourseFinder;


@ManagedBean(name = "browseCourses")
@SessionScoped
public class BrowseCourseBean implements Serializable {
	
	private List<Course> courses;
	private List<Course> allCourses;
	private String filterValue;
	private Course selectedCourse;

	public BrowseCourseBean() {
		courses = new ArrayList<Course>();
		FacesContext context = FacesContext.getCurrentInstance();
		CourseFinder coursefinder = (CourseFinder) context.getApplication()
				.evaluateExpressionGet(context, "#{courseFinder}",
						CourseFinder.class);
		allCourses = coursefinder.findCoursesByAttribute(null);
		courses = allCourses;
	}
	
	public Course getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(Course selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public List<Course> getCourses() {
		return courses;
	}
	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public void filterCourses() {
		courses = new ArrayList<Course>();
		for(Course course: allCourses) {
			if(course.getDescription().contains(filterValue) || course.getTitle().contains(filterValue)){
				courses.add(course);
			}
		}
	}
}
