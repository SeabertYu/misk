package misk.clustering.zookeeper

import com.google.common.util.concurrent.Service
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.google.inject.Key
import com.google.inject.Provides
import misk.inject.KAbstractModule
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.ExponentialBackoffRetry
import javax.inject.Singleton

class ZookeeperModule : KAbstractModule() {
  override fun configure() {
    multibind<Service>().to<ZkService>()
    multibind<Service>().to<ZkLeaseManager>()
    bind<CuratorFramework>().toProvider(CuratorFrameworkProvider::class.java)
  }

  companion object {
    /** @property Key<*> The key of the service which manages the zk connection, for service dependencies */
    val serviceKey: Key<*> = Key.get(ZkService::class.java) as Key<*>

    /** @property Key<*> the Key of the lease manager service */
    val leaseManagerKey: Key<*> = Key.get(ZkLeaseManager::class.java)
  }
}