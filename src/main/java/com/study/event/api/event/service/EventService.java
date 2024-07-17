package com.study.event.api.event.service;

import com.study.event.api.auth.TokenProvider;
import com.study.event.api.event.dto.request.EventSaveDto;
import com.study.event.api.event.dto.response.EventDetailDto;
import com.study.event.api.event.dto.response.EventOneDto;
import com.study.event.api.event.entity.Event;
import com.study.event.api.event.entity.EventUser;
import com.study.event.api.event.repository.EventRepository;
import com.study.event.api.event.repository.EventUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final EventUserRepository eventUserRepository;

    // 전체 조회 서비스
    public Map<String, Object> getEvents(int pageNo, String sort, String userId) {

        Pageable pageable = PageRequest.of(pageNo - 1 , 4);

        Page<Event> eventsPage = eventRepository.findEvents(sort, pageable, userId);

//        EventUser eventUser = eventUserRepository.findById(userId).orElseThrow();
//
//        List<Event> events = eventUser.getEventList();

        // 이벤트 목록
        List<Event> events = eventsPage.getContent();

        List<EventDetailDto> eventDtoList = events
                .stream().map(EventDetailDto::new)
                .collect(Collectors.toList());
//
        // 총 이벤트 개수
        long totalElements = eventsPage.getTotalElements();

        Map<String, Object> map = new HashMap<>();
        map.put("events", eventDtoList);
        map.put("totalCount", totalElements);

        return map;
    }

    // 이벤트 등록
    public List<EventDetailDto> saveEvent(EventSaveDto dto, TokenProvider.TokenUserInfo tokenInfo) {

        // 로그인한 회원 정보 조회
        EventUser eventUser = eventUserRepository.findById(tokenInfo.getUserId()).orElseThrow();

        Event newEvent = dto.toEntity();
        newEvent.setEventUser(eventUser);

        Event savedEvent = eventRepository.save(newEvent);

        log.info("saved event: {}", savedEvent);

        return null;
    }

    // 이벤트 단일 조회
    public EventOneDto getEventDetail(Long id) {
        Event foundEvent = eventRepository.findById(id).orElseThrow();

        return new EventOneDto(foundEvent);

    }

    // 이벤트 삭제
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    // 이벤트 삭제
    public void modifyEvent(Long id, EventSaveDto dto) {
        Event foundEvent = eventRepository.findById(id).orElseThrow();

        foundEvent.changeEvent(dto);

        eventRepository.save(foundEvent);

    }
}
