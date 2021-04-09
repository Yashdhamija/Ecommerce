package filter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import model.AdminService;

/**
 * Servlet Filter implementation class annomized
 */
@WebFilter("/annomized")
public class Annomized implements Filter {
	
	private static AdminService adminService;

    public Annomized() throws ClassNotFoundException, SQLException {
    	Annomized.adminService = AdminService.getInstance();
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		if(request.getParameter("adminReport") != null) {			
			
			List<List<String>> userStats;
			try {
				userStats = this.adminService.retrieveUserStatistics();
				
				for(int i=0; i<userStats.size(); i++) {
					
					
					userStats.get(i).set(0, "****      ***");
					request.setAttribute("annomized", userStats);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
