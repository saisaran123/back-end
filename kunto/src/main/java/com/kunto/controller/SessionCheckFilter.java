package com.kunto.controller;


//
//



//import java.io.IOException;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
///**
// * Servlet Filter implementation class gg
// */
//@WebFilter("/*")
//public class SessionCheckFilter extends HttpFilter implements Filter {
//       
//    
//    public SessionCheckFilter() {
//        super();
//       
//    }
//
//	
//	public void destroy() {
//		
//	}
//
//
////	 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
////	            throws IOException, ServletException {
////
////	        HttpServletRequest httpRequest = (HttpServletRequest) request;
////	        HttpServletResponse httpResponse = (HttpServletResponse) response;
////
////	        // Check if session exists without creating a new one
////	        HttpSession session = httpRequest.getSession(false);
////
////	        // Assuming "user_id" is stored in session after login
////	        if (session == null || session.getAttribute("user_id") == null) {
////	            // Redirect to login if session is invalid
////	            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.html");
////	        } else {
////	            // Continue to requested resource
////	            chain.doFilter(request, response);
////	        }
////	    }
//
//	 @Override
////	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
////	            throws IOException, ServletException {
////
////	        HttpServletRequest req = (HttpServletRequest) request;
////	        HttpServletResponse res = (HttpServletResponse) response;
////
////	        String path = req.getRequestURI();
////	        System.out.println(path);
////	     
//////	        if (path.endsWith(".html") || path.endsWith(".css") || path.endsWith(".js") ||
//////	                path.endsWith(".png") || path.endsWith(".jpg") || path.equals("/kuntoo/login") ||
//////	                path.equals("checkSession")) {
//////	                chain.doFilter(request, response); // Allow without session check
//////	                return
//////	            }
////	        HttpSession session = req.getSession(false);
////	        if (path.equals("/kuntoo/loginandSignup.html") || path.equals("/kuntoo/login") || 
////	        		path.equals("/kuntoo/signup") || path.endsWith(".css") || path.endsWith(".js") || path.contains("loginresources"))
////	                {
////	        	System.out.println("is this login signup....");
////	        	 if (session != null && session.getAttribute("user_id") != null) {
////	                 int username = (int) session.getAttribute("user_id");
////	                 res.sendRedirect("kunto3.html");
////	                //res.getWriter().write("{\"loggedIn\": true, \"user_id\": \"" + username + "\"}");
////	            } 
////	                chain.doFilter(request, response);
////	                return;
////	         }
////
////
////	        // Session check
////	       
////	        else if (session == null || session.getAttribute("user_id") == null) {
////	        	System.out.println("session is expired");
////	           // res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
////	           // res.getWriter().write("{\"error\": \"Session expired\"}");
////	            res.sendRedirect("loginandSignup.html");
////	            chain.doFilter(request, response);
////	            return;
////	        }
////
////	      
////	        
////	    }
//	 
//	 
//	 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//	         throws IOException, ServletException {
//
//	     HttpServletRequest req = (HttpServletRequest) request;
//	     HttpServletResponse res = (HttpServletResponse) response;
//
//	     String path = req.getRequestURI();
//	     System.out.println("Requested Path: " + path);
//
//	     HttpSession session = req.getSession(false);
//
//	     // Allow login/signup pages and static resources
//	     if (path.equals("/kuntoo/loginandSignup.html") || path.equals("/kuntoo/login") ||
//	         path.equals("/kuntoo/signup") || path.endsWith(".css") || path.endsWith(".js") || 
//	         path.contains("loginresources")) {
//
//	         System.out.println("Login/Signup or static resource request...");
//	         
//	         if (session != null && session.getAttribute("user_id") != null) {
//	             int username = (int) session.getAttribute("user_id");
//	             System.out.println("User already logged in, redirecting to home...");
//	             res.sendRedirect("kunto3.html");
//	             //return; // Prevent further execution
//	         }
//
//	         chain.doFilter(request, response);
//	         return;
//	     }
//
//	     // Session check for protected resources
//	     if (session == null || session.getAttribute("user_id") == null) {
//	         System.out.println("Session expired, redirecting to login...");
//	         if (!res.isCommitted()) {
//	             res.sendRedirect("loginandSignup.html");
//	         } else {
//	             System.out.println("Response already committed, skipping redirect.");
//	         }
//	         return;
//	     }
//
//	     // Continue request processing
//	     chain.doFilter(request, response);
//	 }
//
//	
//	public void init(FilterConfig fConfig) throws ServletException {
//		
//	}
//
//}


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*") // Apply to all requests
public class SessionCheckFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization (if needed)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI();

       System.out.println(path);
       
       if (path.endsWith(".css") || path.endsWith(".js") ||
           path.endsWith(".png") || path.endsWith(".jpg") || path.equals("/kunto/Signup")  ||
           path.equals("checkSession") || path.contains("kuntoLandingPage.html") || path.contains("/kunto/images/")) {
           chain.doFilter(request, response); // Allow without session check
           return;
       }
        if (path.endsWith("loginandSignup.html") || path.endsWith("signup.html") ||
        		path.equals("/kunto/login")  || path.endsWith("login.html")) {
        	
        	 if (session != null && session.getAttribute("user_id") != null) {
        		 int username = (int) session.getAttribute("user_id");
        		 res.sendRedirect("kunto.html");
        		 return;
        		 //res.getWriter().write("{\"loggedIn\": true, \"user_id\": \"" + username + "\"}");
        	 } 
            chain.doFilter(request, response);
            return;
        }
        
        
        
        
        if (session == null || session.getAttribute("user_id") == null) {
            System.out.println("Unauthorized access attempt to: " + path);
            
           
            if (!res.isCommitted()) {
                res.sendRedirect("loginandSignup.html");
            }
            return; 
        }

        // Continue with request processing
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup if necessary
    }
}

