package com.velik.comments.servlet;

import javax.servlet.ServletContextEvent;

public class ContextListener implements javax.servlet.ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		AbstractHttpServlet.finder.initalize();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		AbstractHttpServlet.finder.persist();
	}

}
