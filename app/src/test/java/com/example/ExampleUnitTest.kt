package com.example

import com.example.data.RaqeebRepository
import com.example.model.PhoneProblemType
import com.example.model.ServiceSection
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testRepositoryProvidesAllPhoneServices() {
    val repository = RaqeebRepository()
    val services = repository.getPhoneServices()
    assertTrue(services.isNotEmpty())
    assertTrue(services.any { it.type == PhoneProblemType.LOCK_BYPASS })
    assertTrue(services.any { it.type == PhoneProblemType.CARRIER_UNLOCK })
    assertTrue(services.any { it.type == PhoneProblemType.ACCOUNT_RECOVERY })
    assertTrue(services.any { it.type == PhoneProblemType.DATA_RECOVERY })
    assertTrue(services.any { it.type == PhoneProblemType.ARABIZATION })
    assertTrue(services.any { it.type == PhoneProblemType.DATA_ACTIVATION_3G_4G })
  }

  @Test
  fun testServiceSectionsContainLockSimulator() {
    val sections = ServiceSection.values()
    assertTrue(sections.contains(ServiceSection.LOCK_RECOVERY_SIMULATOR))
    assertTrue(sections.contains(ServiceSection.PHONE_SOLUTIONS))
    assertTrue(sections.contains(ServiceSection.MARKETING))
  }
}
