package fr.tse.info4.project.datarecovery;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;


/**
 * This class handles http connection to API URLs and the proper decoding of returned data
 *
 */
public class HttpConnect {
	   
	/**
	 * Read given URL, decode its content and return it as a String
	 * @param str_url
	 * @return Decoded URL content
	 * @throws IOException
	 */
	  public static String readUrl(String str_url) throws IOException{

	      URL url = new URL(str_url);
	      HttpURLConnection con = (HttpURLConnection) url.openConnection();
	      con.setRequestProperty("Accept-Encoding", "gzip");
	      Reader reader = null;
	      String res = "";

	      if ("gzip".equals(con.getContentEncoding())) {
	         reader = new InputStreamReader(new GZIPInputStream(con.getInputStream()));
	      }
	      else {
	         reader = new InputStreamReader(con.getInputStream());
	      }
	
	      while (true) {
	         int ch = reader.read();
	         if (ch==-1) {
	            break;
	         }
	         res += (char)ch;
	      }
	      return res;
   }
	  
}