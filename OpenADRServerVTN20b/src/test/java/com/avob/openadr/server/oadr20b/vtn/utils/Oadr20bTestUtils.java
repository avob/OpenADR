package com.avob.openadr.server.oadr20b.vtn.utils;

import java.io.IOException;
import java.util.List;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Oadr20bTestUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T convertMvcResultToDto(MvcResult result, Class<T> klass)
            throws JsonParseException, JsonMappingException, IOException {
        MockHttpServletResponse mockHttpServletResponse = result.getResponse();
        byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
        return mapper.readValue(contentAsByteArray, klass);
    }

    public static <T> List<T> convertMvcResultToDtoList(MvcResult result, Class<T> klass)
            throws JsonParseException, JsonMappingException, IOException {
        MockHttpServletResponse mockHttpServletResponse = result.getResponse();
        byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, klass);
        return mapper.readValue(contentAsByteArray, type);
    }
    
}
