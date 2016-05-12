package teamproject.auth;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JNDIAwareJdbcRealm extends JdbcRealm {

	private static final Logger log = LoggerFactory.getLogger(JNDIAwareJdbcRealm.class);

	protected String jndiDataSourceName;

	public JNDIAwareJdbcRealm() {
		System.out.println("JdbcRealm being used");
	}

	public String getJndiDataSourceName() {
		return jndiDataSourceName;
	}

	public void setJndiDataSourceName(String jndiDataSourceName) {
		this.jndiDataSourceName = jndiDataSourceName;
		this.dataSource = getDataSourceFromJNDI(jndiDataSourceName);
	}

	private DataSource getDataSourceFromJNDI(String jndiDataSourceName) {
		try {
			InitialContext ic = new InitialContext();
			return (DataSource) ic.lookup(jndiDataSourceName);
		} catch (NamingException e) {
			log.error("JNDI error while retrieving " + jndiDataSourceName, e);
			throw new AuthorizationException(e);
		}
	}
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
		return super.doGetAuthenticationInfo(token);
	}
}
