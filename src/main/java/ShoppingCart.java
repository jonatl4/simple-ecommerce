
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.glassfish.jersey.client.ClientConfig;

/**
 * Servlet implementation class ShoppingCart
 */
public class ShoppingCart extends HttpServlet {

	@SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ClientConfig config = new ClientConfig();

		Client client = ClientBuilder.newClient(config);

		WebTarget target = client.target(getBaseURI());

		String id = request.getParameter("id");
		
		// Get session object
		HttpSession session = request.getSession(true);

		// Get the cart
		HashMap<Integer, Integer> cart = (HashMap<Integer, Integer>) session.getAttribute("cart");
		
		Integer pid = Integer.parseInt(id);
		Integer pQuantity = new Integer(0);
		
		// if the session is new, the cart won't exist.
		if (cart == null) {
			cart = new HashMap<Integer, Integer>();
			cart.put(pid, 1);
		} else {
			pQuantity = cart.get(pid);
			if (pQuantity == null) {
				cart.put(pid, 1);
			} else {
				cart.put(pid, ++pQuantity);
			}
		}
		
		session.setAttribute("cart", cart);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<html lang='en'> " + "<head>" + "<meta charset='utf-8'>" + "<title>Soccer Jerseys</title>"
				+ "<link href= 'css/styles.css' rel='stylesheet' type='text/css'>"
				+ "<script type='text/javascript' src='js/qty.js'></script>" + "</head>" + "<body>"
				+ "<div class='header'>" + "<img src='img/top_header_general.png' width = '100%'/>  " + "</div>"
				+ "<div class='nav'>" + "<ul>" + "<li><a href='index.jsp'>Home</a></li>"
				+ "<li><a href='products.jsp'>Online Store</a></li>" + "</ul>" + "</div>");

		out.println("<div class='cart'>" + 
				"<div class='cartContainer'>" + 
				"<h1>Shopping Cart</h1><br>" + 
				"<table>" + 
				"<tr>" + 
				"<th></th>" + 
				"<th> Item Name </th>" + 
				"<th> Unit Price </th>" + 
				"<th> Quantity </th>" + 
				"<th> Price </th>" + 
				"</tr>" + 
				"</thead>" + 
				"<tbody>");
		double total = 0;
		
		Iterator it = cart.entrySet().iterator();
		
		while (it.hasNext()) {
			HashMap.Entry pair =(HashMap.Entry)it.next();
			Integer productId = (Integer)pair.getKey();
			

			String jsonResponse = target.path("v1").path("api").path("products").path(productId.toString()).request(). // send
			// a
			// request
					accept(MediaType.APPLICATION_JSON). // specify the media
					// type of the response
					get(String.class); // use the get method and return the
			// response as a string

			System.out.println(jsonResponse);

			ObjectMapper objectMapper = new ObjectMapper(); // This object is
			// from the jackson
			// library

			Product product = objectMapper.readValue(jsonResponse, new TypeReference<Product>() {
			});

			
			String image = product.getImage();
			String fullName = product.getFullName();
			Double unitPrice = product.getPrice();
			Integer quantity = (Integer)pair.getValue();
			
			Double price = unitPrice * quantity;
			
			out.println("<tr>" + 
					"<td><img src='img/" + image + "'></td>" + 
					"<td>" + fullName + "</td>" + 
					"<td>" + unitPrice + "0</td>" + 
					"<td>" + quantity + "</td>" + 
					"<td>" + price + "0</td>" + 
					"</tr>");

			total = total + price;
		}
		out.println("</table>");
		out.println("<br><h3> Your current total is $" + total + "0");
		out.println("<br><br><form action='checkout' method='post'>"
				+ "<input type='submit' class='submit' value='Checkout'>" + "</form>");
		out.println("</div>" + "</div>" + "</body>" + "</html>");
	}

	private static URI getBaseURI() {

		// Change the URL here to make the client point to your service.
		return UriBuilder.fromUri("http://andromeda-39.ics.uci.edu:5039/jerseyrest").build();
	}

}
