package org.sports.exercise.battle.server.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sports.exercise.battle.server.exceptions.MessageSerializationException;

public class JsonMessageMarshaller {
    private final ObjectMapper objectMapper;

    public JsonMessageMarshaller() {
        this.objectMapper = new ObjectMapper();
    }

    public String marshalToJSON(Object message) throws MessageSerializationException {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new MessageSerializationException("Could not serialize object to JSON", e);
        }
    }

    public <T> T unmarshalFromJSON(String json, Class<T> type) throws MessageSerializationException {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new MessageSerializationException("Could not deserialize object to DTO", e);
        }
    }
}