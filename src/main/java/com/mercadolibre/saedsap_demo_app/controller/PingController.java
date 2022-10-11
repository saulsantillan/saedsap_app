package com.mercadolibre.saedsap_demo_app.controller;


import com.mercadolibre.saedsap_demo_app.service.ParserXML;
import com.newrelic.api.agent.NewRelic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for /ping implementation.
 */
@RestController
public class PingController {
  private static final Logger log = LoggerFactory.getLogger(PingController.class);
  @Autowired
  ParserXML parserXML;
  /**
   * @return "pong" String.
   */
  @GetMapping("/ping")
  public String ping() {
    NewRelic.ignoreTransaction();
    return "pong";
  }

  /**
   * @return "Hola mundo" String.
   */
  @GetMapping("/mundo")
  public String mundo() {
    NewRelic.ignoreTransaction();
    return "Hola Mundo";
  }

  /**
   * @return "dto" String.
   */
  @PostMapping(path = "/parser")
  public void parser(@RequestBody String xml) {
    NewRelic.ignoreTransaction();
    log.info("Entramos a metodo Parser");
    parserXML.parserTransacciones(xml);
  }

}
