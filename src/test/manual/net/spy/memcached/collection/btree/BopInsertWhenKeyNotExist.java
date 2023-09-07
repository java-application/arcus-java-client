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
package net.spy.memcached.collection.btree;

import java.util.concurrent.TimeUnit;

import net.spy.memcached.collection.BaseIntegrationTest;
import net.spy.memcached.collection.CollectionAttributes;

public class BopInsertWhenKeyNotExist extends BaseIntegrationTest {

  private String key = "BopInsertWhenKeyNotExist";

  private String[] items9 = {
      "value0", "value1", "value2", "value3",
      "value4", "value5", "value6", "value7", "value8"
  };

  @Override
  protected void tearDown() throws Exception {
    deleteBTree(key, items9);
    super.tearDown();
  }

  /**
   * <pre>
   * CREATE FIXED VALUE
   * true false null
   * </pre>
   */
  public void testBopInsert_nokey_01() throws Exception {
    insertToFail(key, true, null);
  }

  /**
   * <pre>
   * CREATE FIXED VALUE
   * false  true  not null
   * </pre>
   */
  public void testBopInsert_nokey_02() throws Exception {
    assertFalse(insertToSucceed(key, false, items9[0]));
  }

  /**
   * <pre>
   * CREATE FIXED VALUE
   * false  false not null
   * </pre>
   */
  public void testBopInsert_nokey_04() throws Exception {
    assertFalse(insertToSucceed(key, false, items9[0]));
  }

  /**
   * <pre>
   * CREATE FIXED VALUE
   * true true  not null
   * </pre>
   */
  public void testBopInsert_nokey_05() throws Exception {
    assertTrue(insertToSucceed(key, true, items9[0]));
  }

  boolean insertToFail(String key, boolean createKeyIfNotExists, Object value) {
    boolean result = false;
    try {
      result = mc
              .asyncBopInsert(
                      key,
                      0,
                      null,
                      value,
                      ((createKeyIfNotExists) ? new CollectionAttributes()
                              : null)).get(1000, TimeUnit.MILLISECONDS);
      fail("should be failed");
    } catch (Exception e) {
      // test success.
    }
    return result;
  }

  boolean insertToSucceed(String key, boolean createKeyIfNotExists,
                          Object value) {
    boolean result = false;
    try {
      result = mc
              .asyncBopInsert(
                      key,
                      0,
                      null,
                      value,
                      ((createKeyIfNotExists) ? new CollectionAttributes()
                              : null)).get(1000, TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      e.printStackTrace();
      fail("should not be failed");
    }
    return result;
  }

}
