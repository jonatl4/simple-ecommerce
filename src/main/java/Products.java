
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
public class Products extends HttpServlet {

	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        ClientConfig config = new ClientConfig();

        Client client = ClientBuilder.newClient(config);

        WebTarget target = client.target(getBaseURI());


        String jsonResponse =
                target.path("v1").path("api").path("products").
                        request(). //send a request
                        accept(MediaType.APPLICATION_JSON). //specify the media type of the response
                        get(String.class); // use the get method and return the response as a string

        System.out.println(jsonResponse);

        ObjectMapper objectMapper = new ObjectMapper(); // This object is from the jackson library

        List<Product> products = objectMapper.readValue(jsonResponse, new TypeReference<List<Product>>(){});

        PrintWriter out = response.getWriter();
        
        for (int i = 0; i < 12 / 3; i++) {
        	out.println("<tr>");
        	for (int j = 0; j < 3; j++) {
        		int pos = i * 3 + j;
        		out.println("<td height='200' align='center'/>");
        		out.println("<a href='product?id=" + products.get(pos).getId() + "'>");
        		out.println("<img class='zoom' src='img/" + products.get(pos).getImage() + "' height='100%' width='100%'/></a>");
        		out.println("<h4>" + products.get(pos).getName() + "</h4>");
        		out.println("<p>$" + products.get(pos).getPrice() + "0</p></td>");
        	}
        }
        
        out.print("</tr>\n");
    }

    private static URI getBaseURI() {

        //Change the URL here to make the client point to your service.
        return UriBuilder.fromUri("http://andromeda-39.ics.uci.edu:5039/jerseyrest").build();
    }
}
