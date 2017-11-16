package BOS.Dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;

import BOS.Result.SearchResult;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SearchResultDao {
	String bos = "bos";

	// 从数据查找一条记录
	public boolean findOneResult(String type, String word) {
		// 利用正则表达式进行查找
		Pattern pattern = Pattern.compile(".*" + type + "");
		BasicDBObject queryBasicDBObject = new BasicDBObject("fileName",
				new BasicDBObject("$regex", pattern)).append("content", word);

		Mongodb mongo = new Mongodb();
		DBObject obj = mongo.findOne(bos, queryBasicDBObject);

		// 如果存在这条记录，判断这条记录是否是在指定时间段内的记录
		if (obj != null) {
			ObjectId objId = (ObjectId) obj.get("_id");
			Date createTime = objId.getDate();
			Date currentTime = new Date();
			System.out.println();
			if (currentTime.getTime() - createTime.getTime() < 3600000 * 34 * 4) {
				return true;
			}
			return false;
		}
		return false;
	}

	// 查找符合条件的所有记录
	public List<SearchResult> findList(String type, String content) {
		Pattern pattern = Pattern.compile(".*" + type + "");
		BasicDBObject queryBasicDBObject = new BasicDBObject("fileName",
				new BasicDBObject("$regex", pattern))
				.append("content", content);

		// 连接mongodb数据库
		Mongodb mongo = new Mongodb();
		List<DBObject> list = mongo.findList(bos, queryBasicDBObject);
		List<SearchResult> resultList = new ArrayList();

		System.out.println(list.size());
		// 遍历查询结果，将结果转化为需要的结果类型
		for (int i = 0; i < list.size(); i++) {
			SearchResult sr = new SearchResult();
			sr.setCompanyId((String) list.get(i).get("companyId"));
			sr.setFileName((String) list.get(i).get("fileName"));
			sr.setLineNumber(Integer.valueOf(String.valueOf(list.get(i).get("LineNumber"))).intValue());
			sr.setContent((String) list.get(i).get("content"));
			resultList.add(sr);
		}
		return resultList;
	}

	// 向数据库插入数据
	public void insertIntoMongodb(List<SearchResult> list) {
		List<BasicDBObject> dbList = new ArrayList<BasicDBObject>();
		for (int i = 0; i < list.size(); i++) {
			// System.out.println(list.get(i));
			BasicDBObject obj = new BasicDBObject();
			obj.put("companyId", list.get(i).getCompanyId());
			obj.put("fileName", list.get(i).getFileName());
			obj.put("LineNumber", list.get(i).getLineNumber());
			obj.put("content", list.get(i).getContent());
			dbList.add(obj);
		}
		Mongodb mongo = new Mongodb();
		mongo.inertMongodb(bos, dbList);
	}
	
	//删除数据库中所有的数据
	public void deleteDB(){
		Mongodb mongo=new Mongodb();
		mongo.deletedb("bos");
	}
}
