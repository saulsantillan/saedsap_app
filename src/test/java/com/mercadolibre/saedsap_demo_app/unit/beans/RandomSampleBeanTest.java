package com.mercadolibre.saedsap_demo_app.unit.beans;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mercadolibre.saedsap_demo_app.beans.RandomSampleBean;
import com.mercadolibre.saedsap_demo_app.dtos.SampleDTO;
import org.junit.jupiter.api.Test;

class RandomSampleBeanTest {

  @Test
  void randomPositiveTestOK() {
    RandomSampleBean randomSample = new RandomSampleBean();

    SampleDTO sample = randomSample.random();

    assertTrue(sample.getRandom() >= 0);
  }
}
