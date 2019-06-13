

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
 * Servlet implementation class Checkout
 */
public class Checkout extends HttpServlet {
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ClientConfig config = new ClientConfig();

		Client client = ClientBuilder.newClient(config);

		WebTarget target = client.target(getBaseURI());


		// Get session object
		HttpSession session = request.getSession(true);

		// Get the cart
		HashMap<Integer, Integer> cart = (HashMap<Integer, Integer>) session.getAttribute("cart");


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
		String cartItems = "";
		
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
			
			String name = product.getName();
			cartItems += name + "," + quantity + ";";
		}
		
		cartItems = cartItems.substring(0, cartItems.length() - 1);
		
		out.println("</table>");
		out.println("<br><h3> Your current total is $" + total + "0");
		out.println("</div>" + "</div>");
		out.println("<br><br><div class='productform'>" + "<div class='innerform'>" + "<h1>Checkout</h1>"
				+ "<h4>Please fill out the details below</h4><br><br>" + "<form  method='post' action='confirmation'>"
				+ "<fieldset>" + "<legend><b>Order Form</b></legend>"
                + "<input type='hidden' value='" + total + "' name='total' id='total' />"
                + "<input type='hidden' value='" + cartItems + "' name='cartItems' id='cartItems' />"
				+ "<p><span class='required'>* Required</span></p>"
				+ "<p><span class='required'>*</span>First Name: <input type='text' name='firstName' pattern='[A-Za-z]{1,32}' required> Max 32 characters</p>"
				+ "<p><span class='required'>*</span>Last Name: <input type='text' name='lastName' pattern='[A-Za-z]{1,32}' required> Max 32 characters</p>"
				+ "<p><span class='required'>*</span>E-mail: <input type='text' name='email' pattern='[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}$' required> Ex. someguy@gmail.com</p>"
				+ "<p><span class='required'>*</span>Phone Number: <input type='tel' name='phoneNumber' pattern= '\\d{3}[\\-]\\d{3}[\\-]\\d{4}' required> Format: XXX-XXX-XXXX</p>"
				+ "<p><span class='required'>*</span>Country: <select name = 'country'>"
				+ "<option value='AX'>Aland Islands</option>" + "<option value='AL'>Albania</option>"
				+ "<option value='DZ'>Algeria</option>" + "<option value='AS'>American Samoa</option>"
				+ "<option value='AD'>Andorra</option>" + "<option value='AO'>Angola</option>"
				+ "<option value='AI'>Anguilla</option>" + "<option value='AQ'>Antarctica</option>"
				+ "<option value='AG'>Antigua and Barbuda</option>" + "<option value='AR'>Argentina</option>"
				+ "<option value='AM'>Armenia</option>" + "<option value='AW'>Aruba</option>"
				+ "<option value='AU'>Australia</option>" + "<option value='AT'>Austria</option>"
				+ "<option value='AZ'>Azerbaijan</option>" + "<option value='BS'>Bahamas</option>"
				+ "<option value='BH'>Bahrain</option>" + "<option value='BD'>Bangladesh</option>"
				+ "<option value='BB'>Barbados</option>" + "<option value='BY'>Belarus</option>"
				+ "<option value='BE'>Belgium</option>" + "<option value='BZ'>Belize</option>"
				+ "<option value='BJ'>Benin</option>" + "<option value='BM'>Bermuda</option>"
				+ "<option value='BT'>Bhutan</option>" + "<option value='BO'>Bolivia, Plurinational State of</option>"
				+ "<option value='BQ'>Bonaire, Sint Eustatius and Saba</option>"
				+ "<option value='BA'>Bosnia and Herzegovina</option>" + "<option value='BW'>Botswana</option>"
				+ "<option value='BV'>Bouvet Island</option>" + "<option value='BR'>Brazil</option>"
				+ "<option value='IO'>British Indian Ocean Territory</option>"
				+ "<option value='BN'>Brunei Darussalam</option>" + "<option value='BG'>Bulgaria</option>"
				+ "<option value='BF'>Burkina Faso</option>" + "<option value='BI'>Burundi</option>"
				+ "<option value='KH'>Cambodia</option>" + "<option value='CM'>Cameroon</option>"
				+ "<option value='CA'>Canada</option>" + "<option value='CV'>Cape Verde</option>"
				+ "<option value='KY'>Cayman Islands</option>" + "<option value='CF'>Central African Republic</option>"
				+ "<option value='TD'>Chad</option>" + "<option value='CL'>Chile</option>"
				+ "<option value='CN'>China</option>" + "<option value='CX'>Christmas Island</option>"
				+ "<option value='CC'>Cocos (Keeling) Islands</option>" + "<option value='CO'>Colombia</option>"
				+ "<option value='KM'>Comoros</option>" + "<option value='CG'>Congo</option>"
				+ "<option value='CD'>Congo, the Democratic Republic of the</option>"
				+ "<option value='CK'>Cook Islands</option>" + "<option value='CR'>Costa Rica</option>"
				+ "<option value='CI'>Côte d'Ivoire</option>" + "<option value='HR'>Croatia</option>"
				+ "<option value='CU'>Cuba</option>" + "<option value='CW'>Curaçao</option>"
				+ "<option value='CY'>Cyprus</option>" + "<option value='CZ'>Czech Republic</option>"
				+ "<option value='DK'>Denmark</option>" + "<option value='DJ'>Djibouti</option>"
				+ "<option value='DM'>Dominica</option>" + "<option value='DO'>Dominican Republic</option>"
				+ "<option value='EC'>Ecuador</option>" + "<option value='EG'>Egypt</option>"
				+ "<option value='SV'>El Salvador</option>" + "<option value='GQ'>Equatorial Guinea</option>"
				+ "<option value='ER'>Eritrea</option>" + "<option value='EE'>Estonia</option>"
				+ "<option value='ET'>Ethiopia</option>" + "<option value='FK'>Falkland Islands (Malvinas)</option>"
				+ "<option value='FO'>Faroe Islands</option>" + "<option value='FJ'>Fiji</option>"
				+ "<option value='FI'>Finland</option>" + "<option value='FR'>France</option>"
				+ "<option value='GF'>French Guiana</option>" + "<option value='PF'>French Polynesia</option>"
				+ "<option value='TF'>French Southern Territories</option>" + "<option value='GA'>Gabon</option>"
				+ "<option value='GM'>Gambia</option>" + "<option value='GE'>Georgia</option>"
				+ "<option value='DE'>Germany</option>" + "<option value='GH'>Ghana</option>"
				+ "<option value='GI'>Gibraltar</option>" + "<option value='GR'>Greece</option>"
				+ "<option value='GL'>Greenland</option>" + "<option value='GD'>Grenada</option>"
				+ "<option value='GP'>Guadeloupe</option>" + "<option value='GU'>Guam</option>"
				+ "<option value='GT'>Guatemala</option>" + "<option value='GG'>Guernsey</option>"
				+ "<option value='GN'>Guinea</option>" + "<option value='GW'>Guinea-Bissau</option>"
				+ "<option value='GY'>Guyana</option>" + "<option value='HT'>Haiti</option>"
				+ "<option value='HM'>Heard Island and McDonald Islands</option>"
				+ "<option value='VA'>Holy See (Vatican City State)</option>" + "<option value='HN'>Honduras</option>"
				+ "<option value='HK'>Hong Kong</option>" + "<option value='HU'>Hungary</option>"
				+ "<option value='IS'>Iceland</option>" + "<option value='IN'>India</option>"
				+ "<option value='ID'>Indonesia</option>" + "<option value='IR'>Iran, Islamic Republic of</option>"
				+ "<option value='IQ'>Iraq</option>" + "<option value='IE'>Ireland</option>"
				+ "<option value='IM'>Isle of Man</option>" + "<option value='IL'>Israel</option>"
				+ "<option value='IT'>Italy</option>" + "<option value='JM'>Jamaica</option>"
				+ "<option value='JP'>Japan</option>" + "<option value='JE'>Jersey</option>"
				+ "<option value='JO'>Jordan</option>" + "<option value='KZ'>Kazakhstan</option>"
				+ "<option value='KE'>Kenya</option>" + "<option value='KI'>Kiribati</option>"
				+ "<option value='KP'>Korea, Democratic People's Republic of</option>"
				+ "<option value='KR'>Korea, Republic of</option>" + "<option value='KW'>Kuwait</option>"
				+ "<option value='KG'>Kyrgyzstan</option>"
				+ "<option value='LA'>Lao People's Democratic Republic</option>" + "<option value='LV'>Latvia</option>"
				+ "<option value='LB'>Lebanon</option>" + "<option value='LS'>Lesotho</option>"
				+ "<option value='LR'>Liberia</option>" + "<option value='LY'>Libya</option>"
				+ "<option value='LI'>Liechtenstein</option>" + "<option value='LT'>Lithuania</option>"
				+ "<option value='LU'>Luxembourg</option>" + "<option value='MO'>Macao</option>"
				+ "<option value='MK'>Macedonia, the former Yugoslav Republic of</option>"
				+ "<option value='MG'>Madagascar</option>" + "<option value='MW'>Malawi</option>"
				+ "<option value='MY'>Malaysia</option>" + "<option value='MV'>Maldives</option>"
				+ "<option value='ML'>Mali</option>" + "<option value='MT'>Malta</option>"
				+ "<option value='MH'>Marshall Islands</option>" + "<option value='MQ'>Martinique</option>"
				+ "<option value='MR'>Mauritania</option>" + "<option value='MU'>Mauritius</option>"
				+ "<option value='YT'>Mayotte</option>" + "<option value='MX'>Mexico</option>"
				+ "<option value='FM'>Micronesia, Federated States of</option>"
				+ "<option value='MD'>Moldova, Republic of</option>" + "<option value='MC'>Monaco</option>"
				+ "<option value='MN'>Mongolia</option>" + "<option value='ME'>Montenegro</option>"
				+ "<option value='MS'>Montserrat</option>" + "<option value='MA'>Morocco</option>"
				+ "<option value='MZ'>Mozambique</option>" + "<option value='MM'>Myanmar</option>"
				+ "<option value='NA'>Namibia</option>" + "<option value='NR'>Nauru</option>"
				+ "<option value='NP'>Nepal</option>" + "<option value='NL'>Netherlands</option>"
				+ "<option value='NC'>New Caledonia</option>" + "<option value='NZ'>New Zealand</option>"
				+ "<option value='NI'>Nicaragua</option>" + "<option value='NE'>Niger</option>"
				+ "<option value='NG'>Nigeria</option>" + "<option value='NU'>Niue</option>"
				+ "<option value='NF'>Norfolk Island</option>" + "<option value='MP'>Northern Mariana Islands</option>"
				+ "<option value='NO'>Norway</option>" + "<option value='OM'>Oman</option>"
				+ "<option value='PK'>Pakistan</option>" + "<option value='PW'>Palau</option>"
				+ "<option value='PS'>Palestinian Territory, Occupied</option>" + "<option value='PA'>Panama</option>"
				+ "<option value='PG'>Papua New Guinea</option>" + "<option value='PY'>Paraguay</option>"
				+ "<option value='PE'>Peru</option>" + "<option value='PH'>Philippines</option>"
				+ "<option value='PN'>Pitcairn</option>" + "<option value='PL'>Poland</option>"
				+ "<option value='PT'>Portugal</option>" + "<option value='PR'>Puerto Rico</option>"
				+ "<option value='QA'>Qatar</option>" + "<option value='RE'>Réunion</option>"
				+ "<option value='RO'>Romania</option>" + "<option value='RU'>Russian Federation</option>"
				+ "<option value='RW'>Rwanda</option>" + "<option value='BL'>Saint Barthélemy</option>"
				+ "<option value='SH'>Saint Helena, Ascension and Tristan da Cunha</option>"
				+ "<option value='KN'>Saint Kitts and Nevis</option>" + "<option value='LC'>Saint Lucia</option>"
				+ "<option value='MF'>Saint Martin (French part)</option>"
				+ "<option value='PM'>Saint Pierre and Miquelon</option>"
				+ "<option value='VC'>Saint Vincent and the Grenadines</option>" + "<option value='WS'>Samoa</option>"
				+ "<option value='SM'>San Marino</option>" + "<option value='ST'>Sao Tome and Principe</option>"
				+ "<option value='SA'>Saudi Arabia</option>" + "<option value='SN'>Senegal</option>"
				+ "<option value='RS'>Serbia</option>" + "<option value='SC'>Seychelles</option>"
				+ "<option value='SL'>Sierra Leone</option>" + "<option value='SG'>Singapore</option>"
				+ "<option value='SX'>Sint Maarten (Dutch part)</option>" + "<option value='SK'>Slovakia</option>"
				+ "<option value='SI'>Slovenia</option>" + "<option value='SB'>Solomon Islands</option>"
				+ "<option value='SO'>Somalia</option>" + "<option value='ZA'>South Africa</option>"
				+ "<option value='GS'>South Georgia and the South Sandwich Islands</option>"
				+ "<option value='SS'>South Sudan</option>" + "<option value='ES'>Spain</option>"
				+ "<option value='LK'>Sri Lanka</option>" + "<option value='SD'>Sudan</option>"
				+ "<option value='SR'>Suriname</option>" + "<option value='SJ'>Svalbard and Jan Mayen</option>"
				+ "<option value='SZ'>Swaziland</option>" + "<option value='SE'>Sweden</option>"
				+ "<option value='CH'>Switzerland</option>" + "<option value='SY'>Syrian Arab Republic</option>"
				+ "<option value='TW'>Taiwan, Province of China</option>" + "<option value='TJ'>Tajikistan</option>"
				+ "<option value='TZ'>Tanzania, United Republic of</option>" + "<option value='TH'>Thailand</option>"
				+ "<option value='TL'>Timor-Leste</option>" + "<option value='TG'>Togo</option>"
				+ "<option value='TK'>Tokelau</option>" + "<option value='TO'>Tonga</option>"
				+ "<option value='TT'>Trinidad and Tobago</option>" + "<option value='TN'>Tunisia</option>"
				+ "<option value='TR'>Turkey</option>" + "<option value='TM'>Turkmenistan</option>"
				+ "<option value='TC'>Turks and Caicos Islands</option>" + "<option value='TV'>Tuvalu</option>"
				+ "<option value='UG'>Uganda</option>" + "<option value='UA'>Ukraine</option>"
				+ "<option value='AE'>United Arab Emirates</option>" + "<option value='GB'>United Kingdom</option>"
				+ "<option value='US'>United States</option>"
				+ "<option value='UM'>United States Minor Outlying Islands</option>"
				+ "<option value='UY'>Uruguay</option>" + "<option value='UZ'>Uzbekistan</option>"
				+ "<option value='VU'>Vanuatu</option>"
				+ "<option value='VE'>Venezuela, Bolivarian Republic of</option>"
				+ "<option value='VN'>Viet Nam</option>" + "<option value='VG'>Virgin Islands, British</option>"
				+ "<option value='VI'>Virgin Islands, U.S.</option>" + "<option value='WF'>Wallis and Futuna</option>"
				+ "<option value='EH'>Western Sahara</option>" + "<option value='YE'>Yemen</option>"
				+ "<option value='ZM'>Zambia</option>" + "<option value='ZW'>Zimbabwe</option>" + "</select></p>"
				+ "<p><span class='required'>*</span>Address Line 1: <input type='text' name='addressLine1' pattern='^\\d{1,5}+\\s[A-z]+\\s[A-z]+' required></p>"
				+ "<p>Address Line 2: <input type='text' name='addressLine2' pattern='^\\d{1,5}+\\s[A-z]+\\s[A-z]+' ></p>"
				+ "<p><span class='required'>*</span>Zip: <input type='text' name='zipCode' id='zipCode' pattern='\\d{5}' required onblur='requestCityState();requestTaxRate();'> Auto-fills city/state</p>"
				+ "<p><span class='required'>*</span>City: <input type='text' name='city' id = 'city'pattern='[A-Za-z\\s]{1,32}' required></p>"
				+ "<p><span class='required'>*</span>State: <select name = 'state' id = 'state'>"
				+ "<option value='AL'>Alabama</option>" + "<option value='AK'>Alaska</option>"
				+ "<option value='AZ'>Arizona</option>" + "<option value='AR'>Arkansas</option>"
				+ "<option value='CA'>California</option>" + "<option value='CO'>Colorado</option>"
				+ "<option value='CT'>Connecticut</option>" + "<option value='DE'>Delaware</option>"
				+ "<option value='DC'>District Of Columbia</option>" + "<option value='FL'>Florida</option>"
				+ "<option value='GA'>Georgia</option>" + "<option value='HI'>Hawaii</option>"
				+ "<option value='ID'>Idaho</option>" + "<option value='IL'>Illinois</option>"
				+ "<option value='IN'>Indiana</option>" + "<option value='IA'>Iowa</option>"
				+ "<option value='KS'>Kansas</option>" + "<option value='KY'>Kentucky</option>"
				+ "<option value='LA'>Louisiana</option>" + "<option value='ME'>Maine</option>"
				+ "<option value='MD'>Maryland</option>" + "<option value='MA'>Massachusetts</option>"
				+ "<option value='MI'>Michigan</option>" + "<option value='MN'>Minnesota</option>"
				+ "<option value='MS'>Mississippi</option>" + "<option value='MO'>Missouri</option>"
				+ "<option value='MT'>Montana</option>" + "<option value='NE'>Nebraska</option>"
				+ "<option value='NV'>Nevada</option>" + "<option value='NH'>New Hampshire</option>"
				+ "<option value='NJ'>New Jersey</option>" + "<option value='NM'>New Mexico</option>"
				+ "<option value='NY'>New York</option>" + "<option value='NC'>North Carolina</option>"
				+ "<option value='ND'>North Dakota</option>" + "<option value='OH'>Ohio</option>"
				+ "<option value='OK'>Oklahoma</option>" + "<option value='OR'>Oregon</option>"
				+ "<option value='PA'>Pennsylvania</option>" + "<option value='RI'>Rhode Island</option>"
				+ "<option value='SC'>South Carolina</option>" + "<option value='SD'>South Dakota</option>"
				+ "<option value='TN'>Tennessee</option>" + "<option value='TX'>Texas</option>"
				+ "<option value='UT'>Utah</option>" + "<option value='VT'>Vermont</option>"
				+ "<option value='VA'>Virginia</option>" + "<option value='WA'>Washington</option>"
				+ "<option value='WV'>West Virginia</option>" + "<option value='WI'>Wisconsin</option>"
				+ "<option value='WY'>Wyoming</option>" + "</select></p>" + "<p>Shipping Method :</p>" + "<form>"
				+ "<input type='radio' name='shippingMethod' value='Overnight Shipping'>Overnight Shipping<br>"
				+ "<input type='radio' name='shippingMethod' value='2-Day Shipping'>2-Day Shipping<br>"
				+ "<input type='radio' name='shippingMethod' value='6-Day Shipping' checked>6-Day Shipping<br>"
				+ "</form>" + "<br><br>" + "<h4><b>Enter Payment Information</b></h4><br>"
				+ "<p>Name (as it appears on your card)<br><input type='text' name='creditCardName' pattern='[A-Za-z ]{1,64}' required></p>"
				+ "<p>Card Number (no dashes or spaces)<br><input type='text' name='creditCardNumber' pattern='\\d{16}' required></p>"
				+ "<p>Expiry Date: <select name='creditCardExpiryMonth' id='expireMM'>"
				+ "<option value=''>Month</option>" + "<option value='01'>January</option>"
				+ "<option value='02'>February</option>" + "<option value='03'>March</option>"
				+ "<option value='04'>April</option>" + "<option value='05'>May</option>"
				+ "<option value='06'>June</option>" + "<option value='07'>July</option>"
				+ "<option value='08'>August</option>" + "<option value='09'>September</option>"
				+ "<option value='10'>October</option>" + "<option value='11'>November</option>"
				+ "<option value='12'>December</option>" + "</select> "
				+ "<select name='creditCardExpiryYear' id='expireYY'>" + "<option value=''>Year</option>"
				+ "<option value='17'>2017</option>" + "<option value='18'>2018</option>"
				+ "<option value='19'>2019</option>" + "<option value='20'>2020</option>" + "</select></p>"
				+ "<p>Security Code: <input type='text' class='csv' name='creditCardCSV'>" + "<br><br>"
				+ "<input type='submit' class='submit' value='Submit'>" + "</fieldset>" + "</form>" + "</div>"
				+ "</div>" + "</body>" + "</html>");
	}

    private static URI getBaseURI() {

        //Change the URL here to make the client point to your service.
        return UriBuilder.fromUri("http://andromeda-39.ics.uci.edu:5039/jerseyrest").build();
    }
}
