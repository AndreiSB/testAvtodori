package ru.apache_maven;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * Test Avtodoria
 * Bogapov A.S.
 */

public class App {

	public final static boolean main(String[] args) throws Exception {
	
		String responseBody;
		String srchHttpAddr;
		String[] srchWords;
		boolean success;
		
		//параметры по умолчанию
		if(args.length<=1) {
			srchHttpAddr = "https://translate.google.com/#en/ru/horse";
			srchWords = new String[2];
			srchWords[0] = "horse";
			srchWords[1] = "лошадь";
		}
		else {
			//передали больше одного параметра
			srchHttpAddr = args[0];
			srchWords = new String[args.length-1];
			for (int i = 0; i < (args.length-1); i++)
			{
				srchWords[i] = args[i+1];
			}
		
		}
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		try {
			HttpGet httpget = new HttpGet(srchHttpAddr);
			
			System.out.println("Executing request " + httpget.getRequestLine());
			
			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
			
				@Override
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			
			};
			responseBody = httpclient.execute(httpget, responseHandler);
			//System.out.println("----------------------------------------");
			//System.out.println(responseBody);
		} finally {
			httpclient.close();
		}
		
		//поиск слов в теле ответа
		for (String s: srchWords) {
			if(!responseBody.contains(s)){
				success=false;
				System.out.println( "Слово : '"+s+"' - нет" );
				System.out.println("----------------------------------------");
				System.out.println( "Веб страница не содержит все искомые слова");
				return success;
			}
			else{
				System.out.println( "Слово : '"+s+"' - да" );
			}
		}
		
		success=true;
		System.out.println("----------------------------------------");
		System.out.println( "Веб страница содержит все искомые слова");
		return success;
		
	}

}
