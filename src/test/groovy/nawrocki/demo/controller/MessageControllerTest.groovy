package nawrocki.demo.controller

import nawrocki.demo.DemoApplication
import nawrocki.demo.service.MessageService
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [MessageControllerConfig, DemoApplication])
class MessageControllerTest extends Specification {

    @Autowired
    TestRestTemplate restTemplate
    @Autowired
    MessageService messageService

    def "get available feedback"() {
        when:

        def headers = new HttpHeaders()
        headers.setContentType(MediaType.TEXT_HTML);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        then:
        response.statusCode == expectedResponseCode
        serviceCall * messageService.findAll(_, _)

        where:
        url                                     || expectedResponseCode   || serviceCall
        "/list.html"                            || HttpStatus.OK          || 1
        "/list.html?type=SUPPORT"               || HttpStatus.OK          || 1
        "/list.html?sort=date,ASC"              || HttpStatus.OK          || 1
        "/list.html?sort=date,ASC&type=SUPPORT" || HttpStatus.OK          || 1
        "/list.html?sort=date,ASC&type=query"   || HttpStatus.BAD_REQUEST || 0


    }

    def "create feedback"() {


        when:

        def headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>()
        map.add("name", name)
        map.add("email", email)
        map.add("type", type)
        map.add("content", content)
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers)
        ResponseEntity<String> response = restTemplate.exchange("/index.html", HttpMethod.POST, request, String.class);

        then:
        response.statusCode == expectedResponseCode
        executeSave * messageService.create(_)

        where:
        name                                                             || email                                                            || type        || content                                                           || expectedResponseCode || executeSave
        "michal nawrocki"                                                || "michal@test.co.uk"                                              || "GENERAL"   || "welcome message"                                                 || HttpStatus.FOUND     || 1
        "michal nawrocki"                                                || "michal@test.co.uk"                                              || "MARKETING" || "welcome message"                                                 || HttpStatus.FOUND     || 1
        "michal nawrocki"                                                || "michal@test.co.uk"                                              || "SUPPORT"   || "welcome message"                                                 || HttpStatus.FOUND     || 1
        "michal nawrocki"                                                || "michal@test.co.uk"                                              || "GENERAL2"  || "welcome message"                                                 || HttpStatus.OK        || 0
        "michal nawrocki"                                                || "michal@test.co.uk"                                              || null        || "welcome message"                                                 || HttpStatus.OK        || 0
        null                                                             || null                                                             || "SUPPORT"   || "welcome message"                                                 || HttpStatus.FOUND     || 1
        ""                                                               || ""                                                               || "SUPPORT"   || "welcome message"                                                 || HttpStatus.FOUND     || 1
        RandomStringUtils.random(101, (('A'..'Z')).join().toCharArray()) || "michal@test.co.uk"                                              || "GENERAL"   || "welcome message"                                                 || HttpStatus.OK        || 0
        RandomStringUtils.random(1, (('A'..'Z')).join().toCharArray())   || RandomStringUtils.random(101, (('A'..'Z')).join().toCharArray()) || "GENERAL"   || "welcome message"                                                 || HttpStatus.OK        || 0
        RandomStringUtils.random(1, (('A'..'Z')).join().toCharArray())   || RandomStringUtils.random(10, (('A'..'Z')).join().toCharArray())  || "GENERAL"   || null                                                              || HttpStatus.OK        || 0
        RandomStringUtils.random(1, (('A'..'Z')).join().toCharArray())   || RandomStringUtils.random(10, (('A'..'Z')).join().toCharArray())  || "GENERAL"   || ""                                                                || HttpStatus.OK        || 0
        RandomStringUtils.random(1, (('A'..'Z')).join().toCharArray())   || RandomStringUtils.random(10, (('A'..'Z')).join().toCharArray())  || "GENERAL"   || RandomStringUtils.random(1001, (('A'..'Z')).join().toCharArray()) || HttpStatus.OK        || 0
        RandomStringUtils.random(1, (('A'..'Z')).join().toCharArray())   || RandomStringUtils.random(10, (('A'..'Z')).join().toCharArray())  || "GENERAL"   || RandomStringUtils.random(999, (('A'..'Z')).join().toCharArray())  || HttpStatus.FOUND     || 1
    }


}
