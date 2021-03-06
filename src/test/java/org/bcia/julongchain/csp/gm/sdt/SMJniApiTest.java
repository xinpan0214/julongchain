/**
 * Copyright SDT. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bcia.julongchain.csp.gm.sdt;

import org.bcia.julongchain.common.util.Convert;
import org.bcia.julongchain.csp.gm.sdt.jni.SMJniApi;
import org.junit.Test;

/**
 * SMJniApi 产生随机数接口单元测试
 *
 * @author tengxiumin
 * @date 2018/05/17
 * @company SDT
 */
public class SMJniApiTest {

    private SMJniApi jni = new SMJniApi();

    @Test
    public void testRandomGen() {
        System.out.println("============= SMJniApi randomGen test =============");
        int[] randomLen = {1, 16, 32, 128, 240, 1024};
        unitTestGenRandom(randomLen);
    }

    @Test
    public void testRandomGenInvalidParams() {
        System.out.println("============= SMJniApi randomGen invalid parameters test =============");
        int[] randomLen = {-1, 0, 1025};
        unitTestGenRandom(randomLen);
    }

    private void unitTestGenRandom(int[] lists) {
        int caseIndex = 1;
        for (int index = 0; index < lists.length; index++) {
            try {
                int len = lists[index];
                System.out.println("\n**** case " + caseIndex++ + ": generate random length = " + len + "  ****");
                byte[] random = jni.randomGen(len);
                if (null != random) {
                    System.out.println("[ output ] random data : " + Convert.bytesToHexString(random));
                } else {
                    System.out.println("[** error **] failed generating random data");
                }
            } catch (Exception e) {
                System.out.println("[## exception ##] " + e.getMessage());
            }
        }
    }
}
