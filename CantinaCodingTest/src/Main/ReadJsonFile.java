package Main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ReadJsonFile {

	public static void main(String[] args) {
		System.out.println("Enter the selector: ");
		Scanner scanner = new Scanner(System.in);
		String selector = scanner.nextLine();
		JSONParser parser = new JSONParser();
	
		try{
		Object obj = parser.parse(new FileReader(args[0].replace("_", " ")));
		JSONObject jsonObject = (JSONObject)obj;
		Set<JSONObject> set = new HashSet<>();
		if(selector.contains(" ")){
			String[] array = selector.split(" ");
			for(String sel : array){
		     set.addAll(recurseJson(jsonObject, sel));		
			}
		}else{
		set = recurseJson(jsonObject, selector);}
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
	
	public static Set<JSONObject> recurseJson(JSONObject jsonObject, String selector){
		Set<JSONObject> set = new HashSet<>();
		String[] array = null;
		if(selector.contains("#")){
			array = selector.split("#");
		}
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
			JSONObject jsonObject3 = (JSONObject) jsonObject1.get("control");
			
			if((array!=null&&arr1!=null&&jsonObject1.get("class").equals(array[0])&&arr1.contains(array[1]))
				|| (array!=null&&jsonObject3!=null&&jsonObject1.get("class").equals(array[0])&&jsonObject3.containsKey("identifier") 
					&& jsonObject3.containsValue(array[1]))){
				set.add(jsonObject1);
			}
			else if(jsonObject1.get("class").equals(selector)){
				set.add(jsonObject1);
			}else if(arr1!=null&&arr1.contains(selector)){
				set.add(jsonObject1);
			}else if(jsonObject3!=null && jsonObject3.containsKey("identifier") && jsonObject3.containsValue(selector))
			{
				set.add(jsonObject1);
			}
			set.addAll(recurseJson(jsonObject1, selector));
		}
		return set;
	}

}
