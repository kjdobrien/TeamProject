package teamproject.auth;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.shiro.SecurityUtils;

@ManagedBean(name="logout")
@RequestScoped
public class Logout {

    public static final String HOME_URL = "index.xhtml";

    public void submit() throws IOException {
        SecurityUtils.getSubject().logout();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect(HOME_URL);
    }

}