package com.example.connection.service;

import com.example.connection.bean.FormData;
import com.example.connection.exception.FormatException;
import com.example.connection.mapper.SearchMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

class SearchServiceTest {

    @Test
    void addDevice1() {
        FormData formData = new FormData("name", "lock", "ROLE_ADMIN", "HTTP");
        SearchMapper mockMapper = mock(SearchMapper.class);
        SearchService searchService = new SearchService();
        searchService.setSearchMapper(mockMapper);
        assertEquals(true, searchService.addDevice(formData).isSuccess());
    }

    @Test
    void addDevice2() {
        FormData formData = new FormData("name", "lock", "ROLE_ADMIN", "ABCD");
        SearchMapper mockMapper = mock(SearchMapper.class);
        SearchService searchService = new SearchService();
        searchService.setSearchMapper(mockMapper);
        assertThrows(FormatException.class, () -> {
            searchService.addDevice(formData);
        });
    }

    @Test
    void getAllTopicWithClients() {
        SearchMapper mockMapper = mock(SearchMapper.class);
        SearchService searchService = new SearchService();
        searchService.setSearchMapper(mockMapper);
        assertEquals(true, searchService.getAllTopicWithClients().isSuccess());
    }

    @Test
    void getDeviceById1() {
        SearchMapper mockMapper = mock(SearchMapper.class);
        SearchService searchService = new SearchService();
        searchService.setSearchMapper(mockMapper);
        assertThrows(NullPointerException.class, () -> {
            searchService.getDeviceById(60);
        });
    }

    @Test
    void getDeviceById2() {
        SearchMapper mockMapper = mock(SearchMapper.class);
        SearchService searchService = new SearchService();
        searchService.setSearchMapper(mockMapper);
        assertThrows(NullPointerException.class, () -> {
            searchService.getDeviceById(100);
        });
    }

    @Test
    void getTopicsWithContentByClientId1() {
        SearchMapper mockMapper = mock(SearchMapper.class);
        SearchService searchService = new SearchService();
        searchService.setSearchMapper(mockMapper);
        assertEquals(true, searchService.getTopicsWithContentByClientId(61).isSuccess());
    }

    @Test
    void getTopicsWithContentByClientId2() {
        SearchMapper mockMapper = mock(SearchMapper.class);
        SearchService searchService = new SearchService();
        searchService.setSearchMapper(mockMapper);
        assertEquals(0, ((List)searchService.getTopicsWithContentByClientId(60).getDetail()).size());
    }

    @Test
    void getPublicKey() {
        SearchMapper mockMapper = mock(SearchMapper.class);
        SearchService searchService = new SearchService();
        searchService.setSearchMapper(mockMapper);
        assertEquals(true, searchService.getPublicKey().isSuccess());
    }

    @Test
    void getAllDevices() {
        SearchMapper mockMapper = mock(SearchMapper.class);
        SearchService searchService = new SearchService();
        searchService.setSearchMapper(mockMapper);
        assertEquals(true, searchService.getAllDevices().isSuccess());
    }

    @Test
    void getAllContentsByTopic() {
        SearchMapper mockMapper = mock(SearchMapper.class);
        SearchService searchService = new SearchService();
        searchService.setSearchMapper(mockMapper);
        assertEquals(0, ((List)searchService.getAllContentsByTopic("english").getDetail()).size());
    }

    @Test
    void getAllTopicsByClientId() {
        SearchMapper mockMapper = mock(SearchMapper.class);
        SearchService searchService = new SearchService();
        searchService.setSearchMapper(mockMapper);
        assertEquals(0, ((List)searchService.getAllTopicsByClientId(60).getDetail()).size());
    }

    @Test
    void getAllRoles() {
        SearchMapper mockMapper = mock(SearchMapper.class);
        SearchService searchService = new SearchService();
        searchService.setSearchMapper(mockMapper);
        assertEquals(true, searchService.getAllRoles().isSuccess());
    }

    @Test
    void getAllDevTypes() {
        SearchMapper mockMapper = mock(SearchMapper.class);
        SearchService searchService = new SearchService();
        searchService.setSearchMapper(mockMapper);
        assertEquals(true, searchService.getAllDevTypes().isSuccess());
    }
}