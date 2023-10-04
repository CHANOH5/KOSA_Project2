package com.my.customer.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
	
	String execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException;

} // end interface
