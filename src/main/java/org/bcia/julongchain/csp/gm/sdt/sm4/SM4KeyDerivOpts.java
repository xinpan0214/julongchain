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
package org.bcia.julongchain.csp.gm.sdt.sm4;

import org.bcia.julongchain.csp.intfs.opts.IKeyDerivOpts;

/**
 * GM SM4密钥派生选项
 *
 * @author tengxiumin
 * @date 2018/05/17
 * @company SDT
 */
public class SM4KeyDerivOpts implements IKeyDerivOpts {

    /**
     * 获取算法名称
     * @return 算法名称
     */
    @Override
    public String getAlgorithm() {
        return "SM4";
    }

    /**
     * 是否为临时密钥
     * @return true/false
     */
    @Override
    public boolean isEphemeral() {
        return true;
    }
}
