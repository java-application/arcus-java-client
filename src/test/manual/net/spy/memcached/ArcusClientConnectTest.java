/*
 * arcus-java-client : Arcus Java client
 * Copyright 2010-2014 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.spy.memcached;

import junit.framework.TestCase;
import net.spy.memcached.collection.BaseIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

import static org.junit.Assume.assumeTrue;

@RunWith(JUnit4ClassRunner.class)
public class ArcusClientConnectTest extends TestCase {

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    // This test assumes we use ZK
    assumeTrue(BaseIntegrationTest.USE_ZK);
  }

  @Test
  public void testOpenAndWait() {
    ConnectionFactoryBuilder cfb = new ConnectionFactoryBuilder();
    ArcusClient client = ArcusClient.createArcusClient(BaseIntegrationTest.ZK_HOST,
            BaseIntegrationTest.ZK_SERVICE_ID, cfb);
    client.shutdown();
  }
}
