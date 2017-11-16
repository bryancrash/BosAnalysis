package BOS.Dao;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

public class Mongodb {

	private DB mydb = null;

	// 连接Mongodb数据库
	public void getConnecteMongodb() {
		try {
			// 连接到mongodb服务
			Mongo mongoClient = new Mongo("localhost", 27017);

			// 连接到数据库
			mydb = mongoClient.getDB("tl");
			System.out.println("Connect to database successfully");

		} catch (Exception e) {
			System.out.println(e.getClass().getName() + ":" + e.getMessage());
		}
	}

	public void insertOne(String collection, BasicDBObject object) {
		getConnecteMongodb();
		DBCollection dbCol = mydb.getCollection(collection);
		System.out.println("集合common选择成功");

		WriteResult result = dbCol.insert(object);
		System.out.println("文档插入成功" + result);
	}

	public void inertMongodb(String collection, List<BasicDBObject> list) {
		getConnecteMongodb();
		// 选择集合
		DBCollection dbCol = mydb.getCollection(collection);
		System.out.println("集合 bos选择成功");

		// 创建文档集合List<Document>
		System.out.println("添加集合开始");

		System.out.println("---" + list.size());
		// 插入到集合中
		WriteResult result = dbCol.insert(list);
		System.out.println("文档插入成功" + result);
	}

	public DBObject findOne(String collection, BasicDBObject queryBasicDBObject) {
		getConnecteMongodb();
		DBCollection dbCol = mydb.getCollection(collection);
		DBObject obj = dbCol.findOne(queryBasicDBObject);

		return obj;
	}

	public List<DBObject> findList(String collection,
			BasicDBObject queryBasicDBObject) {
		getConnecteMongodb();
		DBCollection dbCol = mydb.getCollection(collection);
		DBCursor cursor = dbCol.find(queryBasicDBObject);
		List<DBObject> list = cursor.toArray();
		return list;
	}

	// 查询所有的记录
	public List<DBObject> findAll(String collection) {
		getConnecteMongodb();
		DBCollection dbCol = mydb.getCollection(collection);
		DBCursor cursor = dbCol.find();
		List<DBObject> list = cursor.toArray();
		return list;
	}
	
	public void deletedb(String collection){
		getConnecteMongodb();
		DBCollection dbCol = mydb.getCollection(collection);
	    dbCol.drop();
	}
	
	public void deleteOne(String collection,BasicDBObject queryBasicDBObject){
		getConnecteMongodb();
		DBCollection dbCol=mydb.getCollection(collection);
		dbCol.remove(queryBasicDBObject);
	}
}
