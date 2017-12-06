package com.squareup.exemplar

import misk.MiskApplication
import misk.MiskModule
import misk.environment.Environment
import misk.environment.EnvironmentModule
import misk.hibernate.HibernateModule
import misk.moshi.MoshiModule
import misk.web.WebActionsModule
import misk.web.jetty.JettyModule

fun main(args: Array<String>) {
  MiskApplication(
      MiskModule(),
      WebActionsModule(),

      JettyModule(),

      HibernateModule(),
      ExemplarModule(),
      ExemplarConfigModule(),
      MoshiModule(),
      EnvironmentModule(Environment.DEVELOPMENT)
  ).startAndAwaitStopped()
}
