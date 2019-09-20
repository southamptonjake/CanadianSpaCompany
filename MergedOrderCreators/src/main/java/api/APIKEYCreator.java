package api;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class APIKEYCreator {


	public class ApiJson
	{
		String veeqoApi;
		String postcoderApi;
	}

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader("environment.json"));
		ApiJson data = gson.fromJson(reader, ApiJson.class);
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("./src/main/java/api/APIKEYS.java"), "utf-8"))) {
			writer.write("package api; \n"
					+ "public class APIKEYS {\n" + 
					"	\n" + 
					"	public static String veeqoApi = \""+data.veeqoApi+"\" ;\n" +
					"	public static String postcoderApi = \""+data.postcoderApi+"\" ;\n" +

					"\n" + 
					"\n" + 
					"}");
		}

	}

}
