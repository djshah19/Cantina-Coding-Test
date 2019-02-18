package Main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ReadJsonFile {

	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
	
		try{
		Object obj = parser.parse(new FileReader("C:\\Users\\Dhwani Shah\\Desktop\\IS\\myJSON.json"));
		JSONObject jsonObject = (JSONObject)obj;
		Set<JSONObject> set = recurseJson(jsonObject);
		System.out.println(set.size());
		for(JSONObject val : set){
			System.out.println(val.toJSONString());
		}
		
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(ParseException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Set<JSONObject> recurseJson(JSONObject jsonObject){
		Set<JSONObject> set = new HashSet<>();
		JSONArray arr = (JSONArray) jsonObject.get("subviews");
		if(arr==null){
			JSONObject jsonObject2 = (JSONObject) jsonObject.get("contentView");
			if(jsonObject2==null){
				return set;
			}
			arr = (JSONArray) jsonObject2.get("subviews");
			if(arr==null){
				return set;
			}
		}
		for(int i = 0;i<arr.size();i++){
			JSONObject jsonObject1 = (JSONObject) arr.get(i);
			JSONArray arr1 = (JSONArray) jsonObject1.get("classNames");
			if(jsonObject1.get("class").equals("Input")){
				set.add(jsonObject1);
			}else if(arr1!=null&&arr1.contains("container")){
				set.add(jsonObject1);
			}
			set.addAll(recurseJson(jsonObject1));
		}
		return set;
	}

}