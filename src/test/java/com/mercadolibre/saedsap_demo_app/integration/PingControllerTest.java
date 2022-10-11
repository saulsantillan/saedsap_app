package com.mercadolibre.saedsap_demo_app.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class PingControllerTest extends ControllerTest {

  @Test
  void ping() {
    ResponseEntity<String> responseEntity =
        this.testRestTemplate.exchange(
            "/ping", HttpMethod.GET, this.getDefaultRequestEntity(), String.class);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals("pong", responseEntity.getBody());
  }

  @Test
  void mundo() {
    ResponseEntity<String> responseEntity =
            this.testRestTemplate.exchange(
                    "/mundo", HttpMethod.GET, this.getDefaultRequestEntity(), String.class);
    System.out.println("Respuesta mundo"+responseEntity.getBody());
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals("Hola Mundo", responseEntity.getBody());
  }

}
