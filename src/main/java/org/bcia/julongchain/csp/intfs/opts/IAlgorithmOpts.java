package org.bcia.julongchain.csp.intfs.opts;

/**
 * Copyright Dingxuan. All Rights Reserved.
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

/**
 * @author zhanglin
 * @purpose Define the interface, IAlgorithmOpts
 * @date 2018-01-25
 * @company Dingxuan
 */

// IAlgorithmOpts contains options for algorithms with a CSP.
public interface IAlgorithmOpts {

    // The getAlgorithm returns an identifier for algorithms or cipher suites to be used.
    String getAlgorithm();

}
