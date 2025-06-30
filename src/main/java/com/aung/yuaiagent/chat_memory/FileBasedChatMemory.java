package com.aung.yuaiagent.chat_memory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

/**
 * The InMemoryChatMemory class is an implementation of the ChatMemory interface that
 * represents an in-memory storage for chat conversation history.
 *
 * This class stores the conversation history in a ConcurrentHashMap, where the keys are
 * the conversation IDs and the values are lists of messages representing the conversation
 * history.
 *
 * @see ChatMemory
 * @author Christian Tzolov
 * @since 1.0.0 M1
 */
public class FileBasedChatMemory implements ChatMemory {

	private final String BASE_DIR;

	private static final Kryo kryo = new Kryo();

	static {
		kryo.setRegistrationRequired(false);

		//定义实例化策略
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
	}

	/**
	 *
	 * @param dir
	 */
	public FileBasedChatMemory(String dir) {
		this.BASE_DIR = dir;
		File baseDir = new File(BASE_DIR);
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}
	}

	/**
	 *
	 * @param conversationId
	 * @param messages
	 */


	public void add(String conversationId, List<Message> messages) {
		List<Message> conversationMessage = getOrCreateConversation(conversationId);
		conversationMessage.addAll(messages);
		saveConversation(conversationId, conversationMessage);
	}

	/**
	 *
	 * @param conversationId
	 * @param lastN
	 * @return
	 */

	public List<Message> get(String conversationId, int lastN) {
		List<Message> conversationMessage = getOrCreateConversation(conversationId);
		return conversationMessage.stream().skip(Math.max(0, conversationMessage.size() - lastN)).toList();
	}

	/**
	 *
	 * @param conversationId
	 */

	public void clear(String conversationId) {
		File file = getConversationFile(conversationId);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 *
	 * @param conversationId
	 * @return
	 */
	private File getConversationFile(String conversationId) {
		return new File(BASE_DIR, conversationId + ".kryo");
	}

	private List<Message> getOrCreateConversation(String conversationID){
		List<Message> messages = new ArrayList<>();
		File file = getConversationFile(conversationID);
		if (file.exists()) {
			try(Input input = new Input(new FileInputStream(file))){
				messages = kryo.readObject(input, ArrayList.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return messages;
	}

	/**
	 *
	 * @param conversationId
	 * @param conversationMessage
	 */

	private void saveConversation(String conversationId, List<Message> conversationMessage) {
		File file = getConversationFile(conversationId);

		try (Output output = new Output(new FileOutputStream(file));) {
			kryo.writeObject(output, conversationMessage);
		} catch (Exception e) {
			e.printStackTrace();
			}

	}
}
