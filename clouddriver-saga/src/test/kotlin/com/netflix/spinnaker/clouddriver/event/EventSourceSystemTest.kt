/*
 * Copyright 2019 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.spinnaker.clouddriver.event

import com.netflix.spectator.api.NoopRegistry
import com.netflix.spectator.api.Registry
import com.netflix.spinnaker.clouddriver.event.config.EventSourceAutoConfiguration
import com.netflix.spinnaker.clouddriver.event.persistence.MemoryEventRepository
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.assertj.AssertableApplicationContext
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isA

class EventSourceSystemTest : JUnit5Minutests {

  fun tests() = rootContext<ApplicationContextRunner> {
    fixture {
      ApplicationContextRunner()
      .withConfiguration(AutoConfigurations.of(
        EventSourceAutoConfiguration::class.java
      ))
    }

    test("supports no config") {
      withUserConfiguration(EventSourceAutoConfiguration::class.java, DependencyConfiguration::class.java)
        .run { ctx: AssertableApplicationContext ->
          expectThat(ctx.getBean("eventRepository")).isA<MemoryEventRepository>()
          expect {
            that(ctx.getBean("eventRepository")).describedAs("eventRepository").isA<MemoryEventRepository>()
          }
        }
    }
  }

  @Configuration
  open class DependencyConfiguration {
    @Bean
    open fun registry(): Registry = NoopRegistry()
  }
}
