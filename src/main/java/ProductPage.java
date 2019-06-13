
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.glassfish.jersey.client.ClientConfig;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.List;
/**
 * Servlet implementation class Store
 */
public class ProductPage extends HttpServlet {

	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        ClientConfig config = new ClientConfig();

        Client client = ClientBuilder.newClient(config);

        WebTarget target = client.target(getBaseURI());
        
        String id = request.getParameter("id");

        String jsonResponse =
                target.path("v1").path("api").path("products").path(id).
                        request(). //send a request
                        accept(MediaType.APPLICATION_JSON). //specify the media type of the response
                        get(String.class); // use the get method and return the response as a string

        System.out.println(jsonResponse);

        ObjectMapper objectMapper = new ObjectMapper(); // This object is from the jackson library

        Product product = objectMapper.readValue(jsonResponse, new TypeReference<Product>(){});

        PrintWriter out = response.getWriter();
        
        out.println("<html lang='en'> "
        		+ "<head>"
        		+ "<meta charset='utf-8'>"
        		+ "<title>Soccer Jerseys</title>"
        		+ "<link href= 'css/styles.css' rel='stylesheet' type='text/css'>"
        		+ "<script type='text/javascript' src='js/qty.js'></script>"
        		+ "</head>"
        		+ "<body>"
        		+ "<div class='header'>"
        		+ "<img src='img/top_header_general.png' width = '100%'/>  "
        		+ "</div>"
        		+ "<div class='nav'>"
        		+ "<ul>"
        		+ "<li><a href='home'>Home</a></li>"
        		+ "<li><a href='store'>Online Store</a></li>"
        		+ "</ul>"
        		+ "</div>"
        		+ "<div class='product'>"
        		+ "<header>");
        
        String fullName = product.getFullName();
        Double price = product.getPrice();
        String image2 = product.getImage2();
        String image3 = product.getImage3();
        String description = product.getDescription();
        
        out.println("<h1>" + fullName + "</h1>");
        out.println("<h4>Price: $" + price + "0</h4>");
        out.println("</header>");
        out.println("<figure>"
        		+ "<img src='img/" + image2 + "'>"
        		+ "<img src='img/" + image3 + "'>"
        		+ "</figure>");
        out.println("<section>"
        		+ "<p>" + description + "</p>"
        		+ "<details> "
        		+ "<summary>Features</summary>"
        		+ "<ul>"
        		+ "<li>Ventilated climacool® keeps you cool and dry</li>"
        		+ "<li>Mesh ventilation inserts</li>"
        		+ "<li>Regular fit</li>"
        		+ "<li>100% polyester piqué</li>"
        		+ "<li>Imported</li>"
        		+ "</ul>"
        		+ "</details>"
        		+ "</section>"
        		+ "<br><br>"
        		+ "<form action='shoppingCart?id=" + id + "' method='post'>"
        		+ "<input type='submit' class='submit' value='Add to Cart'>"
        		+ "</form>");
        
        out.println("</div>"
        		+ "</body>"
        		+ "</html>");
    }

    private static URI getBaseURI() {

        //Change the URL here to make the client point to your service.
        return UriBuilder.fromUri("http://andromeda-39.ics.uci.edu:5039/jerseyrest").build();
    }
}
