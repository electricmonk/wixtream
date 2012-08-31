package com.wixpress.streaming.paypal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * Super simple implementation of Paypal for Digital Goods - Express Checkout API
 * Java NVP (name-value pairs) implementation (July 1012 version 69.0)
 *
 * Using API and code samples from:
 * https://cms.paypal.com/cms_content/US/en_US/files/developer/PP_ExpressCheckout_IntegrationGuide_DG.pdf
 * https://cms.paypal.com/us/cgi-bin/?cmd=_render-content&content_ID=developer/e_howto_api_IntroducingExpressCheckoutDG
 * https://cms.paypal.com/us/cgi-bin/?cmd=_render-content&content_ID=developer/howto_api_reference
 * https://www.paypal-labs.com/integrationwizard/ecpaypal/cart.php
 *
 * Usage:
 *     When user presses a "Buy" button on your site:
 *         PayPalManager.startPurchase(2, 5, "Dapper Hat", A virtual item in the game Incredipede", "1.00", response);
 *     When user returns to RETURN_URL after authenitcating purchase on Paypal's website:
 *         String token = request.getParameter("token");
 *         int userId = Integer.parseInt(request.getParameter("userId"));
 *         int itemId = Integer.parseInt(request.getParameter("itemId"));
 *         // first check the status on paypal's system and make sure purchase is for reals
 *         String payerId = PayPalManager.validateDetails(token, userId, itemId);
 *         // next perform the purchase on your system to make sure it succeeds
 *         ...
 *         // finally take the user's money
 *         try {
 *             PayPalManager.finishPurchase(token, payerId, userId, itemId, "Dapper Hat",
 *                 A virtual item in the game Incredipede", "1.00");
 *         } catch (Exception e) {
 *             // roll back the purchase on your system
 *             ...
 *         }
 *
 * @author Sarah Northway
 */
public class PayPalManager
{
	// production creds
//	protected static String API_URL = "https://api-3t.paypal.com/nvp";
//	protected static String REDIRECT_URL = "https://www.paypal.com/cgibin/webscr?cmd=_express-checkout";

	// sandbox creds
	private final String apiUsername;
	private final String apiPassword;
	private final String apiSignature;
	private final String apiUrl;
	private final String redirectUrl;

    public PayPalManager(String apiUsername, String apiPassword, String apiSignature, String apiUrl, String redirectUrl) {
        this.apiUsername = apiUsername;
        this.apiPassword = apiPassword;
        this.apiSignature = apiSignature;
        this.apiUrl = apiUrl;
        this.redirectUrl = redirectUrl;
    }

    protected final static Logger log = Logger.getLogger(PayPalManager.class.getName());

	/**
	 * Step 1: SetExpressCheckout
	 *
	 * The first step of Express Checkout for Digital Goods: send a SetExpressCheckout
	 * request to PayPal and receive a token in response. Redirect the user to Paypal,
	 * then wait for their return through either the returnUrl or cancelUrl.
	 *
	 * As of version 69.0, digital payments must set L_PAYMENTREQUEST_0_ITEMCATEGORY0=Digital,
	 * must specify NOSHIPPING=1 and REQCONFIRMSHIPPING=0,
	 * must use both AMT and ITEMAMT, and must have exactly one
	 * payment (PAYMENTREQUEST_0_[...]) and one item (L_PAYMENTREQUEST_0_[...]0).
	 * https://cms.paypal.com/us/cgi-bin/?cmd=_render-content&content_ID=developer/e_howto_api_nvp_r_SetExpressCheckout
	 *
	 */
	public String startPurchase(String returnUrl, String merchantEmail, double price, String currency)
	{
		// include the userId and itemId in the return urls so we can access them later

		String data =
			"METHOD=SetExpressCheckout" +
			getAuthenticationData() +
			"&REQCONFIRMSHIPPING = 0" +
			"&NOSHIPPING = 1" +
			"&ALLOWNOTE = 0" +
			"&PAYMENTREQUEST_0_PAYMENTACTION=Sale" +
			"&PAYMENTREQUEST_0_SELLERPAYPALACCOUNTID=" + merchantEmail +
			"&PAYMENTREQUEST_0_CURRENCYCODE=" + currency +
			getPurchaseData(price) +
			"&RETURNURL=" + returnUrl +
			"&CANCELURL=" + returnUrl +
			"";

		// tell paypal we want to start a purchase
		HashMap<String, String> results = doServerCall(data);

		// forward the user on to payapal's site with the token identifying this transaction
        return redirectUrl + "&token=" + results.get("TOKEN");
	}

	/**
	 * Step 2: GetExpressCheckoutDetails
	 *
	 * Second step, performed when the user returns from paypal to validate the transaction
	 * details. If we cared about shipping info, the user's name etc it would be fetched here.
	 * Throws an exception if userId or purchase details don't match paypal's values, or if
	 * there's a problem with the purchase itself.
	 * https://cms.paypal.com/us/cgi-bin/?cmd=_render-content&content_ID=developer/e_howto_api_nvp_r_GetExpressCheckoutDetails
	 *
	 * @param token The token created and returned by Paypal in step 1 (from the return url)
	 * @param userId The user's unique id in our system (from the return url)
	 * @param itemId The unique id in our system for the thing being bought (from the return url)
	 * @return Returns the user's paypal PayerId for use in the last step
	 */
	public String validateDetails(String token, int userId, int itemId)
	{
		String data =
			"METHOD=GetExpressCheckoutDetails" +
			getAuthenticationData() +
			"&TOKEN=" + encodeValue(token) +
			"";

		HashMap<String, String> results = doServerCall(data);

		int resultsUserId = Integer.parseInt(results.get("PAYMENTREQUEST_0_CUSTOM"));
		if (resultsUserId != userId) {
			throw new RuntimeException("UserId does not match.");
		}

		int resultsItemId = Integer.parseInt(results.get("PAYMENTREQUEST_0_INVNUM"));
		if (resultsItemId != itemId) {
			throw new RuntimeException("ItemId does not match.");
		}

		String payerId = results.get("PAYERID");
		if (payerId == null || payerId.trim().length() == 0) {
			throw new RuntimeException("Payment has not been initiated by the user.");
		}

		return payerId;
	}

	/**
	 * Step 3: DoExpressCheckoutPayment
	 *
	 * Completes the payment that has already been started and authorized by the user
	 * via the paypal website. Requires passing in purchase information again.
	 * https://cms.paypal.com/us/cgi-bin/?cmd=_render-content&content_ID=developer/e_howto_api_nvp_r_DoExpressCheckoutPayment
	 *
	 */
	public void finishPurchase(String token, String payerId, double price) throws PaypalException {
			String data =
				"METHOD=DoExpressCheckoutPayment" +
				getAuthenticationData() +
				"&TOKEN=" + encodeValue(token) +
				"&PAYERID=" + encodeValue(payerId) +
				getPurchaseData(price) +
				"";

			HashMap<String, String> results = doServerCall(data);

			// warn if transaction type isn't completed or on the way to completed
			String status = results.get("PAYMENTINFO_0_PAYMENTSTATUS");
			if (status == null || !(status.equalsIgnoreCase("Completed")
				|| status.equalsIgnoreCase("In-Progress")
				|| status.equalsIgnoreCase("Processed")
				|| status.equalsIgnoreCase("Completed-Funds-Held"))) {
				throw new PaypalException("Failed performing payment, got status " + status);
			}

	}

	/**
	 * Return the name-value-pair parameters required for SetExpressCheckout and
	 * DoExpressCheckoutPayment steps.
	 *
	 */
	protected String getPurchaseData(double price)
	{
		return
			"&PAYMENTREQUEST_0_AMT=" + price +
			"&PAYMENTREQUEST_0_ITEMAMT=" + price +
//			"&PAYMENTREQUEST_0_DESC=" + itemDescription +
//			"&PAYMENTREQUEST_0_CUSTOM=" + userId +
//			"&PAYMENTREQUEST_0_INVNUM=" + itemId +
			"&L_PAYMENTREQUEST_0_NAME0=VideoChat" +
//			"&L_PAYMENTREQUEST_0_DESC0=" + itemDescription +
			"&L_PAYMENTREQUEST_0_AMT0=" + price +
			"&L_PAYMENTREQUEST_0_QTY0=" + 1 +
			"&L_PAYMENTREQUEST_0_ITEMCATEGORY0=Digital" +
			"";
	}

	/**
	 * Return the name-value-pair parameters required for all paypal api calls
	 * to authenticate the seller account.
	 */
	protected String getAuthenticationData()
	{
		return
			"&VERSION=69.0" +
			"&USER=" + apiUsername +
			"&PWD=" + apiPassword +
			"&SIGNATURE=" + apiSignature +
			"";
	}

	/**
	 * Send off the given data to PayPal's API and return the result in key-value pairs.
	 * Validate the ACK return value from paypal and throw an exception if it isn't "Success".
	 */
	protected HashMap<String, String> doServerCall (String data)
	{
		log.info("Sending data to paypal: " + data);

		String response = "";
		try {
			URL postURL = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection)postURL.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(7000);
			conn.setRequestMethod("POST");

			DataOutputStream output = new DataOutputStream(conn.getOutputStream());
			output.writeBytes(data);
			output.flush();
			output.close();

			// Read input from the input stream.
			int responseCode = conn.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Error " + responseCode + ": " + conn.getResponseMessage());
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while(((line = reader.readLine()) !=null)) {
				response = response + line;
			}
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		log.info("Got response from paypal: " + response);
		if(response.length() <= 0) {
			throw new RuntimeException("Received empty response");
		}
		HashMap<String, String> results = parsePaypalResponse(response);

		// first check for the new version (PAYMENTINFO_0_ACK)
		String ackString = results.get("PAYMENTINFO_0_ACK");
		if (ackString == null || !(ackString.equalsIgnoreCase("Success") || ackString.equalsIgnoreCase("SuccessWithWarning"))) {
			String errorCode = results.get("PAYMENTINFO_0_ERRORCODE");
			String errorLongMsg = results.get("PAYMENTINFO_0_LONGMESSAGE");
			if (errorCode != null && errorCode.trim().length() > 0) {
				throw new RuntimeException("Purchase Failed (code " + errorCode + "): " + errorLongMsg);
			}

			// sometimes API returns old version ACK instead of PAYMENTINFO_0_ACK
			ackString = results.get("ACK");
			if (ackString == null || !(ackString.equalsIgnoreCase("Success") || ackString.equalsIgnoreCase("SuccessWithWarning"))) {
				errorCode = results.get("L_ERRORCODE0");
				errorLongMsg = results.get("L_LONGMESSAGE0");
				throw new RuntimeException("Purchase Failed (code " + errorCode + "): " + errorLongMsg);
			}
		}

		return results;
	}

	/**
	 * Parse results from PayPal to a map of name/value pairs. Their format looks like:
	 * "TOKEN=EC%2d80X519901R8632201&TIMESTAMP=2012%2d07%2d13T09%3a57%3a44Z&ACK=Success"
	 */
	protected HashMap<String, String> parsePaypalResponse (String data)
	{
		HashMap<String, String> results = new HashMap<String, String>();
		StringTokenizer tokenizer = new StringTokenizer(data, "&");
		while (tokenizer.hasMoreTokens()) {
			StringTokenizer tokenizer2 = new StringTokenizer(tokenizer.nextToken(), "=");
			if (tokenizer2.countTokens() != 2) {
				continue;
			}
			String key = decodeValue(tokenizer2.nextToken());
			String value = decodeValue(tokenizer2.nextToken());
			results.put(key.toUpperCase(), value);
		}
		return results;
	}

	/**
	 * Prepare a given string for transmission via HTTP. Spaces become %20, etc.
	 */
	protected String encodeValue(String value)
	{
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Undo encoding of string that was sent via HTTP. %20 becomes a space, etc.
	 */
	protected String decodeValue(String value)
	{
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}