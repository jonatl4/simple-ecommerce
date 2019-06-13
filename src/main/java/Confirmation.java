
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.glassfish.jersey.client.ClientConfig;

/**
 * Servlet implementation class Confirmation
 */
public class Confirmation extends HttpServlet {
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Get session object
		HttpSession session = request.getSession(true);

		// Get the cart
		HashMap<Integer, Integer> cart = (HashMap<Integer, Integer>) session.getAttribute("cart");

		response.setContentType("text/html");
		String items = request.getParameter("cartItems");
		String total = request.getParameter("total");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String phoneNumber = request.getParameter("phoneNumber");
		String country = request.getParameter("country");
		String addressLine1 = request.getParameter("addressLine1");
		String addressLine2 = request.getParameter("addressLine2");
		String city = request.getParameter("city");
		String state = request.getParameter("state");
		String zipCode = request.getParameter("zipCode");
		String shippingMethod = request.getParameter("shippingMethod");
		String creditCardName = request.getParameter("creditCardName");
		String creditCardNumber = request.getParameter("creditCardNumber");
		String creditCardExpiryMonth = request.getParameter("creditCardExpiryMonth");
		String creditCardExpiryYear = request.getParameter("creditCardExpiryYear");
		String creditCardCSV = request.getParameter("creditCardCSV");
		
		Order order = new Order();
		order.setItems(items);
		order.setPrice(Double.parseDouble(total));
		order.setFirstName(firstName);
		order.setLastName(lastName);
		order.setEmail(email);
		order.setPhoneNumber(phoneNumber);
		order.setCountry(country);
		order.setAddressLine1(addressLine1);
		order.setAddressLine2(addressLine2);
		order.setCity(city);
		order.setState(state);
		order.setZipCode(zipCode);
		order.setShippingMethod(shippingMethod);
		order.setCreditCardName(creditCardName);
		order.setCreditCardNumber(creditCardNumber);
		order.setCreditCardExpiryMonth(creditCardExpiryMonth);
		order.setCreditCardExpiryYear(creditCardExpiryYear);
		order.setCreditCardCSV(creditCardCSV);
		
		ClientConfig config = new ClientConfig();

        Client client = ClientBuilder.newClient(config);

        WebTarget target = client.target(getBaseURI());
        
        ObjectMapper objectMapper = new ObjectMapper(); // This object is from the jackson library

        String jsonInString = objectMapper.writeValueAsString(order);

        String jsonResponse =
                target.path("v1").path("api").path("orders").
                request().
                accept(MediaType.APPLICATION_JSON).
                post(Entity.entity(jsonInString, MediaType.APPLICATION_JSON), String.class);

        System.out.println(jsonResponse);
        
		PrintWriter out = response.getWriter();

		out.println("<html lang='en'> " + "<head>" + "<meta charset='utf-8'>" + "<title>Soccer Jerseys</title>"
				+ "<link href= 'css/styles.css' rel='stylesheet' type='text/css'>"
				+ "<script type='text/javascript' src='js/qty.js'></script>" + "</head>" + "<body>"
				+ "<div class='header'>" + "<img src='img/top_header_general.png' width = '100%'/>  " + "</div>"
				+ "<div class='nav'>" + "<ul>" + "<li><a href='index.jsp'>Home</a></li>"
				+ "<li><a href='products.jsp'>Online Store</a></li>" + "</ul>" + "</div>");

		out.println("<div class='cart'>" + "<div class='cartContainer'>"
				+ "<h1>YOUR ORDER HAS BEEN RECEIVED SUCCESSFULLY.</h1><br>" + "<h3>Thank you for your purchase!<br>");

		out.println("<br><h3>Order Summary</h3><br>" + "<table>" + 
				"<tr>" + 
				"<th></th>" + 
				"<th> Item Name </th>" + 
				"<th> Unit Price </th>" + 
				"<th> Quantity </th>" + 
				"<th> Price </th>" + 
				"</tr>" + 
				"</thead>" + 
				"<tbody>");
		
		Iterator it = cart.entrySet().iterator();
		
		while (it.hasNext()) {
			HashMap.Entry pair =(HashMap.Entry)it.next();
			Integer productId = (Integer)pair.getKey();
			

			String prodResponse = target.path("v1").path("api").path("products").path(productId.toString()).request(). // send
			// a
			// request
					accept(MediaType.APPLICATION_JSON). // specify the media
					// type of the response
					get(String.class); // use the get method and return the
			// response as a string

			System.out.println(prodResponse);


			Product product = objectMapper.readValue(prodResponse, new TypeReference<Product>() {
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
		}

		cart.clear();
		out.println("</table>");
		out.println("<br><h3> The total amount paid was $" + total + "0");

		out.println("<br><br><h3>Billing & Shipping Information: </h3>");
		out.println(
				"<b>Customer Name: </b>" + firstName + " " + lastName);
		out.println("<p><b>Shipped to:</b>");
		out.println("<p>" + addressLine1);
		out.println("<br>" + city + ", " + state + " "
				+ zipCode);

		out.println("<p><b>Shipping Method: </b>" + shippingMethod);
	}

	private static URI getBaseURI() {

		// Change the URL here to make the client point to your service.
		return UriBuilder.fromUri("http://andromeda-39.ics.uci.edu:5039/jerseyrest").build();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
}
