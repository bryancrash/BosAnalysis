package BOS.Dao;

import java.util.List;

import net.sf.json.JSONArray;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class CommonDao {
	String common = "common";

	public void insertCommon(String addcommon) {
		Mongodb mongo = new Mongodb();
		BasicDBObject obj = new BasicDBObject();
		obj.put("common", addcommon);
		DBObject dbobject=mongo.findOne(common, obj);
		if(dbobject!=null){
			return;
		}else{
		mongo.insertOne(common, obj);
		}
	}

	// 查询所有的记录
	public JSONArray find() {
		Mongodb mongo = new Mongodb();
		List<DBObject> list = mongo.findAll(common);
		JSONArray json = JSONArray.fromObject(list);
		return json;
	}
	
	//删除一条记录
	public void deleteCommon(String deletecommon){
		Mongodb mongo = new Mongodb();
		BasicDBObject obj = new BasicDBObject();
		obj.put("common", deletecommon);
		mongo.deleteOne(common, obj);
	}
}
