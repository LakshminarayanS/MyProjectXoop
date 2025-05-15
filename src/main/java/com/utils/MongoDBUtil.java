package com.utils;

import static com.mongodb.client.model.Filters.eq;

import java.util.Map;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtil {

	private static MongoClient mongoClient;
	private static MongoDatabase database;

	public static void init(String uri, String dbName) {
		if (mongoClient == null) {
			synchronized (MongoDBUtil.class) {
				if (mongoClient == null) {
					mongoClient = MongoClients.create(uri);
					database = mongoClient.getDatabase(dbName);
				}
			}
		}
	}

	public static MongoCollection<Document> getCollection(String collectionName) {

		if (database == null) {
			throw new IllegalStateException("MongoDBUtil not initialized. Call init() first.");
		}
		return database.getCollection(collectionName);
	}

	public static Document getDocumentById(String collectionName, String objectId) {

		try {
			return getCollection(collectionName).find(new Document("_id", new ObjectId(objectId))).first();
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid ObjectId: " + objectId, e);
		}
	}

	public static Document getDocumentByField(String collectionName, String fieldName, Object fieldValue) {
		return getCollection(collectionName).find(eq(fieldName, fieldValue)).first();
	}

	public static Document getDocumentByFields(String collectionName, Map<String, Object> fieldMap) {

		if (database == null)
			throw new IllegalStateException("MongoDB not initialized");
		
		System.out.println("Expected fieldMap: " + fieldMap);
		
		 MongoCollection<Document> collection = database.getCollection(collectionName);

		    Document filter = new Document();
		    for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
		        filter.append(entry.getKey(), entry.getValue());
		    }

		    CodecRegistry codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
		    System.out.println("Querying MongoDB collection: " + collectionName);
		    System.out.println("With filter: " + filter.toBsonDocument(Document.class, codecRegistry).toJson());

		    return collection.find(filter).first();
	}

	public static void close() {
		if (mongoClient != null) {
			mongoClient.close();
			mongoClient = null;
		}
	}

}
