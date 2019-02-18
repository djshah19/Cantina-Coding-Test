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
		String[] compound = null;
		if(selector.contains("#")){
			compound = selector.split("#");
		}
		JSONArray subviews = (JSONArray) jsonObject.get("subviews");
		if(subviews==null){
			JSONObject contentView = (JSONObject) jsonObject.get("contentView");
			if(contentView==null){
				return set;
			}
			subviews = (JSONArray) contentView.get("subviews");
			if(subviews==null){
				return set;
			}
		}
		for(int i = 0;i<subviews.size();i++){
			JSONObject jsonObject1 = (JSONObject) subviews.get(i);
			JSONArray classNames = (JSONArray) jsonObject1.get("classNames");
			JSONObject control = (JSONObject) jsonObject1.get("control");
			
			if((compound!=null&&classNames!=null&&jsonObject1.get("class").equals(compound[0])&&classNames.contains(compound[1]))
				|| (compound!=null&&control!=null&&jsonObject1.get("class").equals(compound[0])&&control.containsKey("identifier") 
					&& control.containsValue(compound[1]))){
				set.add(jsonObject1);
			}
			else if(jsonObject1.get("class").equals(selector)){
				set.add(jsonObject1);
			}else if(classNames!=null&&classNames.contains(selector)){
				set.add(jsonObject1);
			}else if(control!=null && control.containsKey("identifier") && control.containsValue(selector))
			{
				set.add(jsonObject1);
			}
			set.addAll(recurseJson(jsonObject1, selector));
		}
		return set;
	}

}
