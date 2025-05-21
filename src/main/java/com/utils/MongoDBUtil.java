package com.utils;

import static com.mongodb.client.model.Filters.eq;

import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtil {

	private static MongoClient mongoClient;
	private static MongoDatabase database;

	public static synchronized void init(String uri, String dbName) {
		if (mongoClient == null) {
			mongoClient = MongoClients
					.create(MongoClientSettings.builder().applyConnectionString(new ConnectionString(uri))
							.uuidRepresentation(UuidRepresentation.STANDARD).build());
			database = mongoClient.getDatabase(dbName);
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
			Object value = entry.getValue();
			if (value instanceof UUID) {
				filter.append(entry.getKey(), value.toString());
			} else {
				filter.append(entry.getKey(), value);
			}
		}

		CodecRegistry codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
		System.out.println("Querying MongoDB collection: " + collectionName);
		System.out.println("With filter: " + filter.toBsonDocument(Document.class, codecRegistry).toJson());

		return collection.find(filter).first();
	}

	public static void close() {
		if (mongoClient != null) {
			try {
				mongoClient.close();
			} finally {
				mongoClient = null;
				database = null;
			}
		}
	}

}